package Project;

import java.util.Collections;

public class animated_movie extends movie {
	int recomended_age;
	public animated_movie(int i,  String title, int year, String director) {
		super(title, year, director);
		this.recomended_age = i;
	}
	public int getRecomended_age() {
		return (recomended_age);
	}
	public void setRecomended_age(int recomended_age) {
		this.recomended_age = recomended_age;
	}
	@Override
	public void addRatting(byte stars, String review) {
		rattings.add(new rattings(stars, review));
	}
	@Override
	public String toString() {
		String string = "\nNazov: "+super.title;
		string += "\nRežisér: "+super.director;
		string += "\nRok vydania: "+super.year;
		string += "\nOdporucany vek diváka: "+recomended_age;
		string += "\nAnimátori: ";
		for(int i =0; i<persons.size();i++) {
			string += "\n   "+persons.get(i);
		}
		return string;
	}
}
