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
    private ArrayList<ChallengeBean> allChall = new ArrayList<ChallengeBean>();
    private ChallengeBean tempChallenge;

    //Database stuff
    private String databaseDriver = "org.apache.derby.jdbc.ClientDriver";
    private Connection connection = null;
    private String databaseName = "jdbc:derby://localhost:1527/mathplay;user=mathplay;password=mathplay";

    /** Constructor */
    public Overview() {
    	readUsers();
    }

    public ArrayList<UserBean> getAllUsers() {
        return allUsers;
    }

     public UserBean getCurrentUser() {
	String userName = new UserBean().getCurrentUser();
        for(int i=0;i<allUsers.size();i++) {
            if(allUsers.get(i).getUserName().equals(userName)) return allUsers.get(i);
        }
        return null;
    }
    /** Adds a user to the database */
    public void addUser(UserBean user, String password) {
        openConnection();
        PreparedStatement sql = null;
        PreparedStatement sql2 = null;
        PreparedStatement sql3 = null;
        System.out.println("PLS 1");
        try {
            System.out.println(connection.getAutoCommit());
            connection.setAutoCommit(true);
            sql = connection.prepareStatement("INSERT INTO users(username, password, name) VALUES (?, ?, ?)");
            sql.setString(1,user.getUserName());
            sql.setString(2,password);
            //sql.setString(3,user.getRole());
            sql.setString(3,user.getName());
            System.out.println("PLS 2");

            //Sets user role(Need to upgrade to become admin)
            sql2 = connection.prepareStatement("INSERT INTO user_roles(username, rolename) VALUES(?, ?)");
            sql2.setString(1,user.getUserName());
            System.out.println(user.getRole());
            sql2.setString(2,user.getRole());


            sql3 = connection.prepareStatement("INSERT INTO student_teacher(user_id, username) VALUES(?,?)");
            sql3.setInt(1,getCurrentUser().getUserId());
            sql3.setString(2, user.getUserName());

            sql.executeUpdate();
            sql2.executeUpdate();
            sql3.executeUpdate();
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
    // Checks if a user already exists *//*
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
            result = statement.executeQuery("SELECT * FROM users,user_roles WHERE users.username=user_roles.USERNAME");
            while(result.next()) {
                int tempUserId = result.getInt("user_id");
                String tempName = result.getString("name");
                String tempUserName = result.getString("username");
                String tempRole = result.getString("rolename");

                allUsers.add(new UserBean(tempUserId, tempName,tempUserName, tempRole));
            }
            for(int i = 0;i<allUsers.size();i++) {
                System.out.println("readUsers(): " + allUsers.get(i).getUserName());
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
            sql = connection.prepareStatement("SELECT * FROM challenge RIGHT JOIN challenge_teacher ON (CHALLENGE_TEACHER.CHALLENGE_ID = challenge.CHALLENGE_ID) RIGHT JOIN student_teacher ON (student_teacher.USER_ID = challenge_teacher.USER_ID) RIGHT JOIN challenge_type ON (challenge_type.CHALLENGE_ID = challenge.CHALLENGE_ID) WHERE student_teacher.USERNAME = ? AND NOT EXISTS (SELECT * FROM challenge_student WHERE challenge_student.USER_ID = (SELECT USER_ID FROM users WHERE USERNAME = ?) AND challenge.CHALLENGE_ID = challenge_student.CHALLENGE_ID) AND challenge.DIFFICULTY = ? AND challenge_type.CHALLENGE_TYPE = ? ORDER BY random()");
            sql.setString(1, userName);
            sql.setString(2, userName);
            sql.setInt(3, cDifficulty);
            sql.setString(4, cType);
            result = sql.executeQuery();
            connection.commit();
            result.next();
            int tempCID = result.getInt("CHALLENGE_ID");
            //int tempCID = 2;
            String tempText = result.getString("challenge_text") ;
            double tempCorrect = result.getDouble("answer");

            tempChallenge = new ChallengeBean(tempCID, tempText, tempCorrect, cDifficulty, cType);
        }catch(SQLException e) {
            Cleanup.printMessage(e, "readChallenge()");
        }

        finally {
            Cleanup.closeConnection(connection);
        }
        return tempChallenge;
    }
     public void challengeCompleted(int cid) {
       PreparedStatement sqlChangeRole = null;
        openConnection();
        boolean ok = false;
        try {
            connection.setAutoCommit(false);
            sqlChangeRole = connection.prepareStatement("INSERT INTO challenge_student (challenge_id, user_id) VALUES (?,?)");
            sqlChangeRole.setInt(1, cid);
            sqlChangeRole.setInt(2, getCurrentUser().getUserId());
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

   public int[] readScore() {
       //select * from playerinfo where user_id = ?
       int[] score = new int[5];
       openConnection();
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT * FROM playerinfo WHERE user_id = "+getCurrentUser().getUserId());
            while(result.next()) {
                score[0] = result.getInt("addition_score");
                score[1] = result.getInt("subtraction_score");
                score[2] = result.getInt("multiplication_score");
                score[3] = result.getInt("division_score");
                score[4] = result.getInt("currency_spent");
            }
        }catch(SQLException e) {
            Cleanup.printMessage(e, "readScore()");
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

   public void addChallenge(ChallengeBean chall) {
        openConnection();
        PreparedStatement sql = null;
        PreparedStatement sql2 = null;
        PreparedStatement sql3 = null;
        System.out.println("PLS 1");
        try {
            System.out.println(connection.getAutoCommit());
            connection.setAutoCommit(false);
            // Insert mÃ¥ oppdateres
            sql = connection.prepareStatement("INSERT INTO challenge(challenge_TEXT, ANSWER, DIFFICULTY) VALUES (?, ?, ?)");
            sql2 = connection.prepareStatement("INSERT INTO challenge_TYPE(challenge_TYPE) VALUES (?)");
            sql3 = connection.prepareStatement("INSERT INTO challenge_TEACHER(USER_ID) VALUES (?)");
            sql.setString(1,chall.getText());
            sql.setDouble(2,chall.getCorrect());
            sql.setInt(3,chall.getDifficulty());
            sql2.setString(1,chall.getType());
        // System.out.println("ID: " + getCurrentUser().getUserId());
             sql3.setInt(1,getCurrentUser().getUserId());
        //    System.out.println("PLS 2");
            sql.executeUpdate();
            connection.commit();
            sql2.executeUpdate();
            connection.commit();
            sql3.executeUpdate();
            connection.commit();
        }catch(SQLException e) {
            Cleanup.printMessage(e, "addChallenge()");
        }finally {

        }
    }

    public void updateChallenge(ChallengeBean chall) {
      openConnection();
      PreparedStatement sqlupdateCha = null;

      String text = chall.getText();
      double correct = chall.getCorrect();
      int difficulty = chall.getDifficulty();
      int chall_id = chall.getCID();


      try {
          connection.setAutoCommit(false);
          sqlupdateCha = connection.prepareStatement("UPDATE challenge set challenge_TEXT=?, ANSWER=?, DIFFICULTY=? WHERE challenge_id = ?");
          sqlupdateCha.setString(1, text);
          sqlupdateCha.setDouble(2, correct);
          sqlupdateCha.setInt(3, difficulty);
          sqlupdateCha.setInt(4, chall_id);
          sqlupdateCha.executeUpdate();
          connection.commit();
        } catch (SQLException e) {
	 Cleanup.printMessage(e, "addChallenge()");
        } finally {
   }
 }

    public ArrayList<ChallengeBean> getAllChallenges() {
		PreparedStatement statement1 = null;
		ResultSet res1 = null;
		openConnection();
		try {
		connection.setAutoCommit(false);
		statement1 = connection.prepareStatement("SELECT challenge_teacher.USER_ID, challenge.CHALLENGE_ID, challenge.CHALLENGE_TEXT, challenge.ANSWER, challenge.DIFFICULTY, CHALLENGE_TYPE.CHALLENGE_TYPE FROM MATHPLAY.CHALLENGE LEFT JOIN challenge_teacher ON (challenge.CHALLENGE_ID = challenge_teacher.CHALLENGE_ID) LEFT JOIN challenge_type ON (challenge.CHALLENGE_ID = challenge_type.CHALLENGE_ID) WHERE challenge_teacher.USER_ID = ? ORDER BY challenge.CHALLENGE_ID");
		statement1.setInt(1,getCurrentUser().getUserId());
		res1 = statement1.executeQuery();
		while (res1.next()) {
			int subTid = res1.getInt("USER_ID");
			int subCid = res1.getInt("CHALLENGE_ID");
			String subChall_text = res1.getString("CHALLENGE_TEXT");
			double subAnswer = res1.getDouble("ANSWER");
			int subWor = res1.getInt("DIFFICULTY");
			String subType = res1.getString("CHALLENGE_TYPE");

			ChallengeBean chall = new ChallengeBean(subCid , subChall_text, subAnswer, subWor, subType, subTid);

			allChall.add(chall);
			}
		} catch (SQLException e) {
		  System.out.println(e.toString());

		} finally {

		}

	  return allChall;
  }

     public void updateUserprofile(UserBean user, String password){
	        openConnection();
	        PreparedStatement sql = null;

	        try{
	            System.out.println(connection.getAutoCommit());
	            connection.setAutoCommit(true);
	            sql = connection.prepareStatement("UPDATE users set password=?, name=? WHERE  user_id = "+getCurrentUser().getUserId());

	            sql.setString(1, password);
	            sql.setString(2,user.getName());
	            System.out.println("updateProfile");

	            sql.executeUpdate();
	            connection.commit();

	        }catch(SQLException e){
	            Cleanup.printMessage(e, "updateProfile");
	        }finally{

	        }

    }

	/** sortBy bestemmer hvilken sortering som skal gjøres, med følgende settings:
	*	1=USERNAME, 2=ADDITION_SCORE, 3=SUBTRACTION_SCORE, 4=MULTIPLICATION_SCORE
	*   5=DIVISION_SCORE, 6=CURRENCY_SPENT
	*  asc bestemmer om den skal ASCEND eller DESCEND. True=ASCEND, FALSE=DESCEND.
	*/
	public ArrayList<UserScoresItem> userScoresItemTable(int sortBy, boolean asc) {
		ArrayList<UserScoresItem> usi = new ArrayList<UserScoresItem>();
		Statement statement = null;
		ResultSet result = null;
		openConnection();
		try {
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			if (asc) {
				if (sortBy==1) result = statement.executeQuery("select USERNAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO where users.USER_ID=playerinfo.USER_ID order by USERNAME ASC");
				else if (sortBy==2) result = statement.executeQuery("select USERNAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO where users.USER_ID=playerinfo.USER_ID order by ADDITION_SCORE ASC");
				else if (sortBy==3) result = statement.executeQuery("select USERNAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO where users.USER_ID=playerinfo.USER_ID order by SUBTRACTION_SCORE ASC");
				else if (sortBy==4) result = statement.executeQuery("select USERNAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO where users.USER_ID=playerinfo.USER_ID order by MULTIPLICATION_SCORE ASC");
				else if (sortBy==5) result = statement.executeQuery("select USERNAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO where users.USER_ID=playerinfo.USER_ID order by DIVISION_SCORE ASC");
				else result = statement.executeQuery("select USERNAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO where users.USER_ID=playerinfo.USER_ID order by CURRENCY_SPENT ASC");
			}
			else {
				if (sortBy==1) result = statement.executeQuery("select USERNAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO where users.USER_ID=playerinfo.USER_ID order by USERNAME DESC");
				else if (sortBy==2) result = statement.executeQuery("select USERNAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO where users.USER_ID=playerinfo.USER_ID order by ADDITION_SCORE DESC");
				else if (sortBy==3) result = statement.executeQuery("select USERNAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO where users.USER_ID=playerinfo.USER_ID order by SUBTRACTION_SCORE DESC");
				else if (sortBy==4) result = statement.executeQuery("select USERNAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO where users.USER_ID=playerinfo.USER_ID order by MULTIPLICATION_SCORE DESC");
				else if (sortBy==5) result = statement.executeQuery("select USERNAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO where users.USER_ID=playerinfo.USER_ID order by DIVISION_SCORE DESC");
				else result = statement.executeQuery("select USERNAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO where users.USER_ID=playerinfo.USER_ID order by CURRENCY_SPENT DESC");
			}
			while (result.next()) {
				String username = result.getString("USERNAME");
				int addScore = result.getInt("ADDITION_SCORE");
				int subScore = result.getInt("SUBTRACTION_SCORE");
				int mulScore = result.getInt("MULTIPLICATION_SCORE");
				int divScore = result.getInt("DIVISION_SCORE");
				int curUsed = result.getInt("CURRENCY_SPENT");
				usi.add(new UserScoresItem(username,addScore,subScore,mulScore,divScore,curUsed));
			}
		} catch (SQLException e) {
		  System.out.println(e.toString());
		} finally {
		}
		return usi;
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

    public static void main(String[] args) {
        Overview lol = new Overview();
        ChallengeBean chall = lol.readChallenge("Addition", 1, "andriod");
        System.out.println("Oppgave: "+chall.getText()+" , riktig svar: "+chall.getCorrect());

    }


}//OverView