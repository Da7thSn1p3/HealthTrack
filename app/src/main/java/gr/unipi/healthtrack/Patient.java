package gr.unipi.healthtrack;

public class Patient {
    public String patient_name, patient_uid;

    public Patient() {

    }

    public Patient(String patient_uid, String patient_name) {
        this.patient_uid = patient_uid;
        this.patient_name = patient_name;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_uid() {
        return patient_uid;
    }

    public void setPatient_uid(String patient_uid) {
        this.patient_uid = patient_uid;
    }
}
