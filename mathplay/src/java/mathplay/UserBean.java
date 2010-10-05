/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mathplay;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author ikkedinpc
 */
@ManagedBean
@SessionScoped
public class UserBean {
    public String name;
    public String pw;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public String getPw() {
        return pw;
    }
    
}
