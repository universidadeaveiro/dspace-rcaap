package pt.uminho.sdum.dspace.submit.lookup;

import gr.ekt.bte.core.MutableRecord;
import gr.ekt.bte.core.Record;
import gr.ekt.bte.core.StringValue;
import gr.ekt.bte.core.Value;
import org.dspace.app.util.XMLUtils;
import org.dspace.submit.util.SubmissionLookupPublication;
import org.w3c.dom.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * @author tmmguimaraes on 16/05/2016.
 */
public class RenatesUtils {

    public static Record convertRenatesDomToRecord(Element dataRoot) {
        MutableRecord record = new SubmissionLookupPublication("");


        String tid = XMLUtils.getElementValue(dataRoot, "TID");
        String curso = XMLUtils.getElementValue(dataRoot, "Curso");
        String tipoTese = XMLUtils.getElementValue(dataRoot, "TipoTese");
        String title = XMLUtils.getElementValue(dataRoot, "TemaTese");
        String author = XMLUtils.getElementValue(dataRoot, "NomeCompleto");
        String keywords = XMLUtils.getElementValue(dataRoot, "Palavras_chave");
        String dataGrau = XMLUtils.getElementValue(dataRoot,"DataGrau");

        List<Value> wordList = new LinkedList<>();
        List<Value> authorValues = new LinkedList<>();

        if (tid != null)
            record.addValue("tid", new StringValue(tid));

        if (curso != null)
            record.addValue("curso", new StringValue(curso));

        String ttese = getThesisType(tipoTese);
        if (ttese != null)
            record.addValue("type", new StringValue(ttese));

        if (title != null)
            record.addValue("title", new StringValue(title));

        if (author != null) {
            authorValues.add(new StringValue(author.trim()));
            record.addField("authors", authorValues);
        }
        if (keywords!= null) {
            String[] words = keywords.split("[,;]");
            for (String word : words)
                if (!word.isEmpty())
                    wordList.add(new StringValue(word.trim()));
            record.addField("keywords", wordList);
        }

        if (dataGrau != null)
            record.addValue("issued", new StringValue(formatDate(dataGrau)));

        return record;
    }

    private static String formatDate(String dataGrau) {
        StringBuilder sb = new StringBuilder();

        if(dataGrau.length()==4)
            return dataGrau;
        else if(dataGrau.length()==7){
            sb.append(dataGrau.substring(3));
            sb.append("-");
            sb.append(dataGrau.substring(0,2));
            return sb.toString();
        }else if (dataGrau.length()==10){
            sb.append(dataGrau.substring(6));
            sb.append("-");
            sb.append(dataGrau.substring(3,5));
            sb.append("-");
            sb.append(dataGrau.substring(0,2));
            return sb.toString();
        }else
            return null;
    }

    private static String getThesisType(String tipoTese) {
        if (tipoTese == null)
            return null;
        else if(tipoTese.trim().equals("Mestrado"))
            return "masterThesis";
        else if(tipoTese.trim().equals("Doutoramento"))
            return "doctoralThesis";
        else return null;
    }


}
