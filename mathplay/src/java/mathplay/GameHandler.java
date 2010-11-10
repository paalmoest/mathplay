package mathplay;

import java.util.ArrayList;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="gameHandler")
@SessionScoped
public class GameHandler {

	// OBJECT VARIABLES
	private int additionScore = 0;
	private int subtractionScore = 0;
	private int multiplicationScore = 0;
	private int divisionScore = 0;
	private static int levelTwoThreshold = 10;
	private static int levelThreeThreshold = 30;
	private static int progressLevelThreshold = 1;
	private int valutaSpent = 0;

	// VIEW Variables
	private ChallengeBean currentChallenge;
	private String answer = "";
	public boolean lastCorrect = false;
        private int[] score = new int[5];

        private Overview overview = new Overview();


	/*
	*  Constructor, loads from databae:
	* All the users scores of the different mathematical operations.
	* All the challenges for each of the different mathematical operations.
	*/
	public GameHandler() {
		System.out.println("------START-------");
		System.out.println("Userid: "+overview.getCurrentUser().getUserId());
		score = overview.readScore();
		additionScore = score[0];
		subtractionScore = score[1];
		multiplicationScore = score[2];
		divisionScore = score[3];
		valutaSpent = score[4];
	}


	// ************** PROPERTIES **************

	// PROPERTY: answer
	public String getAnswer() {return answer;}
	public void setAnswer(String answer) {this.answer=answer;}

	// PROPERTY: currentChallenge
	public ChallengeBean getCurrentChallenge() {return currentChallenge;}

	// PROPERTY: additionScore
	public int getAdditionScore() { return additionScore; }

	// PROPERTY: subtractionScore
	public int getSubtractionScore() {return subtractionScore;}

	// PROPERTY: multiplicationScore
	public int getMultiplicationScore() {return multiplicationScore;}

	// PROPERTY: divisionScore
	public int getDivisionScore() {return divisionScore;}

	// PROPERTY: lastCorrect
	public boolean getLastCorrect() {return lastCorrect;}

	// PROPERTY: AdditionLevel
	public int getAdditionLevel() {
		return getLevelForScore(additionScore);
	}

	// PROPERTY: SubtractionLevel
	public int getSubtractionLevel() {
		return getLevelForScore(subtractionScore);
	}

	// PROPERTY: MultiplicationLevel
	public int getMultiplicationLevel() {
		return getLevelForScore(multiplicationScore);
	}

	// PROPERTY: DivisionLevel
	public int getDivisionLevel() {
		return getLevelForScore(divisionScore);
	}

	// PROPERTY: Valuta
	public int getValuta() {
		int newValuta = 0;
		newValuta += additionScore*2;
		newValuta += subtractionScore*3;
		newValuta += multiplicationScore*4;
		newValuta += divisionScore*5;
		newValuta -= valutaSpent;
		return newValuta;
	}

	// PROPERTY: subtractionAccess
	public boolean getSubtractionAccess() {
		if (getAdditionLevel()>=progressLevelThreshold) return true;
		else return false;
	}

	// PROPERTY: multiplicationAccess
	public boolean getMultiplicationAccess() {
		if (getSubtractionLevel()>=progressLevelThreshold) return true;
		else return false;
	}

	// PROPERTY: divisionAccess
	public boolean getDivisionAccess() {
		if (getMultiplicationLevel()>=progressLevelThreshold) return true;
		else return false;
	}

	// PROPERTY: superAccess
	public boolean getSuperAccess() {
		if (getDivisionLevel()>=progressLevelThreshold) return true;
		else return false;
	}

	public void updateScore() {
		score[0] = additionScore;
		score[1] = subtractionScore;
		score[2] = multiplicationScore;
		score[3] = divisionScore;
		score[4] = valutaSpent;
                overview.changeScore(score);
	}


	// ************** METHODS RELEVANT TO ADDITION, SUBTRACTION;  **************
	// ************** MULTIPLICATION and DIVISION;                **************

	/*
	* Loads a random ADDITION challenge from the database,
	* with the approporate difficulty,
	* one that has not been answered correctly before.
	*/
	public String progressAddition() {
		int difficulty = getAdditionLevel();
		if (difficulty==0) difficulty = 1;
		currentChallenge = overview.readChallenge("Addition", difficulty, overview.getCurrentUser().getUserName());;
		return "test_challenge_solve";
	}

	/*
	* Loads a random SUBTRACTION challenge from the database,
	* with the approporate difficulty,
	* one that has not been answered correctly before.
	*/
	public String progressSubtraction() {
		int difficulty = getSubtractionLevel();
		if (difficulty==0) difficulty = 1;
		currentChallenge = overview.readChallenge("Subtraction", difficulty, overview.getCurrentUser().getUserName());
		return "test_challenge_solve";
	}

	/*
	* Loads a random MULTIPLICATION challenge from the database,
	* with the approporate difficulty,
	* one that has not been answered correctly before.
	*/
	public String progressMultiplication() {
		int difficulty = getMultiplicationLevel();
		if (difficulty==0) difficulty = 1;
		currentChallenge = overview.readChallenge("Multiplication", difficulty, overview.getCurrentUser().getUserName());
		return "test_challenge_solve";
	}

