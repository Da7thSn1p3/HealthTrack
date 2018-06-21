package gr.unipi.healthtrack;

public class Symptom {
    public String timestamp, symptom, comment;

    public Symptom(){

    }

    public Symptom(String timestamp, String symptom, String comment){
        this.timestamp = timestamp;
        this.symptom = symptom;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }
}
