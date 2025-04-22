import java.time.LocalDate;
import java.util.ArrayList;

public class Group {
    private int id;
    private String name;
    private int createdBy;
    private LocalDate createdDate;
    private ArrayList<String> members;
    private ArrayList<String> challenges;

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
    public String getName()
    {
        return name;
    }

    public ArrayList<String> getMembers()
    {
        return members;
    }

    public ArrayList<String> getChallenges()
    {
        return challenges;
    }

}
