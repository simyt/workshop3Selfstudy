package sg.edu.nus.iss.workshop3Selfstudy;

import java.io.Console;
import java.io.IOException;
import java.util.List;

public class Session {
    private ShoppingCartDB repository;

    private static final String END = "end";
    private static final String LOGIN = "login";
    private static final String ADD = "add";
    private static final String SAVE = "save";
    private static final String LIST = "list";
    private static final String USERS = "users";

    private ShoppingCart currCart; //hold the path to the database directory

    public Session(ShoppingCartDB repositoryCart) {
        this.repository = repositoryCart;
        System.out.println("Repository initialized: " + (this.repository != null)); // This should print true
    }

    public void start() {
        Console cons = System.console();
        boolean stop = false;
        while (!stop) {
            String input = cons.readLine("> ");
            String[] term = input.split(" ");
            //System.out.println("Command received: " + term[0]);
            switch (term[0]) { //term[0] can be add, save, list or login
                case END:
                    stop = true;
                    break;
                case ADD:
                    for (int i = 1; i < term.length; i++){ //since term[0] is the cmd from the user, start from i =1
                        try {
                            currCart.add(term[i]);
                            System.out.println(term[i] + " added to the cart.");
                        } catch (NullPointerException e) {
                            System.out.println("Please login before adding to the cart!");
                        }
                    }
                    break;
                
                case LIST:
                    if (currCart == null) {
                        System.out.println("Please login before listing the cart contents.");
                        break;
                    }
                    currCart = repository.load(currCart.getUsername());
                    printAllItems(currCart.getContents());
                    break;

                case USERS:
                    List<String> users = repository.listUsers();
                    for (String user : users) {
                        System.out.println(user);
                    }
                    break;
                case SAVE:
                    try {
                        repository.save(currCart);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case LOGIN:
                    currCart = new ShoppingCart(term[1]);
                    System.out.println("Logged in as: " + term[1]);
                    break;
                
                default: //if the term[0] is not list/add/save/login/end
                    break; //the program will not run if the wrong command is given
            
            }
        }
    }

    public void printAllItems(List<String> items) {
        if (items.isEmpty()) {
            System.out.println("no iems in the cart.");
            return;
        }
        for (String item : items) {
            int index = items.indexOf(item) + 1;
            System.out.println(index + " " + item);
        }
    }
}
