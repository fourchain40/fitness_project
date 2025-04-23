import java.time.LocalDate;

public class Workout {
    private int log_id;
    private int member_id;
    private int plan_id;
    private String plan_name;
    private LocalDate workout_date;
    private int duration_minutes;
    private String notes;

    public Workout(int log_id, int member_id, int plan_id, String plan_name, LocalDate workout_date, int duration_minutes, String notes) {
        this.log_id = log_id;
        this.member_id = member_id;
        this.plan_id = plan_id;
        this.plan_name = plan_name;
        this.workout_date = workout_date;
        this.duration_minutes = duration_minutes;
        this.notes = notes;
    }

    public Workout(int log_id, int member_id, int plan_id, LocalDate workout_date, int duration_minutes, String notes) {
        this.log_id = log_id;
        this.member_id = member_id;
        this.plan_id = plan_id;
        this.plan_name = null;
        this.workout_date = workout_date;
        this.duration_minutes = duration_minutes;
        this.notes = notes;
    }

    public Workout(int member_id, int plan_id, LocalDate workout_date, int duration_minutes, String notes) {
        this.log_id = 0;
        this.member_id = member_id;
        this.plan_id = plan_id;
        this.plan_name = null;
        this.workout_date = workout_date;
        this.duration_minutes = duration_minutes;
        this.notes = notes;
    }

    public Workout(int member_id, LocalDate workout_date, int duration_minutes, String notes) {
        this.log_id = 0;
        this.member_id = member_id;
        this.plan_id = 0;
        this.plan_name = null;
        this.workout_date = workout_date;
        this.duration_minutes = duration_minutes;
        this.notes = notes;
    }

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public int getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(int plan_id) {
        this.plan_id = plan_id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public LocalDate getWorkout_date() {
        return workout_date;
    }

    public void setWorkout_date(LocalDate workout_date) {
        this.workout_date = workout_date;
    }

    public int getDuration_minutes() {
        return duration_minutes;
    }

    public void setDuration_minutes(int duration_minutes) {
        this.duration_minutes = duration_minutes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Workout workout = (Workout) o;

        return getLog_id() == workout.getLog_id();
    }

    @Override
    public int hashCode() {
        return getLog_id();
    }

    @Override
    public String toString() {
        return "Workout{" +
                "log_id=" + log_id +
                ", member_id=" + member_id +
                ", plan_id=" + plan_id +
                ", workout_date=" + workout_date +
                ", duration_minutes=" + duration_minutes +
                ", notes='" + notes + '\'' +
                '}';
    }
}
