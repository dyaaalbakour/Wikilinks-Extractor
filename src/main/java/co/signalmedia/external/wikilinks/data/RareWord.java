package co.signalmedia.external.wikilinks.data;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TType;

/**
 * This class represent the rare word element in an item in the wiklinks dataste
 * 
 * @author dyaaalbakour
 *
 */
public class RareWord {

	String word;
	int offset;
	
	public RareWord(TBinaryProtocol protocol) throws TException{
		
		protocol.readStructBegin();
		boolean done = false;
		while(!done){
			TField field = protocol.readFieldBegin();
			short fieldId = field.id;
			if(field.type == TType.STOP) done =true;
			else{
				if(fieldId == 1){
					word = protocol.readString();
				}
				if(fieldId == 2){
					offset = protocol.readI32();
				}
				if (fieldId<1 || fieldId>2) TProtocolUtil.skip(protocol, field.type);
				protocol.readFieldEnd();
			}
		}
		protocol.readStructEnd();
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
}
