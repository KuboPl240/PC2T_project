package Project;

public class film extends movie {
	byte rating;
	public film(String title, int year, String director) {
		super(title, year, director);
	}
	public byte getRating() {
		return rating;
	}
	public void setRating(byte rating) {
		this.rating = rating;
	}
	public String toString() {
		String result = "Nazov: "+super.title + "\nHerci: ";
		persons.forEach((n) -> {
            System.out.println(n);
        });
		return result;
	}
}
