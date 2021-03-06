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
        private int teacherId;
        private String tips;

	/** CID: Challenge ID, used by the database. */
	public ChallengeBean(int CID) {
		this.CID = CID;
	}
        public ChallengeBean(int CID, String text, double correct, int difficulty, String type) {
		this.CID = CID;
		this.text = text;
		this.correct = correct;
		this.difficulty = difficulty;
		this.type = type;
                this.teacherId = teacherId;
	}

        public ChallengeBean(int CID, String text, double correct, int difficulty, String type, String tips){
            this.CID = CID;
            this.text = text;
            this.correct = correct;
            this.difficulty = difficulty;
            this.type = type;
            this.tips = tips;
        }

	/** Constructor */
	public ChallengeBean(int CID, String text, double correct, int difficulty, String type, int teacherId, String tips) {
		this.CID = CID;
		this.text = text;
		this.correct = correct;
		this.difficulty = difficulty;
		this.type = type;
                this.teacherId = teacherId;
                this.tips = tips;
	}

        public ChallengeBean() {};

	// PROPERTY: CID
	public int getCID() {return CID;}
        public void setCID(int CID){this.CID = CID;}

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

        // PROPERTY: techerId
        public int getTeacherId() {return teacherId;}
        public void setTeacherId(int teacherId) {this.teacherId = teacherId;}

        public String getTips() {return tips;}
        public void setTips(String tips) {this.tips = tips;}

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