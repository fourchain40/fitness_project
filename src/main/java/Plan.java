import java.time.LocalDate;

public class Plan {
    private int plan_id;
    private int member_id;
    private int trainer_id;
    private String plan_name;
    private LocalDate start_date;
    private LocalDate end_date;
    private String description;

    public Plan(int plan_id, int member_id, int trainer_id, String plan_name, LocalDate start_date, LocalDate end_date, String description) {
        this.plan_id = plan_id;
        this.member_id = member_id;
        this.trainer_id = trainer_id;
        this.plan_name = plan_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.description = description;
    }

    public Plan(int member_id, int trainer_id, String plan_name, LocalDate start_date, LocalDate end_date, String description) {
        this.plan_id = 0;
        this.member_id = member_id;
        this.trainer_id = trainer_id;
        this.plan_name = plan_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.description = description;
    }

    public int getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(int plan_id) {
        this.plan_id = plan_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public int getTrainer_id() {
        return trainer_id;
    }

    public void setTrainer_id(int trainer_id) {
        this.trainer_id = trainer_id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plan plan = (Plan) o;

        return getPlan_id() == plan.getPlan_id();
    }

    @Override
    public int hashCode() {
        return getPlan_id();
    }

    @Override
    public String toString() {
        return "Plan{" +
                "plan_id=" + plan_id +
                ", member_id=" + member_id +
                ", trainer_id=" + trainer_id +
                ", plan_name='" + plan_name + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", description='" + description + '\'' +
                '}';
    }
}
