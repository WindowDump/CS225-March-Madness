//package marchmadness;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

/**
 *  MarchMadnessGUI
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
    
    @Override
    public void start(Stage primaryStage) {  
        root = new BorderPane();
        CreateToolBars();
        
        //uncomment the next line of code to test with a html Editor in center
        //replace HTMLEditor with your class to test your class
        displayPane(new HTMLEditor());
        
        setActions();
        root.setTop(toolBar);   
        root.setBottom(btoolBar);
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
     * to be properly center aligned in  the parent node
     */
    private void displayPane(Node p){
        root.setCenter(p);
        BorderPane.setAlignment(p,Pos.CENTER);
    }
    
    //creates toolBar and buttons.
    // adds buttons to the toolbar and saves global referances to them
    private void CreateToolBars(){
        toolBar  = new ToolBar();
        btoolBar  = new ToolBar();
        login=new Button("Login");
        addPlayer=new Button("Add Player");
        simulate=new Button("Simulate");
        scoreBoardButton=new Button("ScoreBoard");
        viewBracket= new Button("view Bracket");
        clearButton=new Button("Clear");
        resetButton=new Button("Reset");
        finalizeButton=new Button("Finalize");
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
    private void setActions(){
        login.setOnAction(e->btoolBar.setVisible(false));
        viewBracket.setOnAction(e->btoolBar.setVisible(true));
        //addPlayer.setOnAction(e->simulate.setVisible(false));
        addPlayer.setOnAction(e->simulate.setDisable(true));
    }
    
    //Creates a spacer for centering buttons in a ToolBar
    private Pane createSpacer(){
        Pane spacer = new Pane();
        HBox.setHgrow(
                spacer,
                Priority.SOMETIMES
        );
        return spacer;
    }

}
