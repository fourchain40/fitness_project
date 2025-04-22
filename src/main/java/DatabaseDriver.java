import javax.sound.midi.MetaMessage;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
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
            preparedStatement.setObject(6, member.getDate_of_birth());
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

    public Member getMemberByID(int member_id) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM member WHERE member_id = ?");
        preparedStatement.setInt(1, member_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Member member = buildMember(resultSet);
        preparedStatement.close();
        return member;
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

    public Optional<Member> memberAuthenticated(String email, String password) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM member WHERE email = ? AND password = ?");
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (isEmpty(resultSet)) {
            preparedStatement.close();
            return Optional.empty();
        }
        resultSet.next();
        Member member = buildMember(resultSet);
        preparedStatement.close();
        return Optional.of(member);
    }

    public void updateMember(Member member) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                        UPDATE member
                        SET first_name = ?, last_name = ?, gender = ?, date_of_birth = ?, height = ?, weight = ?, bio = ?
                        WHERE member_id = ?
                        """
            );
            preparedStatement.setString(1, member.getFirst_name());
            preparedStatement.setString(2, member.getLast_name());
            preparedStatement.setString(3, member.getGender());
            preparedStatement.setObject(4, member.getDate_of_birth());
            preparedStatement.setInt(5, member.getHeight());
            preparedStatement.setInt(6, member.getWeight());
            preparedStatement.setString(7, member.getBio());
            preparedStatement.setInt(8, member.getMember_id());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
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

    public Trainer getTrainerByID(int trainer_id) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM trainer WHERE trainer_id = ?");
        preparedStatement.setInt(1, trainer_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Trainer trainer = buildTrainer(resultSet);
        preparedStatement.close();
        return trainer;
    }

    public Optional<Trainer> trainerAuthenticated(String email, String password) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM trainer WHERE email = ? AND password = ?");
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (isEmpty(resultSet)) {
            preparedStatement.close();
            return Optional.empty();
        }
        resultSet.next();
        Trainer trainer = buildTrainer(resultSet);
        preparedStatement.close();
        return Optional.of(trainer);
    }

    private Trainer buildTrainer(ResultSet resultSet) throws SQLException {
        int trainer_id = resultSet.getInt("trainer_id");
        String first_name = resultSet.getString("first_name");
        String last_name = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String gender = resultSet.getString("gender");
        LocalDate date_of_birth = resultSet.getObject("date_of_birth", LocalDate.class);
        int years_of_experience = resultSet.getInt("years_of_experience");
        String bio = resultSet.getString("bio");
        String specialization = resultSet.getString("specialization");
        return new Trainer(trainer_id, first_name, last_name, date_of_birth, gender, email, password, years_of_experience, bio, specialization);
    }

    public Administrator getAdminByID(int admin_id) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM administrator WHERE admin_id = ?");
        preparedStatement.setInt(1, admin_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Administrator admin = buildAdmin(resultSet);
        preparedStatement.close();
        return admin;
    }

    public Optional<Administrator> adminAuthenticated(String email, String password) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM administrator WHERE email = ? AND password = ?");
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (isEmpty(resultSet)) {
            preparedStatement.close();
            return Optional.empty();
        }
        resultSet.next();
        Administrator admin = buildAdmin(resultSet);
        preparedStatement.close();
        return Optional.of(admin);
    }

    private Administrator buildAdmin(ResultSet resultSet) throws SQLException {
        int admin_id = resultSet.getInt("admin_id");
        String first_name = resultSet.getString("first_name");
        String last_name = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        return new Administrator(admin_id, first_name, last_name, email, password);
    }

    private static boolean isEmpty(ResultSet resultSet) throws SQLException {
        return !resultSet.isBeforeFirst() && resultSet.getRow() == 0;
    }
}
