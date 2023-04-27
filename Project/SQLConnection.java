package Project;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.sql.DriverManager;
import java.sql.Connection;

public class SQLConnection {
    private Connection connection;

    public boolean getDBConnection() { // connect to DB
        if (connection == null) ;
        {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:SQL/movieDatabase.db"); //sem treba dat cestu kde sa uklada .db subor
                createTableMovies();
                createTablePersons();
                
                //createTablePersons_movies();
                saveToSQL();
                //importFromSQL();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                return  false;
            }
        }
        return true;
        
    }
    public void createTableMovies() 
    {

        /*String sql = "CREATE TABLE IF NOT EXISTS movies ("
        		   + "title varchar(50) primary key,"
        		   + " director varchar(50), "
        		   + "year int,"
        		   + "age int"
        		   + ");";*/
    	//String sql = "DROP TABLE persons_movies";
        try
        {
            Statement stmt = connection.createStatement();
            //stmt.execute(sql);

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public void createTablePersons() 
    {

        String sql = "CREATE TABLE IF NOT EXISTS persons (name varchar(50) primary key);";
        try
        {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public void createTablePersons_movies() 
    {

        String sql = "CREATE TABLE IF NOT EXISTS persons_movies (name varchar(50) primary key, title varchar(50),FOREIGN KEY (title) REFERENCES movies(title));";
        String sql1 = "CREATE TABLE movies_persons ("
        			+ "movie_persons int NOT NULL unique,"
        			+ "title varchar(50), name varchar(50), "
        			+ "FOREIGN KEY(name) REFERENCES persons(name), "
        			+ "FOREIGN KEY(title) REFERENCES movies(title), "
        			+ "CONSTRAINT movie_persons unique(name, title), "
        			+ "PRIMARY KEY (movie_persons));";
        			
        		
        try
        {
            Statement stmt = connection.createStatement();
            stmt.execute(sql1);

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public void saveToSQL() 
    {

    	String sql2 = "INSERT INTO movies_persons (movie_persons ,name, title) VALUES (4,\"Jakub Lesik\",\"kokot\")";
    	//String sql1 = "INSERT INTO persons (name) VALUES (\"Jozin z bazin\")";
    	String sql = "INSERT INTO movies (title, director, year, age) VALUES (\"kokot2\",\"picus nova\",2024, 15)"; //ulozenie studentov tech oboru
        try
        {
           /* for (Student o: studentsT)
            {*/
                //PreparedStatement pstmt = connection.prepareStatement(sql);
                //PreparedStatement pstmt1 = connection.prepareStatement(sql1);
                PreparedStatement pstmt2 = connection.prepareStatement(sql2);
              /*  pstmt.setInt(1, o.getID());
                pstmt.setString(2, o.getName());
                pstmt.setString(3, o.getSurname());
                pstmt.setInt(4, o.getDay());
                pstmt.setInt(5, o.getMonth());
                pstmt.setInt(6, o.getYear());
                pstmt.executeUpdate();
            }*/
                //pstmt.executeUpdate();
                //pstmt1.executeUpdate();
                //pstmt2.executeUpdate();

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

    }
    public List<person> importPersonsFromSQL() 
    {
    	List<person> persons = new ArrayList<>();
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
    	return persons;
    }
    public List<movie> importMoviesFromSQL() 
    {
    	List<movie> movies = new ArrayList<>();
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
    	return movies;
    }
    public List<movie> importFromSQL() 
    {
    	List<movie> movies =importMoviesFromSQL();
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM movies LEFT JOIN movies_persons ON movies_persons.title = movies.title;");
            while (result.next()) {
                String name = result.getString("name");
                String title = result.getString("title");
				person person= importPersonsFromSQL().stream()
		                .filter(n -> n.compareTo(name) == 0)
		                .findFirst()
		                .orElse(null);
				movies.forEach(movie ->{
                if(movie.compareTo(title)==0) {
                	if(person != null)	movie.addPerson(person);
                }
            });
            }
           
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	return movies;
    }
}
