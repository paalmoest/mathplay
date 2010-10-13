/*
* This class will handle everything that involves challenges.
*/
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

        private Overview overview = new Overview();


	/*
	*  Constructor, loads from databae:
	* All the users scores of the different mathematical operations.
	* All the challenges for each of the different mathematical operations.
	*/
	public GameHandler() {
		System.out.println("------START-------");
		/**INSERT** Load following user related variables from the database::
		**INSERT** additionScore, subtractionScore, multiplicationScore, divisionScore
		**INSERT** valutaSpent
		* Alternatively it loads a userbean, or takes a userbean in the constructor.
		*/
	}


	// ************** PROPERTIES **************

	// PROPERTY: answer
	public String getAnswer() {return answer;}
	public void setAnswer(String answer) {this.answer=answer;}

	// PROPERTY: currentChallenge
	public ChallengeBean getCurrentChallenge() {return currentChallenge;}

	// PROPERTY: additionScore
	public int getAdditionScore() {return additionScore; }

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


	// ************** METHODS RELEVANT TO ADDITION, SUBTRACTION;  **************
	// ************** MULTIPLICATION and DIVISION;                **************

	/*
	* Loads a random ADDITION challenge from the database,
	* with the approporate difficulty,
	* one that has not been answered correctly before.
	*/
	public String progressAddition() {
		int difficulty = getAdditionLevel();
		ArrayList<ChallengeBean> list = new ArrayList<ChallengeBean>();
		list.add(new ChallengeBean(0,"1+2",3,1,"addition")); // JUST A TEST Addition
		list.add(new ChallengeBean(0,"how many animals are 2cats and 11dogs???",13,1,"addition"));
		//**INSERT** Load challenge lists from the database	with the correct difficulty4
                ChallengeBean challenge = overview.getChallenge("addition", difficulty, overview.getCurrentUser().getUserId());
		Random generator = new Random();
		int r = generator.nextInt(list.size());
		currentChallenge = list.get(r);
		return "test_challenge_solve";
	}



	/*
	* Loads a random SUBTRACTION challenge from the database,
	* with the approporate difficulty,
	* one that has not been answered correctly before.
	*/
	public String progressSubtraction() {
		int difficulty = getSubtractionLevel();
		ArrayList<ChallengeBean> list = new ArrayList<ChallengeBean>();
		list.add(new ChallengeBean(0,"6-2",4,1,"subtraction")); // JUST A TEST Addition
		list.add(new ChallengeBean(0,"you got 2 dogs and 7 cats, the dogs ate one cat each, how many cats do you got left?",5,1,"subtraction"));
		//**INSERT** Load a random challenge from the database	with the correct difficulty
		Random generator = new Random();
		int r = generator.nextInt(list.size());
		currentChallenge = list.get(r);
		return "test_challenge_solve";
	}

	/*
	* Loads a random MULTIPLICATION challenge from the database,
	* with the approporate difficulty,
	* one that has not been answered correctly before.
	*/
	public String progressMultiplication() {
		int difficulty = getMultiplicationLevel();
		ArrayList<ChallengeBean> list = new ArrayList<ChallengeBean>();
		list.add(new ChallengeBean(0,"3x5",15,1,"multiplication")); // JUST A TEST Addition
		list.add(new ChallengeBean(0,"You got 2 male dogs and 3 female cats, the dogs got all of the cats pregnant, how many cats do you got after 1year?",6,1,"multiplication"));
		//**INSERT** Load a random challenge from the database	with the correct difficulty
		Random generator = new Random();
		int r = generator.nextInt(list.size());
		currentChallenge = list.get(r);
		return "test_challenge_solve";
	}

	/*
	* Loads a random DIVISION challenge from the database,
	* with the approporate difficulty,
	* one that has not been answered correctly before.
	*/
	public String progressDivision() {
		int difficulty = getDivisionLevel();
		ArrayList<ChallengeBean> list = new ArrayList<ChallengeBean>();
		list.add(new ChallengeBean(0,"10/5",2,1,"division")); // JUST A TEST Addition
		list.add(new ChallengeBean(0,"You got 6cats; half of them died, how many do you got left?",3,1,"division"));
		//**INSERT** Load a random challenge from the database	with the correct difficulty
		Random generator = new Random();
		int r = generator.nextInt(list.size());
		currentChallenge = list.get(r);
		return "test_challenge_solve";
	}


	/*
	* Method used by the view to submit his answer
	*/
	public String submitAnswer() {
		String type = currentChallenge.getType();
		if (type.equals("addition")) submitAddition();
		else if (type.equals("subtraction")) submitSubtraction();
		else if (type.equals("multiplication")) submitMultiplication();
		else if (type.equals("division")) submitDivision();
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
			// ***INSERT*** update that the currentChallenge.getCID() has been completed
			// ***INSERT*** update the new score value to the database
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
			// ***INSERT*** update that the currentChallenge.getCID() has been completed
			// ***INSERT*** update the new score value to the database
		}
		else {
			subtractionScore -= dif; // Formula for removing score
			lastCorrect = false;
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
			// ***INSERT*** update that the currentChallenge.getCID() has been completed
			// ***INSERT*** update the new score value to the database
		}
		else {
			multiplicationScore -= dif; // Formula for removing score
			lastCorrect = false;
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
			// ***INSERT*** update that the currentChallenge.getCID() has been completed
			// ***INSERT*** update the new score value to the database
		}
		else {
			divisionScore -= dif; // Formula for removing score
			lastCorrect = false;
		}
		resetValues();
	}

	/*
	* Reset all challenge values, starting on a cleen sheet for the next one.
	*/
	private void resetValues() {
		answer="";
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