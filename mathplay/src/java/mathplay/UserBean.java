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
    private String  name, userName, role;

     public UserBean(int userId,String name, String userName, String role) {
		this.userId = userId;
		this.name = name;
		this.userName = userName;
		this.role = role;
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

     //Need to add Conextstuff here.
     public String getCurrentUser() {
            ExternalContext context
            = FacesContext.getCurrentInstance().getExternalContext();
            Object requestObject =  context.getRequest();
            HttpServletRequest request = (HttpServletRequest) requestObject;
            name = request.getRemoteUser();
            return name;
    }

 	public synchronized String logout() throws Exception {
		System.out.println("ATTEMPTING LOGOUT");
 		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
 		HttpSession session = (HttpSession) ectx.getSession(false);
 		session.invalidate();
 		loadPage("/mathplay");
 		return "index?faces-redirect=true";
	}

	public synchronized void loadPage(String pagename) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
		try {
			response.sendRedirect(pagename);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		FacesContext.getCurrentInstance().responseComplete();
	}
 }
