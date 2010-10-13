/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mathplay;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.*;

@ManagedBean
@SessionScoped
public class UserBean {
    private int userId;
    private String  name, userName, role, grade;
  
     public UserBean(int userId,String name, String userName, String role, String grade) {
		 this.userId = userId;
                 this.name = name;
                 this.userName = userName;
		 this.role = role;
                 this.grade = grade;
     }

     public UserBean() {
     }

     /** Property: user id */
     public int getUserId() {
         return userId;
     }
     
     public void setUserId(int userId) {
         this.userId = userId;
     }

     /** Property: name */
     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }

      /** Property userName */
     public String getUserName() {
         return userName;
     }

     public void setUserName(String userName) {
         this.userName = userName;
     }

     /** Property: role */
     public String getRole() {
         return role;
     }

     public void setRole(String role) {
         this.role = role;
     }

     /** Property: grade */
     public String getGrade() {
         return grade;
     }

     public void setGrade(String grade) {
         this.grade = grade;
     }

     //Need to add Conextstuff here.
     public int getCurrentUser() {
         return 1;
     }
     
     public synchronized String logout() throws Exception {
	  ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
	  HttpSession session = (HttpSession) ectx.getSession(false);
	  session.invalidate();
	  return "logout";
    }
 }
