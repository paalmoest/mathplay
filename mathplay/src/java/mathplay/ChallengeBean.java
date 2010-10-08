/*
* This class represent one challenge. What type of operation the challenge is in
* is represented by the important "TYPE" string variable.
*/
package mathplay;


public class ChallengeBean {

	private int CID;
	private String text;
	private double correct;
	private int difficulty;
	private String type;

	/*
	* Constructor, text represent the mathematical challenge with its numbers
	* and can be represented in text and numbers or only numbers
	*/
	public ChallengeBean(int CID, String text, double correct, int difficulty, String type) {
		this.CID = CID;
		this.text = text;
		this.correct = correct;
		this.difficulty = difficulty;
		this.type = type;
	}

	// PROPERTY: CID
	public int getCID() {return CID;}

	// PROPERTY: text
	public String getText() {return text;}
	public void setText(String text) {this.text = text;}

	// PROPERTY: correct
	public double getCorrect() {return correct;}
	public void setCorrect(double correct) {this.correct = correct;}

	// PROPERTY: difficulty
	public int getDifficulty() {return difficulty;}
	public void setDifficulty(int difficulty) {this.difficulty = difficulty;}

	// PROPERTY: type
	public String getType() {return type;}
	public void setType(String type) {this.type = type;}

	/*
	* Compares if the correct answer and the users answer is equal.
	*/
	public boolean isCorrect(double answer) {
		if (answer==correct) return true;
		else return false;
	}

	public String toString() {
		return "ID:"+CID+" - text:"+text+" - correct:"+correct+" - dif:"+difficulty+" - type:"+type;
	}
}