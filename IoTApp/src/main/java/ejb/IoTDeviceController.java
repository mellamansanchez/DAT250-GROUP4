package ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import entities.Comment;
import entities.Feedback;
import entities.IoTDevice;
import entities.Rating;
import entities.User;

@Named(value = "IoTDeviceController")
@RequestScoped
public class IoTDeviceController implements Serializable{
	
	private static final long serialVersionUID = 1L;
		
	private String name;
	
	private String url;
	
	private String description;

	private String category;
	
	private String status;
	
	private String devName;
	
	@EJB
	private IoTDeviceDao deviceDao;
	
	@EJB
	private UserDao userdao;
	
	private IoTDevice device;
	
	public String searchDevice() {
		List<IoTDevice> devs = deviceDao.getAllDevices();
		
		if(devs.isEmpty()) {
			return Constants.MAIN_SCREEN;
		}
		
		for(IoTDevice d : devs) {
			if (d.getName().equals(devName)) {
				device = d;
				HttpSession session = SessionUtils.getSession();
				session.setAttribute(Constants.DEVICE, device.getName());
				return Constants.DEVICE;
			}
		}
		return Constants.MAIN_SCREEN;
	}
	
	public String addDevice() {
		if(name!=null && url!=null && description!=null && category!=null && !deviceExists(name)) {
			device = new IoTDevice(name, url, "", description, category);
			HttpSession session = SessionUtils.getSession();
			String name_test = session.getAttribute(Constants.USERNAME).toString();
			User user = userdao.getUserByUsername(name_test);
			user.addDevice(device);
			
			try {
				deviceDao.persist(device);
				userdao.update(user);
			} catch (NamingException | JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Constants.MY_PROFILE;
		} else {
			return Constants.ADD_DEVICE;
		}
	}
	
	public boolean deviceExists(String deviceName) {
		return !deviceDao.getDeviceByName(deviceName).isEmpty();
	}
	
	public String goToPopularDevices() {
		return Constants.POPULAR_DEVICES;
	}
	
	public String goToAllDevices() {
		return Constants.ALL_DEVICES;
	}
	
	public String goToTopRatedDevices() {
		return Constants.TOP_RATED_DEVICES;
	}
	
	public String goToFollowedDevices() {
		return Constants.FOLLOWED_DEVICES;
	}
	
	public String goToMyProfile() {
		return Constants.MY_PROFILE;
	}
	
	public String goToAddDevice() {
		return Constants.ADD_DEVICE;
	}
	
	public String goToDevice(IoTDevice dev) {
		HttpSession session = SessionUtils.getSession();
		session.setAttribute(Constants.DEVICE, dev.getName());
		
		if(this.isDeviceFollowed()) {
			return Constants.DEVICE_F;
		}
		return Constants.DEVICE;
	}
	
	public String goToAdminMyDevice(IoTDevice dev) {
		HttpSession session = SessionUtils.getSession();
		session.setAttribute(Constants.DEVICE, dev.getName());
	
		return Constants.ADMIN_MY_DEVICE;
	}
	
	public IoTDevice getDevice() {
		return device;
	}

	public void setDevice(IoTDevice device) {
		this.device = device;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public UserDao getUserdao() {
		return userdao;
	}

	public void setUserdao(UserDao userdao) {
		this.userdao = userdao;
	}
	
	public List<IoTDevice> getAllDevices() {
		return deviceDao.getAllDevices();
	}

	public List<IoTDevice> getPopularDevices() {
		return deviceDao.getPopularDevices();
	}

	public List<IoTDevice> getTopRatedDevices() {
		return deviceDao.getTopRatedDevices();
	}

	public IoTDeviceDao getDeviceDao() {
		return deviceDao;
	}

	public void setDeviceDao(IoTDeviceDao deviceDao) {
		this.deviceDao = deviceDao;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public String getcategory() {
		return category;
	}

	public void setcategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeviceName() {
		HttpSession session = SessionUtils.getSession();
		this.device = deviceDao.getDeviceByName((String) session.getAttribute(Constants.DEVICE)).get(0);
		return device.getName();
	}
	
	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public String getDeviceUrl() {
		return device.getUrl();
	}
	
	public String getDeviceStatus() {
		return device.getStatus().toString();
	}

	public String getDeviceDescription() {
		return device.getDescription();
	}
	
	public ArrayList<String> getDeviceCategories() {
		HttpSession session = SessionUtils.getSession();
		this.device = deviceDao.getDeviceByName((String) session.getAttribute(Constants.DEVICE)).get(0);
		return device.getCategories();
	}
	
	public double getAverageRating() {
		return device.getAverageRating();
	}
	
	public ArrayList<String> getComments() {
		ArrayList<String> comments = new ArrayList<String>();
		
		for(Feedback f : device.getFeedbacks()) {
			if(f instanceof Comment) {
				comments.add(((Comment) f).getComment());
			}
		}
		return comments;
	}	
	
	public String follow() {
		HttpSession session = SessionUtils.getSession();
		this.device = deviceDao.getDeviceByName((String) session.getAttribute(Constants.DEVICE)).get(0);
		if(!isDeviceFollowed() && !isDeviceFollowRequested()) {
			this.device.addRequestedFollow(userdao.getUserByUsername((String) session.getAttribute(Constants.USERNAME)));
			this.deviceDao.update(device);
		}
		
		return Constants.DEVICE; 
	}
	
	public ArrayList<User> getRequestedFollows() {
		HttpSession session = SessionUtils.getSession();
		IoTDevice dev = deviceDao.getDeviceByName((String) session.getAttribute(Constants.DEVICE)).get(0);
		ArrayList<User> requested = new ArrayList<User>(); 
		requested.addAll(dev.getRequestedFollows());
		return requested;
	}
	
	public boolean isDeviceFollowed() {
		HttpSession session = SessionUtils.getSession();
		User user = userdao.getUserByUsername((String) session.getAttribute(Constants.USERNAME));
		this.device = deviceDao.getDeviceByName((String) session.getAttribute(Constants.DEVICE)).get(0);
		for(IoTDevice i : user.getDevicesFollowed()) {
			if(i.getName().equals(this.device.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isDeviceFollowRequested() {
		HttpSession session = SessionUtils.getSession();
		User user = userdao.getUserByUsername((String) session.getAttribute(Constants.USERNAME));
		for(User u : getRequestedFollows()) {
			if(u.getUsername().equals(user.getUsername())) {
				return true;
			}
		}
		return false;
	}
}
