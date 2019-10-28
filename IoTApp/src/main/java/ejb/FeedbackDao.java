package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entities.Feedback;
import entities.User;

@Stateless
public class FeedbackDao {
	@PersistenceContext(unitName="Dat250IoTApp")
    private EntityManager em;
	
	public void persist(Feedback feedback) {
		em.persist(feedback);
	}

    @SuppressWarnings("unchecked")
	public List<Feedback> getAllFeedback() {
		Query query = em.createQuery("SELECT f FROM Feedback f");
		List<Feedback> feedback = new ArrayList<Feedback>();
		feedback = query.getResultList();
		return feedback;
	}
}
