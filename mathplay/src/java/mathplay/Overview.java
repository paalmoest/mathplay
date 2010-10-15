/************************************************************************
*      ___   __  __     __   _ __   __  __ /\_\     __   __  __  __     *
*     / __`\/\ \/\ \  /'__`\/\`'__\/\ \/\ \\/\ \  /'__`\/\ \/\ \/\ \    *
*    /\ \L\ \ \ \_/ |/\  __/\ \ \/ \ \ \_/ |\ \ \/\  __/\ \ \_/ \_/ \   *
*    \ \____/\ \___/ \ \____\\ \_\  \ \___/  \ \_\ \____\\ \___x___/'   *
*     \/___/  \/__/   \/____/ \/_/   \/__/    \/_/\/____/ \/__//__/     *
*                                                                       *
*************************************************************************
**/

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
    private String databaseName = "jdbc:derby://localhost:1527/mathplay;user=mathplay;password=mathplay";

    /** Constructor */
    public Overview() {
    //readUsers();
    }

    public ArrayList<UserBean> getAllUsers() {
        return allUsers;
    }

    public UserBean getCurrentUser() {
        for(int i=0;i<allUsers.size();i++) {
            if(allUsers.get(i).getUserName().equals(new UserBean().getCurrentUser())) return allUsers.get(i);
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
            sqlChangeRole = connection.prepareStatement("UPDATE user_roles SET rolename=? WHERE username = ?");
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

    public ChallengeBean readChallenge(String cType, int cDifficulty, String userName) {
        openConnection();
        Statement statement = null;
        PreparedStatement sql = null;
        ResultSet result;
        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            sql = connection.prepareStatement("select challenge.CHALLENGE_ID, challenge.CHALLENGE_TEXT, challenge.ANSWER from challenge left join challenge_teacher on challenge.challenge_id = challenge_teacher.CHALLENGE_ID left join challenge_type on (challenge.CHALLENGE_ID = challenge_type.CHALLENGE_ID) left join challenge_student on (challenge.CHALLENGE_ID = challenge_student.CHALLENGE_ID) where challenge.DIFFICULTY = ? AND challenge_teacher.USER_ID = (SELECT USER_ID FROM STUDENT_TEACHER WHERE username = ?) AND challenge_type.CHALLENGE_TYPE = ? AND CHALLENGE_STUDENT.CHALLENGE_ID IS NULL ORDER BY random()");
            sql.setInt(1, cDifficulty);
            sql.setString(2, userName);
            sql.setString(3, cType);
            result = sql.executeQuery();
            connection.commit();
            result.next();
            int tempCID = result.getInt("challenge_id");
            String tempText = result.getString("challenge_text") ;
            double tempCorrect = result.getDouble("answer");
            
            tempChallenge = new ChallengeBean(tempCID, tempText, tempCorrect, cDifficulty, cType);
        }catch(SQLException e) {
            Cleanup.printMessage(e, "checkUser()");
        }

        finally {
            Cleanup.closeConnection(connection);
        }
        return tempChallenge;
    }
    /** Flag a challenge as completed for a given userId */
    public void challengeCompleted(int cid) {
       PreparedStatement sqlChangeRole = null;
        openConnection();
        boolean ok = false;
        try {
            connection.setAutoCommit(false);
            sqlChangeRole = connection.prepareStatement("INSERT INTO challenge_student (challenge_id, user_id) VALUES (?,?)");
            sqlChangeRole.setInt(1, cid);
            sqlChangeRole.setInt(2, new UserBean().getUserId());
            sqlChangeRole.executeUpdate();
            connection.commit();
            ok = true;
        }catch(SQLException e) {
            Cleanup.rollBack(connection);
            String sqlStatus = e.getSQLState().trim();
            String statusClass = sqlStatus.substring(0, 2);
            if(statusClass.equals("23")) {
                ok = false;
            }else Cleanup.printMessage(e, "challengeCompleted");
        }finally {
            Cleanup.closeConnection(connection);
            //TODO: add a call to readUsers
        }

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

   public int[] readScore() {
       //select * from playerinfo where user_id = ?
       int[] score = new int[5];
       openConnection();
        allUsers.clear();
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT * FROM playerinfo WHERE user_id = "+getCurrentUser().getUserId());
            while(result.next()) {
                int tempUserId = result.getInt("userid");
                score[0] = result.getInt("addition_score");
                score[1] = result.getInt("subtraction_score");
                score[2] = result.getInt("multiplication_score");
                score[3] = result.getInt("division_score");
                score[4] = result.getInt("currency_spent");
            }
        }catch(SQLException e) {
            Cleanup.printMessage(e, "readUsers()");
        }
        return score;
   }

   public void changeScore(int[] score) {
        PreparedStatement sqlChangeScore = null;
        openConnection();
        boolean ok = false;
        try {
            connection.setAutoCommit(false);
            sqlChangeScore = connection.prepareStatement("UPDATE playerinfo SET addition_score=?,subtraction_score=?,multiplication_score=?, division_score=?, currency_spent=? WHERE user_id = ?");
            sqlChangeScore.setInt(1, score[0]);
            sqlChangeScore.setInt(2, score[1]);
            sqlChangeScore.setInt(3, score[2]);
            sqlChangeScore.setInt(4, score[3]);
            sqlChangeScore.setInt(5, score[4]);
            sqlChangeScore.setInt(6, getCurrentUser().getUserId());
            sqlChangeScore.executeUpdate();
            connection.commit();
            ok = true;
        }catch(SQLException e) {
            Cleanup.rollBack(connection);
            String sqlStatus = e.getSQLState().trim();
            String statusClass = sqlStatus.substring(0, 2);
            if(statusClass.equals("23")) {
                ok = false;
            }else Cleanup.printMessage(e, "changeScore()");
        }finally {
            Cleanup.closeConnection(connection);
            //TODO: add a call to readUsers
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

   /* public static void main(String[] args) {
        Overview lol = new Overview();
        ChallengeBean chall = lol.getChallenge("Addisjon", 1, "andriod");
        System.out.println("Oppgave: "+chall.getText()+" , riktig svar: "+chall.getCorrect());
        
    }*/


}//OverView