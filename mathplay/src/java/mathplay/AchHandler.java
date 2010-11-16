/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mathplay;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;

@ManagedBean(name = "AchHandler")
@SessionScoped

/**
 *
 * @author Nelich
 * Detter er AchievementHandler klassen
 */
public class AchHandler implements Serializable {


    //variabler
    private ArrayList<AchievementBean> yourAch = new ArrayList<AchievementBean>();
    private ArrayList<AchievementBean> allAch = new ArrayList<AchievementBean>();
    private ArrayList<String> newAch = new ArrayList<String>();
    private boolean gratz;
    private Overview overview = new Overview();
    private ArrayList<String> yourChall = new ArrayList<String>();
    private int add;
    private int sub;
    private int mul;
    private int div;
    private int streak;
    private int points;
    private String addText = "Addition";
    private String subText = "Subtraction";
    private String mulText = "Multiplication";
    private String divText = "Division";

   public AchHandler(){
       System.out.println("--- START AchHandler ----");
       checkAch();
    }

   public String showAllAch(){
      checkAch();
      yourAch = overview.getAllAchievements();
      allAch = overview.listAllAchievements();
      calcPoints();
      return "achievement";
   }


   public int getStreak() {return streak;}
   public void setStreak(int streak) {
        this.streak = streak;
        checkNewAchivement();
   }
   public int getPoints() {return points;}

   public void updateAdd(){
        add++;
        streak++;
        checkNewAchivement();
        System.out.println("update: add " + add + "med en streak på " + streak);
    }

   public void updateSub(){
        sub++;
        streak++;
        checkNewAchivement();
        System.out.println("update: add" + sub + "med en streak på " + streak);
    }

   public void updateMul(){
       mul++;
       streak++;
       checkNewAchivement();
       System.out.println("update: add" + mul + "med en streak på " + streak);
    }

   public void updateDiv(){
        div++;
        streak++;
        checkNewAchivement();
        System.out.println("update: add" + div + "med en streak på " + streak);
    }

   private void calcPoints() {
        points = 0;
        for (int i = 0; i < yourAch.size(); i++) {
            points += yourAch.get(i).getAchValue();
        }
    }

   public ArrayList<AchievementBean> getYourAch(){
        return yourAch;
    }

   private void checkAch(){
       yourChall.clear();
       yourChall = overview.getChallProgress();
       int subAdd = 0;
       int subSub = 0;
       int subMul = 0;
       int subDiv = 0;

        for (String chall : yourChall) {
            if (chall.equals(addText)){subAdd++;}
            else if (chall.equals(subText)){subSub++;}
            else if (chall.equals(mulText)){subMul++;}
            else if (chall.equals(divText)){subDiv++;}
        }
        add = subAdd;
        sub = subSub;
        mul = subMul;
        div = subDiv;

        for (int i = 0; i < yourAch.size(); i++){
            int id = yourAch.get(i).getAchId();
            if (id==20){streak = yourAch.get(i).getAchStatus();}
            else if (id==19){streak = yourAch.get(i).getAchStatus();}
            else if (id==18){streak = yourAch.get(i).getAchStatus();}
            else if (id==17){streak = yourAch.get(i).getAchStatus();}
        }
    }


   public void setDone(){
        for (int i = 0; i <yourAch.size(); i++){
            int id = yourAch.get(i).getAchId();
            allAch.get(id-1).setDone();
         }
    }


