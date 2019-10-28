package ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSSessionMode;
import javax.jms.Topic;
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;

import ejb.UserDao;
import entities.IoTDevice;
import entities.User;

@Named(value = "userController")
@RequestScoped
public class UserController implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	@JMSConnectionFactory("jms/dat250/ConnectionFactory")
	@JMSSessionMode(JMSContext.AUTO_ACKNOWLEDGE)
	private JMSContext context;


	@Resource(lookup = "jms/dat250/Topic")
	private Topic topic;
	
	// Injected DAO EJB:
	@EJB
	private UserDao userdao;
	
	@EJB
	private IoTDeviceDao deviceDao;

	private User user;
	
	private IoTDevice device;

	
	public boolean validateUser(String username, String pass) {
		user = userdao.getUserByUsername(username);
		
		return user != null && user.getPassword().equals(pass);
	}
	
	public String getUsername() {
		HttpSession session = SessionUtils.getSession();
		this.user = userdao.getUserByUsername((String) session.getAttribute(Constants.USERNAME));
		return user.getUsername();
	}
	
	public String getEmail() {
		HttpSession session = SessionUtils.getSession();
		this.user = userdao.getUserByUsername((String) session.getAttribute(Constants.USERNAME));
		return user.getEmail();
	}
	
	public String goToMyDevices() {
		return Constants.MY_DEVICES;
	}
	
	public String goToAddDevice() {
		return Constants.ADD_DEVICE;
	}
	
	public String unfollow() {
		HttpSession session = SessionUtils.getSession();
		IoTDevice device = deviceDao.getDeviceByName((String) session.getAttribute(Constants.DEVICE)).get(0);
		this.user = userdao.getUserByUsername((String) session.getAttribute(Constants.USERNAME));
		user.unfollow(device);
		userdao.update(user);
		deviceDao.update(device);
		
		return Constants.DEVICE; 
	}
	
	public String adminFollow(User requested, boolean result) {
		HttpSession session = SessionUtils.getSession();
		this.device = deviceDao.getDeviceByName((String) session.getAttribute(Constants.DEVICE)).get(0);
		this.user = userdao.getUserByUsername((String) session.getAttribute(Constants.USERNAME));
		boolean flag = this.user.adminFollowRequest(this.device, requested, result);
		this.userdao.update(requested);
		this.deviceDao.update(device);
		if(flag) {
			context.createProducer()
			.setProperty("topicUser", "dweet")
			.setProperty("deviceName", device.getName())
			.send(topic, user);
		}
		
		return Constants.ADMIN_MY_DEVICE;
	}
	
	public String acceptFollow(User u) {
		return adminFollow(u, true);
	}
	
	public String rejectFollow(User u) {
		return adminFollow(u, false);
	}
	
	public ArrayList<IoTDevice> getMyDevices() {
		HttpSession session = SessionUtils.getSession();
		this.user = userdao.getUserByUsername((String) session.getAttribute(Constants.USERNAME));
		return user.getDevicesOwned();
	}
	
	public Set<IoTDevice> getFollowedDevices() {
		HttpSession session = SessionUtils.getSession();
		this.user = userdao.getUserByUsername((String) session.getAttribute(Constants.USERNAME));
		return user.getDevicesFollowed();
	}
	
}
