package Project;

public class animated_movie extends movie {
	int recomended_age;
	public animated_movie(int i,  String title, int year, String director) {
		super(title, year, director);
		this.recomended_age = i;
	}
	public int getRecomended_age() {
		return (recomended_age);
	}
	
	@Override
	public String toString() {
		String result = "Nazov: "+super.title + "\nAnimatori: ";
		persons.forEach((n) -> {
            System.out.println(n);
        });
		return result;
	}
}
