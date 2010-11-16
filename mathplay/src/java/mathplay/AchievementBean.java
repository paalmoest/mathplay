/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mathplay;

/**
 *
 * @author Stian
 */
public class AchievementBean {
    private int achId, achValue, achStatus;
    private String achName, achText, achImg;
    private boolean done = false;

    public AchievementBean(int achId, int achValue, String achName, String achText, String achImg, int achStatus){
        this.achId = achId;
        this.achValue = achValue;
        this.achName = achName;
        this.achText = achText;
        this.achImg = achImg;
        this.achStatus = achStatus;

    }

     public AchievementBean(int achId, int achValue, String achName, String achText, String achImg){
        this.achId = achId;
        this.achValue = achValue;
        this.achName = achName;
        this.achText = achText;
        this.achImg = achImg;

    }

    public AchievementBean(){}

    public int getAchId() {return achId;}
    public void setAchId(int achId) {this.achId = achId;}

    public int getAchValue() {return achValue;}
    public void setAchValue(int achValue) {this.achValue = achValue;}

    public String getAchName() {return achName;}
    public void setAchName(String achName) {this.achName = achName;}

    public String getAchText() {return achText;}
    public void setAchText(String achText) {this.achText = achText;}

    public String getAchImg() {return achImg;}
    public void setAchImg(String achImg) {this.achImg = achImg;}

    public int getAchStatus() {return achStatus;}
    public void setAchStatus(int achStatus) {this.achStatus = achStatus;}

    public void setDone(){done = true;}

    public boolean getDone(){return done;}
}
