package co.signalmedia.external.wikilinks.data;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TType;

/**
 * This class represents an enity mention with an item in the wikilinks dataset
 * 
 * @author dyaaalbakour
 *
 */
public class Mention {

	String wikiUrl;
	String anchorText;
	int rawTextOffset;
	Context context;
	String freeBaseId;
	
	public Mention(TBinaryProtocol protocol) throws TException{
		
		protocol.readStructBegin();
		boolean done = false;
		while(!done){
			TField field = protocol.readFieldBegin();
			short fieldId = field.id;
			if(field.type == TType.STOP) done = true;
			else{
				if(fieldId==1){
					wikiUrl = protocol.readString();
					
				}
				if(fieldId==2){
					anchorText= protocol.readString();
					
				}
				if(fieldId==3){
					rawTextOffset = protocol.readI32();
				}
				if(fieldId==4){
					context =new Context(protocol);
					
				}
				if(fieldId==5){
					freeBaseId = protocol.readString();
				}
				if(fieldId<1 || fieldId>5) TProtocolUtil.skip(protocol, field.type);
				protocol.readFieldEnd();
			}
		}
		protocol.readStructEnd();
	}

	public String getWikiUrl() {
		return wikiUrl;
	}

	public void setWikiUrl(String wikiUrl) {
		this.wikiUrl = wikiUrl;
	}

	public String getAnchorText() {
		return anchorText;
	}

	public void setAnchorText(String anchorText) {
		this.anchorText = anchorText;
	}

	public int getRawTextOffset() {
		return rawTextOffset;
	}

	public void setRawTextOffset(int rawTextOffset) {
		this.rawTextOffset = rawTextOffset;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getFreeBaseId() {
		return freeBaseId;
	}

	public void setFreeBaseId(String freeBaseId) {
		this.freeBaseId = freeBaseId;
	}
	
}
