package sg.edu.nus.iss.workshop3Selfstudy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

//implementation class to write the cart into a database txt file
public class ShoppingCartDB {
    private File repository;

    public ShoppingCartDB(String _repository) {
        this.repository = new File(_repository);
        //System.out.println("Creating ShoppingCartDB with path: " + _repository);
        //creates a new File object with the path provided in _repository 
        //and assigns it to the repository member variable. 
        //The 'this' keyword is used here to refer to the current object's repository field, 
        //making it clear that you're assigning the value to the class's field, not the parameter itself.
        // Use this.repository for the existence check and directory creation
    }
        //to check if list command works
        /* File directory = new File(_repository);
        if (!directory.exists()){
            directory.mkdirs(); // Create the directory if it doesn't exist
        }
        if (!this.repository.exists()) {
            boolean wasSuccessful = this.repository.mkdirs(); // Create the directory if it doesn't exist
            if (wasSuccessful) {
                System.out.println("Successfully created the directory: " + _repository);
            } else {
                System.out.println("Failed to create the directory: " + _repository);
            }
        }
    } */

    public File getRepository() {
        return repository;
    }

    public void setRepository(File repository) {
        this.repository = repository;
    }

    public List<String> listUsers() {
        List<String> users = new LinkedList<>();
        for (File cartFile : repository.listFiles()) {
            users.add(cartFile.getName().replace(".db", ""));
        }
        return users;
    }

    public ShoppingCart load (String username) {//ShoppingCart is the return type, indicating that this method will return an instance of ShoppingCart.
        String cartName = username + ".db";
        ShoppingCart cart = new ShoppingCart(username);
        if (repository.exists() && repository.isDirectory()) { //** */
        for (File cartFile : repository.listFiles()) { //iterate over all files in the 'repository' directory where all the cartFiles are stored.
            if (cartFile.getName().equals(cartName)) { //check ea file to see if its name matches the constructed cartName
                InputStream is;
                try {
                    is = new FileInputStream(cartFile); //creates an InputStream if curr cartFiile match the cartName
                    cart.load(is); //If the matching file is found, it reads the file's contents and populates the ShoppingCart object with the items listed in the file.
                } catch (FileNotFoundException e) {
                    //System.err.println("File not found: " + cartFile.getAbsolutePath());
                    e.printStackTrace();
                } catch (IOException e) {
                    //System.err.println("IO Exception while loading cart: " + cartFile.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        } } else { System.out.println("Repository directory does not exist or is not a directory.");}
        return cart; //Returns the populated ShoppingCart object, whether or not items were loaded 
        //(if no matching file was found, the cart would be empty).
        
    }//The method's declaration as returning a ShoppingCart type is a promise that, 
    //after calling this method with a valid username, the caller will receive a 
    //ShoppingCart object. This could be a newly instantiated cart if no corresponding file 
    //was found or a cart populated with items from the user's saved cart file.

    public void save (ShoppingCart cart) throws IOException {//construct db file based on the username
        //constructing the File Path
        String cartDBfilename = cart.getUsername() + ".db";
        String savedDBlocation = repository.getPath() + File.separator 
            + cartDBfilename; //constructs the full path to the file where the cart's contents will be saved.

        File cartDB = new File(savedDBlocation); //create 'File' object for cart database file
        OutputStream os = null; 

        //File preparation
        try {
            if (!cartDB.exists()){
                try { //if the file does not exist
                    Path p = Paths.get(repository.getPath());
                    Files.createDirectories(p); //create the directory using Files.createDirectory(p); 
                    //if the directory specified by repository doesn't already exist.
                } catch (FileAlreadyExistsException e) { //If the directory already exists (FileAlreadyExistsException),
                    System.err.println("File already exists: " + e.getMessage()); //prints a message but doesn't interrupt the execution.
                }
                cartDB.createNewFile(); //create the file if it doesn't exist.
            }
            //Saving the cart
            os = new FileOutputStream(savedDBlocation); //creates an OutputStream that writes to the file specified by savedbLocation.
            cart.save(os); //serializes the shopping cart's contents and writes them to the file through the OutputStream.
        } catch (IOException e) { //block catches any IOException that might occur during file writing and prints the stack trace 
            e.printStackTrace();
        } finally { //release resources and ensure that all data is written to the file.
            os.flush();
            os.close();
        }
    }
}
