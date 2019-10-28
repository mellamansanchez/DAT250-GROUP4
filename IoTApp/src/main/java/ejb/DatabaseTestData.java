package ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.Comment;
import entities.Feedback;
import entities.IoTDevice;
import entities.User;

@Singleton
@Startup
public class DatabaseTestData {

	@PersistenceContext(unitName="Dat250IoTApp")
    private EntityManager em;
	
	@PostConstruct
	public void createData() {
		/*
		
		User userBob  = new User("Bob", "bob@example.com", "hunter2");
		//userBob.addDevice("Garage lights", "https://example.com/device/garage/", "/dev/bob/glights.jpg",
		//		"The lights in my garage, thought it would be funny to put this up here", "lights");
		
		//User userAnna = new User("Anna", "anna@example.com", "qwerty");
		
		
		Feedback fb = new Comment(userBob, "This is a comment", 0);
		
		em.persist(userBob);
		em.persist(fb);
		/*
		em.persist(userBob);
		*/
	}
}
