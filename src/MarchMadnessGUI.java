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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    
    
    private BorderPane root;
    private ToolBar toolBar;
    private ToolBar btoolBar;
    private Button simulate;
    private Button login;
    private Button scoreBoardButton;
    private Button viewBracket;
    private Button clearButton;
    private Button resetButton;
    private Button finalizeButton;
    
    private Bracket emptyBracket;
    private Bracket startingBracket;
    //referance to currently logged in bracket
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
        //showError("hey",true);
        try {
            teamInfo=new TournamentInfo();
        } catch (IOException ex) {

        }
        
        
        // ??
        emptyBracket = new Bracket(new ArrayList<String>());
        startingBracket=new Bracket(TournamentInfo.loadStartingBracket());
        simResultBracket=new Bracket(TournamentInfo.loadStartingBracket());

     
        //create test acount
        simResultBracket.setPlayerName("Grant");
        //simResultBracket.setPassword("hunter2");
        simResultBracket.setPassword("h");
        seralizeBracket(simResultBracket);
        
        
         //deserialize stored brackets
        playerBrackets = loadBrackets();
        //playerBrackets.forEach(b->System.out.println(b.getPlayerName()));
        playerMap = new HashMap<>();
        addAllToMap();
        


        
        root = new BorderPane();
        scoreBoard= new ScoreBoardPane();
        bracketPane= new BracketPane();
        loginP=createLogin();
        CreateToolBars();
        
        //test you frontend object with displayPane()
        //login();
        
        setActions();
        root.setTop(toolBar);   
        root.setBottom(btoolBar);
        Scene scene = new Scene(root, 800, 600);
        //scene.getStylesheets().add("style.css");
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
     * 
     */
    private void simulate(){
        //cant login and restart prog after simulate
//        login.setDisable(true);
//        btoolBar.setDisable(true);
//        simulate.setDisable(true);
        //disable
//        scoreBoardButton.setDisable(false);
//        viewBracket.setDisable(false);
        
    
        teamInfo.simulate(simResultBracket);

     
    }
    
    /**
     * Displays the login screen
     * 
     */
    private void login(){
//           simulate.setDisable(true);
//           scoreBoardButton.setDisable(true);
//           viewBracket.setDisable(true);
//           btoolBar.setDisable(true);
           displayPane(loginP);
    }
    
     /**
     * Displays the score board
     * 
     */
    private void scoreBoard(){
        
        //sort brackets by score 
        //playerBrackets.sort((Bracket p1, Bracket p2) -> p1.scoreBracket(simResultBracket) -p2.scoreBracket(simResultBracket));        
        
         //scoreBoardButton.setDisable(true);
          displayPane(scoreBoard._start());
          //viewBracket.setDisable(false);
    }
    
    /**
     * displays the users bracket
     * 
     */
    private void viewBracket(){
        //login.setDisable(true);
        displayPane(new BracketPane());
        
    }
    
    
    private void chooseBracket(){
//        login.setDisable(true);
//        btoolBar.setDisable(false);
        displayPane(new BracketPane());
        
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
        
    }
    
    private void finalizeBracket(){
       if(selectedBracket.isComplete()){
            //simulate.setDisable(false);
            //login.setDisable(false);
            seralizeBracket(selectedBracket);
       }
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
        viewBracket= new Button("view Bracket");
        clearButton=new Button("Clear");
        resetButton=new Button("Reset");
        finalizeButton=new Button("Finalize");
        toolBar.getItems().addAll(
                createSpacer(),
                login,
                simulate,
                scoreBoardButton,
                viewBracket,
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
        viewBracket.setOnAction(e->viewBracket());
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

            System.out.println(name + " " + playerPass);
          
            
            if (playerMap.get(name) != null) {
                //check password of user
                 
                Bracket tmpBracket = this.playerMap.get(name);
                //System.out.println(playerMap.get(name).getPassword());

                String password1 = tmpBracket.getPassword();
                System.out.println(password1 + " " + playerPass);

                if (Objects.equals(password1, playerPass)) {
                    // load bracket
                    selectedBracket=playerMap.get(name);
                    // bracketPane.setCurrent(playerMap.get(name));
                    chooseBracket();
                   
                    System.out.println("load bracket of user: " + name);
                } else {
                    System.out.println("Password incorrect!");
                }

            } else {
                //check for empty fields
                if(!name.equals("")&&!playerPass.equals("")){
                    //create new bracket
                    System.out.println("User " + name + " did not exist, created new bracket");
                    Bracket tmpPlayerBracket = new Bracket(emptyBracket, name);
                    playerBrackets.add(tmpPlayerBracket);
                    tmpPlayerBracket.setPassword(playerPass);

                    playerMap.put(name, tmpPlayerBracket);
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
     * 
     * @param msg message to be displayed to the user
     * @param fatal true if the program should exit false will resume 
     */
    private void showError(String msg,boolean fatal){
        if(fatal){
            msg=msg+" \nthe program will now close";
        }
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("something went wrong");
        alert.setContentText(msg);

        alert.showAndWait();
        if(fatal){ 
            System.exit(666);
        }
        
    }
    
    
    /**
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
    catch(Exception e)
    {
      e.printStackTrace();
    }
    }
    /**
     * deseralizedBracket
     * @param filename of the seralized bracket file
     * @return 
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
    } catch (Exception e) {
      e.printStackTrace();
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
        //System.out.println(extension);
        if (extension.equals("ser")){
            list.add(deseralizeBracket(fileName));
        }
        }
        return list;
    }
    
    
    
    
     
}
