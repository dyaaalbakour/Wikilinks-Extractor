package co.signalmedia.external.wikilinks.data;

import java.nio.ByteBuffer;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TType;

/**
 * This class repesent the page content within an item in the wikilinks dataset
 * 
 * @author dyaaalbakour
 *
 */
public class PageContentItem {
	ByteBuffer raw;
	String fullText;
	String articleText;
	String dom;
	
	public PageContentItem (TBinaryProtocol protocol) throws TException{
		
		boolean done = false;
		protocol.readStructBegin();
		while(!done){
			TField field = protocol.readFieldBegin();
			
			if (field.type == TType.STOP) done =true;
			else{
				short fieldId = field.id;
				if(fieldId==1) //raw
				{
					raw = protocol.readBinary();
				}
				if (fieldId==2) 
				{
					fullText = protocol.readString();
					
				}
				if(fieldId ==3)
				{
					articleText = protocol.readString();				
				}
				if(fieldId == 4)
				{
					dom = protocol.readString();
				}
				if(fieldId<1 || fieldId>4) {
					TProtocolUtil.skip(protocol, field.type);
				}
				protocol.readFieldEnd();
			}
			
		}
		protocol.readStructEnd();		
	}

	public ByteBuffer getRaw() {
		return raw;
	}

	public void setRaw(ByteBuffer raw) {
		this.raw = raw;
	}

	public String getFullText() {
		return fullText;
	}

	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

	public String getArticleText() {
		return articleText;
	}

	public void setArticleText(String articleText) {
		this.articleText = articleText;
	}

	public String getDom() {
		return dom;
	}

	public void setDom(String dom) {
		this.dom = dom;
	}
	
}
