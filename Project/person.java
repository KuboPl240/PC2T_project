package Project;

import java.util.List;
import java.util.ArrayList;

public class person<movie> implements Comparable<person<movie>>{
	public List<movie> movies;
	private String name;
	public person(String name) {
		this.name = name;
		movies = new ArrayList<>();
	}
	public String getName() {
		return name;
	}
	
	public boolean addMovie(movie t) {
		return movies.add(t);
	}
	
	public boolean removeMovie(movie t) {
		return movies.remove(t);
	}
	
	public List<movie> getPersonMovies() {
		return movies;
	}
	@Override
	public String toString() {
		return name;
	}
	public int compareTo(person person) {
		return this.name.compareToIgnoreCase(person.getName());
	}
	public int compareTo(String name) {
		return this.name.compareToIgnoreCase(name);
	}
}
