package Project;
import java.io.BufferedReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.Reader;
import java.util.List;
import java.io.FileReader;
import org.json.simple.JSONValue;   
public class database {
	@SuppressWarnings("rawtypes")
	protected List<person> persons;
	protected List<movie> movies;
	
	public database() {	
		
	}
	public List<movie> getMovies() {	
		return movies;
	}
	@SuppressWarnings("rawtypes")
	public List<person> getPersons() {	
		return persons;
	}
	public boolean addMovie(movie movie) {
		if(findMovie(movie.getTitle())!=null)return false;
		movies.add(movie);
		return true;
	}
	public boolean deleteMovie(String Title) {
		movie movie = findMovie(Title);
		person person;
		if(movie==null)return false;
		for(int i =0; i<movie.getPersons().size();i++) {
			person = movie.getPersons().get(i);
			person.removeMovie(movie);
		}
		movies.remove(movie);
		return true;
	}
	public movie findMovie(String Title) {
	movie movie=	movies.stream()
                .filter(n -> n.compareTo(Title) == 0)
                .findFirst()
                .orElse(null);
		return movie;	}
	@SuppressWarnings("rawtypes")
	public person findPerson(String name) {

		person person= persons.stream()
                .filter(n -> n.compareTo(name) == 0)
                .findFirst()
                .orElse(null);
	return person;
	}
	public boolean addMovieToPerson(String name, movie movie) {
		findPerson(name).addMovie(movie);
	return true;
}
	@SuppressWarnings("unchecked")
	public boolean exportMovie(movie movie) {
		try {
			FileWriter file = new FileWriter("movies/"+movie.getTitle()+".json");
			JSONObject export = new JSONObject();
			export.put("Title", movie.getTitle());
			export.put("Director", movie.getDirector());
			export.put("Year", movie.getYear());
			export.put("persons", movie.getPersonsString());
			JSONArray rattings = new JSONArray();
			movie.getRattings().forEach(n ->{
				JSONObject ratting = new JSONObject();
				if(movie instanceof animated_movie)ratting.put("stars", n.getStars());
				else ratting.put("points", n.getPoints());
				ratting.put("review", n.getReview());
				rattings.add(ratting);
			});
			export.put("rattings", rattings);
			if(movie instanceof animated_movie) {
				animated_movie animated_movie = (animated_movie)movie;
				export.put("Age", animated_movie.getRecomended_age());
			}
			file.write(export.toString());
			file.close();
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	@SuppressWarnings("unchecked")
	public boolean importMovie(String Title) {
		try {
			movie movie;
			FileReader file=new FileReader("movies/"+Title+".json");
			BufferedReader im = new BufferedReader(file);
			Object obj=JSONValue.parse(im.readLine());  
			JSONObject jsonObject = (JSONObject) obj;
			if(jsonObject.get("Age")==null) {
				 movie = new film((String)jsonObject.get("Title"), Number.class.cast(jsonObject.get("Year")).intValue(), (String)jsonObject.get("Director"));
			}
			else {
				 movie = new animated_movie(Number.class.cast(jsonObject.get("Age")).intValue(),(String)jsonObject.get("Title"),Number.class.cast(jsonObject.get("Year")).intValue(), (String)jsonObject.get("Director"));
				}	
			JSONArray persons_names = (JSONArray) jsonObject.get("persons");
			persons_names.forEach(name -> {
				persons.forEach(e -> {
					  if(e.compareTo(name.toString())==0) { 
						  movie.addPerson(e);
					  }
				});
			});
			JSONArray rattings = (JSONArray) jsonObject.get("rattings");
			for(int i = 0;i<rattings.size();i++) {
				Object ratting =rattings.get(i);
				JSONObject json= (JSONObject) ratting;
				if(json.get("stars")!=null)movie.addRatting((byte)Number.class.cast(json.get("stars")).intValue(), (String)json.get("review"));
				if(json.get("points")!=null)movie.addRatting(Number.class.cast(json.get("points")).intValue(), (String)json.get("review"));
			}
			im.close();
			if(findMovie(movie.title)!=null)throw new Exception("Film už existuje!");
			movies.add(movie);
		}
		catch(Exception e) {
			System.out.println(e);
			return false;
		}
		return true;
	}
	public boolean fromSQLDatabase() {
		SQLConnection connection = new SQLConnection();
		connection.getDBConnection();
		movies = connection.getMovies();
		persons = connection.getPersons();
		return true;
	}
	public boolean toSQLDatabase() { 
		SQLConnection connection = new SQLConnection();
		connection.getDBConnection();
		return connection.saveToSQL(movies);
	}
}
