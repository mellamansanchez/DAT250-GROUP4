package ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import entities.Comment;
import entities.Feedback;
import entities.IoTDevice;
import entities.Rating;

@Named(value = "FeedbackController")
@RequestScoped
public class FeedbackController implements Serializable{
	
	private static final long serialVersionUID = 1L;
		
	@EJB
	private IoTDeviceDao deviceDao;
	
	@EJB
	private FeedbackDao feedbackDao;
	
	@EJB
	private UserDao userdao;
	
	private IoTDevice device;
	
	private String comment;
	
	private String rating;
		
	public FeedbackDao getFeedbackDao() {
		return feedbackDao;
	}

	public void setFeedbackDao(FeedbackDao feedbackDao) {
		this.feedbackDao = feedbackDao;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public IoTDeviceDao getDeviceDao() {
		return deviceDao;
	}

	public void setDeviceDao(IoTDeviceDao deviceDao) {
		this.deviceDao = deviceDao;
	}
	
	public String rate() {
		if (rating != null) {
			int number = Integer.parseInt(rating);
			if (number>=1 && number <=5) {
				Rating newRating = new Rating(number);
				HttpSession session = SessionUtils.getSession();
				this.device = deviceDao.getDeviceByName((String) session.getAttribute(Constants.DEVICE)).get(0);
				device.addFeedback(newRating);
				feedbackDao.persist(newRating);
				deviceDao.update(device);
			}
		}
		return Constants.DEVICE_F;
	}
	
	public String createComment() {
		if (comment != null) {
			Comment newComment = new Comment(comment);
			HttpSession session = SessionUtils.getSession();
			this.device = deviceDao.getDeviceByName((String) session.getAttribute(Constants.DEVICE)).get(0);
			device.addFeedback(newComment);
			feedbackDao.persist(newComment);
			deviceDao.update(device);
		}
		return Constants.DEVICE_F;
	}
}
