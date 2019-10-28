package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import entities.User;
import entities.IoTDevice;

@Stateless
public class UserDao {
	@PersistenceContext(unitName="Dat250IoTApp")
	private EntityManager em;
	
	public void persist(User user) {
		em.persist(user);
	}
	
	public void update(User user) {
        em.merge(user);
    }

    @SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		Query query = em.createQuery("SELECT u FROM User u");
		List<User> users = new ArrayList<User>();
		users = query.getResultList();
		return users;
	}
    
    @SuppressWarnings("unchecked")
	public List<User> getUser(long id) {
		Query query = em.createQuery("SELECT u FROM User u WHERE u.id = "+id);
		List<User> users = new ArrayList<User>();
		users = query.getResultList();
		return users;
	}
    
    public User getUserByUsername(String username) {
    	TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
		try {
			return query
					.setParameter("username", username)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
    
	public boolean validateUser(String username, String pass) {
		User user = getUserByUsername(username);
		
		return user != null && user.getPassword().equals(pass);
	}
    
    //REVISE - query not correct 100%
    @SuppressWarnings("unchecked")
	public List<User> getRequestedFollows(IoTDevice device) {
		Query query = em.createQuery("SELECT u FROM RequestedFollows r, User u WHERE "+device.getId()+" = r.id_device AND r.id_appuser = u.id");
		List<User> users = new ArrayList<User>();
		users = query.getResultList();
		return users;
	}
    
    //REVISE - query not correct 100%
    @SuppressWarnings("unchecked")
	public List<User> getDevicesFollowed(IoTDevice device) {
		Query query = em.createQuery("SELECT u FROM DevicesFollowed df, User u WHERE df.ID_DEVICE = "+device.getId()+" AND u.ID = df.ID_APPUSER");
		List<User> users = new ArrayList<User>();
		users = query.getResultList();
		return users;
	}
}
