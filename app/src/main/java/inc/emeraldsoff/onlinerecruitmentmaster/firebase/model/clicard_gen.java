package inc.emeraldsoff.onlinerecruitmentmaster.firebase.model;

import java.util.Date;

public class clicard_gen {
    String x = "";
    private String client_name, spouse, children, gender, date, app_userid;
    private String address_i, address_ii, city, country, post_office, areapin, dist, state;
    private String isdcode, std, mobile_no, smobile_no, telephoneno, emailid, qualification, occupation, note;
    private String anni_dd, bday_dd;
    private Date anni_code, bday_code;


    public clicard_gen() {
        //empty constructor is needed.
    }

    public clicard_gen(String client_name, String spouse, String children, String gender, String date, String app_userid, String address_i, String address_ii, String city, String country, String post_office, String areapin, String dist, String state, String isdcode, String std, String mobile_no, String smobile_no, String telephoneno, String emailid, String qualification, String occupation, String note, String anni_dd, String bday_dd, Date anni_code, Date bday_code) {
        this.client_name = client_name;
        this.spouse = spouse;
        this.children = children;
        this.gender = gender;
        this.date = date;
        this.app_userid = app_userid;
        this.address_i = address_i;
        this.address_ii = address_ii;
        this.city = city;
        this.country = country;
        this.post_office = post_office;
        this.areapin = areapin;
        this.dist = dist;
        this.state = state;
        this.isdcode = isdcode;
        this.std = std;
        this.mobile_no = mobile_no;
        this.smobile_no = smobile_no;
        this.telephoneno = telephoneno;
        this.emailid = emailid;
        this.qualification = qualification;
        this.occupation = occupation;
        this.note = note;
        this.anni_dd = anni_dd;
        this.bday_dd = bday_dd;
        this.anni_code = anni_code;
        this.bday_code = bday_code;
    }


    public String getAnni_dd() {
        return anni_dd;
    }

    public String getBday_dd() {
        return bday_dd;
    }

    public Date getAnni_code() {
        return anni_code;
    }

    public Date getBday_code() {
        return bday_code;
    }

    public String getClient_name() {
        if (client_name != null) {
            return client_name;
        } else {
            client_name = " ";
            return client_name;
        }
    }

    public String getSpouse() {
        if (spouse != null) {
            return spouse;
        } else {
            spouse = " ";
            return spouse;
        }
    }

    public String getChildren() {
        if (children != null) {
            return children;
        } else {
            children = " ";
            return children;
        }
    }

    public String getGender() {
        if (gender != null) {
            return gender;
        } else {
            gender = "unspecified";
            return gender;
        }
    }

    public String getDate() {
        if (date != null) {
            return date;
        } else {
            date = " ";
            return date;
        }
    }

    public String getApp_userid() {
        return app_userid;
    }

//    public String getAnni_dd() {
//        if(anni_dd!=null) {
//            return anni_dd;
//        }else{
//            anni_dd = " ";
//            return anni_dd;
//        }
//    }
//
//    public String getAnni_code() {
//        if(anni_dd!=null) {
//            return anni_code;
//        }else{
//            anni_code = " ";
//            return anni_code;
//        }
//    }

//    public String getBday_code() {
//        return bday_code;
//    }

    public String getAddress_i() {
        return address_i;
    }

    public String getAddress_ii() {
        return address_ii;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPost_office() {
        return post_office;
    }

    public String getAreapin() {
        return areapin;
    }

    public String getDist() {
        return dist;
    }

    public String getState() {
        return state;
    }

//    public String getBday_dd() {
//        return bday_dd;
//    }

    public String getIsdcode() {
        return isdcode;
    }

    public String getStd() {
        return std;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getSmobile_no() {
        return smobile_no;
    }

    public String getTelephoneno() {
        return telephoneno;
    }

    public String getEmailid() {
        return emailid;
    }

    public String getQualification() {
        return qualification;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getNote() {
        return note;
    }
}
