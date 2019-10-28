package entities;

import javax.persistence.Entity;

@Entity
public class Rating extends Feedback {
	int rating;

	public Rating() {
		
	}
	
	public Rating(int rating) {
		if(this.rating < 0) {
			this.rating = 0;
		}
		else if(this.rating > 5) {
			this.rating = 5;
		}
		else {
			this.rating = rating;
		}
	}
	
	@Override
	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	@Override
	public int isRating() {
		return 1;
	}

}


