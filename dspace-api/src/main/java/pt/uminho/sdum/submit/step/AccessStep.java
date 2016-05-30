package pt.uminho.sdum.submit.step;

import org.dspace.app.util.SubmissionInfo;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.authorize.ResourcePolicy;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.eperson.Group;
import org.dspace.submit.step.CompleteStep;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tmmguimaraes on 15/04/2016.
 */
public class AccessStep extends CompleteStep {


    @Override
    public int doProcessing(Context context, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, SubmissionInfo submissionInfo) throws ServletException, IOException, SQLException, AuthorizeException {
        int s = super.doProcessing(context,httpServletRequest,httpServletResponse,submissionInfo);

        Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            d = sdf.parse("9999");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Item item = submissionInfo.getSubmissionItem().getItem();
        if(item.getMetadata("dc.rights").equals("restrictedAccess")
                || item.getMetadata("dc.rights").equals("closedAccess")) {



            Group anonGroup  = Group.find(context, Group.ANONYMOUS_ID);

            for (Bundle b : item.getBundles()) {
                if(b.getName().startsWith("ORIG")) {
                    AuthorizeManager.removeGroupPolicies(context, b, anonGroup);
                    for (Bitstream bit : b.getBitstreams()) {
                         for(ResourcePolicy rp: AuthorizeManager.getPolicies(context,bit)) {
                             rp.setStartDate(d);
                             rp.update();
                         }
                    }
                }
            }
            context.commit();
        }
        return s;
    }
}
