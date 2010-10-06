/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mathplay;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author ikkedinpc
 */


@ManagedBean
@SessionScoped
  public class UserBean {
     private String name;
     private String role;
     private Logger logger = Logger.getLogger("oving8");
     private String rolle;
     private String navn;

     public UserBean(String sNavn, String sRolle) {
		 navn = sNavn;
		 rolle = sRolle;
	 }
    public UserBean() {
	 }

     public String getName() {
        if (name == null) getUserData();
        return name == null ? "" : name;
     }

     public String getRole() { return role == null ? "" : role; }
     public void setRole(String newValue) { role = newValue; }

     public boolean isInRole() {
        ExternalContext context
           = FacesContext.getCurrentInstance().getExternalContext();
        Object requestObject =  context.getRequest();
        if (!(requestObject instanceof HttpServletRequest)) {
           logger.severe("request object has type " + requestObject.getClass());
           return false;
        }
       HttpServletRequest request = (HttpServletRequest) requestObject;
       return request.isUserInRole(role);
    }

     private void getUserData() {
        ExternalContext context
           = FacesContext.getCurrentInstance().getExternalContext();
        Object requestObject =  context.getRequest();
        if (!(requestObject instanceof HttpServletRequest)) {
           logger.severe("request object has type " + requestObject.getClass());
           return;
        }
        HttpServletRequest request = (HttpServletRequest) requestObject;
        name = request.getRemoteUser();
     }

     public String getUser() {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		Object requestObject =  context.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestObject;
		name = request.getRemoteUser();
		return name;
	}
	public String getRolle() {
		return rolle;
	}

	public void setRolle(String nyRolle) {
		rolle = nyRolle;
	}

	public String getNavn() {
		return navn;
	}

	public void setNavn(String nyNavn) {
		navn = nyNavn;
	}
	public synchronized String logout() throws Exception
	 {
	  ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
	  HttpSession session = (HttpSession) ectx.getSession(false);
	  session.invalidate();
	  return "logout";
    }
 }