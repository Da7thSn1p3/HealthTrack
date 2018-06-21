package gr.unipi.healthtrack;

public class UserInformation {

    private String bdate;
    private String clinic_spinner_text;
    private String email;
    private String name;
    private String phone;
    private String sex_spinner_text;
    private String type;

    public UserInformation(){

    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getClinic_spinner_text() {
        return clinic_spinner_text;
    }

    public void setClinic_spinner_text(String clinic_spinner_text) {
        this.clinic_spinner_text = clinic_spinner_text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex_spinner_text() {
        return sex_spinner_text;
    }

    public void setSex_spinner_text(String sex_spinner_text) {
        this.sex_spinner_text = sex_spinner_text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
