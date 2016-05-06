package co.signalmedia.external.wikilinks.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TList;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TType;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.l3s.boilerpipe.BoilerpipeProcessingException;

/**
 * This class represents an item in the wikilinks full dataset (with context).
 * http://www.iesl.cs.umass.edu/data/wiki-links
 * 
 * @author dyaaalbakour
 *
 */
public class WikiLinkItem {

	int docId;
	String url;
	PageContentItem pci =null;
	List<RareWord> rareWords = new ArrayList<RareWord>();
	List<Mention> mentions = new ArrayList<Mention>();
	
	public WikiLinkItem(TBinaryProtocol protocol) throws TException, BoilerpipeProcessingException{
		protocol.readStructBegin();
    	boolean done =false;
    	while(!done){
    		TField field = protocol.readFieldBegin();
    		if (field.type == TType.STOP) {
    			done =true;
    		}
    		else{
    			short fieldId = field.id;
    			if(fieldId ==1) // docId
    			{
    				docId = protocol.readI32();
    			}
    			if(fieldId==2){
    				url = protocol.readString();
    			}    			
    			if(fieldId==3){
    				pci = new PageContentItem(protocol);
    				String dom =  pci.getDom();    				
    				convertToJson();    				
    			}
    			if(fieldId == 4){
    				TList list  = protocol.readListBegin();				
    				int i=0;
    				while(i<list.size){
    					rareWords.add(new RareWord(protocol));
    					i++;
    				}
    				protocol.readListEnd();
    			}
    			if(fieldId==5){
    				TList list = protocol.readListBegin();
    				int i=0;
    				while(i<list.size){
    					mentions.add(new Mention(protocol));
    					i++;
    				}
    				protocol.readListEnd();
    			}
    			if(fieldId<1 || fieldId>5){    				
    				TProtocolUtil.skip(protocol,field.type);
    			}
    			protocol.readFieldEnd();
    		}    		
    	}
    	protocol.readStructEnd();
		
	}

	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public PageContentItem getPci() {
		return pci;
	}

	public void setPci(PageContentItem pci) {
		this.pci = pci;
	}

	public List<RareWord> getRareWords() {
		return rareWords;
	}

	public void setRareWords(List<RareWord> rareWords) {
		this.rareWords = rareWords;
	}

	public List<Mention> getMentions() {
		return mentions;
	}

	public void setMentions(List<Mention> mentions) {
		this.mentions = mentions;
	}
	
	
	public JsonObject convertToJson(){
		String dom = pci.getDom();		
		
		JsonObject wikiLinkItemObject = new JsonObject();
		wikiLinkItemObject.addProperty("docId", this.docId+"");
		wikiLinkItemObject.addProperty("url", this.url);		
		
		if(dom!=null){
			org.jsoup.nodes.Document document= Jsoup.parse(dom);			
			
			final StringBuilder accum = new StringBuilder();
			final int[] offsets = new int[mentions.size()];
			
			new NodeTraversor(new NodeVisitor() {
				public void head(Node node, int depth) {
	                if (node instanceof TextNode) {
	                    TextNode textNode = (TextNode) node;			                    
	                    appendNormalisedText(accum, textNode);
	                } else if (node instanceof Element) {
	                    Element element = (Element) node;    			                    
	                    if (accum.length() > 0 &&
	                        (element.isBlock() || element.tag().getName().equals("br")) &&
	                        lastCharIsWhitespace(accum))
	                        accum.append("\n");	                    
	                    for(int i=0;i<offsets.length;i++){
	                        
		                    if(node.nodeName().equals("a")&&
		                    		node.attr("href").equals(mentions.get(i).getWikiUrl())
		                    		&& ((Element)node).text().equals(mentions.get(i).getAnchorText()))
		                    	offsets[i]= accum.toString().length();
	                    }
	                }
	            }
	            public void tail(org.jsoup.nodes.Node node, int depth) {
	            }
	        }).traverse(document.body());
			
			wikiLinkItemObject.addProperty("rawText",accum.toString());
			JsonArray mentionsObject = new JsonArray();
			
			for(int i=0;i<offsets.length;i++){
				JsonObject mentionObject = new JsonObject();
				mentionObject.addProperty("wikiUrl",mentions.get(i).wikiUrl);
				mentionObject.addProperty("anchorText", mentions.get(i).anchorText);
				mentionObject.addProperty("offset", offsets[i]);
				mentionsObject.add(mentionObject);
			}
			wikiLinkItemObject.add("mentions", mentionsObject);			
		}
		return wikiLinkItemObject;
	}
	
	
	 public static void appendNormalisedText(StringBuilder accum, TextNode textNode) {
        String text = textNode.getWholeText();
        if (preserveWhitespace(textNode.parentNode()))
            accum.append(text);
        else
            StringUtil.appendNormalisedWhitespace(accum, text, lastCharIsWhitespace(accum));
    }

    public static boolean preserveWhitespace(Node node) {
        // looks only at this element and one level up, to prevent recursion & needless stack searches
        if (node != null && node instanceof Element) {
            Element element = (Element) node;
            return element.tag().preserveWhitespace() ||
                element.parent() != null && element.parent().tag().preserveWhitespace();
        }
        return false;
    }
    
    public static boolean lastCharIsWhitespace(StringBuilder sb) {
        return sb.length() != 0 && sb.charAt(sb.length() - 1) == ' ';
    }
	    
}
