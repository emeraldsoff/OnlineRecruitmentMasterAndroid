package inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.model;

import com.google.gson.annotations.SerializedName;

public class employee_model {
    @SerializedName("Id")
    private String Id;;
    @SerializedName("intv_no")
    private long intv_no;
    @SerializedName("f_name")
    private String f_name;
    @SerializedName("m_name")
    private String m_name;
    @SerializedName("l_name")
    private String l_name;
    @SerializedName("qualification")
    private String qualification;
    @SerializedName("school")
    private String school;
    @SerializedName("adr1")
    private String adr1;
    @SerializedName("adr2")
    private String adr2;
    @SerializedName("adr3")
    private String adr3;
    @SerializedName("st_no")
    private String st_no;
    @SerializedName("st_name")
    private String st_name;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("pin")
    private String pin;
    @SerializedName("employment_type")
    private String employment_type;
    @SerializedName("interview_date")
    private String interview_date;
    @SerializedName("online_diary_no")
    private String online_diary_no;
    @SerializedName("dispatch_no")
    private String dispatch_no;
    @SerializedName("join_date")
    private String join_date;
    @SerializedName("dated")
    private String dated;
    @SerializedName("gen_applied_post")
    private String gen_applied_post;
    @SerializedName("gen_basic_pay")
    private String gen_basic_pay;
    @SerializedName("gen_pay_scale_level")
    private String gen_pay_scale_level;
    @SerializedName("gen_payscale")
    private String gen_payscale;
    @SerializedName("adhoc_net_salary")
    private String adhoc_net_salary;
    @SerializedName("adhoc_tenure")
    private String adhoc_tenure;
    @SerializedName("adhoc_from_date")
    private String adhoc_from_date;
    @SerializedName("adhoc_to_date")
    private String adhoc_to_date;

    public employee_model(String Id, long intv_no, String f_name, String m_name, String l_name, String qualification, String school, String adr1, String adr2, String adr3, String st_no, String st_name, String city, String state, String pin, String employment_type, String interview_date, String online_diary_no, String dispatch_no, String join_date, String dated, String gen_applied_post, String gen_basic_pay, String gen_pay_scale_level, String gen_payscale, String adhoc_net_salary, String adhoc_tenure, String adhoc_from_date, String adhoc_to_date)
    {
        this.Id = Id;
        this.intv_no = intv_no;
        this.f_name = f_name;
        this.m_name = m_name;
        this.l_name = l_name;
        this.qualification = qualification;
        this.school = school;
        this.adr1 = adr1;
        this.adr2 = adr2;
        this.adr3 = adr3;
        this.st_no = st_no;
        this.st_name = st_name;
        this.city = city;
        this.state = state;
        this.pin = pin;
        this.employment_type = employment_type;
        this.interview_date = interview_date;
        this.online_diary_no = online_diary_no;
        this.dispatch_no = dispatch_no;
        this.join_date = join_date;
        this.dated = dated;
        this.gen_applied_post = gen_applied_post;
        this.gen_basic_pay = gen_basic_pay;
        this.gen_pay_scale_level = gen_pay_scale_level;
        this.gen_payscale = gen_payscale;
        this.adhoc_net_salary = adhoc_net_salary;
        this.adhoc_tenure = adhoc_tenure;
        this.adhoc_from_date = adhoc_from_date;
        this.adhoc_to_date = adhoc_to_date;
    }

    public String getId() {
        return Id;
    }

    public long getIntv_no() {
        return intv_no;
    }

    public String getF_name() {
        return f_name;
    }

    public String getM_name() {
        return m_name;
    }

    public String getL_name() {
        return l_name;
    }

    public String getQualification() {
        return qualification;
    }

    public String getSchool() {
        return school;
    }

    public String getAdr1() {
        return adr1;
    }

    public String getAdr2() {
        return adr2;
    }

    public String getAdr3() {
        return adr3;
    }

    public String getSt_no() {
        return st_no;
    }

