package mathplay;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.InitialContext;

/**
 *
 * @author Bertie
 */
public class Overview {
    private ArrayList<UserBean> allUsers = new ArrayList<UserBean>();
    //private ArrayList<UserBean> allChallenges = new ArrayList<ChallengeBean>();
    private ChallengeBean tempChallenge;

    //Database stuff
    private String databaseDriver = "org.apache.derby.jdbc.ClientDriver";
    private Connection connection = null;
    private String databaseName = "jdbc:derby://localhost:1527/mathplay;user=admin;password=admin";

    /** Constructor */
    public Overview() {
    //readUsers();
    }

    public ArrayList<UserBean> getAllUsers() {
        return allUsers;
    }

    public UserBean getCurrentUser() {
        for(int i=0;i<allUsers.size();i++) {
            if(allUsers.get(i).getUserId() == new UserBean().getCurrentUser()) return allUsers.get(i);
        }
        return null;
    }
    /** Adds a user to the database */
    public void addUser(UserBean user, String password) {
        openConnection();
        PreparedStatement sql = null;
        PreparedStatement sql2 = null;
        System.out.println("PLS 1");
        try {
            System.out.println(connection.getAutoCommit());
            connection.setAutoCommit(true);
            sql = connection.prepareStatement("INSERT INTO usertable(userid, name, username, grade, password) VALUES (?, ?, ?, ?)");
            sql.setString(1,user.getName());
            sql.setString(2,user.getUserName());
            //sql.setString(3,user.getRole());
            sql.setString(3,user.getGrade());
            sql.setString(4,password);
            System.out.println("PLS 2");

            //Sets user role(Need to upgrade to become admin)
            sql2 = connection.prepareStatement("INSERT INTO roles(username, role) VALUES(?,?)");
            sql2.setString(1,user.getUserName());
            System.out.println(user.getRole());
            sql2.setString(2,user.getRole());
            sql.executeUpdate();
            sql2.executeUpdate();
            connection.commit();
        }catch(SQLException e) {
            Cleanup.printMessage(e, "addUser()");
        }finally {
           readUsers();
        }
    }

    /** Changes user role */
    public void changeRole(UserBean u) {
        PreparedStatement sqlChangeRole = null;
        openConnection();
        boolean ok = false;
        try {
            connection.setAutoCommit(false);
            sqlChangeRole = connection.prepareStatement("UPDATE roletable SET role=? WHERE username = ?");
            sqlChangeRole.setString(1, u.getRole());
            sqlChangeRole.setString(2, u.getName());
            sqlChangeRole.executeUpdate();
            connection.commit();
            ok = true;
        }catch(SQLException e) {
            Cleanup.rollBack(connection);
            String sqlStatus = e.getSQLState().trim();
            String statusClass = sqlStatus.substring(0, 2);
            if(statusClass.equals("23")) {
                ok = false;
            }else Cleanup.printMessage(e, "changeRole()");
        }finally {
            Cleanup.closeConnection(connection);
            //TODO: add a call to readUsers
        }


    }
    /** Checks if a user already exists */
    public boolean checkUser(String username) {
        openConnection();
        Statement statement = null;
        ResultSet result = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT * FROM usertable WHERE username ='"+username+"'");
            while(result.next()) {
                if(username.equals(result.getString("username"))) return true;
                connection.commit(); //I doubt this one is really needed(autoCommit)
            }

        }catch(SQLException e) {
            Cleanup.printMessage(e, "checkUser()");
        }finally {
            Cleanup.closeConnection(connection);
        }return false;
    }

    public void readUsers() {
        openConnection();
        allUsers.clear();
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT * FROM usertable, roles WHERE roles.role = 'pupil'");
            while(result.next()) {
                int tempUserId = result.getInt("userid");
                String tempName = result.getString("name");
                String tempUserName = result.getString("username");
                String tempRole = result.getString("role");
                String tempGrade = result.getString("grade");

                allUsers.add(new UserBean(tempUserId, tempName,tempUserName, tempRole, tempGrade));
            }
            for(int i = 0;i<allUsers.size();i++) {
                System.out.println("User: " + allUsers.get(i).getUserName());
            }
        }catch(SQLException e) {
            Cleanup.printMessage(e, "readUsers()");
        }

    }

    public ChallengeBean getChallenge(String cType, int cDifficulty, int teacher_id) {
        openConnection();
        Statement statement = null;
        ResultSet result = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT * AS RANDOM FROM usertable WHERE challengetype ='"+cType+"' AND difficulty = '"+cDifficulty+"' AND '"+teacher_id+"'");//legges til:(Og challengeId IKKE er koblet til currentUser)

            int tempCID = result.getInt("CID");
            String tempText = result.getString("text") ;
            double tempCorrect = result.getDouble("correct");
            int tempDifficulty = result.getInt("difficulty");
            String tempType = result.getString("type");

            tempChallenge = new ChallengeBean(tempCID, tempText, tempCorrect, tempDifficulty, tempType);
        }catch(SQLException e) {
            Cleanup.printMessage(e, "checkUser()");
        }

        finally {
            Cleanup.closeConnection(connection);
        }
        return tempChallenge;
    }

   public void addChallenge(String tid, ChallengeBean chall) {
        openConnection();
        PreparedStatement sql = null;

        System.out.println("PLS 1");
        try {
            System.out.println(connection.getAutoCommit());
            connection.setAutoCommit(true);
            sql = connection.prepareStatement("INSERT INTO challenge(Teacher_Id, Challenge_Id, Challenge_dis, Svar, Grad, Oppgavetype) VALUES (?, DEFAULT, ?, ?, ?, ?)");
            sql.setString(1,tid);
            sql.setString(2,chall.getText());
            sql.setDouble(3,chall.getCorrect());
            sql.setInt(4,chall.getDifficulty());
            sql.setString(5,chall.getType());
            System.out.println("PLS 2");
            sql.executeUpdate();
            connection.commit();
        }catch(SQLException e) {
            Cleanup.printMessage(e, "addChallenge()");
        }finally {

        }
    }

    /** Opens a connection to the database */
    public void openConnection() {
       try {
           Class.forName(databaseDriver);
           connection = DriverManager.getConnection(databaseName);
           InitialContext cxt = new InitialContext();
           if(cxt == null) {
               throw new Exception("Uh oh -- no context!");
           }
       }catch (Exception e) {
           Cleanup.printMessage(e, "openConnection()");
       }
    }


}//OverView