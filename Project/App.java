package Project;


import java.util.Scanner;
import java.io.IOException;
import java.util.List;

public class App {
	private static void menu() {
		System.out.println("====================================================================================\n"
				 + "1. Zobraziť všetky filmy\n"
				 + "2. Pridať film\n" 
				 + "3. Upraviť film\n"
				 + "4. Zmazať film\n" 
				 + "5. Vyhladať film\n"
				 + "6. Pridať hodnotenie na film\n"
				 + "7. Zobraziť zoznam filmov herca/animátora\n" 
				 + "8. Zobraziť zoznam hercov/animátorov ktorý sa podielali na viac ako jednom filme\n" 
				 + "9. Uložiť film do súboru\n" 
				 + "10. Načítať film zo súboru\n" 
				 + "11. Ukoncenie aplikacie\n");
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		List <movie> movies;
		Scanner sc=new Scanner(System.in);
		boolean run = true;
		int option;
		String name = "";
		database database = new database();
		database.fromSQLDatabase();
		menu();
		while(run) {
			String title, director;
			int year;
			person person;
			option= tools.intOnly(sc);
			switch(option) {
				case 1:
					database.getMovies().forEach(n ->{
						System.out.println(n);
					});
					break;
				case 2:
					boolean type = false;
					System.out.println("Zadajte druh filmu:\n1.Hraný film\n2.Animovaný film\n");
					if(tools.intOnly(sc)==2)type = true;
					sc = new Scanner(System.in);
					System.out.println("Zadajte nazov filmu:\n");
					title = sc.nextLine();
					sc = new Scanner(System.in);
					System.out.println("Zadajte meno režiséra filmu:\n");
					director = sc.nextLine();
					sc = new Scanner(System.in);
					System.out.println("Zadajte rok vydania filmu:\n");
					year = tools.intOnly(sc);
					sc = new Scanner(System.in);
					movie movie;
					if(type) {
						int age;
						System.out.println("Odporucany vek diváka:\n");
						age = tools.intOnly(sc);
						movie = new animated_movie(age,title,year,director);
						System.out.println("Zadajte postupne animatórov filmu, pre zobrazenie dostupných osôb zadajte: \"list\", pre ukoncenie pridavania zadajte: \\\"stop\\\"\n");
					}
					else {
						movie = new film(title,year,director);
						System.out.println("Zadajte postupne hercov filmu, pre zobrazenie dostupných osôb zadajte: \"list\", pre ukoncenie pridavania zadajte: \"stop\" \n");
					}
					name = "";
					sc = new Scanner(System.in);
					while(name.compareTo("stop")!=0) {
						name = sc.nextLine();
						if(name.compareTo("list")==0) {
							database.getPersons().forEach(n ->{
								System.out.println(n);
							});
						}
						else if(name.compareTo("stop")!=0){
							if(database.findPerson(name)==null)System.out.println("Herec/animator "+name+" neexistuje!\n");
							else {
								if(!movie.addPerson(database.findPerson(name)))System.out.println("Herec/animator "+name+"  sa uz vo filme ucinkuje!\n");
								else {
									database.addMovieToPerson(name,movie);
									System.out.println("Herec/animator pridaný!");
								}
							}
						}
					}
					if(database.addMovie(movie))System.out.println("Film pridaný!\n");
					else System.out.println("Film s rovnakým názvom sa už nachádza v databázi!\n");
					break;
				case 4:
					sc = new Scanner(System.in);
					System.out.println("Zadajte názov filmu pre zmazanie:\n");
					title = sc.nextLine();
					if(!database.deleteMovie(title))System.out.println("Film nieje v databázi!\n");
					else {
						System.out.println("Film " +title+" zmazaný!");
					}
					break;
				case 5:
					sc = new Scanner(System.in);
					System.out.println("Zadajte názov filmu:\n");
					title = sc.nextLine();
					movie = database.findMovie(title);
					if(movie ==null)System.out.println("Film nieje v databázi!\n");
					else {
						System.out.println(movie);
						System.out.println("\nHodnotenia: \n");
						if(movie.getRattings() ==null) {
							System.out.println("Film ešte nemá žiadne hodnotenia");
							break;
						}
						for(rattings ratting: movie.getRattings()) {
							System.out.println(ratting);
						}
					}
					break;
				case 6:
					sc = new Scanner(System.in);
					int ratting;
					String review;
					System.out.println("Zadajte názov filmu:\n");
					title = sc.nextLine();
					movie = database.findMovie(title);
					if(movie==null)System.out.println("Film nieje v databázi!\n");
					else {
						if(movie instanceof animated_movie) {
							System.out.println("Zadajte hodnotenie v hviezdičkách\n");
							ratting  = sc.next().length();
							if(ratting>4) {
								System.out.println("Hodnotenie ste zadali nesprávne");
								break;
							}
						}
						else {
							System.out.println("Zadajte hodnotenie od 0 po 10\n");
							ratting = tools.intOnly(sc);
							if(ratting>10 || ratting<0) {
								System.out.println("Hodnotenie ste zadali nesprávne");
								break;
							}
						}
						sc = new Scanner(System.in);
						System.out.println("Zadajte hodnotenie slovné hodnotenie");
						review = sc.nextLine();
						if(movie instanceof animated_movie)movie.addRatting((byte)ratting, review);
						else movie.addRatting(ratting, review);
						System.out.println("Hodnotenie pridané!");
					}
					break;
				case 3:
					sc = new Scanner(System.in);
					System.out.println("Zadajte názov filmu:\n");
					title = sc.nextLine();
					movie =database.findMovie(title); 
					if(movie==null)System.out.println("Film nieje v databázi!\n");
					else {
						System.out.println("Vyberte čo chcete upraviť:\n"
										 + "1. Názov filmu\n"
										 + "2. Režiséra filmu\n"
										 + "3. Rok vydania filmu\n"
										 + "4. Upraviť doporučený vek (ak je film animavaný)\n"
										 + "5. Pridať herca/animátora\n"
										 + "6. Zmazať herca/animátora\n");
						switch(tools.intOnly(sc)) {
							case 1:
								sc = new Scanner(System.in);
								System.out.println("Zadajte nový názov filmu:\n");
								movie.setTitle(sc.nextLine());
								break;
							case 2:
								sc = new Scanner(System.in);
								System.out.println("Zadajte nového režiséra filmu:\n");
								movie.setDirector(sc.nextLine());
								break;
							case 3:
								sc = new Scanner(System.in);
								System.out.println("Zadajte nový rok vydania filmu:\n");
								movie.setYear(tools.intOnly(sc));
								break;
							case 4:
								sc = new Scanner(System.in);
								if(movie instanceof animated_movie) {
									System.out.println("Zadajte nový odporučaný vek animovaného filmu filmu:\n");
									((animated_movie) movie).setRecomended_age(tools.intOnly(sc));
								}
								else {
									System.out.println("Film nieje animovaný!\n");
								}
								break;
							case 5:
								sc = new Scanner(System.in);
								name = "list";
								System.out.println("Zadajte dalšieho herca/animátora filmu (pre zobrazenie hercov/animátovor napíšte \"list\"): \n");
								while(name.compareTo("list")==0) {
									name = sc.nextLine();
									if(name.compareTo("list")==0) {
										database.getPersons().forEach(n ->{
											System.out.println(n);
										});
									}
								}
								if(database.findPerson(name)==null) {
									System.out.println("Meno neexistuje!");
									break;
								}
								movie.addPerson(database.findPerson(name));
								break;
							case 6:
								sc = new Scanner(System.in);
								name = "list";
								System.out.println("Zadajte herca/animátora filmu ktorého chcete zmazať (pre zobrazenie hercov/animátovor napíšte \"list\"): \n");
								while(name.compareTo("list")==0) {
									name = sc.nextLine();
									if(name.compareTo("list")==0) {
										database.getPersons().forEach(n ->{
											System.out.println(n);
										});
									}
								}
								if(database.findPerson(name)==null) {
									System.out.println("Meno neexistuje!");
									break;
								}
								movie.removePerson(database.findPerson(name));
								break;
							default:
								System.out.println("Zadajte správnu operáciu");
								break;
						}
						System.out.println("Upravené!\n");
						database.deleteMovie(title);
						database.addMovie(movie);
					}
					break;
				case 7:
					sc = new Scanner(System.in);
					System.out.println("Zadajte meno herca/animatora, pre zobrazenie všetkých zadajte: \"list\"\n");
					name = sc.nextLine();
					person = database.findPerson(name);
					while(name.compareTo("list")==0) {
						name = sc.nextLine();
						if(name.compareTo("list")==0) {
							database.getPersons().forEach(n ->{
								System.out.println(n);
							});
						}
					}
					if(database.findPerson(name)==null) {
						System.out.println("Meno neexistuje!");
						break;
					}
					else {
						System.out.println("Filmy na ktorých sa podielal herec/animator "+name+" :\n");
						if(person.getPersonMovies()==null) {
							System.out.println("Herec nehral v žiadnych filmoch!");
						}
						movies = person.getPersonMovies();
						for(int i = 0; i < movies.size();i++) {
							System.out.println("   "+movies.get(i).getTitle());
						}
					}
				case 8:
					System.out.println("Herci/animátori ktorý sa podielali na viac ako jednom filme:\n");
						for(int i = 0; i < database.getPersons().size();i++) {
							if(!(database.getPersons().get(i).getPersonMovies().size()>1))continue;
							person = database.getPersons().get(i);
							System.out.println("Meno: "+person+"\nFilmy:");
							movies = person.getPersonMovies();
							for(int a = 0; a < movies.size();a++) {
								System.out.println("   "+movies.get(a).getTitle());
							}
						}
					break;
				case 9:
					sc = new Scanner(System.in);
					System.out.println("Zadajte názov filmu:\n");
					title = sc.nextLine();
					movie = database.findMovie(title);
					if(movie==null)System.out.println("Film nieje v databázi!\n");
					else {
						if(database.exportMovie(movie))System.out.println("Film ulozený!\n");
						else System.out.println("Film sa nepodarilo uložiť!\n");
					}
					break;
				case 10:
					sc = new Scanner(System.in);
					System.out.println("Zadajte názov súboru:\n");
					title = sc.nextLine();
					if(database.importMovie(title))System.out.println("Film načítaný!\n");
					else System.out.println("Film sa nepodarilo načítať!\n");
					break;
				default:
					if(database.toSQLDatabase())System.out.println("Databáza uložená!\n");
					System.out.println("Koniec\n");
					run = false;
					sc.close();
					break;	
			}
			if(run)menu();
			System.out.println("====================================================================================\n");
		}
		
	}
	
}
