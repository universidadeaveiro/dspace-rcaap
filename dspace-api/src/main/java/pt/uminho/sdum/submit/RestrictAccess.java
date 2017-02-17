package pt.uminho.sdum.submit;

import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.authorize.ResourcePolicy;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.eperson.Group;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tmmguimaraes on 25/07/2016.
 */
public class RestrictAccess {

    private static final Date date = policyDate();

    private static Date policyDate(){
        Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            d = sdf.parse("9999");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }



    public static void restrictItemAccess(Context context,Item item){

        //Changed to correct null Pointer Exception
       String dcRights = "";
       if(item != null)
       	dcRights = item.getMetadata("dc.rights") != null ? item.getMetadata("dc.rights") : dcRights;

       //Changed to correct null Pointer Exception
       if(dcRights.equals("restrictedAccess") || dcRights.equals("closedAccess")) {

            Group anonGroup  = null;
            try {
                anonGroup = Group.find(context, Group.ANONYMOUS_ID);

            for (Bundle b : item.getBundles()) {
                if(b.getName().startsWith("ORIG")) {
                    AuthorizeManager.removeGroupPolicies(context, b, anonGroup);
                    for (Bitstream bit : b.getBitstreams()) {
                        for(ResourcePolicy rp: AuthorizeManager.getPolicies(context,bit)) {
                            rp.setStartDate(date);
                            rp.update();
                        }
                    }
                }
            }

            context.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (AuthorizeException e) {
                e.printStackTrace();
            }
        }
    }
}
