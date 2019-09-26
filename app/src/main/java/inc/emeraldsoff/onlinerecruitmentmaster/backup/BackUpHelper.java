package inc.emeraldsoff.onlinerecruitmentmaster.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;

import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.DATABASE_EXTERNAL_PATH_ZIP_NAME;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.DATABASE_INTERNAL_PATH_DB;

public class BackUpHelper extends BackupAgentHelper {

    @Override
    public void onCreate(){
        FileBackupHelper fileBackupHelper = new FileBackupHelper(this,DATABASE_INTERNAL_PATH_DB,DATABASE_EXTERNAL_PATH_ZIP_NAME);
        addHelper("onlinerecruitmentmaster.db",fileBackupHelper);


    }

}
