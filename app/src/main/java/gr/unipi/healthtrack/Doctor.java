package gr.unipi.healthtrack;

public class Doctor {
    public String doctor_uid, doctor_name;

    public Doctor(){

    }

    public Doctor(String doctor_uid, String doctor_name){
        this.doctor_uid = doctor_uid;
        this.doctor_name = doctor_name;
    }

    public String getDoctor_uid() {
        return doctor_uid;
    }

    public void setDoctor_uid(String doctor_uid) {
        this.doctor_uid = doctor_uid;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }
}
