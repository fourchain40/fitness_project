import java.time.LocalDate;

public class Member{
    private int member_id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String gender;
    private LocalDate date_of_birth;
    private int height;
    private int weight;
    private String bio;
    private boolean public_visibility;

    public Member()
    {
    }

    public Member(int member_id, String first_name, String last_name, String email, String password, String gender, LocalDate date_of_birth, int height, int weight, String bio, boolean public_visibility) {
        this.member_id = member_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.height = height;
        this.weight = weight;
        this.bio = bio;
        this.public_visibility = public_visibility;
    }

    public Member(int member_id, String first_name, String last_name, String email, String password, String gender, LocalDate date_of_birth, int height, int weight, String bio) {
        this.member_id = member_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.height = height;
        this.weight = weight;
        this.bio = bio;
        this.public_visibility = false;
    }

    public Member(String first_name, String last_name, String email, String password, String gender, LocalDate date_of_birth, int height, int weight, String bio) {
        this.member_id = 0;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.height = height;
        this.weight = weight;
        this.bio = bio;
        this.public_visibility = false;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isPublic_visibility() {
        return public_visibility;
    }

    public void setPublic_visibility(boolean public_visibility) {
        this.public_visibility = public_visibility;
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;

        Member member = (Member) object;

        if (getMember_id() != member.getMember_id()) return false;

        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getMember_id();
        return result;
    }

    @Override
    public String toString() {
        return "Member{" +
                "member_id=" + member_id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", date_of_birth=" + date_of_birth +
                ", height=" + height +
                ", weight=" + weight +
                ", bio='" + bio + '\'' +
                '}';
    }
}
