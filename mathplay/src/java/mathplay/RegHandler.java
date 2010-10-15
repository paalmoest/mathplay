/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mathplay;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Bertie
 */

@ManagedBean(name = "RegHandler")
@SessionScoped
public class RegHandler {

    private ChallengeBean tempChall = new ChallengeBean();
    private int CID =0;
    private String text;
    private double correct;
    private int difficulty;
    private String type;
    private String[] types = {"Addisjon", "Subtraksjon", "Multiplikasjon","Divisjon"};
    private Overview overview = new Overview();

    public RegHandler() {
    }

    public int getCID() {return CID;}
    public void setCID(int text) {tempChall.setCID(CID);}

    // PROPERTY: text
    public String getText() {return text;}
    public void setText(String text) {tempChall.setText(text);}

    // PROPERTY: correct
    public double getCorrect() {return correct;}
    public void setCorrect(double correct) {tempChall.setCorrect(correct);}

    // PROPERTY: difficulty
    public int getDifficulty() {return difficulty;}
    public void setDifficulty(int difficulty) {tempChall.setDifficulty(difficulty);}

    // PROPERTY: type


    public String[] getTypes() {
        return types;
    }


    public String getType() {return type;}
    public void setType(String type) {tempChall.setType(type);}

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

     public void addChallenge(){
            overview.addChallenge("Lærer id 1", tempChall);
        }

}
