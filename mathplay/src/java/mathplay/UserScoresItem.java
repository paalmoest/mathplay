
package mathplay;

public class UserScoresItem {

	private static int levelTwoThreshold = 10;
	private static int levelThreeThreshold = 30;
	private static int progressLevelThreshold = 1;

	private String username;
	private int addScore;
	private int subScore;
	private int mulScore;
	private int divScore;
	private int currency;
	private int curUsed;
	private int addLevel;
	private int subLevel;
	private int mulLevel;
	private int divLevel;
	private int totalScore;
	private double avgLevel;

	public UserScoresItem(String username, int addScore, int subScore, int mulScore, int divScore, int curUsed) {
		this.username = username;
		this.addScore = addScore;
		this.subScore = subScore;
		this.mulScore = mulScore;
		this.divScore = divScore;
		this.currency = getValuta();
		this.curUsed = curUsed;
		this.addLevel = getLevelForScore(addScore);
		this.subLevel = getLevelForScore(subScore);
		this.mulLevel = getLevelForScore(mulScore);
		this.divLevel = getLevelForScore(divScore);
		totalScore = (addScore*2) + (subScore*3) + (mulScore*4) + (divScore*5);
		avgLevel = (addLevel+subLevel+mulLevel+divLevel)/4;
	}

	public String getUsername() {return username;}
	public int getAddScore() {return addScore;}
	public int getSubScore() {return subScore;}
	public int getMulScore() {return mulScore;}
	public int getDivScore() {return subScore;}
	public int getCurrency() {return currency;}
	public int getCurUsed() {return curUsed;}
	public int getAddLevel() {return addLevel;}
	public int getSubLevel() {return subLevel;}
	public int getMulLevel() {return mulLevel;}
	public int getDivLevel() {return divLevel;}
	public int getTotalScore() {return totalScore;}
	public double getAvgLevel() {return avgLevel;}

	private int getLevelForScore(int score) {
		if (score>=levelThreeThreshold) return 3;
		else if (score>=levelTwoThreshold) return 2;
		else if (score>0) return 1;
		else return 0;
	}

	public int getValuta() {
		int newValuta = 0;
		newValuta += addScore*2;
		newValuta += subScore*3;
		newValuta += mulScore*4;
		newValuta += divScore*5;
		newValuta -= curUsed;
		return newValuta;
	}

}