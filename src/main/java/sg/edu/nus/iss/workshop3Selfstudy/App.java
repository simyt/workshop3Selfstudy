package sg.edu.nus.iss.workshop3Selfstudy;

/**
 * Hello world!
 *
 */
public class App 
{
    private static String defaultDB = "db";
    public static void main( String[] args )
    {
        System.out.println(args.length);
        if (args.length > 0) {//The application checks if any command-line arguments (args) were provided.
            System.out.println("arg db directory from arg"); // If at least one argument is present, 
            App.defaultDB = args[0]; //it updates defaultDB with the value of the first argument. 
            //This allows the user to specify a custom database directory when launching the application.
        }
        System.out.println(defaultDB); //prints the final database directory path to the console, 
        //which will be either the default "db" or the one provided as a command-line argument.

        ShoppingCartDB db = new ShoppingCartDB(defaultDB); // creates an instance of ShoppingCartDB using the determined database directory path.
        System.out.println("Is ShoppingCartDB 'db' null? " + (db == null));
        Session session = new Session(db); //initializes a Session object with the created ShoppingCartDB instance
        session.start(); //// A new session starts with a fresh currentCart.
    }
}
