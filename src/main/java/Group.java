import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Group {
    private int id;
    private String name;
    private int createdBy;
    private LocalDate createdDate;
    private List<String> members;
    private List<String> challenges;

    public Group(int id, String name, int createdBy, LocalDate createdDate, ArrayList<String> members, ArrayList<String> challenges) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.members = new ArrayList<String>();
        for(String member: members)
        {
            this.members.add(member);
        }
        this.challenges = new ArrayList<String>();
        for(String challenge: challenges)
        {
            this.challenges.add(challenge);
        }
    }

    public Group(String name, int createdBy, LocalDate createdDate, ArrayList<String> members, ArrayList<String> challenges) {
        this.id = 0;
        this.name = name;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.members = new ArrayList<String>();
        for(String member: members)
        {
            this.members.add(member);
        }
        this.challenges = new ArrayList<String>();
        for(String challenge: challenges)
        {
            this.challenges.add(challenge);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public List<String> getChallenges() {
        return challenges;
    }

    public void setChallenges(ArrayList<String> challenges) {
        this.challenges = challenges;
    }
}
