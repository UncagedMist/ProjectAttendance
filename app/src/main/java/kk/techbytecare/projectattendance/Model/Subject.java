package kk.techbytecare.projectattendance.Model;

public class Subject {
    private String subjectId;
    private String subjectName;
    private String reqPercent;
    private String subjectBunked;
    private String subjectAttended;
    private String freeBunks;
    private String presentPercent;
    private String userPhone;

    public Subject() {
    }

    public Subject(String subjectId, String subjectName, String reqPercent, String subjectBunked, String subjectAttended, String freeBunks, String presentPercent, String userPhone) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.reqPercent = reqPercent;
        this.subjectBunked = subjectBunked;
        this.subjectAttended = subjectAttended;
        this.freeBunks = freeBunks;
        this.presentPercent = presentPercent;
        this.userPhone = userPhone;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getReqPercent() {
        return reqPercent;
    }

    public void setReqPercent(String reqPercent) {
        this.reqPercent = reqPercent;
    }

    public String getSubjectBunked() {
        return subjectBunked;
    }

    public void setSubjectBunked(String subjectBunked) {
        this.subjectBunked = subjectBunked;
    }

    public String getSubjectAttended() {
        return subjectAttended;
    }

    public void setSubjectAttended(String subjectAttended) {
        this.subjectAttended = subjectAttended;
    }

    public String getFreeBunks() {
        return freeBunks;
    }

    public void setFreeBunks(String freeBunks) {
        this.freeBunks = freeBunks;
    }

    public String getPresentPercent() {
        return presentPercent;
    }

    public void setPresentPercent(String presentPercent) {
        this.presentPercent = presentPercent;
    }
}
