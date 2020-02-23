package co.nexus.votingapp.Helpers;

public class Candidate implements Comparable{
    String name, dob, gender, department, imgPath, party;
    int yearOfStudy, voteCount;

    public Candidate(String name, String dob, String gender, String department, String imgPath, int yearOfStudy, int voteCount ,String party) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.department = department;
        this.imgPath = imgPath;
        this.yearOfStudy = yearOfStudy;
        this.voteCount = voteCount;
        this.party = party;
    }

    public Candidate() {}

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
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