	/*
	* Loads a random DIVISION challenge from the database,
	* with the approporate difficulty,
	* one that has not been answered correctly before.
	*/
	public String progressDivision() {
		int difficulty = getDivisionLevel();
		if (difficulty==0) difficulty = 1;
		currentChallenge = overview.readChallenge("Division", difficulty, overview.getCurrentUser().getUserName());
		return "test_challenge_solve";
	}


	/*
	* Method used by the view to submit his answer
	*/
	public String submitAnswer() {
		String type = currentChallenge.getType();
		if (type.equals("Addition")) submitAddition();
		else if (type.equals("Subtraction")) submitSubtraction();
		else if (type.equals("Multiplication")) submitMultiplication();
		else if (type.equals("Division")) submitDivision();
		else return "challenge_error";
		return "test_challenge";
	}

	/*
	* Undermethod of the submitAnswer method, for when user
	* submits a ADDITION answer
	*/
	private void submitAddition() {
		double double_answer = Double.parseDouble(answer);
		int dif = currentChallenge.getDifficulty();
		if (currentChallenge.isCorrect(double_answer)) {
			additionScore += (dif*dif); // Formula for adding score
			lastCorrect = true;
			overview.challengeCompleted(currentChallenge.getCID());
			updateScore();
		}
		else {
			additionScore -= dif; // Formula for removing score
			lastCorrect = false;
		}
		resetValues();
	}

	/*
	* Undermethod of the submitAnswer method, for when user
	* submits a SUBTRACTION answer
	*/
	private void submitSubtraction() {
		double double_answer = Double.parseDouble(answer);
		int dif = currentChallenge.getDifficulty();
		if (currentChallenge.isCorrect(double_answer)) {
			subtractionScore += (dif*dif); // Formula for adding score
			lastCorrect = true;
			overview.challengeCompleted(currentChallenge.getCID());
			updateScore();
		}
		else {
			subtractionScore -= dif; // Formula for removing score
			lastCorrect = false;
                        updateScore();
                        overview.changeScore(score);
		}
		resetValues();
	}

	/*
	* Undermethod of the submitAnswer method, for when user
	* submits a MULTIPLICATION answer
	*/
	private void submitMultiplication() {
		double double_answer = Double.parseDouble(answer);
		int dif = currentChallenge.getDifficulty();
		if (currentChallenge.isCorrect(double_answer)) {
			multiplicationScore += (dif*dif); // Formula for adding score
			lastCorrect = true;
			overview.challengeCompleted(currentChallenge.getCID());
			updateScore();
		}
		else {
			multiplicationScore -= dif; // Formula for removing score
			lastCorrect = false;
                        updateScore();
		}
		resetValues();
	}

	/*
	* Undermethod of the submitAnswer method, for when user
	* submits a DIVISION answer
	*/
	private void submitDivision() {
		double double_answer = Double.parseDouble(answer);
		int dif = currentChallenge.getDifficulty();
		if (currentChallenge.isCorrect(double_answer)) {
			divisionScore += (dif*dif); // Formula for adding score
			lastCorrect = true;
			overview.challengeCompleted(currentChallenge.getCID());
			updateScore();
			overview.changeScore(score);
		}
		else {
			divisionScore -= dif; // Formula for removing score
			lastCorrect = false;
                        updateScore();
                        overview.changeScore(score);
		}
		resetValues();
	}

	/*
	* Reset all challenge values, starting on a cleen sheet for the next one.
	*/
	private void resetValues() {
		answer="";
	}

	// ************** METHODS RELEVANT TO GENEREATING TOPPLIST   **************

	boolean ascend = true;
	ArrayList<UserScoresItem> usi;

	// PROPERTY: usi
	public ArrayList<UserScoresItem> getUsi() {return usi;}


	public String sortByUsername() {
		if (ascend) ascend = false;
		else ascend = true;
		usi = overview.userScoresItemTable(1,ascend);
		return "toppList";
	}

	public String sortByAdd() {
		if (ascend) ascend = false;
		else ascend = true;
		usi = overview.userScoresItemTable(2,ascend);
		return "toppList";
	}

	public String sortBySub() {
		if (ascend) ascend = false;
		else ascend = true;
		usi = overview.userScoresItemTable(3,ascend);
		return "toppList";
	}

	public String sortByMul() {
		if (ascend) ascend = false;
		else ascend = true;
		usi = overview.userScoresItemTable(4,ascend);
		return "toppList";
	}

	public String sortByDiv() {
		if (ascend) ascend = false;
		else ascend = true;
		usi = overview.userScoresItemTable(5,ascend);
		return "toppList";
	}

	public String sortByCurSpent() {
		if (ascend) ascend = false;
		else ascend = true;
		usi = overview.userScoresItemTable(6,ascend);
		return "toppList";
	}

	// ************** INTERNAL METHODS **************

	/*
	* Internal method to calculate the level
	* used by the property get<operation>Level methods.
	*/
	private int getLevelForScore(int score) {
		if (score>=levelThreeThreshold) return 3;
		else if (score>=levelTwoThreshold) return 2;
		else if (score>0) return 1;
		else return 0;
	}
}