    public String getSt_name() {
        return st_name;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPin() {
        return pin;
    }

    public String getEmployment_type() {
        return employment_type;
    }

    public String getInterview_date() {
        return interview_date;
    }

    public String getOnline_diary_no() {
        return online_diary_no;
    }

    public String getDispatch_no() {
        return dispatch_no;
    }

    public String getJoin_date() {
        return join_date;
    }

    public String getDated() {
        return dated;
    }

    public String getGen_applied_post() {
        return gen_applied_post;
    }

    public String getGen_basic_pay() {
        return gen_basic_pay;
    }

    public String getGen_pay_scale_level() {
        return gen_pay_scale_level;
    }

    public String getGen_payscale() {
        return gen_payscale;
    }

    public String getAdhoc_net_salary() {
        return adhoc_net_salary;
    }

    public String getAdhoc_tenure() {
        return adhoc_tenure;
    }

    public String getAdhoc_from_date() {
        return adhoc_from_date;
    }

    public String getAdhoc_to_date() {
        return adhoc_to_date;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setIntv_no(long intv_no) {
        this.intv_no = intv_no;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setAdr1(String adr1) {
        this.adr1 = adr1;
    }

    public void setAdr2(String adr2) {
        this.adr2 = adr2;
    }

    public void setAdr3(String adr3) {
        this.adr3 = adr3;
    }

    public void setSt_no(String st_no) {
        this.st_no = st_no;
    }

    public void setSt_name(String st_name) {
        this.st_name = st_name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setEmployment_type(String employment_type) {
        this.employment_type = employment_type;
    }

    public void setInterview_date(String interview_date) {
        this.interview_date = interview_date;
    }

    public void setOnline_diary_no(String online_diary_no) {
        this.online_diary_no = online_diary_no;
    }

    public void setDispatch_no(String dispatch_no) {
        this.dispatch_no = dispatch_no;
    }

    public void setJoin_date(String join_date) {
        this.join_date = join_date;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public void setGen_applied_post(String gen_applied_post) {
        this.gen_applied_post = gen_applied_post;
    }

    public void setGen_basic_pay(String gen_basic_pay) {
        this.gen_basic_pay = gen_basic_pay;
    }

    public void setGen_pay_scale_level(String gen_pay_scale_level) {
        this.gen_pay_scale_level = gen_pay_scale_level;
    }

    public void setGen_payscale(String gen_payscale) {
        this.gen_payscale = gen_payscale;
    }

    public void setAdhoc_net_salary(String adhoc_net_salary) {
        this.adhoc_net_salary = adhoc_net_salary;
    }

    public void setAdhoc_tenure(String adhoc_tenure) {
        this.adhoc_tenure = adhoc_tenure;
    }

    public void setAdhoc_from_date(String adhoc_from_date) {
        this.adhoc_from_date = adhoc_from_date;
    }

    public void setAdhoc_to_date(String adhoc_to_date) {
        this.adhoc_to_date = adhoc_to_date;
    }


    /*public String Id(){ return Id;}
    public long intv_no(){return intv_no;}
    public String f_name(){return f_name;}
    public String m_name(){return m_name;}
    public String l_name(){return l_name;}
    public String qualification(){return qualification;}
    public String school(){return school;}
    public String adr1(){return adr1;}
    public String adr2(){return adr2;}
    public String adr3(){return adr3;}
    public String st_no(){return st_no;}
    public String st_name(){return st_name;}
    public String city(){return city;}
    public String state(){return state;}
    public String pin(){return pin;}
    public String employment_type(){return employment_type;}
    public String interview_date(){return interview_date;}
    public String online_diary_no(){return online_diary_no}
    public String dispatch_no(){return dispatch_no;}
    public String join_date(){return join_date;}
    public String dated(){return dated;}
    public String gen_applied_post(){return gen_applied_post;}
    public String gen_basic_pay(){return gen_basic_pay;}
    public String gen_pay_scale_level(){return gen_pay_scale_level;}
    public String gen_payscale(){return gen_payscale;}
    public String adhoc_net_salary(){return adhoc_net_salary;}
    public String adhoc_tenure(){return adhoc_tenure;}
    public String adhoc_from_date(){return adhoc_from_date;}
    public String adhoc_to_date(){return adhoc_to_date;}*/
}
