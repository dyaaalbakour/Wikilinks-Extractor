package co.signalmedia.external.wikilinks.data;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TType;

/**
 * This class represents Context within an item in the wikilinks dataset
 * 
 * @author dyaaalbakour
 *
 */
public class Context {

	String leftField;
	String rightField;
	String middleField;
	
	public String getLeftField() {
		return leftField;
	}

	public void setLeftField(String leftField) {
		this.leftField = leftField;
	}

	public String getRightField() {
		return rightField;
	}

	public void setRightField(String rightField) {
		this.rightField = rightField;
	}

	public String getMiddleField() {
		return middleField;
	}

	public void setMiddleField(String middleField) {
		this.middleField = middleField;
	}

	public Context(TBinaryProtocol protocol) throws TException{
		protocol.readStructBegin();
		boolean done =false;
		while(!done){
			TField field = protocol.readFieldBegin();
			short fieldId = field.id;
			if (field.type == TType.STOP) done =true;
			else{
				if(fieldId==1) {
					leftField = protocol.readString();
				}
				if(fieldId==2){
					rightField = protocol.readString();
				}
				if(fieldId == 3){
					middleField = protocol.readString();
				}
				if(fieldId<1 || fieldId>3) TProtocolUtil.skip(protocol, field.type);
				protocol.readFieldEnd();
			}
		}
		protocol.readStructEnd();
	}
}
