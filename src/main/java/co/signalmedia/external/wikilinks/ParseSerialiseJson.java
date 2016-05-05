package co.signalmedia.external.wikilinks;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TIOStreamTransport;

import co.signalmedia.external.wikilinks.data.WikiLinkItem;
import de.l3s.boilerpipe.BoilerpipeProcessingException;

public class ParseSerialiseJson {	
	
	public static void main(String[] args) throws IOException {	
		
		if(args.length<2){
			System.err.println("No input or output files were specified as arguments.");
			System.exit(-1);	
		}
		
		TBinaryProtocol protocol = new TBinaryProtocol(
    			new TIOStreamTransport(
    					new BufferedInputStream(
    							new GZIPInputStream(new FileInputStream(args[0])))));//"~/Downloads/wiki-link/data/001.gz"    	
    	int i=0;    	
    	FileWriter writer = new FileWriter(args[1]);//"~/Downloads/wiki-link/data/10000.json"    	
    	while(true){    		
			try{
	    		WikiLinkItem item = new WikiLinkItem(protocol);
				writer.write(item.convertToJson().toString() + "\n");
				writer.flush();
				i++;
				if(i%100 == 0) 
					System.out.println(i +" wikilink items have been processed...");
			}catch(TException e){
				//e.printStackTrace();				
				break;
			} catch (BoilerpipeProcessingException e) {
				//e.printStackTrace();
				break;
			}			
    	}
    	System.out.println("A total of " + i + " wikilikns items have been extracted and serialised into JSON" );
    	writer.close();
	}
}
