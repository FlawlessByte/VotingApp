package co.nexus.votingapp.Helpers;

public class Candidate implements Comparable{
    String name, dob, gender, department, imgPath, description;
    int yearOfStudy, voteCount;
    boolean isConfirmed, isReviewed;

    public Candidate(String name, String dob, String gender, String department, String imgPath, int yearOfStudy, int voteCount ,String description, boolean isConfirmed, boolean isReviewed) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.department = department;
        this.imgPath = imgPath;
        this.yearOfStudy = yearOfStudy;
        this.voteCount = voteCount;
        this.description = description;
        this.isConfirmed = isConfirmed;
        this.isReviewed = isReviewed;
    }

    public Candidate() {}


    public boolean isReviewed() {
        return isReviewed;
    }

    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public int compareTo(Object o) {
        int vote = ((Candidate)o).getVoteCount();
        return vote-this.voteCount;
    }
}
