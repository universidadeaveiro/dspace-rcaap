package pt.uminho.sdum.dspace.curationtasks;

import org.dspace.content.Item;
import org.dspace.curate.Distributive;

import java.sql.SQLException;

/**
 * @author tmmguimaraes on 19/05/2016.
 */


@Distributive
public class DoiValidator extends Distributor {

    @Override
    protected String processItem(Item item) throws SQLException {
        StringBuilder sb = new StringBuilder();

        String handle = item.getHandle();
        String doi = item.getMetadata("dc.identifier.doi");


        if (doi != null) {
           /* if (!doi.startsWith("http://dx.doi.org/")) {
                sb.append("<a href=\"http://hdl.handle.net/");
                sb.append(handle);
                sb.append("\" target=\"_blank\">");
                sb.append(handle);
                sb.append("</a> não tem o prefixo do DOI (http://dx.doi.org/)  <br>");
                //got regex from http://blog.crossref.org/2015/08/doi-regular-expressions.html
            } else { */
            String sub;
            if (doi.startsWith("http://dx.doi.org/"))
                sub = doi.substring(18);
            else if(doi.startsWith("http://doi.org/"))
                sub = doi.substring(15);
            else
                sub = doi;
            if (!sub.matches("^10.\\d{4,9}/[-._;()/:a-zA-Z0-9]+$")) {
                sb.append("<a href=\"http://hdl.handle.net/");
                sb.append(handle);
                sb.append("\" target=\"_blank\">");
                sb.append(handle);
                sb.append("</a> pode ter um DOI inválido <br>");
            }
            //}
        }

        return sb.toString();
    }
}
