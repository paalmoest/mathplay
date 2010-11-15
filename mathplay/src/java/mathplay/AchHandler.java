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
    private ArrayList<AchievementBean> allAch = new ArrayList<AchievementBean>();
    private Overview overview = new Overview();


      public AchHandler(){
      }





    public ArrayList<AchievementBean> getAllAch(){
        allAch = overview.getAllAchievements();
    
    return allAch;



    }
}
