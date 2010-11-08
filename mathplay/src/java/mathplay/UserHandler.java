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

@ManagedBean(name = "UserHandler")
@SessionScoped
public class UserHandler {
    private UserBean tempUser = new UserBean();
    private String name;
    private String userName;
    private String role;
    
    private String password;
  //  private String[] grades = {"1 A", "1 B", "2 A","2 B", "3 A", "3 B", "Spesialundervisning"};
    private Overview overview = new Overview();

    public UserHandler() {
    }
   
    public String getName() {return name;}
    public void setName(String name) {this.name = name; tempUser.setName(name);}
    
    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName; tempUser.setUserName(userName);}
      
    public String getRole() {return role;}
    public void setRole(String role) {this.role = role; tempUser.setRole(role);}

    public void setPassword(String password) {this.password = password;}

    /** Add a pupil, used by admins/tutors */
    public void addUser() {
        tempUser.setRole("user");
        overview.addUser(tempUser, "lolmann");
    }
    /** Add an admin/tutor, used by super admin */
    public void addAdmin() {
        tempUser.setRole("admin");
        overview.addUser(tempUser, "lolmann");
    }

}
