package mathplay;

import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.*;

@ManagedBean(name="toppListHandler")
@SessionScoped
public class ToppListHandler {

	private String xhtml_page = "";
	private Overview overview = new Overview();
	boolean ascend = false;
	ArrayList<UserScoresItem> usi;
	String lastSorted = "";
	private boolean status = false; // Variabel som beskriver om dette er en statusutskrift for en bestemt lærer.

	public ToppListHandler() {
	}


	// PROPERTY: usi
	public ArrayList<UserScoresItem> getUsi() {return usi;}

	public boolean getDummySortMethod_STATUS() {
		if (!xhtml_page.equals("listYourUsers")) { // Sjekker om metoden er kjørt allerede
			System.out.println("DEBUGG_ToppListHandler_INNI_DUMMY_STATUS");
			status = true;
			ascend = toAscend("username");
			usi = overview.userScoresItemTable(status,1,ascend);
			xhtml_page = "listYourUsers";
		}
		return false;
	}

	public boolean getDummySortMethod_TOPPLIST() {
		if (!xhtml_page.equals("toppList")) { // Sjekker om metoden er kjørt allerede
		System.out.println("DEBUGG_ToppListHandler_INNI_DUMMY_TOPPLIST");
			status = false;
			ascend = toAscend("username");
			usi = overview.userScoresItemTable(status,1,ascend);
			xhtml_page = "toppList";
		}
		return false;
	}


	//**** ALLL SORTING METHODS ******
	//********************************

	public String statusSort() {
		status = true;
		ascend = toAscend("username");
		usi = overview.userScoresItemTable(status,1,ascend);
		return xhtml_page;
	}

	public String topplistSort() {
		ascend = toAscend("username");
		usi = overview.userScoresItemTable(status,1,ascend);
		return xhtml_page;
	}

	public String sortByUsername() {
		ascend = toAscend("username");
		usi = overview.userScoresItemTable(status,1,ascend);
		return xhtml_page;
	}

	public String sortByAddScore() {
		ascend = toAscend("addScore");
		usi = overview.userScoresItemTable(status,2,ascend);
		return xhtml_page;
	}

	public String sortBySubScore() {
		ascend = toAscend("subScore");
		usi = overview.userScoresItemTable(status,3,ascend);
		return xhtml_page;
	}

	public String sortByMulScore() {
		ascend = toAscend("mulScore");
		usi = overview.userScoresItemTable(status,4,ascend);
		return xhtml_page;
	}

	public String sortByDivScore() {
		ascend = toAscend("divScore");
		usi = overview.userScoresItemTable(status,5,ascend);
		return xhtml_page;
	}

	public String sortByCurUsed() {
		ascend = toAscend("curUsed");
		usi = overview.userScoresItemTable(status,6,ascend);
		return xhtml_page;
	}

	public String sortByName() {
		ascend = toAscend("name");
		usi = overview.userScoresItemTable(status,7,ascend);
		return xhtml_page;
	}

	public String sortByCurrency() {
		ascend = toAscend("currency");
		usi = overview.userScoresItemTable(status,1,ascend);
		for (int i = usi.size(); --i>=0; ) {
			boolean flipped = false;
			for (int j = 0; j<i; j++) {
				if (usi.get(j).getCurrency() > usi.get(j+1).getCurrency()&&ascend) {
					swap(j);
					flipped = true;
				}
				else if (usi.get(j).getCurrency() < usi.get(j+1).getCurrency()&&(!ascend)) {
					swap(j);
					flipped = true;
				}
			}
			if (!flipped) {
				return xhtml_page;
			}
		}
		return xhtml_page;
	}

	public String sortByAddLevel() {
		ascend = toAscend("addLevel");
		usi = overview.userScoresItemTable(status,1,ascend);
		for (int i = usi.size(); --i>=0; ) {
			boolean flipped = false;
			for (int j = 0; j<i; j++) {
				if (usi.get(j).getAddLevel() > usi.get(j+1).getAddLevel()&&ascend) {
					swap(j);
					flipped = true;
				}
				else if (usi.get(j).getAddLevel() < usi.get(j+1).getAddLevel()&&(!ascend)) {
					swap(j);
					flipped = true;
				}
			}
			if (!flipped) {
				return xhtml_page;
			}
		}
		return xhtml_page;
	}

