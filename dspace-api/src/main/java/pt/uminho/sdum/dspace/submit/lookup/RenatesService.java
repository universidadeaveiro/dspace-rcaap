package pt.uminho.sdum.dspace.submit.lookup;

/**
 * @author tmmguimaraes on 16/05/2016.
 */

import gr.ekt.bte.core.Record;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.dspace.core.ConfigurationManager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.net.URISyntaxException;

public class RenatesService {
    private static Logger log = Logger.getLogger(RenatesService.class);
    private static String renatesURI = "";

    public static Record getByTID(String tid) throws HttpException, IOException {

        if (StringUtils.isNotBlank(tid)) {
            Record r = null;
            tid = tid.trim();
            HttpGet method;
            try {
                HttpClient client = HttpClientBuilder.create().build();

                try {
					//Verify if the renates URI exists in dspace configuration
					if( ConfigurationManager.getProperty("renatesURI") != null){
						renatesURI = ConfigurationManager.getProperty("renatesURI");
						//log.info("Renates URI: " + renatesURI);
					}
					else{
						log.error("No URI for Renates found in dsapce configuration - renatesURI");
						throw new Exception("No URI for Renates found");
					}

                    //URIBuilder uriBuilder = new URIBuilder("http://renates.dgeec.mec.pt/ws/renatesws.asmx/Tese");
                    URIBuilder uriBuilder = new URIBuilder(renatesURI);
                    uriBuilder.addParameter("tid", tid);

                    method = new HttpGet(uriBuilder.build());
                    //log.warn("URI: " + uriBuilder.build());
                } catch (URISyntaxException ex) {
                    throw new HttpException(ex.getMessage());
                }

                HttpResponse response = client.execute(method);

                StatusLine responseStatus = response.getStatusLine();
                int statusCode = responseStatus.getStatusCode();
                if (statusCode != HttpStatus.SC_OK)
                    throw new RuntimeException("Http call failed: " + responseStatus);


                DocumentBuilderFactory factory = DocumentBuilderFactory
                        .newInstance();
                factory.setValidating(false);
                factory.setIgnoringComments(true);
                factory.setIgnoringElementContentWhitespace(true);

                DocumentBuilder db = factory.newDocumentBuilder();
                Document inDoc = db.parse(response.getEntity().getContent());

                log.warn("indoc: " + inDoc.getXmlVersion());
                inDoc.getDocumentElement().normalize();


                NodeList nList = inDoc.getElementsByTagName("Table");
                Node nNode = nList.item(0);
                if(nNode.getNodeType() == Node.ELEMENT_NODE )
                    r = RenatesUtils.convertRenatesDomToRecord((Element) nNode);


            }catch (Exception e){
                e.printStackTrace();
            }
            return r;
        }else
            return null;
    }
}
