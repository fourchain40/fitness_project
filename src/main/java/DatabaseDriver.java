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

    // Member operations

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

    public List<Member> getAllPublicMembers() throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM member WHERE public_visibility = TRUE");
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

    public boolean isMemberEmailAvailable(String email) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM member WHERE email = ?");
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (isEmpty(resultSet)) {
            preparedStatement.close();
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

    // Trainer operations

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

    public List<Trainer> getAllTrainers() throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM trainer");
        List<Trainer> trainers = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            Trainer trainer = buildTrainer(resultSet);
            trainers.add(trainer);
        }
        preparedStatement.close();
        return trainers;
    }

    public void addTrainer(Trainer trainer) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                        INSERT INTO trainer (first_name, last_name, date_of_birth, gender, email, password, years_of_experience, bio, specialization)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """
            );
            preparedStatement.setString(1, trainer.getFirst_name());
            preparedStatement.setString(2, trainer.getLast_name());
            preparedStatement.setObject(3, trainer.getDate_of_birth());
            preparedStatement.setString(4, trainer.getGender());
            preparedStatement.setString(5, trainer.getEmail());
            preparedStatement.setString(6, trainer.getPassword());
            preparedStatement.setInt(7, trainer.getYears_of_experience());
            preparedStatement.setString(8, trainer.getBio());
            preparedStatement.setString(9, trainer.getSpecialization());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
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

    public boolean isTrainerEmailAvailable(String email) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM trainer WHERE email = ?");
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (isEmpty(resultSet)) {
            preparedStatement.close();
            return true;
        }
        preparedStatement.close();
        return false;
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

    // Administrator operations

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

    // Plan operations

    public List<Plan> getPlansByMemberID(int member_id) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM workoutplan WHERE member_id = ?");
        preparedStatement.setInt(1, member_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Plan> plans = new ArrayList<>();
        while(resultSet.next()) {
            Plan plan = buildPlan(resultSet);
            plans.add(plan);
        }
        preparedStatement.close();
        return plans;
    }

    private Plan buildPlan(ResultSet resultSet) throws SQLException {
        int plan_id = resultSet.getInt("plan_id");
        int member_id = resultSet.getInt("member_id");
        int trainer_id = resultSet.getInt("trainer_id");
        String plan_name = resultSet.getString("plan_name");
        LocalDate start_date = resultSet.getObject("start_date", LocalDate.class);
        LocalDate end_date = resultSet.getObject("end_date", LocalDate.class);
        String description = resultSet.getString("description");
        return new Plan(plan_id, member_id, trainer_id, plan_name, start_date, end_date, description);
    }

    // Workout operations

    public void logWorkout(Workout workout) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                        INSERT INTO workoutlog(member_id, plan_id, workout_date, duration_minutes, notes)
                        VALUES (?, ?, ?, ?, ?)
                        """
            );
            preparedStatement.setInt(1, workout.getMember_id());
            if (workout.getPlan_id() == 0) {
                preparedStatement.setNull(2, Types.INTEGER);
            } else {
                preparedStatement.setInt(2, workout.getPlan_id());
            }
            preparedStatement.setObject(3, workout.getWorkout_date());
            preparedStatement.setInt(4, workout.getDuration_minutes());
            preparedStatement.setString(5, workout.getNotes());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }

    public List<Workout> getWorkoutHistoryByID(int member_id) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement(
                """
                SELECT *
                FROM WorkoutLog wl
                LEFT JOIN WorkoutPlan wp ON wl.plan_id = wp.plan_id
                WHERE wl.member_id = ?
                ORDER BY wl.workout_date DESC
            """
        );
        preparedStatement.setInt(1, member_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Workout> workouts = new ArrayList<>();
        while(resultSet.next()) {
            Workout workout = buildWorkout(resultSet);
            workouts.add(workout);
        }
        preparedStatement.close();
        return workouts;
    }

    public void deleteWorkoutByID(int log_id) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                    DELETE FROM workoutlog
                    WHERE log_id = ?
                    """
            );
            preparedStatement.setInt(1, log_id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }

    private Workout buildWorkout(ResultSet resultSet) throws SQLException {
        int log_id = resultSet.getInt("log_id");
        int member_id = resultSet.getInt("member_id");
        int plan_id = resultSet.getInt("plan_id");
        String plan_name = null;
        if (resultSet.getMetaData().getColumnCount() > 6) {
            plan_name = resultSet.getString("plan_name");
        }
        LocalDate workout_date = resultSet.getObject("workout_date", LocalDate.class);
        int duration_minutes = resultSet.getInt("duration_minutes");
        String notes = resultSet.getString("notes");
        return new Workout(log_id, member_id, plan_id, plan_name, workout_date, duration_minutes, notes);
    }

    // MemberStats operations

    public Optional<MemberStats> getMemberStatsByID(int member_id) throws SQLException{
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement(
                        """
                        SELECT COUNT(*) AS total_workouts,
                        SUM(duration_minutes) AS total_minutes, 
                        AVG(duration_minutes) AS avg_duration
                        FROM workoutlog
                        WHERE member_id = ?
                        """
        );
        preparedStatement.setInt(1, member_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (isEmpty(resultSet)) {
            preparedStatement.close();
            return Optional.empty();
        }
        resultSet.next();
        MemberStats stats = buildMemberStats(resultSet);
        stats.setMember_id(member_id);
        preparedStatement.close();
        return Optional.of(stats);
    }

    private MemberStats buildMemberStats(ResultSet resultSet) throws SQLException {
        int total_workouts = resultSet.getInt("total_workouts");
        int total_minutes = resultSet.getInt("total_minutes");
        double avg_duration = resultSet.getDouble("avg_duration");
        return new MemberStats(total_workouts, total_minutes, avg_duration);
    }

    // Group operations

    public void addGroup(Group group) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                        INSERT INTO fitnessgroup (group_name, created_date, created_by)
                        VALUES (?, ?, ?)
                        """
            );
            preparedStatement.setString(1, group.getName());
            preparedStatement.setDate(2, Date.valueOf(group.getCreatedDate()));
            preparedStatement.setInt(3, group.getCreatedBy());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }

    public Group getGroupByName(String name) throws SQLException
    {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement(
                """
                    SELECT *
                    FROM fitnessgroup
                    WHERE group_name = ?
                    """
        );
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        Group group = null;
        if(resultSet.next()) {
            group = buildGroup(resultSet);
        }
        return group;
    }

    public void addMembersToGroup(Group group, List<Member> members) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        for (Member member : members) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        """
                            INSERT INTO groupmembership (group_id, member_id)
                            VALUES (?, ?)
                            """
                );
                preparedStatement.setInt(1, group.getId());
                preparedStatement.setInt(2, member.getMember_id());
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                rollback();
                throw e;
            }
        }
    }

    public List<Group> getGroupsByMemberID(int member_id) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement(
                """
                    SELECT *
                    FROM fitnessgroup
                    WHERE group_id IN (SELECT DISTINCT group_id FROM groupmembership WHERE member_id = ?)
                    """
        );
        preparedStatement.setInt(1, member_id);
        List<Group> groups = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            Group group = buildGroup(resultSet);
            groups.add(group);
        }
        preparedStatement.close();
        return groups;
    }

    public List<Group> getGroupsNotIn(int member_id) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement(
                """
                    SELECT *
                    FROM fitnessgroup
                    WHERE group_id NOT IN (SELECT DISTINCT group_id FROM groupmembership WHERE member_id = ?)
                    """
        );
        preparedStatement.setInt(1, member_id);
        List<Group> groups = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            Group group = buildGroup(resultSet);
            groups.add(group);
        }
        preparedStatement.close();
        return groups;
    }

    public List<Member> getMembersInGroup(Group group) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement(
                """
                    SELECT *
                    FROM member
                    WHERE member_id IN (SELECT DISTINCT member_id FROM groupmembership WHERE group_id = ?)
                    """
        );
        preparedStatement.setInt(1, group.getId());
        List<Member> members = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            Member member = buildMember(resultSet);
            members.add(member);
        }
        preparedStatement.close();
        return members;
    }

    public void deleteMemberfromGroup(int group_id, int member_id) throws SQLException
    {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                    DELETE FROM groupmembership
                    WHERE group_id = ? AND member_id = ?
                    """
            );
            preparedStatement.setInt(1, group_id);
            preparedStatement.setInt(2, member_id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            rollback();
            throw e;
        }

    }

    private Group buildGroup(ResultSet resultSet) throws SQLException {
        int group_id = resultSet.getInt("group_id");
        String group_name = resultSet.getString("group_name");
        LocalDate created_date = resultSet.getObject("created_date", LocalDate.class);
        int created_by = resultSet.getInt("created_by");
        Group newGroup = new Group(group_id, group_name, created_by, created_date, new ArrayList<>(), new ArrayList<>());
        List<Member> members = getMembersInGroup(newGroup);
        List<Challenge> challenges = getChallengesByGroupID(group_id);
        ArrayList<String> memberNames = new ArrayList<String>();
        for(Member member : members) {
            memberNames.add(member.getFirst_name() + " " + member.getLast_name());
        }
        newGroup.setMembers(memberNames);
        ArrayList<String> challengeNames = new ArrayList<String>();
        for(Challenge challenge : challenges) {
            challengeNames.add(challenge.getChallenge_name());
        }
        newGroup.setChallenges(challengeNames);

        return newGroup;
    }

    // Challenge operations

    public void addChallenge(Challenge challenge) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                        INSERT INTO challenge (challenge_name, start_date, end_date, created_by, group_id)
                        VALUES (?, ?, ?, ?, ?)
                        """
            );
            preparedStatement.setString(1, challenge.getChallenge_name());
            preparedStatement.setDate(2, Date.valueOf(challenge.getStart_date()));
            preparedStatement.setDate(3, Date.valueOf(challenge.getEnd_date()));
            preparedStatement.setInt(4, challenge.getCreated_by());
            preparedStatement.setInt(5, challenge.getGroup_id());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }

    public void addMembersToChallenge(int challenge_id, List<Member> members) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        for (Member member : members) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        """
                            INSERT INTO challengeparticipation (challenge_id, member_id, points)
                            VALUES (?, ?, 0)
                            """
                );
                preparedStatement.setInt(1, challenge_id);
                preparedStatement.setInt(2, member.getMember_id());
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                rollback();
                throw e;
            }
        }
    }

    public void updateChallengeMemberPoints(Challenge challenge, Member member, int new_points) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                        UPDATE challengeparticipation
                        SET points = ?
                        WHERE challenge_id = ? AND member_id = ?
                        """
            );

            preparedStatement.setInt(1, new_points);
            preparedStatement.setInt(2, challenge.getChallenge_id());
            preparedStatement.setInt(3, member.getMember_id());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }

    public List<Challenge> getChallengesByMemberID(int member_id) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement(
                """
                    SELECT *
                    FROM challenge
                    WHERE challenge_id IN (SELECT DISTINCT challenge_id FROM challengeparticipation WHERE member_id = ?)
                    """
        );
        preparedStatement.setInt(1, member_id);
        List<Challenge> challenges = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            Challenge challenge = buildChallenge(resultSet);
            challenges.add(challenge);
        }
        preparedStatement.close();
        return challenges;
    }

    public List<Challenge> getChallengesByGroupID(int group_id) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement(
                """
                    SELECT *
                    FROM challenge
                    WHERE group_id = ?
                    """
        );
        preparedStatement.setInt(1, group_id);
        List<Challenge> challenges = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            Challenge challenge = buildChallenge(resultSet);
            challenges.add(challenge);
        }
        preparedStatement.close();
        return challenges;
    }

    public List<Member> getMembersInChallenge(Challenge challenge) throws SQLException {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement(
                """
                    SELECT *
                    FROM member
                    WHERE member_id IN (SELECT DISTINCT member_id FROM challengeparticipation WHERE challenge_id = ?)
                    """
        );
        preparedStatement.setInt(1, challenge.getChallenge_id());
        List<Member> members = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            Member member = buildMember(resultSet);
            members.add(member);
        }
        preparedStatement.close();
        return members;
    }

    public int getChallengeIDByNameandGroup(String name, int group_id) throws SQLException
    {
        if(connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement(
                """
                    SELECT *
                    FROM challenge
                    WHERE challenge_name = ? AND group_id = ?
                    """
        );
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, group_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        int challenge_id = 0;
        if(resultSet.next()) {
            challenge_id = resultSet.getInt("challenge_id");
        }
        return challenge_id;
    }

    private Challenge buildChallenge(ResultSet resultSet) throws SQLException {
        int challenge_id = resultSet.getInt("challenge_id");
        String challenge_name = resultSet.getString("challenge_name");
        LocalDate start_date = resultSet.getObject("start_date", LocalDate.class);
        LocalDate end_date = resultSet.getObject("end_date", LocalDate.class);
        int created_by = resultSet.getInt("created_by");
        return new Challenge(challenge_id, challenge_name, start_date, end_date, created_by);
    }

    private static boolean isEmpty(ResultSet resultSet) throws SQLException {
        return !resultSet.isBeforeFirst() && resultSet.getRow() == 0;
    }
}
