package gr.unipi.healthtrack;

public class User {
    public String name, email, phone, bdate, type, clinic_spinner_text, sex_spinner_text;

    public User(){
        //yolo

    }


    public User(String name, String email, String phone, String bdate, String type ,String clinic_spinner_text, String sex_spinner_text){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.bdate = bdate;
        this.type = type;
        this.clinic_spinner_text = clinic_spinner_text;
        this.sex_spinner_text = sex_spinner_text;
    }
}
