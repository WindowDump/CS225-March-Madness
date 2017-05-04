import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
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

    private static boolean isTop = true;
    private ArrayList<String> strings = new ArrayList<>();
    private static ArrayList<BracketNode> nodes = new ArrayList<>();
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
     * Keeps track of whether or not bracket has been finalized.
     */
    private boolean finalized;

    /**
     *
     * @param currentBracket
     */
    public BracketPane(Bracket currentBracket) {
        this.currentBracket = currentBracket;

    }

    /**
     *
     * @param start
     */
    public void drawSubtree(int start) {
        // TODO: clear window and draw new stuff.

    }

    /**
     *
     * @param target
     */
    public void setBracket(Bracket target) {
        currentBracket = target;
    }

    /**
     *
     * @param position
     */
    public void clearSubtree(int position) {
        currentBracket.resetSubtree(position);
    }

    /**
     *
     */
    public void resetBracket() {
        currentBracket.resetSubtree(0);
    }

    /**
     *
     * @param isFinalized
     */
    public void setFinalized(boolean isFinalized) {
        finalized = isFinalized && currentBracket.isComplete();
    }

    /**
     *
     * @return
     */
    public boolean isComplete() {
        return currentBracket.isComplete();
    }

    /**
     *
     * @return
     */
    public boolean isFinalized() {
        return currentBracket.isComplete() && finalized;
    }

    public BracketPane() {

        panes = new HashMap<>();
        this.setFocusTraversable(true);
        ArrayList<Text> buttons = new ArrayList<>();
//        buttons.add(new Text("Overview"));
        buttons.add(new Text("North"));
        buttons.add(new Text("South"));
        buttons.add(new Text("East"));
        buttons.add(new Text("West"));
        buttons.add(new Text("Full"));

        for (int j = 0; j < buttons.size()-1; j++) {
            GridPane tmp = new GridPane();
            tmp.add(new Root(), 0, 0);
            tmp.setAlignment(Pos.CENTER);
            panes.put(buttons.get(j),tmp);
        }

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

        GridPane buttonGrid = new GridPane();
        for (int i = 0; i < buttons.size(); i++)
            buttonGrid.add(buttons.get(i), 0, i);

        buttonGrid.setAlignment(Pos.CENTER);

        this.setCenter(buttonGrid);

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
                setCenter(null);
                if (panes.get(t) != fullPane) {
                    panes.get(t).setScaleX(1.5);
                    panes.get(t).setScaleY(1.5);
                }
                setCenter(panes.get(t));
            });
            if (this.getCenter() != null) this.getCenter().requestFocus();
            System.out.println(this.getCenter().isFocused());
        }

    }

    public void updateDisplay(GridPane pane) {

        setCenter(pane);
        pane.requestFocus();
        this.requestFocus();
        this.requestLayout();
    }

    private class Root extends Pane {

        public Root() {
            createVertecies(20, 25, 100, 25, 8, 50);
            createVertecies(120, 35, 100, 50, 4, 100);
            createVertecies(220, 60, 100, 100, 2, 200);
            //TODO: Set names of nodes
            for (BracketNode n : nodes) {
                n.setOnMouseClicked(clicked);
                n.setOnMouseEntered(enter);
                n.setOnMouseExited(exit);
            }
        }
        private void createVertecies(int iX, int iY, int iXO, int iYO, int num, int increment) {
            int y = iY;

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

    private EventHandler<MouseEvent> clicked = mouseEvent -> {
        // TODO: update bracket
        System.out.println("HEY");
    };

    private EventHandler<MouseEvent> enter = mouseEvent -> {
        BracketNode tmp = (BracketNode) mouseEvent.getSource();
        tmp.setStyle("-fx-background-color: lightcyan;");
        tmp.setEffect(new InnerShadow(10, Color.LIGHTCYAN));
    };

    private EventHandler<MouseEvent> exit = mouseEvent -> {
        BracketNode tmp = (BracketNode) mouseEvent.getSource();
        tmp.setStyle(null);
        tmp.setEffect(null);

    };

    private class BracketNode extends Pane {
        private String teamName;
        private Rectangle rect;
        private Label name;

        public BracketNode(String teamName, int x, int y, int rX, int rY) {
            this.setLayoutX(x);
            this.setLayoutY(y);
            this.setMaxSize(rX, rY);
            this.teamName = teamName;
            rect = new Rectangle(rX, rY);
            rect.setFill(Color.TRANSPARENT);
            name = new Label(teamName);
//            setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            name.setTranslateX(5);
            getChildren().addAll(name, rect);
        }

        public void setName(String teamName) {
            this.teamName = teamName;
            name.setText(teamName);
        }

        public String getName() {
            return teamName;
        }

    }



}
