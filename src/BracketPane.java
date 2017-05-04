import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Richard and Ricardo on 5/3/17.
 */
public class BracketPane extends BorderPane {

    /**
     * Reference to the graphical representation of the nodes within the bracket.
     * TODO:
     */
    private static ArrayList<BracketNode> nodes;

    /**
     *  Maps the text "buttons" to it's respective grid-pane
     */
    private HashMap<Text, GridPane> panes;

    /**
     * Reference to the current bracket.
     */
    private Bracket currentBracket;

    /**
     * Reference to active subtree within current bracket.
     */
    private int displayedSubtree;

    /**
     * Used to initiate the paint of the bracket nodes
     */
    private static boolean isTop = true;


    /**
     * Keeps track of whether or not bracket has been finalized.
     */
    private boolean finalized;

    /**
     * @param currentBracket  TODO: change other constructor to take in this param
     */
    public BracketPane(Bracket currentBracket) {
        this.currentBracket = currentBracket;
    }

    /**
     * TODO
     * @param start the numeric indicator of which subtree
     */
    public void drawSubtree(int start) {
        // TODO: clear window and draw new stuff.
    }

    /**
     * Sets the current bracket to,
     * @param target The bracket to replace currentBracket
     */
    public void setBracket(Bracket target) {
        currentBracket = target;
    }

    /**
     * Clears the sub tree from,
     * @param position The position to clear after
     */
    public void clearSubtree(int position) {
        currentBracket.resetSubtree(position);
    }

    /**
     * TODO
     * Resets the bracket-display
     */
    public void resetBracket() {
        currentBracket.resetSubtree(0);
    }

    /**
     * @param isFinalized The value to set finalized to.
     */
    public void setFinalized(boolean isFinalized) {
        finalized = isFinalized && currentBracket.isComplete();
    }

    /**
     * Requests a message from current bracket to tell if the bracket
     * has been completed.
     * @return True if completed, false otherwise.
     */
    public boolean isComplete() {
        return currentBracket.isComplete();
    }

    /**
     * Returns
     * @return true if the current-bracket is complete and the value
     * of finalized is also true.
     */
    public boolean isFinalized() {
        return currentBracket.isComplete() && finalized;
    }

    /**
     * TODO: Reduce. reuse, recycle!
     * Initializes the properties needed to construct a bracket.
     */
    public BracketPane() {
        panes = new HashMap<>();
        nodes = new ArrayList<>();

        // Creates "buttons" of Text objects.
        ArrayList<Text> buttons = new ArrayList<>();
        buttons.add(new Text("North"));
        buttons.add(new Text("South"));
        buttons.add(new Text("East"));
        buttons.add(new Text("West"));
        buttons.add(new Text("Full"));

        // initializes each graphical representation of sub-trees
        // adds each to the panes map
        for (int j = 0; j < buttons.size()-1; j++) {
            GridPane tmp = new GridPane();
            tmp.add(new Root(), 0, 0);
            tmp.setAlignment(Pos.CENTER);
            panes.put(buttons.get(j),tmp);
        }

        // Fills the "full" pane (Entire bracket view)
        GridPane fullPane = new GridPane();
        GridPane gp1 = new GridPane();
        gp1.add(new Root(), 0, 0);
        gp1.add(new Root(), 0, 1);
        GridPane gp2 = new GridPane();
        gp2.add(new Root(), 0, 0);
        gp2.add(new Root(), 0, 1);
        gp2.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        fullPane.add(gp1, 0, 0);
        fullPane.add(new Rectangle(400, 400, Color.TRANSPARENT), 1, 0);
        fullPane.add(gp2, 2, 0);
        fullPane.add(new Root(), 0, 0);
        fullPane.setAlignment(Pos.CENTER);
        panes.put(buttons.get((buttons.size()-1)), fullPane);

        // Initializes the button grid
        // TODO spruce up aesthetics
        GridPane buttonGrid = new GridPane();
        for (int i = 0; i < buttons.size(); i++)
            buttonGrid.add(buttons.get(i), 0, i);
        buttonGrid.setAlignment(Pos.CENTER);

        // set default center to the button grid
        this.setCenter(buttonGrid);

        // Adds functionality to each text object
        for (Text t : buttons) {
            t.setTextAlignment(TextAlignment.CENTER);
            t.setStyle("-fx-background-color: orange;");
            t.setOnMouseEntered(mouseEvent -> {
                t.setStyle("-fx-background-color: darkgray;");
                t.setEffect(new InnerShadow(10, Color.LIGHTCYAN));
            });
            t.setOnMouseExited(mouseEvent -> {
                t.setStyle(null);
                t.setEffect(null);
            });
            t.setOnMouseClicked(mouseEvent -> {
               // Changes center on mouse clicked
                // TODO Disable appropriate text objects from being click-able
                setCenter(null);
                if (panes.get(t) != fullPane) {
                    panes.get(t).setScaleX(1.5);
                    panes.get(t).setScaleY(1.5);
                }
                setCenter(panes.get(t));
            });
            if (this.getCenter() != null) this.getCenter().requestFocus();
        }

    }

