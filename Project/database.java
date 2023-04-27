package Project;
import java.io.BufferedReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.util.List;
import java.util.stream.Stream;
import java.io.FileReader;
import org.json.simple.JSONValue;   
public class database {
	protected List<person> persons;
	protected List<movie> movies;
	static person person;
	public database(List<person> persons) {	
		this.persons = persons;
	}
	/*public movie findMovie(String Title) {
			movies.forEach(movie-> {
				if(movie.compareTo(Title)==0) {
					return movie;
				}
			});
		return null;
	}*/
	public person findPerson(String name) {

				person= persons.stream()
                .filter(n -> n.compareTo(name) == 0)
                .findFirst()
                .orElse(null);
		System.out.println(person);
	return person;
}
	public boolean exportMovie(movie movie) {
		try {
			FileWriter file = new FileWriter(movie.getTitle()+".json");
			JSONObject export = new JSONObject();
			export.put("Title", movie.getTitle());
			export.put("Director", movie.getDirector());
			export.put("Year", movie.getYear());
			export.put("persons", movie.getPersons());
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
	public boolean importMovie() {
		try {
			movie movie;
			FileReader file=new FileReader("Shrek.json");
			BufferedReader im = new BufferedReader(file);
			Object obj=JSONValue.parse(im.readLine());  
			JSONObject jsonObject = (JSONObject) obj;
			if(jsonObject.get("Age")==null) {
				 movie = new film((String)jsonObject.get("Title"), (int)jsonObject.get("Year"), (String)jsonObject.get("Director"));
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
			im.close();
			movies.add(movie);
			System.out.println(movie);
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
		persons = connection.importPersonsFromSQL();
		movies = connection.importFromSQL();
		movies.forEach(movie ->{
			System.out.println(movie);
		});
		return true;
	}
	public boolean toSQLDatabase() {
		return true;
	}
}
