package co.signalmedia.external.wikilinks.tests;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TIOStreamTransport;

import co.signalmedia.external.wikilinks.data.Mention;
import co.signalmedia.external.wikilinks.data.WikiLinkItem;
import de.l3s.boilerpipe.extractors.KeepEverythingExtractor;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	try{
	    	TBinaryProtocol protocol = new TBinaryProtocol(
	    			new TIOStreamTransport(
	    					new BufferedInputStream(
	    							new GZIPInputStream(new FileInputStream("~/Downloads/wiki-link/data/001.gz")))));//
	    	
	    	List<WikiLinkItem> items = new ArrayList<WikiLinkItem>();
	    	int i=0;
	    	int allMentions = 0;
	    	int nonvalid=0;
	
	    	while(i<1000){
	    		try{
	    			WikiLinkItem item = new WikiLinkItem(protocol);
	    			if(item.getPci().getDom()!=null){
	    	    		
	    				String fullText = KeepEverythingExtractor.INSTANCE.getText(item.getPci().getDom());
	    	    		
	    	    		
	    	    		for(Mention m: item.getMentions()){
	    	    			
	    	    			
	    	    			int counts = org.apache.commons.lang3.StringUtils.countMatches(fullText, m.getAnchorText());
	    	    			if(counts>1)
	    	    				nonvalid++;
	    	    			allMentions++;
	    	    		}
	        		}        		
	        		i++;        		
	    		}catch(Exception e){    			
	    			break;
	    		}	    		
	    	}
	    	System.out.println(allMentions);
	    	System.out.println(nonvalid);    		
	        assertTrue( true );
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
}
