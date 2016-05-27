package pt.uminho.sdum.dspace.submit.lookup;

import gr.ekt.bte.core.Record;
import org.apache.http.HttpException;
import org.dspace.core.Context;
import org.dspace.submit.lookup.NetworkSubmissionLookupDataLoader;

import java.io.IOException;
import java.util.*;

/**
 * @author  tmmguimaraes on 13/05/2016.
 */
public class RenatesOnlineDataLoader extends NetworkSubmissionLookupDataLoader {

    public final static String TID = "tid";

    @Override
    public List<String> getSupportedIdentifiers() {
        return Collections.singletonList(TID);
    }

    @Override
    public boolean isSearchProvider() {
        return false;
    }

    @Override
    public List<Record> search(Context context, String title, String author, int year) throws HttpException, IOException {
        return null;
    }

    @Override
    public List<Record> getByIdentifier(Context context, Map<String, Set<String>> keys) throws HttpException, IOException {
        List<Record> results = new ArrayList<>();
        if (keys != null) {
            Set<String> tids = keys.get(TID);
            List<Record> items = new ArrayList<>();

            if (tids != null && tids.size() > 0)
                for (String tid : tids)
                    items.add(RenatesService.getByTID(tid));

            for (Record item : items)
                if(item != null)
                    results.add(convertFields(item));

        }
        return results;
    }
}