	public String sortBySubLevel() {
		ascend = toAscend("subLevel");
		usi = overview.userScoresItemTable(status,1,ascend);
		for (int i = usi.size(); --i>=0; ) {
			boolean flipped = false;
			for (int j = 0; j<i; j++) {
				if (usi.get(j).getSubLevel() > usi.get(j+1).getSubLevel()&&ascend) {
					swap(j);
					flipped = true;
				}
				else if (usi.get(j).getSubLevel() < usi.get(j+1).getSubLevel()&&(!ascend)) {
					swap(j);
					flipped = true;
				}
			}
			if (!flipped) {
				return xhtml_page;
			}
		}
		return xhtml_page;
	}

	public String sortByMulLevel() {
		ascend = toAscend("mulLevel");
		usi = overview.userScoresItemTable(status,1,ascend);
		for (int i = usi.size(); --i>=0; ) {
			boolean flipped = false;
			for (int j = 0; j<i; j++) {
				if (usi.get(j).getMulLevel() > usi.get(j+1).getMulLevel()&&ascend) {
					swap(j);
					flipped = true;
				}
				else if (usi.get(j).getMulLevel() < usi.get(j+1).getMulLevel()&&(!ascend)) {
					swap(j);
					flipped = true;
				}
			}
			if (!flipped) {
				return xhtml_page;
			}
		}
		return xhtml_page;
	}

	public String sortByDivLevel() {
		ascend = toAscend("divLevel");
		usi = overview.userScoresItemTable(status,1,ascend);
		for (int i = usi.size(); --i>=0; ) {
			boolean flipped = false;
			for (int j = 0; j<i; j++) {
				if (usi.get(j).getDivLevel() > usi.get(j+1).getDivLevel()&&ascend) {
					swap(j);
					flipped = true;
				}
				else if (usi.get(j).getDivLevel() < usi.get(j+1).getDivLevel()&&(!ascend)) {
					swap(j);
					flipped = true;
				}
			}
			if (!flipped) {
				return xhtml_page;
			}
		}
		return xhtml_page;
	}

	public String sortByTotalScore() {
		ascend = toAscend("totalScore");
		usi = overview.userScoresItemTable(status,1,ascend);
		for (int i = usi.size(); --i>=0; ) {
			boolean flipped = false;
			for (int j = 0; j<i; j++) {
				if (usi.get(j).getTotalScore() > usi.get(j+1).getTotalScore()&&ascend) {
					swap(j);
					flipped = true;
				}
				else if (usi.get(j).getTotalScore() < usi.get(j+1).getTotalScore()&&(!ascend)) {
					swap(j);
					flipped = true;
				}
			}
			if (!flipped) {
				return xhtml_page;
			}
		}
		return xhtml_page;
	}

	public String sortByAverageLevel() {
		ascend = toAscend("averageLevel");
		usi = overview.userScoresItemTable(status,1,ascend);
		for (int i = usi.size(); --i>=0; ) {
			boolean flipped = false;
			for (int j = 0; j<i; j++) {
				if (usi.get(j).getAvgLevel() > usi.get(j+1).getAvgLevel()&&ascend) {
					swap(j);
					flipped = true;
				}
				else if (usi.get(j).getAvgLevel() < usi.get(j+1).getAvgLevel()&&(!ascend)) {
					swap(j);
					flipped = true;
				}
			}
			if (!flipped) {
				return xhtml_page;
			}
		}
		return xhtml_page;
	}



	//******* INTERNAL METHODS *******
	//********************************

	private boolean toAscend(String sortMethod) {
		if (sortMethod.equals(lastSorted)) {
			lastSorted = sortMethod;
			if (ascend) return false;
			else return true;
		}
		lastSorted = sortMethod;
		return false;
	}

	private void swap(int j) {
		UserScoresItem T = usi.get(j);
		usi.set(j, usi.get(j+1));
		usi.set(j+1,T);
	}
}