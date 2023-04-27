package Project;

import java.util.Arrays;
import java.util.List;

public class App {
	public static void main(String[] args) {
		movie film1 = new animated_movie((byte)15,"Shrek",2002, "Elon Musk");
		List<person> actors = listOfActors();
		database database = new database(actors);
		actors.forEach((n) -> film1.addPerson(n));
		movie film2 = new animated_movie((byte)18,"Shrek",2004, "Jakub Lepik");
		//database.exportMovie(film1);
		//System.out.println(database.importMovie());
		database.fromSQLDatabase();
		//database.findPerson("Jakub Lepsik");
		
		
	}
	
	public static List<person> listOfActors() {
	    person act1 = new person("Jiri Bekr");
	    person act2 = new person("Honza macak");
	    person act3 = new person("Jakub");
	    return Arrays.asList(act1, act2, act3);
  }
}
