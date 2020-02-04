package co.nexus.votingapp.Helpers;

public class Student{
    String name, dob, phone, gender, admissionNo, department;
    int yearOfJoining, yearOfStudy;
    boolean isVoteEligible, isConfirmed;

    public Student() {}


    public Student(String name, String dob, String phone, String gender, String admissionNo, String department, int yearOfJoining, int yearOfStudy, boolean isVoteEligible, boolean isConfirmed) {
        this.name = name;
        this.dob = dob;
        this.phone = phone;
        this.gender = gender;
        this.admissionNo = admissionNo;
        this.department = department;
        this.yearOfJoining = yearOfJoining;
        this.yearOfStudy = yearOfStudy;
        this.isVoteEligible = isVoteEligible;
        this.isConfirmed = isConfirmed;
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

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
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

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public boolean isVoteEligible() {
        return isVoteEligible;
    }

    public void setVoteEligible(boolean voteEligible) {
        isVoteEligible = voteEligible;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }
}
