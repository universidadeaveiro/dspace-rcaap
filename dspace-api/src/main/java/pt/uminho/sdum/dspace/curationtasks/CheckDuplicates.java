package pt.uminho.sdum.dspace.curationtasks;

import org.dspace.content.*;
import org.dspace.content.Collection;
import org.dspace.curate.AbstractCurationTask;
import org.dspace.curate.Curator;
import org.dspace.curate.Distributive;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Naive approach, memory heavy
 *
 * @author tmmguimaraes on 18/05/2016.
 */


@Distributive
public class CheckDuplicates extends AbstractCurationTask {


    @Override
    public int perform(DSpaceObject dso) throws IOException {
        if (dso instanceof Site) {
            try {
                List<Item> col = collectAllItems();
                setResult(checkDuplicates(col));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        } else if (dso instanceof Community) {
            try {
                List<Item> col = collectAllItemsFromComunnity((Community) dso);
                setResult(checkDuplicates(col));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        } else if (dso instanceof Collection) {
            try {
                List<Item> col = collectAllItemsFromCollection((Collection) dso);
                setResult(checkDuplicates(col));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        } else {
            setResult("A tarefa deve ser aplicada ao site, comunidade ou coleção");
            return 2;
        }
    }

    private String checkDuplicates(List<Item> col) throws SQLException {
        StringBuilder sb = new StringBuilder("<br>");
        Set<Integer> processed = new HashSet<>();
        for (int i = 0; i < col.size(); ++i) {
            if (!processed.contains(i)) {
                Item itemI = col.get(i);
                for (int j = i + 1; j < col.size(); ++j) {
                    if (!processed.contains(j)) {
                        Item itemJ = col.get(j);
                        if (itemComparator(itemI, itemJ) && !itemI.equals(itemJ)) {
                            processed.add(j);
                            String handleI = itemI.getHandle();
                            String handleJ = itemJ.getHandle();
                            sb.append("Item <a href=\"http://hdl.handle.net/");
                            sb.append(handleI);
                            sb.append("\" target=\"_blank\">");
                            sb.append(handleI);
                            sb.append("</a> and Item <a href=\"http://hdl.handle.net/");
                            sb.append(handleJ);
                            sb.append("\" target=\"_blank\">");
                            sb.append(handleJ);
                            sb.append("</a> Podem ser registos duplicados <br>");
                            itemJ.decache();
                        }
                    }
                }
                itemI.decache();
            }
        }
        return sb.toString();
    }

    private boolean itemComparator(Item itemI, Item itemJ) {

        String titleI = itemI.getMetadata("dc.title");
        String typeI = itemI.getMetadata("dc.type");
        String dateI = itemI.getMetadata("dc.date.issued");

        String titleJ = itemJ.getMetadata("dc.title");
        String typeJ = itemJ.getMetadata("dc.type");
        String dateJ = itemJ.getMetadata("dc.date.issued");


        if (titleI == null || typeI == null || dateI == null ||
                titleJ == null || typeJ == null || dateJ == null)
            return false;

        else
            return titleI.trim().equals(titleJ.trim()) &&
                    typeI.trim().equals(typeJ.trim()) &&
                    dateI.trim().equals(dateJ.trim());
    }


    private List<Item> collectAllItemsFromCollection(Collection col) {
        List<Item> res = new ArrayList<>();
        try {
            ItemIterator it = col.getAllItems();
            if (it != null)
                while (it.hasNext())
                    res.add(it.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private List<Item> collectAllItemsFromComunnity(Community com) {
        List<Item> res = new ArrayList<>();
        try {
            for (Collection col : com.getAllCollections())
                res.addAll(collectAllItemsFromCollection(col));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private List<Item> collectAllItems() {
        List<Item> res = new ArrayList<>();
        try {
            for (Community com : Community.findAllTop(Curator.curationContext()))
                res.addAll(collectAllItemsFromComunnity(com));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
