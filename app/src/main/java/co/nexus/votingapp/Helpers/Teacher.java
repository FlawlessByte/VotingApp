package co.nexus.votingapp.Helpers;

import java.io.Serializable;

public class Teacher implements Serializable {
    String name, dob, phone, gender, department, tid, email;
    int yearOfJoining;
    boolean isConfirmed, isRemoved;

    public Teacher(String name, String dob, String phone,String email, String gender, String tid, String department, int yearOfJoining, boolean isConfirmed, boolean isRemoved) {
        this.name = name;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.department = department;
        this.yearOfJoining = yearOfJoining;
        this.isConfirmed = isConfirmed;
        this.tid = tid;
        this.isRemoved = isRemoved;
    }

    public Teacher() {}

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getYearOfJoining() {
        return yearOfJoining;
    }

    public void setYearOfJoining(int yearOfJoining) {
        this.yearOfJoining = yearOfJoining;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }
}
