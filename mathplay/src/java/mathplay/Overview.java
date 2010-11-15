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
import java.util.*;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.*;


public class Overview {
    private ArrayList<UserBean> allUsers = new ArrayList<UserBean>();
    private ArrayList<UserBean> teacherUsers = new ArrayList<UserBean>();
    private ArrayList<ChallengeBean> allChall = new ArrayList<ChallengeBean>();
    private ChallengeBean tempChallenge;
    private List<SelectItem> names = new ArrayList<SelectItem>();
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

    public ArrayList<UserBean> getTeacherUsers() {
        return teacherUsers;
    }

    public List<SelectItem> getNames() {
        return names;
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
		PreparedStatement sql4 = null;
		ResultSet result = null;
		Statement statement = null;
		int userId = 0;
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
			sql.executeUpdate();
			sql2.executeUpdate();
			connection.commit();
			statement = connection.createStatement();
			result = statement.executeQuery("SELECT user_id FROM users WHERE username ='" + user.getUserName() + "'");
			while(result.next()) {
				userId = result.getInt("user_id");
				connection.commit(); //I doubt this one is really needed(autoCommit)
			}
			if (user.getRole().equals("user")) {
				sql3 = connection.prepareStatement("INSERT INTO student_teacher(user_id, username) VALUES(?,?)");
				sql3.setInt(1,getCurrentUser().getUserId());
				sql3.setString(2, user.getUserName());
				sql4 = connection.prepareStatement("INSERT INTO playerinfo(user_id, addition_score, subtraction_score, multiplication_score, division_score, currency_spent) VALUES(?,?,?,?,?,?)");
				sql4.setInt(1, userId);
				sql4.setInt(2, 0);
				sql4.setInt(3, 0);
				sql4.setInt(4, 0);
				sql4.setInt(5, 0);
				sql4.setInt(6, 0);
				sql3.executeUpdate();
				sql4.executeUpdate();
			}
			connection.commit();
		}catch(SQLException e) {
			Cleanup.printMessage(e, "addUser()");
		}finally {
		   readUsers();
		}
    }

    /** Delete user from database **/
    public void deleteUser(UserBean user) {
        openConnection();
        PreparedStatement sql = null;
        PreparedStatement sql2 = null;
        PreparedStatement sql3 = null;
        PreparedStatement sql4 = null;
        PreparedStatement sql5 = null;
        try {
            System.out.println(connection.getAutoCommit());
            connection.setAutoCommit(true);
            sql = connection.prepareStatement("DELETE FROM users WHERE user_id = ?");
            sql.setInt(1,user.getUserId());
            sql2 = connection.prepareStatement("DELETE FROM user_roles WHERE username = ?");
            sql2.setString(1,user.getUserName());
            sql3 = connection.prepareStatement("DELETE FROM student_teacher WHERE username = ?");
            sql3.setString(1,user.getUserName());
            sql4 = connection.prepareStatement("DELETE FROM challenge_student WHERE user_id = ?");
            sql4.setInt(1,user.getUserId());
            sql5 = connection.prepareStatement("DELETE FROM playerinfo WHERE user_id = ?");
            sql5.setInt(1,user.getUserId());
            sql5.executeUpdate();
            sql2.executeUpdate();
            sql3.executeUpdate();
            sql4.executeUpdate();
            sql.executeUpdate();
            connection.commit();
        }catch(SQLException e) {
            Cleanup.printMessage(e, "deleteUser()");
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
            result = statement.executeQuery("SELECT * FROM users WHERE username ='"+username+"'");
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
        if (loggedInn()) readTeacherUsers();
    }

    /* Reads the users belonging to the teacher who is logged in */
    public void readTeacherUsers() {
        openConnection();
        teacherUsers.clear();
        names.clear();
        Statement statement = null;
        PreparedStatement sql = null;
        ResultSet result = null;
        try {
            connection.setAutoCommit(false);
            sql = connection.prepareStatement("SELECT student_teacher.username, users.name, users.user_id FROM student_teacher, users WHERE student_teacher.user_id =? AND student_teacher.username = users.username");
            sql.setInt(1, getCurrentUser().getUserId());
            result = sql.executeQuery();
            connection.commit();
            while(result.next()) {
                int tempUserId = result.getInt("user_id");
                String tempName = result.getString("name");
                String tempUserName = result.getString("username");
                String tempRole = "user";
                teacherUsers.add(new UserBean(tempUserId, tempName,tempUserName, tempRole));
                names.add(new SelectItem(tempUserId, tempName));
            }
            for(int i = 0;i<teacherUsers.size();i++) {
                System.out.println("readTeacherUsers(): " + teacherUsers.get(i).getUserName());
            }
        }catch(SQLException e) {
            Cleanup.printMessage(e, "readTeacherUsers()");
        }finally {
            Cleanup.closeConnection(connection);
        }
    }

    public ChallengeBean readChallenge(String cType, int cDifficulty, String userName) {
        openConnection();
        Statement statement = null;
        PreparedStatement sql = null;
        ResultSet result = null;
        try {
            connection.setAutoCommit(false);
            sql = connection.prepareStatement("SELECT * FROM challenge RIGHT JOIN challenge_teacher ON (CHALLENGE_TEACHER.CHALLENGE_ID = challenge.CHALLENGE_ID) RIGHT JOIN student_teacher ON (student_teacher.USER_ID = challenge_teacher.USER_ID) RIGHT JOIN challenge_type ON (challenge_type.CHALLENGE_ID = challenge.CHALLENGE_ID) WHERE student_teacher.USERNAME = ? AND NOT EXISTS (SELECT * FROM challenge_student WHERE challenge_student.USER_ID = (SELECT USER_ID FROM users WHERE USERNAME = ?) AND challenge.CHALLENGE_ID = challenge_student.CHALLENGE_ID) AND challenge.DIFFICULTY = ? AND challenge_type.CHALLENGE_TYPE = ? ORDER BY random()");
            sql.setString(1, userName);
            sql.setString(2, userName);
            sql.setInt(3, cDifficulty);
            sql.setString(4, cType);
            result = sql.executeQuery();
            connection.commit();
            if(result.next()) {
				int tempCID = result.getInt("CHALLENGE_ID");
			    String tempText = result.getString("challenge_text") ;
			    double tempCorrect = result.getDouble("answer");
			    tempChallenge = new ChallengeBean(tempCID, tempText, tempCorrect, cDifficulty, cType);
			}
            else tempChallenge = new ChallengeBean(-1, "ingen oppgave tilgjengelig", 1, 1, "Addition");
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

     public String readTips(int challenge_id){
	        String tips = "";
	        openConnection();
	        PreparedStatement sql = null;
	        ResultSet result = null;

	        try{
	            connection.setAutoCommit(false);
	            sql = connection.prepareStatement("SELECT challenge_tips FROM challenge_tips WHERE challenge_id =?");
	            sql.setInt(1, challenge_id);//tempChallenge.getCID());
	            result = sql.executeQuery();
	            connection.commit();
	            while(result.next()){
	                tips = result.getString("challenge_tips");
	            }
	        }catch(SQLException e){
	            Cleanup.printMessage(e, "readTips()");
	        }finally{
	            Cleanup.closeConnection(connection);
	        }
	        System.out.println(tips);
	        return tips;

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

   public int[] readUserScore(String username) {
       //select * from playerinfo where user_id = ?
       int[] score = new int[5];
       openConnection();
        allUsers.clear();
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT * FROM playerinfo WHERE username = " + username);
            while(result.next()) {
                score[0] = result.getInt("addition_score");
                score[1] = result.getInt("subtraction_score");
                score[2] = result.getInt("multiplication_score");
                score[3] = result.getInt("division_score");
                score[4] = result.getInt("currency_spent");
            }
        }catch(SQLException e) {
            Cleanup.printMessage(e, "readUserScore()");
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
          PreparedStatement sql4 = null;
          System.out.println("PLS 1");
          try {
              System.out.println(connection.getAutoCommit());
              connection.setAutoCommit(false);
              // Insert må oppdateres
              sql = connection.prepareStatement("INSERT INTO challenge(challenge_TEXT, ANSWER, DIFFICULTY) VALUES (?, ?, ?)");
              sql2 = connection.prepareStatement("INSERT INTO challenge_TYPE(challenge_TYPE) VALUES (?)");
              sql3 = connection.prepareStatement("INSERT INTO challenge_TEACHER(USER_ID) VALUES (?)");
              sql4 = connection.prepareStatement("INSERT INTO challenge_TIPS(challenge_tips) VALUES (?)");
              sql.setString(1,chall.getText());
              sql.setDouble(2,chall.getCorrect());
              sql.setInt(3,chall.getDifficulty());
              sql2.setString(1,chall.getType());
              // System.out.println("ID: " + getCurrentUser().getUserId());
               sql3.setInt(1,getCurrentUser().getUserId());
               sql4.setString(1, chall.getTips());
              // System.out.println("PLS 2");
              sql.executeUpdate();
              connection.commit();
              sql2.executeUpdate();
              connection.commit();
              sql3.executeUpdate();
              connection.commit();
              sql4.executeUpdate();
              connection.commit();
          }catch(SQLException e) {
              Cleanup.printMessage(e, "addChallenge()");
          }finally {

          }
    }

    public void updateChallenge(ChallengeBean chall) {
      openConnection();
      PreparedStatement sqlupdateCha = null;
      PreparedStatement sqlupdateCha1 = null;

      String text = chall.getText();
      double correct = chall.getCorrect();
      int difficulty = chall.getDifficulty();
      int chall_id = chall.getCID();
      String tips = chall.getTips();

      try {
          connection.setAutoCommit(false);
          sqlupdateCha = connection.prepareStatement("UPDATE challenge set challenge_TEXT=?, ANSWER=?, DIFFICULTY=? WHERE challenge_id = ?");
          sqlupdateCha1 = connection.prepareStatement("UPDATE challenge_tips set CHALLENGE_TIPS=? WHERE challenge_id = ?");
          sqlupdateCha.setString(1, text);
          sqlupdateCha.setDouble(2, correct);
          sqlupdateCha.setInt(3, difficulty);
          sqlupdateCha.setInt(4, chall_id);
          sqlupdateCha1.setString(1, tips);
          sqlupdateCha1.setInt(2, chall_id);
          sqlupdateCha.executeUpdate();
          sqlupdateCha1.executeUpdate();
          connection.commit();
        } catch (SQLException e) {
	 Cleanup.printMessage(e, "addChallenge()");
        } finally {
   }
 }

     public ArrayList<ChallengeBean> getAllChallenges() {
    PreparedStatement statement1 = null;
    ResultSet res1 = null;
    allChall.clear();

    openConnection();
	try {
	connection.setAutoCommit(false);
        statement1 = connection.prepareStatement("SELECT challenge_teacher.USER_ID, challenge.CHALLENGE_ID, challenge.CHALLENGE_TEXT, challenge.ANSWER, challenge.DIFFICULTY, CHALLENGE_TYPE.CHALLENGE_TYPE, CHALLENGE_TIPS.CHALLENGE_TIPS FROM MATHPLAY.CHALLENGE LEFT JOIN challenge_teacher ON (challenge.CHALLENGE_ID = challenge_teacher.CHALLENGE_ID) LEFT JOIN challenge_type ON (challenge.CHALLENGE_ID = challenge_type.CHALLENGE_ID)LEFT JOIN CHALLENGE_TIPS ON (challenge.CHALLENGE_ID = CHALLENGE_TIPS.CHALLENGE_ID) WHERE challenge_teacher.USER_ID = ? ORDER BY challenge_type.CHALLENGE_TYPE, challenge.DIFFICULTY");
   	statement1.setInt(1,getCurrentUser().getUserId());
	res1 = statement1.executeQuery();
	while (res1.next()) {
                int subTid = res1.getInt("USER_ID");
		int subCid = res1.getInt("CHALLENGE_ID");
		String subChall_text = res1.getString("CHALLENGE_TEXT");
		double subAnswer = res1.getDouble("ANSWER");
		int subWor = res1.getInt("DIFFICULTY");
                String subType = res1.getString("CHALLENGE_TYPE");
                String subTip = res1.getString("CHALLENGE_TIPS");
                ChallengeBean chall = new ChallengeBean(subCid , subChall_text, subAnswer, subWor, subType, subTid, subTip);
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
	*  filter bestemmer hvilket filter som skal legges på, 0=Alle, 1=Lærer sine elever, 2=Elev sin klasse
	*/
	public ArrayList<UserScoresItem> userScoresItemTable(int status, int sortBy, boolean asc) {
            ArrayList<UserScoresItem> usi = new ArrayList<UserScoresItem>();
            PreparedStatement statement = null;
            ResultSet result = null;
            openConnection();
            try {
                connection.setAutoCommit(false);
                if (status==1) {
					int teacherID = getCurrentUser().getUserId();
					result = getUsiResult(teacherID,sortBy,asc);
				}
				else if (status==2) {
					int teacherID = getYourTeacherID();
					result = getUsiResult(teacherID,sortBy,asc);
				}
				else result = getUsiResult(sortBy,asc);
                while (result.next()) {
                    String username = result.getString("USERNAME");
                    String item_name = result.getString("NAME");
                    int addScore = result.getInt("ADDITION_SCORE");
                    int subScore = result.getInt("SUBTRACTION_SCORE");
                    int mulScore = result.getInt("MULTIPLICATION_SCORE");
                    int divScore = result.getInt("DIVISION_SCORE");
                    int curUsed = result.getInt("CURRENCY_SPENT");
                    usi.add(new UserScoresItem(username,item_name, addScore,subScore,mulScore,divScore,curUsed));
                }
            } catch (SQLException e) {
				Cleanup.printMessage(e, "userScoresItemTable()");
            } finally {
				Cleanup.closeConnection(connection);
        	}
        return usi;
    }

	/** Undermetode til userScoresItemTable
	* Returnerer bare en bestemt lærers spillere.
	*/
    private ResultSet getUsiResult(int teacherID, int sortBy, boolean asc) {
		try {
			PreparedStatement statement;
			if (asc) {
				if (sortBy==1) statement = connection.prepareStatement("select users.USERNAME,users.NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,STUDENT_TEACHER where users.USER_ID=playerinfo.USER_ID AND users.USERNAME=student_teacher.USERNAME AND student_teacher.user_id=? order by USERNAME DESC");
				else if (sortBy==2) statement = connection.prepareStatement("select users.USERNAME,users.NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,STUDENT_TEACHER where users.USER_ID=playerinfo.USER_ID AND users.USERNAME=student_teacher.USERNAME AND student_teacher.user_id=? order by ADDITION_SCORE ASC");
				else if (sortBy==3) statement = connection.prepareStatement("select users.USERNAME,users.NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,STUDENT_TEACHER where users.USER_ID=playerinfo.USER_ID AND users.USERNAME=student_teacher.USERNAME AND student_teacher.user_id=? order by SUBTRACTION_SCORE ASC");
				else if (sortBy==4) statement = connection.prepareStatement("select users.USERNAME,users.NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,STUDENT_TEACHER where users.USER_ID=playerinfo.USER_ID AND users.USERNAME=student_teacher.USERNAME AND student_teacher.user_id=? order by MULTIPLICATION_SCORE ASC");
				else if (sortBy==5) statement = connection.prepareStatement("select users.USERNAME,users.NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,STUDENT_TEACHER where users.USER_ID=playerinfo.USER_ID AND users.USERNAME=student_teacher.USERNAME AND student_teacher.user_id=? order by DIVISION_SCORE ASC");
				else if (sortBy==6) statement = connection.prepareStatement("select users.USERNAME,users.NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,STUDENT_TEACHER where users.USER_ID=playerinfo.USER_ID AND users.USERNAME=student_teacher.USERNAME AND student_teacher.user_id=? order by CURRENCY_SPENT ASC");
				else statement = connection.prepareStatement("select users.USERNAME,users.NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,STUDENT_TEACHER where users.USER_ID=playerinfo.USER_ID AND users.USERNAME=student_teacher.USERNAME AND student_teacher.user_id=? order by NAME DESC");
			}
			else {
				if (sortBy==1) statement = connection.prepareStatement("select users.USERNAME,users.NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,STUDENT_TEACHER where users.USER_ID=playerinfo.USER_ID AND users.USERNAME=student_teacher.USERNAME AND student_teacher.user_id=? order by USERNAME ASC");
				else if (sortBy==2) statement = connection.prepareStatement("select users.USERNAME,users.NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,STUDENT_TEACHER where users.USER_ID=playerinfo.USER_ID AND users.USERNAME=student_teacher.USERNAME AND student_teacher.user_id=? order by ADDITION_SCORE DESC");
				else if (sortBy==3) statement = connection.prepareStatement("select users.USERNAME,users.NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,STUDENT_TEACHER where users.USER_ID=playerinfo.USER_ID AND users.USERNAME=student_teacher.USERNAME AND student_teacher.user_id=? order by SUBTRACTION_SCORE DESC");
				else if (sortBy==4) statement = connection.prepareStatement("select users.USERNAME,users.NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,STUDENT_TEACHER where users.USER_ID=playerinfo.USER_ID AND users.USERNAME=student_teacher.USERNAME AND student_teacher.user_id=? order by MULTIPLICATION_SCORE DESC");
				else if (sortBy==5) statement = connection.prepareStatement("select users.USERNAME,users.NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,STUDENT_TEACHER where users.USER_ID=playerinfo.USER_ID AND users.USERNAME=student_teacher.USERNAME AND student_teacher.user_id=? order by DIVISION_SCORE DESC");
				else if (sortBy==6) statement = connection.prepareStatement("select users.USERNAME,users.NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,STUDENT_TEACHER where users.USER_ID=playerinfo.USER_ID AND users.USERNAME=student_teacher.USERNAME AND student_teacher.user_id=? order by CURRENCY_SPENT DESC");
				else statement = connection.prepareStatement("select users.USERNAME,users.NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,STUDENT_TEACHER where users.USER_ID=playerinfo.USER_ID AND users.USERNAME=student_teacher.USERNAME AND student_teacher.user_id=? order by NAME ASC");
			}
			statement.setInt(1,teacherID);
			ResultSet result = statement.executeQuery();
			return result; // Will always be returned
		} catch (SQLException e) {
			Cleanup.printMessage(e, "getUsiStatement1()");
		} finally {
			Cleanup.closeConnection(connection);
		}
		return null; // Will never be returned
	}

	/**
	* Vil returner alle spillere
	*/
    private ResultSet getUsiResult(int sortBy, boolean asc) {
		try {
			Statement statement = connection.createStatement();
			ResultSet result;
			if (asc) {
				if (sortBy==1) result = statement.executeQuery("select users.USERNAME,NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,USER_ROLES where users.USER_ID=playerinfo.USER_ID AND rolename='user' AND users.USERNAME=user_roles.USERNAME order by USERNAME DESC");
				else if (sortBy==2) result = statement.executeQuery("select users.USERNAME,NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,USER_ROLES where users.USER_ID=playerinfo.USER_ID AND rolename='user' AND users.USERNAME=user_roles.USERNAME order by ADDITION_SCORE ASC");
				else if (sortBy==3) result = statement.executeQuery("select users.USERNAME,NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,USER_ROLES where users.USER_ID=playerinfo.USER_ID AND rolename='user' AND users.USERNAME=user_roles.USERNAME order by SUBTRACTION_SCORE ASC");
				else if (sortBy==4) result = statement.executeQuery("select users.USERNAME,NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,USER_ROLES where users.USER_ID=playerinfo.USER_ID AND rolename='user' AND users.USERNAME=user_roles.USERNAME order by MULTIPLICATION_SCORE ASC");
				else if (sortBy==5) result = statement.executeQuery("select users.USERNAME,NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,USER_ROLES where users.USER_ID=playerinfo.USER_ID AND rolename='user' AND users.USERNAME=user_roles.USERNAME order by DIVISION_SCORE ASC");
				else if (sortBy==6) result = statement.executeQuery("select users.USERNAME,NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,USER_ROLES where users.USER_ID=playerinfo.USER_ID AND rolename='user' AND users.USERNAME=user_roles.USERNAME order by CURRENCY_SPENT ASC");
				else result = statement.executeQuery("select users.USERNAME,NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,USER_ROLES where users.USER_ID=playerinfo.USER_ID AND rolename='user' AND users.USERNAME=user_roles.USERNAME order by NAME DESC");
			}
			else {
				if (sortBy==1) result = statement.executeQuery("select users.USERNAME,NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,USER_ROLES where users.USER_ID=playerinfo.USER_ID AND rolename='user' AND users.USERNAME=user_roles.USERNAME order by USERNAME ASC");
				else if (sortBy==2) result = statement.executeQuery("select users.USERNAME,NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,USER_ROLES where users.USER_ID=playerinfo.USER_ID AND rolename='user' AND users.USERNAME=user_roles.USERNAME order by ADDITION_SCORE DESC");
				else if (sortBy==3) result = statement.executeQuery("select users.USERNAME,NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,USER_ROLES where users.USER_ID=playerinfo.USER_ID AND rolename='user' AND users.USERNAME=user_roles.USERNAME order by SUBTRACTION_SCORE DESC");
				else if (sortBy==4) result = statement.executeQuery("select users.USERNAME,NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,USER_ROLES where users.USER_ID=playerinfo.USER_ID AND rolename='user' AND users.USERNAME=user_roles.USERNAME order by MULTIPLICATION_SCORE DESC");
				else if (sortBy==5) result = statement.executeQuery("select users.USERNAME,NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,USER_ROLES where users.USER_ID=playerinfo.USER_ID AND rolename='user' AND users.USERNAME=user_roles.USERNAME order by DIVISION_SCORE DESC");
				else if (sortBy==6) result = statement.executeQuery("select users.USERNAME,NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,USER_ROLES where users.USER_ID=playerinfo.USER_ID AND rolename='user' AND users.USERNAME=user_roles.USERNAME order by CURRENCY_SPENT DESC");
				else result = statement.executeQuery("select users.USERNAME,NAME,ADDITION_SCORE,SUBTRACTION_SCORE,MULTIPLICATION_SCORE,DIVISION_SCORE,CURRENCY_SPENT from USERS,PLAYERINFO,USER_ROLES where users.USER_ID=playerinfo.USER_ID AND rolename='user' AND users.USERNAME=user_roles.USERNAME order by NAME ASC");
			}
			return result; // Will always be returned
		} catch (SQLException e) {
			Cleanup.printMessage(e, "getUsiStatement2()");
		} finally {
			Cleanup.closeConnection(connection);
		}
		return null; // Will never be returned
	}

	/** Deletes all the users completed challenges records */
	public void resetUser() {
		int user_id = getCurrentUser().getUserId();
		openConnection();
		Statement statement = null;
		ResultSet result = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM challenge_student WHERE user_id="+user_id);
		}catch(SQLException e) {
			Cleanup.printMessage(e, "resetUser()");
		} finally {
			Cleanup.closeConnection(connection);
		}
	}

	/** Returns your teachers user_id **/
	public int getYourTeacherID() {
		String username = new UserBean().getCurrentUser();
		openConnection();
		Statement statement = null;
		ResultSet result = null;
		try {
			statement = connection.createStatement();
			result = statement.executeQuery("select user_id from MATHPLAY.STUDENT_TEACHER where username='"+username+"'");
			result.next();
			return result.getInt("user_id");
		}catch(SQLException e) {
			Cleanup.printMessage(e, "getYourTeacherID()");
		} finally {
			Cleanup.closeConnection(connection);
		}
		return 0;
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

    private boolean loggedInn() {
		ExternalContext context
		= FacesContext.getCurrentInstance().getExternalContext();
		Object requestObject =  context.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestObject;
		String loggedInnName = request.getRemoteUser();
		System.out.println("DEBUGG_OVERVIEW_NAME :"+loggedInnName);
		if (loggedInnName==null) return false;
		else return true;
	}

   /* public static void main(String[] args) {
        Overview lol = new Overview();
        ChallengeBean chall = lol.getChallenge("Addisjon", 1, "andriod");
        System.out.println("Oppgave: "+chall.getText()+" , riktig svar: "+chall.getCorrect());

    }*/


}//OverView