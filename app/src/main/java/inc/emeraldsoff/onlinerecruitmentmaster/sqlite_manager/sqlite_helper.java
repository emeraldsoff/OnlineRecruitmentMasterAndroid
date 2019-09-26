package inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class sqlite_helper extends SQLiteOpenHelper {

    public sqlite_helper(@Nullable Context context) {
        super(context, sqlite_commands.DATABASE_NAME, null, sqlite_commands.DATABASE_VERSION);
//        SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()+ File.separator+DATABASE_NAME,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db_create(db);
    }

    private void db_create(SQLiteDatabase db) {
        db.execSQL(sqlite_commands.SQL_CREATE_CONTACTS_TABLE);
        db.execSQL(sqlite_commands.SQL_CREATE_DIARY_TABLE);
        db.execSQL(sqlite_commands.SQL_CONTACT_NAME_INDEX);
        db.execSQL(sqlite_commands.SQL_CONTACT_BIRTHDAY_INDEX);
        db.execSQL(sqlite_commands.SQL_CONTACT_ANNIVERSARY_INDEX);
        db.execSQL(sqlite_commands.SQL_DIARY_CREATION_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db_upgrade(db, oldVersion, newVersion);

//        db.execSQL("DROP TABLE IF EXISTS " + sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME);
//        onCreate(db);
    }

    private void db_upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion)
        {
            switch (upgradeTo)
            {
                case 2:
                    db.execSQL(sqlite_commands.SQL_CREATE_DIARY_TABLE);
                    db.execSQL(sqlite_commands.SQL_DIARY_CREATION_INDEX);
                    break;
                case 3:
                    //db.execSQL(sqlite_commands.SQL_MOD_TEST);
                    break;
            }
            upgradeTo++;
        }
    }

}
