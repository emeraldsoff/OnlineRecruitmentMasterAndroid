package inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager;

public class sqlite_commands {

    public static final String DATABASE_NAME = "megaprospectsnext.db";

    public static final int DATABASE_VERSION = 2;

    //DATABASE TABLES
    //CONTACTS TABLE CREATION
    public static final String SQL_CREATE_CONTACTS_TABLE = "CREATE TABLE " +
            sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME + " (" +
            sqlite_basecolumns.contacts._ID + " TEXT PRIMARY KEY UNIQUE, " +
            sqlite_basecolumns.contacts.client_name + " TEXT NOT NULL, " +
            sqlite_basecolumns.contacts.mobile_no + " TEXT NOT NULL, " +
            sqlite_basecolumns.contacts.spouse + " TEXT, " +
            sqlite_basecolumns.contacts.children + " TEXT, " +
            sqlite_basecolumns.contacts.gender + " TEXT, " +
            sqlite_basecolumns.contacts.address_i + " TEXT, " +
            sqlite_basecolumns.contacts.address_ii + " TEXT, " +
            sqlite_basecolumns.contacts.city + " TEXT, " +
            sqlite_basecolumns.contacts.post_office + " TEXT, " +
            sqlite_basecolumns.contacts.areapin + " TEXT , " +
            sqlite_basecolumns.contacts.dist + " TEXT, " +
            sqlite_basecolumns.contacts.state + " TEXT, " +
            sqlite_basecolumns.contacts.country + " TEXT, " +
            sqlite_basecolumns.contacts.std + " TEXT, " +
            sqlite_basecolumns.contacts.smobile_no + " TEXT, " +
            sqlite_basecolumns.contacts.telephoneno + " TEXT, " +
            sqlite_basecolumns.contacts.emailid + " TEXT, " +
            sqlite_basecolumns.contacts.note + " TEXT, " +
            sqlite_basecolumns.contacts.qualification + " TEXT, " +
            sqlite_basecolumns.contacts.occupation + " TEXT, " +
            sqlite_basecolumns.contacts.employer + " TEXT, " +
            sqlite_basecolumns.contacts.bday_code + " DATE, " +
            sqlite_basecolumns.contacts.anni_code + " DATE, " +
            sqlite_basecolumns.contacts.anni_dd + " DATE, " +
            sqlite_basecolumns.contacts.bday_dd + " DATE, " +
            sqlite_basecolumns.contacts.created_at + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ");";

    public static final String SQL_CREATE_DIARY_TABLE = "CREATE TABLE " +
            sqlite_basecolumns.diary.DIARY_TABLE_NAME + " (" +
            sqlite_basecolumns.diary._ID + " TEXT PRIMARY KEY UNIQUE, " +
            sqlite_basecolumns.diary.content + " TEXT NOT NULL, " +
            sqlite_basecolumns.diary.purpose + " TEXT NOT NULL, " +
            sqlite_basecolumns.diary.created_date + " DATE,"+
            sqlite_basecolumns.diary.created_time + " TIME,"+
            sqlite_basecolumns.diary.created_at + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"+
            ");";

    //SQL INDICES
    public static final String SQL_CONTACT_NAME_INDEX = "CREATE INDEX SQL_CONTACT_NAME_INDEX ON " +
            sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME + " ( " +
            sqlite_basecolumns.contacts.client_name + " ASC" +
            ");";
    public static final String SQL_CONTACT_ANNIVERSARY_INDEX = "CREATE INDEX SQL_CONTACT_ANNIVERSARY_INDEX ON " +
            sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME + " ( " +
            sqlite_basecolumns.contacts.anni_code + " ASC" +
            ");";
    public static final String SQL_CONTACT_BIRTHDAY_INDEX = "CREATE INDEX SQL_CONTACT_BIRTHDAY_INDEX ON " +
            sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME + " ( " +
            sqlite_basecolumns.contacts.bday_code + " ASC" +
            ");";
    public static final String SQL_DIARY_CREATION_INDEX = "CREATE INDEX SQL_DIARY_CREATION_INDEX ON " +
            sqlite_basecolumns.diary.DIARY_TABLE_NAME + " ( " +
            sqlite_basecolumns.diary.created_at + " DESC" +
            ");";

    //CONTACTS COLUMN POSITIONS
    public static final int contacts_ID = 0;
    public static final int contacts_client_name = 1;
    public static final int contacts_mobile_no = 2;
    public static final int contacts_spouse = 3;
    public static final int contacts_children = 4;
    public static final int contacts_gender = 5;
    public static final int contacts_address_i = 6;
    public static final int contacts_address_ii = 7;
    public static final int contacts_city = 8;
    public static final int contacts_post_office = 9;
    public static final int contacts_areapin = 10;
    public static final int contacts_dist = 11;
    public static final int contacts_state = 12;
    public static final int contacts_country = 13;
    public static final int contacts_std = 14;
    public static final int contacts_smobile_no = 15;
    public static final int contacts_telephoneno = 16;
    public static final int contacts_emailid = 17;
    public static final int contacts_note = 18;
    public static final int contacts_qualification = 19;
    public static final int contacts_occupation = 20;
    public static final int contacts_employer = 21;
    public static final int contacts_bday_code = 22;
    public static final int contacts_anni_code = 23;
    public static final int contacts_anni_dd = 24;
    public static final int contacts_bday_dd = 25;
    public static final int contacts_created_at = 26;

    //DIARY COLUMN POSITION
    public static final int diary_ID = 0;
    public static final int diary_content = 1;
    public static final int diary_purpose = 2;
    public static final int diary_created_date = 3;
    public static final int diary_created_time = 4;
    public static final int diary_created_at = 5;

}
