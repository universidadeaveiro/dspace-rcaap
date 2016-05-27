package pt.uminho.sdum.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Returns null if not parsed
 * Created by tmmguimaraes on 21/04/2016.
 */
public class DateValidator {

    private Date dYMD=null;
    private Date dYM=null;
    private Date dY=null;

    private SimpleDateFormat dfYMD = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dfYM = new SimpleDateFormat("yyyy-MM");
    private SimpleDateFormat dfY = new SimpleDateFormat("yyyy");
    /**
     * yyyy-MM-dd for 1st string
     * yyyy-MM for 2nd string
     * yyyy for 3rd string
     * No protection, use with care;
     */
    public DateValidator(String sYMD,String sYM, String sY ){
        try{

            dYMD = dfYMD.parse(sYMD);
            dYM = dfYM.parse(sYM);
            dY = dfY.parse(sY);
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    public DateValidator(Date dYMD, Date dYM, Date dY) {
        this.dYMD = dYMD;
        this.dYM = dYM;
        this.dY = dY;
    }

    //returns false if it fails to parse the date
    public boolean verifyAfter(String sQueryDate){
        Date dQueryDate;
        try {
            dQueryDate = dfYMD.parse(sQueryDate);
            return dQueryDate.after(dYMD);
        } catch (ParseException e) {
            try {
                dQueryDate = dfYM.parse(sQueryDate);
                return dQueryDate.after(dYM);
            } catch (ParseException e2) {
                try {
                    dQueryDate = dfY.parse(sQueryDate);
                    return dQueryDate.after(dY);
                } catch (ParseException e3) {
                    return false;
                }
            }
        }
    }

    public boolean verifyBefore(String sQueryDate){
        Date dQueryDate;
        try {
            dQueryDate = dfYMD.parse(sQueryDate);
            return dQueryDate.before(dYMD);
        } catch (ParseException e) {
            try {
                dQueryDate = dfYM.parse(sQueryDate);
                return dQueryDate.before(dYM);
            } catch (ParseException e2) {
                try {
                    dQueryDate = dfY.parse(sQueryDate);
                    return dQueryDate.before(dY);
                } catch (ParseException e3) {
                    return false;
                }
            }
        }
    }

}
