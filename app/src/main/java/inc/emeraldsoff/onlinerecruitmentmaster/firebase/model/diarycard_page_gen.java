package inc.emeraldsoff.onlinerecruitmentmaster.firebase.model;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class diarycard_page_gen {
    private String data, timestmp_mod, time, docid, folder_name, dt, t;
    private Timestamp timestmp;
    //    private Date date;
    private SimpleDateFormat fullFormat_time = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE', 'hh:mm:ss a", Locale.US);
    private SimpleDateFormat fullFormat_onlytime = new SimpleDateFormat("hh:mm:ss a", Locale.US);
    private SimpleDateFormat fullFormat_onlydate = new SimpleDateFormat("dd MMMM yyyy', 'EEEE", Locale.US);
    private SimpleDateFormat folderdoc = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE", Locale.US);
    private SimpleDateFormat foldername = new SimpleDateFormat("YYYY-MMMM-dd-EEEE", Locale.US);
    private SimpleDateFormat fullFormat_time_doc = new SimpleDateFormat("YYYY-MMMM-dd-EEEE-hh-mm-ss-a", Locale.US);

    public diarycard_page_gen() {
        //empty constructor needed
    }

    public diarycard_page_gen(String data, String time, String folder_name, String dt, String t, String docid, Timestamp timestmp) {
        this.data = data;
        this.time = time;
        this.timestmp = timestmp;
        this.folder_name = folder_name;
        this.docid = docid;
        this.dt = dt;
        this.t = t;
    }

    public String getDt() {
        dt = fullFormat_onlydate.format(timestmp);
        return dt;
    }

    public String getT() {
        t = fullFormat_onlytime.format(timestmp.toDate());
        return t;
    }

    public String getDocid() {
        docid = fullFormat_time_doc.format(timestmp.toDate());
        return docid;
    }

    public String getFolder_name() {
        folder_name = foldername.format(timestmp.toDate());
        return folder_name;
    }

    public String getdata() {
        return data;
    }

    public String getTime() {
        time = fullFormat_time.format(timestmp.toDate());
        return time;
    }

    public Timestamp gettimestmp() {
        return timestmp;
    }
}
