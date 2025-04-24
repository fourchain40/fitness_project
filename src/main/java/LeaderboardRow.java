public class LeaderboardRow {
    private int rank;
    private String name;
    private int score;

    public LeaderboardRow(int rank, String name, int score) {
        this.rank = rank;
        this.name = name;
        this.score = score;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank()
    {
        return rank;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore()
    {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
