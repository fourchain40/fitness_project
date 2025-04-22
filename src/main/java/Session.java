public class Session {
    private static Session instance;
    private int userID;
    private String role;
    private DatabaseDriver databaseDriver;

    private Session() {
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setUserID(int uid) {
        userID = uid;
    }

    public void setRole(String r)
    {
        role = r;
    }

    public void setDatabaseDriver(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
    }

    public int getUserID()
    {return userID;}

    public String getRole()
    {
        return role;
    }

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }
}