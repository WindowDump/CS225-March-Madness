//package marchmadness;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *  MarchMadnessGUI
 * 
 * this class contains the buttons the user interacts
 * with and controls the actions of other objects 
 *
 * @author Grant Osborn
 */
public class MarchMadnessGUI extends Application {
    
    
    //all the gui ellements
    private BorderPane root;
    private ToolBar toolBar;
    private ToolBar btoolBar;
    private Button simulate;
    private Button login;
    private Button scoreBoardButton;
    private Button viewBracketButton;
    private Button clearButton;
    private Button resetButton;
    private Button finalizeButton;
    
    private  Bracket startingBracket; 
    //reference to currently logged in bracket
    private Bracket selectedBracket;
    private Bracket simResultBracket;

    
    private ArrayList<Bracket> playerBrackets;
    private HashMap<String, Bracket> playerMap;

    
  
    private ScoreBoardPane scoreBoard;
    private BracketPane bracketPane;
    private GridPane loginP;
    private TournamentInfo teamInfo;
    
    
    @Override
    public void start(Stage primaryStage) {

        try {
            teamInfo=new TournamentInfo();
            startingBracket= new Bracket(TournamentInfo.loadStartingBracket()); 
            simResultBracket=new Bracket(TournamentInfo.loadStartingBracket());
        } catch (IOException ex) {
            showError(new Exception("Can't find "+ex.getMessage(),ex),true);
        }
        
        //create test acount
        //simResultBracket.setPlayerName("Grant");
        //simResultBracket.setPassword("hunter2");
        //seralizeBracket(simResultBracket);
        
        
         //deserialize stored brackets
        playerBrackets = loadBrackets();
        //playerBrackets.forEach(b->System.out.println(b.getPlayerName()));
        playerMap = new HashMap<>();
        addAllToMap();
        


        //the main layout container
        root = new BorderPane();
        scoreBoard= new ScoreBoardPane();
        
        loginP=createLogin();
        CreateToolBars();
        
        //display login screen
        login();
        
        setActions();
        root.setTop(toolBar);   
        root.setBottom(btoolBar);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("March Madness Bracket Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
    
    /**
     * simulates the tournament  
     * simulation happens only once and
     * after the simulation no more users can login
     */
    private void simulate(){
        //cant login and restart prog after simulate
        login.setDisable(true);
        btoolBar.setDisable(true);
        simulate.setDisable(true);
        
        scoreBoardButton.setDisable(false);
        viewBracketButton.setDisable(false);
        
       teamInfo.simulate(simResultBracket);
       for(Bracket b:playerBrackets){
           b.scoreBracket(simResultBracket);
       }
       
       displayPane(scoreBoard._start());

     
    }
    
    /**
     * Displays the login screen
     * 
     */
    private void login(){
           //simulate.setDisable(true);
           scoreBoardButton.setDisable(true);
           viewBracketButton.setDisable(true);
           btoolBar.setDisable(true);
           displayPane(loginP);
    }
    
     /**
     * Displays the score board
     * 
     */
    private void scoreBoard(){
        
        //sort brackets by score 
        playerBrackets.sort((Bracket p1, Bracket p2) -> p1.scoreBracket(simResultBracket) -p2.scoreBracket(simResultBracket));        
       
         //scoreBoardButton.setDisable(true);
          displayPane(scoreBoard._start());
          //viewBracket.setDisable(false);
    }
    
     /**
      * Displays Simulated Bracket
      * 
      */
    private void viewBracket(){
       selectedBracket=simResultBracket;
       bracketPane=new BracketPane(selectedBracket);
       //bracketPane.setDisable(true);
        displayPane(bracketPane);
    }
    
    /**
     * allows user to choose bracket
     * 
     */
    private void chooseBracket(){
        //login.setDisable(true);
        btoolBar.setDisable(false);
        bracketPane=new BracketPane(selectedBracket);
        displayPane(bracketPane);
        
        
    }
    /**
     * resets current selected sub tree
     * for final4 reset Ro2 and winner
     */
    private void clear(){
        
        
    }
    
    /**
     * resets entire bracket
     */
    private void reset(){
        //horrible hack to reset
        selectedBracket=new Bracket(startingBracket);
        bracketPane=new BracketPane(selectedBracket);
        displayPane(bracketPane);
    }
    
    private void finalizeBracket(){

       if(bracketPane.isComplete()){
            simulate.setDisable(false);
            login.setDisable(false);
            
       }else{
        //TODO disable displayed Pane
           
        //horrible hack to go back to bracket section selection screen
        bracketPane=new BracketPane(selectedBracket);
        displayPane(bracketPane);
        
       }
       
       seralizeBracket(selectedBracket);
        
     
    }
    
    
    /**
     * displays element in the center of the screen
     * 
     * @param p must use a subclass of Pane for layout. 
     * to be properly center aligned in  the parent node
     */
    private void displayPane(Node p){
        root.setCenter(p);
        BorderPane.setAlignment(p,Pos.CENTER);
    }
    
    /**
     * creates toolBar and buttons.
     * adds buttons to the toolbar and saves global references to them
     */
    private void CreateToolBars(){
        toolBar  = new ToolBar();
        btoolBar  = new ToolBar();
        login=new Button("Login");
        simulate=new Button("Simulate");
        scoreBoardButton=new Button("ScoreBoard");
        viewBracketButton= new Button("View Simulated Bracket");
        clearButton=new Button("Clear");
        resetButton=new Button("Reset");
        finalizeButton=new Button("Finalize");
        toolBar.getItems().addAll(createSpacer(),
                login,
                simulate,
                scoreBoardButton,
                viewBracketButton,
                createSpacer()
        );
        btoolBar.getItems().addAll(
                createSpacer(),
                clearButton,
                resetButton,
                finalizeButton,
                createSpacer()
        );
    }
    
   /**
    * sets the actions for each button
    */
    private void setActions(){
        login.setOnAction(e->login());
        simulate.setOnAction(e->simulate());
        scoreBoardButton.setOnAction(e->scoreBoard());
        viewBracketButton.setOnAction(e->viewBracket());
        clearButton.setOnAction(e->clear());
        resetButton.setOnAction(e->reset());
        finalizeButton.setOnAction(e->finalizeBracket());
    }
    
    /**
     * Creates a spacer for centering buttons in a ToolBar
     */
    private Pane createSpacer(){
        Pane spacer = new Pane();
        HBox.setHgrow(
                spacer,
                Priority.SOMETIMES
        );
        return spacer;
    }
    
    
    private GridPane createLogin(){
        
        
        /*
        LoginPane
        Sergio and Joao
         */

        GridPane loginPane = new GridPane();
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setHgap(10);
        loginPane.setVgap(10);
        loginPane.setPadding(new Insets(5, 5, 5, 5));

        Text welcomeMessage = new Text("March Madness Login Welcome");
        loginPane.add(welcomeMessage, 0, 0, 2, 1);

        Label userName = new Label("User Name: ");
        loginPane.add(userName, 0, 1);

        TextField enterUser = new TextField();
        loginPane.add(enterUser, 1, 1);

        Label password = new Label("Password: ");
        loginPane.add(password, 0, 2);

        PasswordField passwordField = new PasswordField();
        loginPane.add(passwordField, 1, 2);

        Button signButton = new Button("Sign in");
        loginPane.add(signButton, 1, 4);

        Label message = new Label();
        loginPane.add(message, 1, 5);

        signButton.setOnAction(event -> {

            // the name user enter
            String name = enterUser.getText();
            // the password user enter
            String playerPass = passwordField.getText();

        
          
            
            if (playerMap.get(name) != null) {
                //check password of user
                 
                Bracket tmpBracket = this.playerMap.get(name);
               
                String password1 = tmpBracket.getPassword();

                if (Objects.equals(password1, playerPass)) {
                    // load bracket
                    selectedBracket=playerMap.get(name);
                    chooseBracket();
                } else {
                   loginAlert("The password you have entered is incorrect!");
                }

            } else {
                //check for empty fields
                if(!name.equals("")&&!playerPass.equals("")){
                    //create new bracket
                    System.out.println("User " + name + " did not exist, created new bracket");
                    Bracket tmpPlayerBracket = new Bracket(startingBracket, name);
                    playerBrackets.add(tmpPlayerBracket);
                    tmpPlayerBracket.setPassword(playerPass);

                    playerMap.put(name, tmpPlayerBracket);
                    selectedBracket = tmpPlayerBracket;
                    //alert user that an account has been created
                    loginAlert("no user with the name "+tmpPlayerBracket.getPlayerName()+" exists \na new account has been created");
                    chooseBracket();
                }
            }
        });
        
        return loginPane;
    }
    
    /**
     * addAllToMap
     * adds all the brackets to the map for login
     */
    private void addAllToMap(){
        for(Bracket b:playerBrackets){
            playerMap.put(b.getPlayerName(), b);   
        }
    }
    
    /**
     * The Exception handler
     * Displays a error message to the user
     * and if the error is bad enough closes the program
     * @param msg message to be displayed to the user
     * @param fatal true if the program should exit. false otherwise 
     */
    private void showError(Exception e,boolean fatal){
        String msg=e.getMessage();
        if(fatal){
            msg=msg+" \n\nthe program will now close";
            //e.printStackTrace();
        }
        Alert alert = new Alert(AlertType.ERROR,msg);
        alert.setResizable(true);
        alert.getDialogPane().setMinWidth(420);   
        alert.setTitle("Error");
        alert.setHeaderText("something went wrong");
        alert.showAndWait();
        if(fatal){ 
            System.exit(666);
        }   
    }
    
    /**
     * alerts user to the result of their actions in the login pane 
     * 
     * @param msg the message to be displayed to the user
     */
    private void loginAlert(String msg){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("March Madness Bracket Simulator");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    
    /**
     * Tayon Watson 5/5
     * seralizedBracket
     * @param B The bracket the is going to be seralized
     */
    private void seralizeBracket(Bracket B){
        FileOutputStream outStream = null;
        ObjectOutputStream out = null;
    try 
    {
      outStream = new FileOutputStream(B.getPlayerName()+".ser");
      out = new ObjectOutputStream(outStream);
      out.writeObject(B);
      out.close();
    } 
    catch(IOException e)
    {
      // Grant osborn 5/6 hopefully this never happens 
      showError(new Exception("Error saving bracket \n"+e.getMessage(),e),false);
    }
    }
    /**
     * Tayon Watson 5/5
     * deseralizedBracket
     * @param filename of the seralized bracket file
     * @return deserialized bracket 
     */
    private Bracket deseralizeBracket(String filename){
        Bracket bracket = null;
        FileInputStream inStream = null;
        ObjectInputStream in = null;
    try 
    {
        inStream = new FileInputStream(filename);
        in = new ObjectInputStream(inStream);
        bracket = (Bracket) in.readObject();
      in.close();
    } catch (IOException | ClassNotFoundException e) {
      // Grant osborn 5/6 hopefully this never happens either
      showError(new Exception("Error loading bracket \n"+e.getMessage(),e),false);
    } 
    return bracket;
    }
    
    
    private ArrayList<Bracket> loadBrackets()
    {   
        ArrayList<Bracket> list=new ArrayList<Bracket>();
        File dir = new File(".");
        for (final File fileEntry : dir.listFiles()){
        String fileName = fileEntry.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);
       
        if (extension.equals("ser")){
            list.add(deseralizeBracket(fileName));
        }
        }
        return list;
    }
       
}
