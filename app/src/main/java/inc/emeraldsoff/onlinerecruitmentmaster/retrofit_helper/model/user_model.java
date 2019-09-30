package inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.model;

import com.google.gson.annotations.SerializedName;

public class user_model {
    @SerializedName("id")
    private String id;
    @SerializedName("f_name")
    private String f_name;
    @SerializedName("m_name")
    private String m_name;
    @SerializedName("l_name")
    private String l_name;
    @SerializedName("p_address")
    private String p_address;
    @SerializedName("l_address")
    private String l_address;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("password")
    private String password;
    @SerializedName("gauth")
    private String gauth;

    public user_model(String id, String f_name, String m_name, String l_name, String p_address, String l_address, String email, String phone, String password, String gauth) {
        this.id = id;
        this.f_name = f_name;
        this.m_name = m_name;
        this.l_name = l_name;
        this.p_address = p_address;
        this.l_address = l_address;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.gauth = gauth;
    }

    public String getId() {
        return id;
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

    public String getP_address() {
        return p_address;
    }

    public String getL_address() {
        return l_address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getGauth() {
        return gauth;
    }


    public void setId(String id) {
        this.id = id;
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

    public void setP_address(String p_address) {
        this.p_address = p_address;
    }

    public void setL_address(String l_address) {
        this.l_address = l_address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGauth(String gauth) {
        this.gauth = gauth;
    }
}
