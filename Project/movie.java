package Project;


import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
@SuppressWarnings("unchecked")
public abstract class movie implements Comparable<movie> {
	protected List<person> persons;
	protected List<rattings> rattings;
	protected String title;
	protected String director;
	protected int year;
	public movie(String title,int year, String director) {
		this.year = year;
		this.title = title;
		this.director = director;
		persons = new ArrayList<>();
		rattings = new ArrayList<>();
	}
	public String getTitle() {
		return(title);
	}
	public void setTitle(String Title) {
		this.title = Title;
	}
	public String getDirector() {
		return(director);
	}
	public void setDirector(String name) {
		this.director = name;
	}
	public int getYear() {
		return(year);
	}
	public void setYear(int year) {
		this.year = year;
	}
	public List<String> getPersonsString() {
		List<String> strings = new ArrayList<>(persons.size());
		for (Object object : persons) {
		    strings.add(Objects.toString(object, null));
		}
		return strings;
	}
	public List<person> getPersons() {
		return persons;
	}
	public boolean addPerson(person person) {
		return persons.add(person);
		
	}
	public boolean removePerson(person person) {
		return persons.remove(person);
	}
	
	public List<rattings> getRattings() {
		Collections.sort(rattings);
		return rattings;
	}
	public int compareTo(movie movie) {
		return this.title.compareToIgnoreCase(movie.getTitle());
	}
	public int compareTo(String title) {
		return this.title.compareToIgnoreCase(title);
	}
	public void addRatting(byte í, String review) {
		// TODO Auto-generated method stub
		
	}
	public void addRatting(int í, String review) {
		// TODO Auto-generated method stub
		
	}
	
}
