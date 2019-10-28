package ejb;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import entities.User;

/**
 * 
 * Dat250 course
 *
 *Session Controller for validate an user 
 */

@Named(value = "sessionController")
@SessionScoped
public class SessionController implements Serializable {

	@EJB
	private UserDao userdao;
	
	private static final long serialVersionUID = 1L;

	private String password;

	private String passwordRepetition;
	
	private String email;
	
	private String username;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPasswordRepetition() {
		return passwordRepetition;
	}

	public void setPasswordRepetition(String passwordRepetition) {
		this.passwordRepetition = passwordRepetition;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String validateUsernamePassword() {
		HttpSession session = SessionUtils.getSession();
		session.setAttribute(Constants.USERNAME, this.username);

		if(userdao.validateUser(username, password)) {
			return Constants.MAIN_SCREEN;			
		}else {
			return Constants.LOGIN;
		}		
	}

	public String logout() {
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		return Constants.LOGIN;
	}
	
	public String goToRegister() {
		return Constants.REGISTER;
	}	
	
	private boolean userExists(String username) {
		User user = userdao.getUserByUsername(username);
		
		return user != null;
	}
	
	public String register() {
		User user;
		
		if(userExists(username)) {
			return Constants.REGISTER;			
		}else {
			if(password.equals(passwordRepetition)) {
				user = new User(username, email, password);
				userdao.persist(user);
				return validateUsernamePassword();
			}
			return Constants.REGISTER;
		}
	}
	
	public String redirect() throws IOException {
		HttpSession session = SessionUtils.getSession();
		if (session.getAttribute(Constants.USERNAME)==null) {
			SessionUtils.getResponse().sendRedirect(Constants.LOGIN + ".xhtml");
		}
		return Constants.MAIN_SCREEN;
	}
	
	public String goToLogin() {
		return Constants.LOGIN;
	}
	
	public String goToMainScreen() {
		return Constants.MAIN_SCREEN;
	}

}
