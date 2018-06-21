package gr.unipi.healthtrack;

public class Symptom {
    public String timestamp, symptom, comment, endtime;

    public Symptom(){

    }

    public Symptom(String timestamp, String symptom, String comment, String endtime){
        this.timestamp = timestamp;
        this.symptom = symptom;
        this.comment = comment;
        this.endtime = endtime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
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
