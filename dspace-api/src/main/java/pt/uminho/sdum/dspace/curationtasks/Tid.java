package pt.uminho.sdum.dspace.curationtasks;

import org.apache.log4j.Logger;
import org.dspace.content.Item;
import org.dspace.curate.Distributive;
import pt.uminho.sdum.utils.DateValidator;

import java.sql.SQLException;
import java.util.Date;

/**
 * @author Tiago Guimarães
 */

@Distributive
public class Tid extends Distributor {
    private static Logger log = Logger.getLogger(Tid.class);
    private static final int YMD = 1;
    private static final int YM = 2;
    private static final int Y = 3;

    // Check if THESIS deposited after 2013 has TID
    @Override
    protected String processItem(Item item) throws SQLException {

        StringBuilder res = new StringBuilder();

        String handle = item.getHandle();

        String type = item.getMetadata("dc.type");
        String tid = item.getMetadata("dc.identifier.tid");
        String sDate = item.getMetadata("dc.date.issued");

        DateValidator dv = new DateValidator("2013-08-07", "2013-07", "2012");

        if (type != null && (type.equals("masterThesis") || type.equals("doctoralThesis"))) {
            if (dv.verifyAfter(sDate) && (tid == null || tid.isEmpty())) {
                if (item.isArchived() && !item.isWithdrawn()) {
                    res.append("<a href=\"http://hdl.handle.net/");
                    res.append(handle);
                    res.append("\" target=\"_blank\">");
                    res.append(handle);
                    res.append("</a> não tem TID  <br>");
                    //res.append("<br> DATE: <br>" + date + "<br>"); //2009-05-23
                }
            }
        }

        item.decache();
        return res.toString();
    }

    private boolean verifyDate(Date ymd, Date ym, Date y, Date queryDate, int level) {


        if (level == YMD)
            return queryDate.after(ymd);
        else if (level == YM)
            return queryDate.after(ym);
        else //if level == Y:
            return queryDate.after(y);
    }
}
