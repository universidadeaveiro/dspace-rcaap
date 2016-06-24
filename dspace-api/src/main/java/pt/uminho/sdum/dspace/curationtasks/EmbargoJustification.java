package pt.uminho.sdum.dspace.curationtasks;

import org.dspace.content.Item;
import org.dspace.curate.Distributive;

import java.sql.SQLException;

/**
 * @author tmmguimaraes on 13/04/2016.
 */

@Distributive
public class EmbargoJustification extends Distributor {

    @Override
    protected String processItem(Item item) throws SQLException {
        String handle = item.getHandle();
        String rights = item.getMetadata("dc.rights");
        String relation = item.getMetadata("dc.relation");
        String justification = item.getMetadata("rcaap.embargofct");
        StringBuilder res = new StringBuilder();

        if(item.isArchived() && !item.isWithdrawn())
            if( relation != null && relation.length()> 0 &&
                    rights != null && !rights.equals("openAccess") &&
                    (justification == null || justification.isEmpty()) ){

                res.append("<a href=\"http://hdl.handle.net/");
                res.append(handle);
                res.append("\" target=\"_blank\">");
                res.append(handle);
                res.append("</a> não tem justificação para o embargo.  <br>");
            }
        item.decache();
        return res.toString();
    }
}
