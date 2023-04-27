package Project;


import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

public abstract class movie implements Comparable<movie> {
	protected List<person> persons;
	protected String title;
	protected String director;
	protected int year;
	public movie(String title,int year, String director) {
		this.year = year;
		this.title = title;
		this.director = director;
		persons = new ArrayList<>();
	}
	public String getTitle() {
		return(title);
	}
	public String getDirector() {
		return(director);
	}
	public int getYear() {
		return(year);
	}
	public List<String> getPersons() {
		List<String> strings = new ArrayList<>(persons.size());
		for (Object object : persons) {
		    strings.add(Objects.toString(object, null));
		}
		return strings;
	}
	public boolean addPerson(person person) {
		return persons.add(person);
	}
	public boolean removePerson(person person) {
		return persons.remove(person);
	}
	public int compareTo(movie movie) {
		return this.title.compareToIgnoreCase(movie.getTitle());
	}
	public int compareTo(String title) {
		return this.title.compareToIgnoreCase(title);
	}
}
