package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @Author Alvaro Sanchez Romero
 * 
 * The persistent class for the IOTDEVICE database table.
 * 
 */

@Entity
@XmlRootElement
@Table(name="iotdevice")
@NamedQuery(name="IoTDevice.findAll", query="SELECT i FROM IoTDevice i")
public class IoTDevice implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name = "name_dev")
	private String name;
	private String url;
	private String picture;
	private String description;
	private int numFollowers;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "Categories")
	@Column(name = "Name")
	private Set<String> categories;
	
	 @Enumerated(EnumType.STRING)
	private Status status;
	
	@OneToMany
	private List<Feedback> feedbacks;
	
	@ManyToMany
	@JoinTable(
		name = "requestedFollows",
		joinColumns = @JoinColumn(name = "id_device"),
		inverseJoinColumns = @JoinColumn(name = "id_appuser")
			)
	private Set<User> requestedFollows;
	
	//Create elements ids automatically, incremented 1 by 1
	/*@TableGenerator(
			  name = "yourTableGenerator",
			  allocationSize = 1,
			  initialValue = 1)*/

	
	public IoTDevice(String name, String url, String picture, String description, String ... categories) {
		this.name = name;
		this.url = url;
		this.picture = picture;
		this.description = description;
		this.categories = new TreeSet<>();
		this.addCategories(categories);
		this.status = Status.PUBLISHED;
		this.feedbacks = new ArrayList<>();
		this.numFollowers = 0;
	}
	
	public IoTDevice() {		
	}

	
	public int getNumFollowers() {
		return numFollowers;
	}


	public void incNumFollowers() {
		this.numFollowers++;
	}
	
	public void decNumFollowers() {
		this.numFollowers--;
	}

	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}

	public Set<User> getRequestedFollows() {
		return requestedFollows;
	}

	public void setRequestedFollows(Set<User> requestedFollows) {
		this.requestedFollows = requestedFollows;
	}
	
	public boolean addRequestedFollow(User user) {
		return this.requestedFollows.add(user);
	}
	
	public void removeRequestedFollow(User user) {
		for(User u : requestedFollows) {
			if(u.getUsername().equals(user.getUsername())) {
				this.requestedFollows.remove(u);
			}
		}
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<String> getCategories() {
		ArrayList<String> listCat = new ArrayList<String>();
		listCat.addAll(categories);
		return listCat;
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public void addCategories(String ... categories) {
		this.categories.addAll(Arrays.asList(categories));
	}
	
	public double getAverageRating() {
		double sum = 0;
		double ratingQuantity = 0;
		
		for(Feedback f : this.feedbacks) {
			sum += f.getRating();
			ratingQuantity += f.isRating();
		}
		if(ratingQuantity == 0) {
			return 0;
		} else {
			return sum/ratingQuantity;
		}
	}
	
	public void addFeedback(Feedback f) {
		this.feedbacks.add(f);
	}

}
