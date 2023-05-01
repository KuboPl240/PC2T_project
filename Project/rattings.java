package Project;

public class rattings implements Comparable<rattings>{
	String review;
	byte stars = 0;
	int points = 0;
	public rattings(int points, String review){
		this.points = points;
		this.review = review;
	}
	
	public rattings(byte stars, String review){
		this.stars = stars;
		this.review = review;
	}
	public String getReview() {
		return review;
	}
	public int getPoints() {
		return points; 
	}
	public byte getStars() {
		return stars;
	}
	@Override
	public String toString() {
		String string = "";
		if(stars>0) {
			for(int i =0;i<stars;i++) {
				string += "*";
			}	
		}
		else string += points+"/10";
		string += " "+review + "\n";
		return string;
	}
	public int compareTo(rattings ratting) {
		if(stars>0)return ratting.getStars()-this.stars;
		if(points>0)return  ratting.getPoints()- this.points;
		return 0;
	}
}
