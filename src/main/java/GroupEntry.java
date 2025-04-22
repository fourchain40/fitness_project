import javafx.beans.property.*;
import java.util.ArrayList;

public class GroupEntry {
    private final StringProperty name;
    private final StringProperty members;
    private final StringProperty challenges;

    public GroupEntry(String name, ArrayList<String> members, ArrayList<String> challenges) {
        this.name = new SimpleStringProperty(name);
        String member_list = "";
        for(int i=0; i<members.size(); i++){
            if(i != members.size()-1){
                member_list += members.get(i) + ", ";
            }
            else{
                member_list += members.get(i);
            }
        }
        this.members = new SimpleStringProperty(member_list);
        String challenge_list = "";
        for(int i=0; i<challenges.size(); i++){
            if(i != challenges.size()-1){
                challenge_list += challenges.get(i) + ", ";
            }
            else{
                challenge_list += challenges.get(i);
            }
        }
        this.challenges = new SimpleStringProperty(challenge_list);
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty membersProperty() { return members; }
    public StringProperty challengesProperty() { return challenges; }

    public String getName() {
        return name.get();
    }
    public String getMembers() {
        return members.get();
    }
    public String getChallenges() {
        return challenges.get();
    }
}

