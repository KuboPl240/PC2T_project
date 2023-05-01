package Project;

public class film extends movie {
	byte rating;
	public film(String title, int year, String director) {
		super(title, year, director);
	}
	@Override
	public void addRatting(int points, String review) {
		rattings.add(new rattings(points, review));
	}
	public String toString() {
		String string = "\nNazov: "+super.title;
		string += "\nRežisér: "+super.director;
		string += "\nRok vydania: "+super.year;
		string += "\nHerci: ";
		for(int i =0; i<persons.size();i++) {
			string += "\n  "+persons.get(i);
		}
		return string;
	}
}
