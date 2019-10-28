package entities;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSSessionMode;
import javax.jms.Topic;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Table(name="appuser")
//@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
@Entity
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(unique = true)
	private String username;
	private String email;
	private String password;
	
	
	@ManyToMany
	@JoinTable(
		name = "devicesFollowed",
		joinColumns = @JoinColumn(name = "id_user"),
		inverseJoinColumns = @JoinColumn(name = "id_device")
			)
	private Set<IoTDevice> devicesFollowed;
	
	@OneToMany
	@JoinTable(
		name = "devicesOwned",
		joinColumns = @JoinColumn(name = "id_name"),
		inverseJoinColumns = @JoinColumn(name = "id_device")
			)
	private ArrayList<IoTDevice> devicesOwned;
	
	public User() {
	}
	
	public User(String u, String e, String p) {
		username = u;
		email = e;
		password = p;
		devicesFollowed = new TreeSet<IoTDevice>();
		devicesOwned = new ArrayList<IoTDevice>();
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public boolean followDevice(IoTDevice device) {
		if (devicesFollowed.contains(device)) {
			return false;
		}
		else {
			return device.addRequestedFollow(this);
		}
	}
	
	public boolean unfollowDevice(IoTDevice device) {
		if(devicesFollowed.contains(device)) {
			devicesFollowed.remove(device);
			device.decNumFollowers();
			return true;
		}
		else if(device.getRequestedFollows().contains(this)) {
			device.getRequestedFollows().remove(this);
			device.decNumFollowers();
			return true;
		}
		return false;
	}
	
	public boolean addDevice(String n, String url, String p, String d, String ... c) {
		IoTDevice device = new IoTDevice(n, url, p, d, c);
		return devicesOwned.add(device);
	}

	public boolean addDevice(IoTDevice device) {
		return devicesOwned.add(device);
	}
	
	public boolean deleteDevice(IoTDevice device) {
		return devicesOwned.remove(device);
	}
	
	public boolean adminFollowRequest(IoTDevice device, User user, boolean accept) {
		device.removeRequestedFollow(user);
		
		if(accept) {
			user.devicesFollowed.add(device);
			device.incNumFollowers();
			return true;
		}
		return false;
	}
	
	public boolean equals(User user) {
		if(this.username == user.getUsername()) {
			return true;
		}	
		return false;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<IoTDevice> getDevicesFollowed() {
		return devicesFollowed;
	}

	public void setDevicesFollowed(Set<IoTDevice> devicesFollowed) {
		this.devicesFollowed = devicesFollowed;
	}

	public ArrayList<IoTDevice> getDevicesOwned() {
		return this.devicesOwned;
	}

	public void setDevicesOwned(ArrayList<IoTDevice> devicesOwned) {
		this.devicesOwned = devicesOwned;
	}
	
	public void unfollow(IoTDevice device) {
		for(IoTDevice d : devicesFollowed) {
			if(d.getName().equals(device.getName())) {
				this.devicesFollowed.remove(d);
				device.decNumFollowers();
			}
		}
	}
	
	
}
