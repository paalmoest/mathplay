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

@ManagedBean
@SessionScoped
public class UserHandler {
    private UserBean tempUser = new UserBean();
    private String password;
    private String[] grades = {"1 A", "1 B", "2 A","2 B", "3 A", "3 B", "Spesialundervisning"};
    private Overview overview = new Overview();
    public UserHandler() {
    }

    public void setName(String name) {
        tempUser.setName(name);
    }

    public String getName() {
        return tempUser.getName();
    }

    public void setUserName(String userName) {
        tempUser.setUserName(userName);
    }

    public String getUserName() {
        return tempUser.getUserName();
    }

    public void setRole(String role) {
        tempUser.setRole(role);
    }

     public String getRole() {
        return tempUser.getRole();
    }

    public void setGrade(String grade) {
        tempUser.setGrade(grade);
    }

    public String getGrade() {
        return tempUser.getGrade();
    }

    public String[] getGrades() {
        return grades;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /** Add a pupil, used by admins/tutors */
    public void addUser() {
        tempUser.setRole("pupil");
        overview.addUser(tempUser, "lolmann");
    }
    /** Add an admin/tutor, used by super admin */
    public void addAdmin() {
        tempUser.setRole("admin");
        overview.addUser(tempUser, "lolmann");
    }

}
