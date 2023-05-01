package Project;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLConnection {
	List<movie> movies = new ArrayList<>();
	List<person> persons= new ArrayList<>();
    private Connection connection;

    public boolean getDBConnection() { 
        if (connection == null) ;
        {
            try {
                Class.forName("org.s"
                		+ "qlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:SQL/movieDatabase.db"); 
                importFromSQL();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                return  false;
            }
        }
        return true;
        
    }
    private void resetTables() 
    {
    	   try
           {
               Statement stmt = connection.createStatement();
               stmt.execute("DROP TABLE IF EXISTS movies;");
               stmt.execute("DROP TABLE IF EXISTS movies_reviews;");
               stmt.execute("DROP TABLE IF EXISTS movies_persons;");

           }
           catch (SQLException e) {
               System.out.println(e.getMessage());
           }
    	
    }
    private void createTables() 
    {

        String querry_movies = "CREATE TABLE IF NOT EXISTS movies ("
        		   + "title varchar(50) primary key,"
        		   + " director varchar(50), "
        		   + "year int,"
        		   + "age int"
        		   + ");";
        String querry_persons = "CREATE TABLE IF NOT EXISTS persons ("
        		   + "name varchar(50) primary key"
        		   + ");";
        String querry_movies_persons = "CREATE TABLE IF NOT EXISTS movies_persons ("
        		   + "movie_persons int NOT NULL unique,"
        		   + "title varchar(50), name varchar(50), "
        		   + "FOREIGN KEY(name) REFERENCES persons(name), "
        		   + "FOREIGN KEY(title) REFERENCES movies(title), "
        		   + "CONSTRAINT movie_persons unique(name, title), "
        		   + "PRIMARY KEY (movie_persons));";
        String querry_movies_reviews = "CREATE TABLE IF NOT EXISTS movies_reviews ("
        		   + "movie_review int NOT NULL unique,"
        		   + "title varchar(50), review varchar(250), stars int, points int,"
        		   + "FOREIGN KEY(title) REFERENCES movies(title), "
        		   + "PRIMARY KEY (movie_review));";
        try
        {
            Statement stmt = connection.createStatement();
            stmt.execute(querry_movies);
            stmt.execute(querry_persons);
            stmt.execute(querry_movies_persons);
            stmt.execute(querry_movies_reviews);

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public void savePersonToSQL(String name) {
    	String sql_person = "INSERT INTO persons (name) VALUES (\""+name+"\")";
    	 try
         {
    		 PreparedStatement PreparedStatement = connection.prepareStatement(sql_person);
    		 PreparedStatement.executeUpdate();
         }
    	 catch(SQLException e){
    		 System.out.println(e.getMessage());
    	}
    }
    public boolean saveToSQL(List<movie> moviesToSave) 
    {
    	resetTables(); 
    	createTables(); 
    	movies = moviesToSave;
    	
    	String sql_movies = "INSERT INTO movies (title, director, year, age) VALUES (?,?,?,?)";
    	String sql_movies_persons = "INSERT INTO movies_persons (movie_persons ,name, title) VALUES (?,?,?)";
    	String sql_movies_reviews = "INSERT INTO movies_reviews (movie_review ,title, review,stars, points) VALUES (?,?,?,?,?)";
        try
        {
            for (movie movie: movies)
            {
            	PreparedStatement PreparedStatement = connection.prepareStatement(sql_movies);
            	PreparedStatement.setString(1, movie.getTitle());
            	PreparedStatement.setString(2, movie.getDirector());
            	PreparedStatement.setInt(3, movie.getYear());
            	if(movie instanceof animated_movie) {
            		PreparedStatement.setInt(4, ((animated_movie) movie).getRecomended_age());
            	}
            	PreparedStatement.executeUpdate();
            }
            int id = 0;
            for (movie movie: movies)
            {
            	for(String name: movie.getPersonsString()) {
            		PreparedStatement PreparedStatement = connection.prepareStatement(sql_movies_persons );
            		PreparedStatement.setInt(1, id);
            		PreparedStatement.setString(2, name);
            		PreparedStatement.setString(3, movie.getTitle());
            		PreparedStatement.executeUpdate();
            		id++;
            	}
            }
            id = 0;
            for (movie movie: movies)
            {
            	for(rattings ratting: movie.getRattings()) {
            		PreparedStatement PreparedStatement = connection.prepareStatement(sql_movies_reviews);
            		PreparedStatement.setInt(1, id);
            		PreparedStatement.setString(2, movie.getTitle());
            		PreparedStatement.setString(3, ratting.getReview());
            		PreparedStatement.setInt(4, ratting.getStars());
            		PreparedStatement.setInt(5, ratting.getPoints());
            		PreparedStatement.executeUpdate();
            		id++;
            	}
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public void importPersonsFromSQL() 
    {
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM persons;");
            while (result.next()) {
                String name = result.getString("name");
                persons.add(new person(name));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void importMoviesFromSQL() 
    {
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM movies;");
            while (result.next()) {
                String title = result.getString("title");
                String director = result.getString("director");
                int year = result.getInt("year");
                int age = result.getInt("age");
                movie movie;
                if(age>0)movie = new animated_movie(age, title, year, director);
                else movie = new film(title, year, director);
                movies.add(movie);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void importReviewsFromSQL() {
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM movies LEFT JOIN movies_reviews ON movies_reviews.title = movies.title;");
            while (result.next()) {
                String title = result.getString("title");
				movies.forEach(movie ->{
                if(movie.compareTo(title)==0) {
                	try {
                		
						if(result.getInt("stars")>0)movie.addRatting((byte)result.getInt("stars"), result.getString("review"));
						if(result.getInt("points")>0)movie.addRatting(result.getInt("points"), result.getString("review"));
					} 
                	catch (SQLException e) {
                		System.out.println(e.getMessage());
                }
                }
            });
           }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public boolean importFromSQL() 
    {
    	importMoviesFromSQL(); 
    	importPersonsFromSQL();
    	importReviewsFromSQL();
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM movies LEFT JOIN movies_persons ON movies_persons.title = movies.title;");
            while (result.next()) {
                String name = result.getString("name");
                String title = result.getString("title");
                if(name!=null) {
				person person= persons.stream()
		                .filter(n -> n.compareTo(name) == 0)
		                .findFirst()
		                .orElse(null);
				movies.forEach(movie ->{
                if(movie.compareTo(title)==0) {
                	if(person != null) {
                		movie.addPerson(person);
                		person.addMovie(movie);
                	}
                }
            });
            }
            }
           
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    	return true;
    }
    public List<movie> getMovies(){
    	return movies;
    }
    public List<person> getPersons(){
    	return persons;
    }
}
