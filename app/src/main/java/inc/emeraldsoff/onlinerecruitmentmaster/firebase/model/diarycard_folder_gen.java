package inc.emeraldsoff.onlinerecruitmentmaster.firebase.model;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class diarycard_folder_gen {
    private String folder_doc, time, folder_name;
    private Timestamp timestamp;
    //    private Date date;
    private SimpleDateFormat folderdoc = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE", Locale.US);
    private SimpleDateFormat fullFormat_onlydate = new SimpleDateFormat("dd MMMM yyyy', 'EEEE", Locale.US);
    private SimpleDateFormat foldername = new SimpleDateFormat("YYYY-MMMM-dd-EEEE", Locale.US);

    public diarycard_folder_gen() {
    }

    public diarycard_folder_gen(String folder_doc, String folder_name, String time, Timestamp timestamp) {
        this.folder_doc = folder_doc;
        this.time = time;
        this.timestamp = timestamp;
    }

    public String getFolder_name() {
        folder_name = foldername.format(timestamp.toDate());
        return folder_name;
    }

    public String getFolder_doc() {
        return folder_doc;
    }

    public String getTime() {
        return time;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
