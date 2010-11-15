/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mathplay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "RegHandler")
@SessionScoped
public class RegHandler implements Serializable{

    private randomChallenge random = new randomChallenge();
    private ChallengeBean tempChall = new ChallengeBean();
    private int CID =0;
    private String text;
    private double correct;
    private int difficulty;
    private String type;
    private String tips;
    private boolean show;
    private String[] types = {"Addition", "Subtraction", "Multiplication","Division"};
    private Overview overview = new Overview();
    private ArrayList<ChallengeBean> allChall = new ArrayList<ChallengeBean>();
    private ArrayList<ChallengeBean> temp = new ArrayList<ChallengeBean>();

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

    // PROPERTY: tips
    public String getTips(){ return tips;}
    public void setTips(String tips) {tempChall.setTips(tips);}

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
    /*
     * Adds a challenge
     */
     public void addChallenge(){
            overview.addChallenge(tempChall);
            //Set fields blank
            text = "";
            correct=0;
            difficulty=0;
            type = "Addition";
            tips="";
     }
     /*
      * Makes a random challenge
      */
     public String randomize(){
         ChallengeBean cb = random.makeChallenge();
         text = cb.getText();
         correct = cb.getCorrect();
         difficulty = cb.getDifficulty();
         type = cb.getType();
         tips = cb.getTips();
         return "regChallenge";
     }

      public String showAllChall() {
         temp = overview.getAllChallenges();
         return "allChall";
     }




     public ArrayList<ChallengeBean> getAllChall(){
            return temp;

    }




     public void update() {
               show(true);
     }
     public void updateChall() {
             show(false);

               for(int i=0; i < temp.size();){
               allChall.add(temp.get(i));


            i++;
        }
        for (ChallengeBean chall : allChall) {

            System.out.println(chall);
           // chall.setText("TEST");
            overview.updateChallenge(chall);
         }

        allChall.clear();
       }

     public void show(boolean show){
         this.show = show;
     }

     public boolean isShow(){
         return show;
     }

}
