package main;

public class User {
    private String userID;
    private String username;
    private String password;
    private String role;
    private String address;
    private String phoneNum;
    private String gender;
    
    public User() {
    
    }

    public User(String userID, String username, String password, String role, String address, String phoneNum, String gender) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.role = role;
        this.address = address;
        this.phoneNum = phoneNum;
        this.gender = gender;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getGender() {
        return gender;
    }


    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
