package pt.uminho.sdum.dspace.curationtasks;

import org.dspace.authorize.AuthorizeManager;
import org.dspace.authorize.ResourcePolicy;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.curate.Curator;
import org.dspace.curate.Distributive;
import org.dspace.eperson.Group;

import java.sql.SQLException;
import java.util.Iterator;

/**
 * DONT USE THIS TASK, THIS ASSUMES POST 3.0 EMBARGO SYSTEM, BUT WE ARE USING PRE 3.0
 * Checks if an item still has dc.access = embargo, even tough it is no longer embargoed
 *
 * @author Tiago Guimarães on 22/04/2016.
 */
@Distributive
public class PolicyCheck extends Distributor {

    private Group anonGroup;

    @Override
    protected String processItem(Item item) throws SQLException {
        StringBuilder res = new StringBuilder();
        String rights = item.getMetadata("dc.rights");


        if (item.isArchived() && !item.isWithdrawn() && rights != null
                && !rights.equals("openAccess"))  {

            String handle = item.getHandle();
            anonGroup = Group.find(Curator.curationContext(), Group.ANONYMOUS_ID );
            boolean foundAnonPolicy = false;
            int i = 0;
            Bundle[] bundles =  item.getBundles("ORIGINAL");
            int bundleLength = bundles.length;
            while(!foundAnonPolicy && i< bundleLength){

                Iterator<ResourcePolicy> rpIterator = bundles[i].getBundlePolicies().iterator();
                while (!foundAnonPolicy && rpIterator.hasNext()) {
                    ResourcePolicy rp = rpIterator.next();
                    if(rp.getGroup().equals(anonGroup))
                        foundAnonPolicy = true;
                }

                if (foundAnonPolicy)
                    foundAnonPolicy = checkAllBitstreamPolicies(bundles[i].getBitstreams());


                if (foundAnonPolicy) {

                    res.append("<a href=\"http://hdl.handle.net/");
                    res.append(handle);
                    res.append("\" target=\"_blank\">");
                    res.append(handle);
                    res.append("</a> dc.rights não está de acordo com as atuais politicas de acesso <br>");
                }
                i++;
            }

        }

        return res.toString();
    }


    private boolean checkAllBitstreamPolicies(Bitstream[] bitstreams) throws SQLException {

        boolean allBitstreamsAnon = true;
        int j = 0;
        int bitstreamsArrayLength = bitstreams.length;

        while(allBitstreamsAnon && j < bitstreamsArrayLength)
            allBitstreamsAnon = checkBitstreamPolicies(bitstreams[j++]);

        return allBitstreamsAnon;
    }

    private boolean checkBitstreamPolicies(Bitstream bitstream) throws SQLException {
        Iterator<ResourcePolicy> rps = AuthorizeManager.getPolicies(
                Curator.curationContext(), bitstream)
                .iterator();

        boolean bitstreamAnon = false;
        while(!bitstreamAnon && rps.hasNext()){
            ResourcePolicy resourcePolicy = rps.next();
            if (resourcePolicy.getGroup().equals(anonGroup))
                bitstreamAnon = true;
        }
        return bitstreamAnon;
    }
}


