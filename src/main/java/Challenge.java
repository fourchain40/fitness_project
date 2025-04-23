import java.time.LocalDate;
import java.util.ArrayList;

public class Challenge {
    private int challenge_id;
    private String challenge_name;
    private LocalDate start_date;
    private LocalDate end_date;
    private int created_by;
    private ArrayList<Member> participants;

    public Challenge(int challenge_id, String challenge_name, LocalDate start_date, LocalDate end_date, int created_by, ArrayList<Member> participants) {
        this.challenge_id = challenge_id;
        this.challenge_name = challenge_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.created_by = created_by;
        this.participants = participants;
    }

    public Challenge(int challenge_id, String challenge_name, LocalDate start_date, LocalDate end_date, int created_by) {
        this.challenge_id = challenge_id;
        this.challenge_name = challenge_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.created_by = created_by;
        this.participants = new ArrayList<>();
    }

    public Challenge(String challenge_name, LocalDate start_date, LocalDate end_date, int created_by) {
        this.challenge_id = 0;
        this.challenge_name = challenge_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.created_by = created_by;
        this.participants = new ArrayList<>();
    }

    public int getChallenge_id() {
        return challenge_id;
    }

    public void setChallenge_id(int challenge_id) {
        this.challenge_id = challenge_id;
    }

    public String getChallenge_name() {
        return challenge_name;
    }

    public void setChallenge_name(String challenge_name) {
        this.challenge_name = challenge_name;
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

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public ArrayList<Member> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Member> participants) {
        this.participants = participants;
    }

    public boolean addParticipant(Member member) {
        return participants.add(member);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Challenge challenge = (Challenge) o;

        return getChallenge_id() == challenge.getChallenge_id();
    }

    @Override
    public int hashCode() {
        return getChallenge_id();
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "challenge_id=" + challenge_id +
                ", challenge_name='" + challenge_name + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", created_by=" + created_by +
                ", participants=" + participants +
                '}';
    }
}
