package Project;
import java.util.Scanner;

public class tools {
	   public static int intOnly(Scanner sc)
	    {
	        int number = 0;
	        try
	        {
	            number = sc.nextInt();
	        }
	        catch(Exception e)
	        {
	            System.out.println("Zadejte prosim cele cislo ");
	            sc.nextLine();
	            number = intOnly(sc);
	        }
	        return number;
	    }

}
