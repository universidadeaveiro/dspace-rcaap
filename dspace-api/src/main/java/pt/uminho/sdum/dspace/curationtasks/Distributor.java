package pt.uminho.sdum.dspace.curationtasks;

import org.dspace.content.*;
import org.dspace.curate.AbstractCurationTask;
import org.dspace.curate.Curator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tiago Guimarães on 13/04/2016.
 */
abstract class Distributor extends AbstractCurationTask {

    @Override
    public int perform(DSpaceObject dso) throws IOException {
        try {

            if (dso instanceof Item) {
                setResult(processItem((Item) dso));
                return 0;

            } else if (dso instanceof Community) {
                setResult(processCommunity((Community) dso, new HashSet<Collection>()));
                return 0;

            } else if (dso instanceof Collection) {

                setResult(processCollection((Collection) dso, new HashSet<Collection>()));
                return 0;

            } else if (dso instanceof Site) {
                setResult(processSite());
                return 0;
            } else {
                setResult("Task must be applied to Collection or Community or Site");
                return 2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }


    private String processSite() throws SQLException {
        StringBuilder res = new StringBuilder();
        Set<Collection> collectionsProcessed = new HashSet<>();

        for (Community com : Community.findAllTop(Curator.curationContext()))
            res.append(processCommunity(com,collectionsProcessed));

        return res.toString();
    }

    private String processCommunity(Community com, Set<Collection> collectionsProcessed) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String res;
        for (Community c : com.getSubcommunities())
            sb.append(processCommunity(c, collectionsProcessed));

        for (Collection col : com.getAllCollections())
            sb.append(processCollection(col,collectionsProcessed));

        res = sb.toString();
        if (!res.equals(""))
            res = "<br>" + com.getName() + ":<br>" + sb.toString();
        return res;
    }

    /**

     */
    private String processCollection(Collection col, Set<Collection> collectionsProcessed) throws SQLException {
        String result = "";
        if(!collectionsProcessed.contains(col)) {
            collectionsProcessed.add(col);
            StringBuilder stringBuilder = new StringBuilder();
            ItemIterator it = col.getAllItems();
            if (it != null)
                while (it.hasNext()) {
                    Item item = it.next();
                    String strProc = processItem(item);
                    if (strProc != null && !strProc.isEmpty())
                        stringBuilder.append(strProc);
                }
            result = stringBuilder.toString();
            if (!result.equals(""))
                result = col.getName() + ":<br>" + result;
        }
        return result;
    }

    protected abstract String processItem(Item item) throws SQLException;
}