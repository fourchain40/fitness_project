import java.time.LocalDate;

public class Trainer {
    private int trainer_id;
    private String first_name;
    private String last_name;
    private LocalDate date_of_birth;
    private String gender;
    private String email;
    private String password;
    private int years_of_experience;
    private String bio;
    private String specialization;

    public Trainer(int trainer_id, String first_name, String last_name, LocalDate date_of_birth, String gender, String email, String password, int years_of_experience, String bio, String specialization) {
        this.trainer_id = trainer_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.years_of_experience = years_of_experience;
        this.bio = bio;
        this.specialization = specialization;
    }

    public Trainer(String first_name, String last_name, LocalDate date_of_birth, String gender, String email, String password, int years_of_experience, String bio, String specialization) {
        this.trainer_id = 0;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.years_of_experience = years_of_experience;
        this.bio = bio;
        this.specialization = specialization;
    }

    public int getTrainer_id() {
        return trainer_id;
    }

    public void setTrainer_id(int trainer_id) {
        this.trainer_id = trainer_id;
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

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public int getYears_of_experience() {
        return years_of_experience;
    }

    public void setYears_of_experience(int years_of_experience) {
        this.years_of_experience = years_of_experience;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trainer trainer = (Trainer) o;

        return getTrainer_id() == trainer.getTrainer_id();
    }

    @Override
    public int hashCode() {
        return getTrainer_id();
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "trainer_id=" + trainer_id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", date_of_birth=" + date_of_birth +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", years_of_experience=" + years_of_experience +
                ", bio='" + bio + '\'' +
                ", specialization='" + specialization + '\'' +
                '}';
    }
}
