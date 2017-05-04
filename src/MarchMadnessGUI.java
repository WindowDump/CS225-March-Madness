//package marchmadness;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * MarchMadnessGUI
 *
 * @author Grant Osborn
 */
public class MarchMadnessGUI extends Application {

    private BorderPane root;
    private ToolBar toolBar;
    private ToolBar btoolBar;
    private Button addPlayer;
    private Button simulate;
    private Button login;
    private Button scoreBoardButton;
    private Button viewBracket;
    private Button clearButton;
    private Button resetButton;
    private Button finalizeButton;
    private Bracket emptyBracket;

    //private BracketPane bracketPane;
    private ArrayList<Bracket> playerBrackets;
    private HashMap<String, Bracket> playerMap;

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        playerBrackets = new ArrayList<>();

        // ??
        emptyBracket = new Bracket(new ArrayList<String>());


        playerMap = new HashMap<>();

        CreateToolBars();


        //uncomment the next line of code to test with a html Editor in center
        //replace HTMLEditor with your class to test your class
        displayPane(new HTMLEditor());

        setActions();
        root.setTop(toolBar);
        root.setBottom(btoolBar);


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
                String password1 = tmpBracket.getPassword();
                System.out.println(password1 + " " + playerPass);

                if (Objects.equals(password1, playerPass)) {
                    // load bracket
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

            //            for ( Bracket bracket : playerBrackets) {
//
//
//                if(name.equals(bracket.getPlayerName())
//                        || playerPass.equals(bracket.getPassword())
//
//                        load the bracket object
//
//                        );
//                else {
//
//                     create a new Bracket
//                     new Bracket();
//
            //Bracket playerBracket = new Bracket();
            //playerBracket.setPassword();
//                }
//            }

        });
        root.setCenter(loginPane);

        Scene scene = new Scene(root, 600, 480);
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
     * displays element in the center of the screen
     *
     * @param p must use a subclass of Pane for layout.
     *          to be properly center aligned in  the parent node
     */
    private void displayPane(Node p) {
        root.setCenter(p);
        BorderPane.setAlignment(p, Pos.CENTER);
    }

    //creates toolBar and buttons.
    // adds buttons to the toolbar and saves global referances to them
    private void CreateToolBars() {
        toolBar = new ToolBar();
        btoolBar = new ToolBar();
        login = new Button("Login");
        addPlayer = new Button("Add Player");
        simulate = new Button("Simulate");
        scoreBoardButton = new Button("ScoreBoard");
        viewBracket = new Button("view Bracket");
        clearButton = new Button("Clear");
        resetButton = new Button("Reset");
        finalizeButton = new Button("Finalize");
        toolBar.getItems().addAll(
                createSpacer(),
                login,
                addPlayer,
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

    //dummy button handeling
    private void setActions() {
        login.setOnAction(e -> btoolBar.setVisible(false));
        viewBracket.setOnAction(e -> btoolBar.setVisible(true));
        //addPlayer.setOnAction(e->simulate.setVisible(false));
        addPlayer.setOnAction(e -> simulate.setDisable(true));
    }

    //Creates a spacer for centering buttons in a ToolBar
    private Pane createSpacer() {
        Pane spacer = new Pane();
        HBox.setHgrow(
                spacer,
                Priority.SOMETIMES
        );
        return spacer;
    }
}
