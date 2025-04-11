import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDriver{
    private final String postgresURL;
    private Properties postgresProps;
    private Connection connection;

    public DatabaseDriver(String postgresURL, Properties postgresProps) {
        this.postgresURL = postgresURL;
        this.postgresProps = postgresProps;
    }

    public String getPostgresURL() {
        return postgresURL;
    }

    public Properties getPostgresProps() {
        return postgresProps;
    }

    public void setPostgresProps(Properties postgresProps) {
        this.postgresProps = postgresProps;
    }

    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            throw new IllegalStateException("The connection is already opened");
        }
        connection = DriverManager.getConnection(postgresURL, postgresProps);
        connection.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    public void addMember(Member member) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                        INSERT INTO member (first_name, last_name, email, password, gender, date_of_birth, height, weight, bio)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """
            );
            preparedStatement.setString(1, member.getFirst_name());
            preparedStatement.setString(2, member.getLast_name());
            preparedStatement.setString(3, member.getEmail());
            preparedStatement.setString(4, member.getPassword());
            preparedStatement.setString(5, member.getGender());
            preparedStatement.setObject(6, member.getDate_of_bith());
            preparedStatement.setInt(7, member.getHeight());
            preparedStatement.setInt(8, member.getWeight());
            preparedStatement.setString(9, member.getBio());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }

    public List<Member> getAllMembers() throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM member");
        List<Member> members = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            Member member = buildMember(resultSet);
            members.add(member);
        }
        preparedStatement.close();
        return members;
    }

    public boolean isEmailAvailable(String email) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM member WHERE email = ?");
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (isEmpty(resultSet)) {
            return true;
        }
        preparedStatement.close();
        return false;
    }

    public Member getMemberByEmail(String email) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM member WHERE email = ?");
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Member member = buildMember(resultSet);
        preparedStatement.close();
        return member;
    }

    private Member buildMember(ResultSet resultSet) throws SQLException {
        int member_id = resultSet.getInt("member_id");
        String first_name = resultSet.getString("first_name");
        String last_name = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String gender = resultSet.getString("gender");
        LocalDate date_of_birth = resultSet.getObject("date_of_birth", LocalDate.class);
        int height = resultSet.getInt("height");
        int weight = resultSet.getInt("weight");
        String bio = resultSet.getString("bio");
        return new Member(member_id, first_name, last_name, email, password, gender, date_of_birth, height, weight, bio);
    }

    private static boolean isEmpty(ResultSet resultSet) throws SQLException {
        return !resultSet.isBeforeFirst() && resultSet.getRow() == 0;
    }
}
