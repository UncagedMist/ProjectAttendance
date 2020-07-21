package kk.techbytecare.projectattendance.Model;

public class User {

    private String fName,lName;
    private String collegeName;
    private String phoneNumber;
    private String profileImage;

    public User() {
    }

    public User(String fName, String lName, String collegeName, String phoneNumber) {
        this.fName = fName;
        this.lName = lName;
        this.collegeName = collegeName;
        this.phoneNumber = phoneNumber;
    }

    public User(String fName, String lName, String collegeName, String phoneNumber, String profileImage) {
        this.fName = fName;
        this.lName = lName;
        this.collegeName = collegeName;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
