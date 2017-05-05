import java.io.IOException;
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
    private Bracket selectedBracket;
    private Bracket simResultBracket;

    
    private ArrayList<Bracket> playerBrackets;
    private HashMap<String, Bracket> playerMap;

    
  
    private ScoreBoardPane scoreBoard;
    private BracketPane bracketPane;
    private GridPane loginP;
    //private TeamInfo teamInfo;
    
    
    @Override
    public void start(Stage primaryStage) {
        playerBrackets = new ArrayList<>();
//        try {
//            teamInfo=new TeamInfo();
//        } catch (IOException ex) {
//            Alert alert = new Alert(AlertType.ERROR);
//            alert.setTitle("Error Dialog");
//            alert.setHeaderText("Look, an Error Dialog");
//            alert.setContentText("Ooops, there was an error!");
//
//            alert.showAndWait();
//            //primaryStage.close();
//        }

        // ??
        emptyBracket = new Bracket(new ArrayList<String>());


        playerMap = new HashMap<>();

        
        root = new BorderPane();
        scoreBoard= new ScoreBoardPane();
        bracketPane= new BracketPane();
        loginP=createLogin();
        CreateToolBars();
        
        //test you frontend object with displayPane()
        displayPane(loginP);
        
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
        
        
    }
    
    /**
     * Displays the login screen
     * 
     */
    private void login(){
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
        displayPane(new BracketPane());
        
    }
    
    private void clear(){
        
        
    }
    
    
    private void reset(){
        
    }
    
    private void finalizeBracket(){
        
        
    }
    
    
    /**
     * displays element in the center of the screen
     * 
     * @param p must use a subclass of Pane for layout. 
     * to be properly center aligned in  the parent node
     */
    private void displayPane(Node p){
        root.setCenter(p);
        //BorderPane.setAlignment(p,Pos.CENTER);
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
                System.out.println(playerMap.get(name).getPassword());

                String password1 = tmpBracket.getPassword();
                System.out.println(password1 + " " + playerPass);

                if (Objects.equals(password1, playerPass)) {
                    // load bracket
                    //
                    // bracketPane.setCurrent(playerMap.get(name));
                    System.out.println("load bracket of user: " + name);
                } else {
                    System.out.println("Password incorrect!");
                }

            } else {
                //create new bracket
                System.out.println("User " + name + " did not exist, created new bracket");
                Bracket tmpPlayerBracket = new Bracket(emptyBracket, name);
                playerBrackets.add(tmpPlayerBracket);
                tmpPlayerBracket.setPassword(playerPass);

                playerMap.put(name, tmpPlayerBracket);
            }
        });
        
        return loginPane;
    }
    
    
    
     
}
