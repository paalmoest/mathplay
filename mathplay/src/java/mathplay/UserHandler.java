/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mathplay;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.*;
import javax.faces.model.SelectItem;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


@ManagedBean(name = "UserHandler")
@SessionScoped
public class UserHandler {
    private UserBean tempUser = new UserBean();
    private String name;
    private String userName;
    private String role;
    private int deleteMe;
    private String password;
    private Overview overview = new Overview();
    private ArrayList<UserBean> teacherUsers = overview.getTeacherUsers();
    private List<SelectItem> names = overview.getNames();

    public UserHandler() {
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name; tempUser.setName(name);}

    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName; tempUser.setUserName(userName);}

    public String getRole() {return role;}
    public void setRole(String role) {this.role = role; tempUser.setRole(role);}

	public String getPassword(){return password;}
    public void setPassword(String password) {this.password = password;}

    public List<SelectItem> getNames() {return overview.getNames();}

    public int getDeleteMe() {return deleteMe;}
    public void setDeleteMe(int deleteMe) {this.deleteMe = deleteMe;}

    /** Add a pupil, used by admins/tutors */
    public void addUser() {
		if (overview.checkUser(tempUser.getUserName())) {
			FacesMessage fm = new FacesMessage("Brukernavnet finnes allerede");
			FacesContext.getCurrentInstance().addMessage("Brukernavnet finnes allerede", fm);
		} else {
			tempUser.setRole("user");
			overview.addUser(tempUser, "lolmann");
			name = "";
			userName = "";
		}

	}
	/** Add an admin/tutor, used by super admin */
	public void addAdmin() {
		if (overview.checkUser(tempUser.getUserName())) {
			FacesMessage fm = new FacesMessage("Brukernavnet finnes allerede");
			FacesContext.getCurrentInstance().addMessage("Brukernavnet finnes allerede", fm);
		} else if (password.equals("")){
			FacesMessage fm = new FacesMessage("Skriv inn et passord");
			FacesContext.getCurrentInstance().addMessage("Skriv inn et passord", fm);
		} else {
			tempUser.setRole("admin");
			overview.addUser(tempUser, password);
			name = "";
			userName = "";
		}
    }

    /** Deletes a user from the database **/
    public void deleteUser() {
        for (int i = 0; i < teacherUsers.size(); i++) {
            if (deleteMe == teacherUsers.get(i).getUserId()) {
                tempUser = teacherUsers.get(i);
            }
        }
        for (int i = 0; i < names.size(); i++) {
            if (deleteMe == names.get(i).getValue()) {
                names.remove(i);
            }
        }
        System.out.println("Slett: " + tempUser.getName());
        overview.deleteUser(tempUser);
        deleteMe = 0;
    }
     public void updateUser(){
		  if (password.equals("") || tempUser.getName().equals("")){
			 FacesMessage fm = new FacesMessage("Fyll inn begge felt");
			 FacesContext.getCurrentInstance().addMessage("Fyll inn begge felt", fm);
		  } else {
			  overview.updateUserprofile(tempUser, password);
		  }
    }

}
