package inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager;

import android.provider.BaseColumns;

public class sqlite_basecolumns {

    private sqlite_basecolumns() {
    }

    public static final class contacts implements BaseColumns {
        public static final String CONTACTS_TABLE_NAME = "contacts";
        public static final String client_name = "client_name";
        public static final String spouse = "spouse";
        public static final String children = "children";
        public static final String gender = "gender";
        public static final String address_i = "address_i";
        public static final String address_ii = "address_ii";
        public static final String city = "city";
        public static final String post_office = "post_office";
        public static final String areapin = "areapin";
        public static final String dist = "dist";
        public static final String state = "state";
        public static final String country = "country";
        public static final String std = "std";
        public static final String mobile_no = "mobile_no";
        public static final String smobile_no = "smobile_no";
        public static final String telephoneno = "telephoneno";
        public static final String emailid = "emailid";
        public static final String anni_dd = "anni_dd";
        public static final String bday_dd = "bday_dd";
        public static final String note = "note";
        public static final String bday_code = "bday_code";
        public static final String anni_code = "anni_code";
        public static final String qualification = "qualification";
        public static final String occupation = "occupation";
        public static final String employer = "employer";
        public static final String created_at = "created_at";
//    public static final String date = "date";
    }

    public static final class diary implements BaseColumns {
        public static final String DIARY_TABLE_NAME = "diary";
        public static final String content = "content";
        public static final String purpose = "purpose";
        public static final String created_at = "created_at";
        public static final String created_date = "created_date";
        public static final String created_time = "created_time";
    }

    public static final class policy implements BaseColumns {
        public static final String POLICY_TABLE_NAME = "policy";
        public static final String content = "content";
        public static final String purpose = "purpose";
        public static final String created_at = "created_at";
        public static final String for_date = "for_date";
        public static final String for_time = "for_date";

    }

}