    /**
     * Creates the graphical representation of a subtree.
     * Note, this is a vague model. TODO: MAKE MODULAR
     */
    private class Root extends Pane {

        public Root() {
            createVertices(20, 25, 100, 25, 8, 50);
            createVertices(120, 35, 100, 50, 4, 100);
            createVertices(220, 60, 100, 100, 2, 200);
            createVertices(320,119,100,200,1,0);
            createVertices(420,200,100,20,0,0);

            //TODO: Set names of nodes
            for (BracketNode n : nodes) {
                n.setOnMouseClicked(clicked);
                n.setOnMouseEntered(enter);
                n.setOnMouseExited(exit);
            }
        }
        /**
         * The secret sauce... well not really,
         * Creates 3 lines in appropriate location unless it is the last line.
         * Adds these lines and "BracketNodes" to the Pane of this inner class
         */
        private void createVertices(int iX, int iY, int iXO, int iYO, int num, int increment) {
            int y = iY;

            if (num == 0 && increment == 0) {
                BracketNode last = new BracketNode("", iX, y - 20, iXO, 20);
                nodes.add(last);
                getChildren().addAll(new Line(iX,iY,iX+iXO,iY), last);
            } else {
                for (int i = 0; i < num; i++) {
                    Point2D tl = new Point2D(iX, y);
                    Point2D tr = new Point2D(iX + iXO, y);
                    Point2D bl = new Point2D(iX, y + iYO);
                    Point2D br = new Point2D(iX + iXO, y + iYO);
                    BracketNode nTop= new BracketNode("", iX, y - 20, iXO, 20);
                    BracketNode nBottom = new BracketNode("", iX, y + (iYO - 20), iXO, 20);
                    nodes.add(nTop);
                    nodes.add(nBottom);
                    Line top = new Line(tl.getX(), tl.getY(), tr.getX(), tr.getY());
                    Line bottom = new Line(bl.getX(), bl.getY(), br.getX(), br.getY());
                    Line right = new Line(tr.getX(), tr.getY(), br.getX(), br.getY());
                    getChildren().addAll(top, bottom, right,nTop,nBottom);
                    isTop = !isTop;
                    y += increment;
                }
            }

        }
    }

    /**
     * Handles clicked events for BracketNode objects
     */
    private EventHandler<MouseEvent> clicked = mouseEvent -> {
        // TODO: update bracket
        System.out.println("HEY");
    };

    /**
     * Handles mouseEntered events for BracketNode objects
     */
    private EventHandler<MouseEvent> enter = mouseEvent -> {
        BracketNode tmp = (BracketNode) mouseEvent.getSource();
        tmp.setStyle("-fx-background-color: lightcyan;");
        tmp.setEffect(new InnerShadow(10, Color.LIGHTCYAN));
    };

    /**
     * Handles mouseExited events for BracketNode objects
     */
    private EventHandler<MouseEvent> exit = mouseEvent -> {
        BracketNode tmp = (BracketNode) mouseEvent.getSource();
        tmp.setStyle(null);
        tmp.setEffect(null);

    };

    /**
     * The BracketNode model for the Graphical display of the "Bracket"
     */
    private class BracketNode extends Pane {
        private String teamName;
        private Rectangle rect;
        private Label name;

        /**
         * Creates a BracketNode with,
         * @param teamName The name if any
         * @param x The starting x location
         * @param y The starting y location
         * @param rX The width of the rectangle to fill pane
         * @param rY The height of the rectangle
         */
        public BracketNode(String teamName, int x, int y, int rX, int rY) {
            this.setLayoutX(x);
            this.setLayoutY(y);
            this.setMaxSize(rX, rY);
            this.teamName = teamName;
            rect = new Rectangle(rX, rY);
            rect.setFill(Color.TRANSPARENT);
            name = new Label(teamName);
            // setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            name.setTranslateX(5);
            getChildren().addAll(name, rect);
        }

        /**
         * @param teamName TODO: Is this necessary?
         */
        public void setName(String teamName) {
            this.teamName = teamName;
            name.setText(teamName);
        }

        /**
         * @return TODO: Or this?
         */
        public String getName() {
            return teamName;
        }

    }



}
