public class MemberStats {
    private int member_id;
    private int total_workouts;
    private int total_minutes;
    private double avg_duration;

    public MemberStats(int member_id, int total_workouts, int total_minutes, double avg_duration) {
        this.member_id = member_id;
        this.total_workouts = total_workouts;
        this.total_minutes = total_minutes;
        this.avg_duration = avg_duration;
    }

    public MemberStats(int total_workouts, int total_minutes, double avg_duration) {
        this.member_id = 0;
        this.total_workouts = total_workouts;
        this.total_minutes = total_minutes;
        this.avg_duration = avg_duration;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public int getTotal_workouts() {
        return total_workouts;
    }

    public void setTotal_workouts(int total_workouts) {
        this.total_workouts = total_workouts;
    }

    public int getTotal_minutes() {
        return total_minutes;
    }

    public void setTotal_minutes(int total_minutes) {
        this.total_minutes = total_minutes;
    }

    public double getAvg_duration() {
        return avg_duration;
    }

    public void setAvg_duration(double avg_duration) {
        this.avg_duration = avg_duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberStats that = (MemberStats) o;

        if (getTotal_workouts() != that.getTotal_workouts()) return false;
        if (getTotal_minutes() != that.getTotal_minutes()) return false;
        return Double.compare(getAvg_duration(), that.getAvg_duration()) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getTotal_workouts();
        result = 31 * result + getTotal_minutes();
        temp = Double.doubleToLongBits(getAvg_duration());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "MemberStats{" +
                "total_workouts=" + total_workouts +
                ", total_minutes=" + total_minutes +
                ", avg_duration=" + avg_duration +
                '}';
    }
}