    /**sjekker om oppnåelser er oppnådd */
   public void checkNewAchivement() {
        checkAdd10();
        checkAdd25();
        checkAdd50();
        checkAdd100();
        checkSub10();
        checkSub25();
        checkSub50();
        checkSub100();
        checkMul10();
        checkMul25();
        checkMul50();
        checkMul100();
        checkDiv10();
        checkDiv25();
        checkDiv50();
        checkDiv100();
        checkIf10();
        checkIf25();
        checkIf50();
        checkIf100();
        oneChallDone();
    }
    //addisjon
   public void checkAdd10() {
	   System.out.println("DEBUGG_checkadd10_ADD: "+add);
       if (add == 1) overview.setAchievement(1, add, 0);
       else if(add == 10) {
           overview.updateAchievement(1, add, 1);
           newAch.add("add10");
       }
       else if (add<10) overview.updateAchievement(1, add, 0);
    }
   public void checkAdd25() {
       if (add == 11) overview.setAchievement(2, add, 0);
       else if (add==25) {
           overview.updateAchievement(2, add, 1);
           newAch.add("add25");
       }
       else if ((10<add)&&(add<25)) overview.updateAchievement(2, add, 0);
    }
   public void checkAdd50() {
       if (add == 26) overview.setAchievement(3, add, 0);
       else if (add==50) {
           overview.updateAchievement(3, add, 1);
           newAch.add("add50");
       }
       else if ((25<add)&&(add<50)) overview.updateAchievement(3, add, 0);
    }
   public void checkAdd100() {
       if (add == 51) overview.setAchievement(4, add, 0);
       else if (add==100) {
           overview.updateAchievement(4, add, 1);
           newAch.add("add100");
       }
       else if ((50<add)&&(add<100)) overview.updateAchievement(4, add, 0);
    }
   //subtraksjon
   public void checkSub10() {
       if (sub == 1) overview.setAchievement(5, sub, 0);
       else if (sub==10) {
           overview.updateAchievement(5, sub, 1);
           newAch.add("sub10");
       }
       else if (sub<10) overview.updateAchievement(5, sub, 0);
    }
   public void checkSub25() {
       if (sub == 11) overview.setAchievement(6, sub, 0);
       else if (sub==25) {
           overview.updateAchievement(6, sub, 1);
           newAch.add("sub25");
       }
       else if ((10<sub)&&(sub<25)) overview.updateAchievement(6, sub, 0);
    }
   public void checkSub50() {
       if (sub == 26) overview.setAchievement(7, sub, 0);
       else if (sub==50) {
           overview.updateAchievement(7, sub, 1);
           newAch.add("sub50");
       }
       else if ((25<sub)&&(sub<50)) overview.updateAchievement(7, sub, 0);
    }
   public void checkSub100() {
       if (sub == 51) overview.setAchievement(8, sub, 0);
       else if (sub==100) {
           overview.updateAchievement(8, sub, 1);
           newAch.add("sub100");
       }
       else if ((50<sub)&&(sub<100)) overview.updateAchievement(8, sub, 0);
    }
   //multiplikasjon
    public void checkMul10() {
        if (mul == 1) overview.setAchievement(9, mul, 0);
        else if (mul==10) {
            overview.updateAchievement(9, mul, 1);
            newAch.add("mul10");
        }
        else if (mul<10) overview.updateAchievement(9, mul, 0);
    }
   public void checkMul25() {
        if (mul == 11) overview.setAchievement(10, mul, 0);
        else if (mul==25) {
            overview.updateAchievement(10, mul, 1);
            newAch.add("mul25");
        }
        else if ((10<mul)&&(mul<25)) overview.updateAchievement(10, mul, 0);
    }
   public void checkMul50() {
        if (mul == 26) overview.setAchievement(11, mul, 0);
        else if (mul==50) {
            overview.updateAchievement(11, mul, 1);
            newAch.add("mul50");
        }
        else if ((25<mul)&&(mul<50)) overview.updateAchievement(11, mul, 0);
    }
   public void checkMul100() {
        if (mul == 51) overview.setAchievement(12, mul, 0);
        else if (mul==100) {
            overview.updateAchievement(12, mul, 1);
            newAch.add("mul100");
        }
        else if ((50<mul)&&(mul<100)) overview.updateAchievement(12, mul, 0);
    }
    //divisjon
    public void checkDiv10() {
        if (div == 1) overview.setAchievement(13, div, 0);
        else if (div==10) {
            overview.updateAchievement(13, div, 1);
            newAch.add("div10");
        }
        else if (div<10) overview.updateAchievement(13, div, 0);
    }
   public void checkDiv25() {
       if (div == 11) overview.setAchievement(14, div, 0);
       else if (div==25) {
           overview.updateAchievement(14, div, 1);
           newAch.add("div25");
       }
       else if ((10<div)&&(div<25)) overview.updateAchievement(14, div, 0);
    }
   public void checkDiv50() {
       if (div == 26) overview.setAchievement(15, div, 0);
       else if (div==50) {
           overview.updateAchievement(15, div, 1);
           newAch.add("div50");
       }
       else if ((25<div)&&(div<50)) overview.updateAchievement(15, div, 0);
    }
   public void checkDiv100() {
       if (div == 51) overview.setAchievement(16, div, 0);
       else if (div==100) {
           overview.updateAchievement(16, div, 1);
           newAch.add("div100");
       }
       else if ((50<div)&&(div<100)) overview.updateAchievement(16, div, 0);
   }
   //på rad
   public void checkIf10() {
       if (streak == 1) overview.setAchievement(17, streak, 0);
       else if (streak==10) {
           overview.updateAchievement(17, streak, 1);
           newAch.add("rad10");
       }
       else if (streak<10) overview.updateAchievement(17, streak, 0);
   }
   public void checkIf25() {
       if (streak == 1) overview.setAchievement(18, streak, 0);
       else if (streak==25) {
           overview.updateAchievement(18, streak, 1);
           newAch.add("rad25");
       }
       else if (streak<25) overview.updateAchievement(18, streak, 0);
   }
   public void checkIf50() {
       if (streak == 1) overview.setAchievement(19, streak, 0);
       else if (streak==50) {
           overview.updateAchievement(19, streak, 1);
           newAch.add("rad50");
       }
       else if (streak<50) overview.updateAchievement(19, streak, 0);
   }
   public void checkIf100() {
       if (streak == 1) overview.setAchievement(20, streak, 0);
       else if (streak==100) {
           overview.updateAchievement(20, streak, 1);
           newAch.add("rad100");
       }
       else if (streak<100) overview.updateAchievement(20, streak, 0);
   }
   public void oneChallDone() {
		if (add==1) {
			System.out.println("DEBUGG_ACHIHANDLER_1");
			overview.setAchievement(21, 1, 1);
			System.out.println("DEBUGG_ACHIHANDLER_2");
			newAch.add("1opg");
		}
   }


   public String getNewAchString() {
	   if (newAch.size()==0) System.out.println("newAch har ingen elementer, nullpointer INC");
	   String tempRetur = newAch.get(0);
	   newAch.remove(0);
	   return tempRetur;
	}

   /*public String getNewAchStringTWO() {
	   if (newAch.size()<2) System.out.println("newAch har ikke 2 elementer, nullpointer INC");
	   String tempRetur = newAch.get(0);
	   newAch.remove(1);
	   return tempRetur;
	}*/

	public boolean getAchComplete() {
		System.out.println("DEBUGG_ACHIHANDLER_NEWACHISIZE: "+ newAch.size());
		if (newAch.size() > 0) return true;
		else return false;
	}

	/*public boolean getAchCompleteTWO() {
		System.out.println("DEBUGG_ACHIHANDLER_NEWACHISIZE: "+ newAch.size());
		if (newAch.size() > 0) return true;
		else return false;
	}*/

}
