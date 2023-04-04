import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.scene.layout.Region;

/**
 * Created by Richard and Ricardo on 5/3/17.
 */
public class BracketPane extends BorderPane {

        //Reference to the graphical representation of the nodes within the bracket.
        private static ArrayList<BracketNode> nodes;

        /**
         * Maps the text "buttons" to its respective grid-pane
         */
        private final HashMap<StackPane, Pane> panes;
        /**
         * Reference to the current bracket.
         */
        private Bracket currentBracket;
        /**
         * Reference to active subtree within current bracket.
         */
        private int displayedSubtree;

        /**
         * Important logical simplification for allowing for code that is easier
         * to maintain.
         */
        private HashMap<BracketNode, Integer> bracketMap;
        /**
         * Reverse of the above;
         */
        private HashMap<Integer, BracketNode> nodeMap;

        private GridPane center;
        private GridPane fullPane;


        //Default constructor
        public BracketPane() {
                currentBracket = null;
                nodes = new ArrayList<>();
                panes = new HashMap<>();
                displayedSubtree = 0;
                bracketMap = new HashMap<>();
                nodeMap = new HashMap<>();
                createCenterGridPane();
                createButtonGridPane();
                setButtonActions();

        }


        //Parameter constructor
        public BracketPane(Bracket currentBracket) {
                this.currentBracket = currentBracket;
                displayedSubtree = 0;
                bracketMap = new HashMap<>();
                nodeMap = new HashMap<>();
                panes = new HashMap<>();
                nodes = new ArrayList<>();
                center = new GridPane();
                createCenterGridPane();
                createButtonGridPane();
                setButtonActions();
        }

        /**
         * Clears the entries of a team future wins
         *
         */
        private void clearAbove(int treeNum) {
                int nextTreeNum = (treeNum - 1) / 2;
                if (!nodeMap.get(nextTreeNum).getName().isEmpty()) {
                        nodeMap.get(nextTreeNum).setName("");
                        clearAbove(nextTreeNum);
                }
        }


        public void clear(){
                clearSubtree(displayedSubtree);
        }

        /**
         * Handles clicked events for BracketNode objects
         */
        private final EventHandler<MouseEvent> clicked = mouseEvent -> {
                //conditional added by matt 5/7 to differentiate between left and right mouse click
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        BracketNode n = (BracketNode) mouseEvent.getSource();
                        int treeNum = bracketMap.get(n);
                        int nextTreeNum = (treeNum - 1) / 2;
                        if (!nodeMap.get(nextTreeNum).getName().equals(n.getName())) {
                                currentBracket.removeAbove((nextTreeNum));
                                clearAbove(treeNum);
                                nodeMap.get((bracketMap.get(n) - 1) / 2).setName(n.getName());
                                currentBracket.moveTeamUp(treeNum);
                        }
                }
                //added by matt 5/7, shows the teams info if you right-click
                else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                        String text = "";
                        BracketNode n = (BracketNode) mouseEvent.getSource();
                        int treeNum = bracketMap.get(n);
                        String teamName = currentBracket.getBracket().get(treeNum);
                        try {
                                TournamentInfo info = new TournamentInfo();
                                Team t = info.getTeam(teamName);
                                //by Tyler - added the last two pieces of info to the pop-up window
                                text += "Team: " + teamName + " | Ranking: " + t.getRanking() + "\nMascot: " + t.getNickname() + "\nInfo: " + t.getInfo() + "\nAverage Offensive PPG: " + t.getOffensePPG() + "\nAverage Defensive PPG: "+ t.getDefensePPG();
                        } catch (IOException e) {//if for some reason TournamentInfo isn't working, it will display info not found
                                text += "Info for " + teamName + "not found";
                        }
                        //create a popup with the team info
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, text, ButtonType.CLOSE);
                        alert.setTitle("March Madness Bracket Simulator");
                        alert.setHeaderText(null);
                        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                        alert.showAndWait();
                }
        };
        /**
         * Handles mouseEntered events for BracketNode objects
         */
        private final EventHandler<MouseEvent> enter = mouseEvent -> {
                BracketNode tmp = (BracketNode) mouseEvent.getSource();
                tmp.setStyle("-fx-background-color: lightcyan;");
                tmp.setEffect(new InnerShadow(10, Color.LIGHTCYAN));
        };

        /**
         * Handles mouseExited events for BracketNode objects
         */
        private final EventHandler<MouseEvent> exit = mouseEvent -> {
                BracketNode tmp = (BracketNode) mouseEvent.getSource();
                tmp.setStyle(null);
                tmp.setEffect(null);

        };

        public GridPane getFullPane() {
                return fullPane;
        }

        private ArrayList<StackPane> createButtons() {
                ArrayList<StackPane> buttons = new ArrayList<>();
                buttons.add(customButton("EAST"));
                buttons.add(customButton("WEST"));
                buttons.add(customButton("MIDWEST"));
                buttons.add(customButton("SOUTH"));
                buttons.add(customButton("FULL"));
                return buttons;
        }

        private void createCenterGridPane() {
                ArrayList<Root> roots = new ArrayList<>();
                ArrayList<StackPane> buttons = createButtons();

                for (int i = 0; i < buttons.size() - 1; i++) {
                        roots.add(new Root(3 + i));
                        panes.put(buttons.get(i), roots.get(i));
                }

                Pane finalPane = createFinalFour();
                GridPane fullPane = createFullPane(roots, finalPane);
                panes.put(buttons.get(buttons.size() - 1), fullPane);
                finalPane.toBack();
        }

        private GridPane createFullPane(ArrayList<Root> roots, Pane finalPane) {
                GridPane fullPane = new GridPane();
                GridPane gp1 = new GridPane();
                gp1.add(roots.get(0), 0, 0);
                gp1.add(roots.get(1), 0, 1);
                GridPane gp2 = new GridPane();
                gp2.add(roots.get(2), 0, 0);
                gp2.add(roots.get(3), 0, 1);
                gp2.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

                fullPane.add(gp1, 0, 0);
                fullPane.add(finalPane, 1, 0, 1, 2);
                fullPane.add(gp2, 2, 0);
                fullPane.setAlignment(Pos.CENTER);
                return fullPane;
        }

        private void createButtonGridPane() {
                ArrayList<StackPane> buttons = createButtons();
                GridPane buttonGrid = new GridPane();
                for (int i = 0; i < buttons.size(); i++) {
                        buttonGrid.add(buttons.get(i), 0, i);
                }
                buttonGrid.setAlignment(Pos.CENTER);
                this.setCenter(buttonGrid);
        }

        private void setButtonActions() {
                ArrayList<StackPane> buttons = createButtons();
                for (StackPane t : buttons) {
                        t.setOnMouseEntered(mouseEvent -> {
                                t.setStyle("-fx-background-color: lightblue;");
                                t.setEffect(new InnerShadow(10, Color.LIGHTCYAN));
                        });
                        t.setOnMouseExited(mouseEvent -> {
                                t.setStyle("-fx-background-color: orange;");
                                t.setEffect(null);
                        });
                        t.setOnMouseClicked(mouseEvent -> {
                                setCenter(null);
                                center.add(new ScrollPane(panes.get(t)), 0, 0);
                                center.setAlignment(Pos.CENTER);
                                setCenter(center);
                                displayedSubtree = buttons.indexOf(t) == 7 ? 0 : buttons.indexOf(t) + 3;
                        });
                }
        }

        /**
         * Helpful method to retrieve our magical numbers
         *
         * @param root the root node (3,4,5,6)
         * @param pos  the position in the tree (8 (16) , 4 (8) , 2 (4) , 1 (2))
         * @return The list representing the valid values.
         */
        public ArrayList<Integer> helper(int root, int pos) {
                ArrayList<Integer> positions = new ArrayList<>();
                int base = 0;
                int tmp = (root * 2) + 1;
                if (pos == 8) base = 3;
                else if (pos == 4) base = 2;
                else if (pos == 2) base = 1;
                for (int i = 0; i < base; i++) tmp = (tmp * 2) + 1;
                for (int j = 0; j < pos * 2; j++) positions.add(tmp + j);
                return positions; //                while ((tmp = ((location * 2) + 1)) <= 127) ;
        }

        /**
         * Sets the current bracket to,
         *
         * @param target The bracket to replace currentBracket
         */
        public void setBracket(Bracket target) {
                currentBracket = target;
        }

        /**
         * Clears the sub tree from,
         *
         * @param position The position to clear after
         */
        public void clearSubtree(int position) {
                currentBracket.resetSubtree(position);
        }

        /**
         * Requests a message from current bracket to tell if the bracket
         * has been completed.
         *
         * @return True if completed, false otherwise.
         */
        public boolean isComplete() {
                return currentBracket.isComplete();
        }


        /**
         * Returns a custom "Button" with specified
         *
         * @param name The name of the button
         * @return pane The stack-pane "button"
         */
        private StackPane customButton(String name) {
                StackPane pane = new StackPane();
                Rectangle r = new Rectangle(100, 50, Color.TRANSPARENT);
                Text t = new Text(name);
                t.setTextAlignment(TextAlignment.CENTER);
                pane.getChildren().addAll(r, t);
                pane.setStyle("-fx-background-color: orange;");
                return pane;
        }

        public Pane createFinalFour() {
                Pane finalPane = new Pane();
                createFinalNodes(finalPane);
                setNodeStyles();
                finalPane.setMinWidth(400.0);
                return finalPane;
        }

        private void createFinalNodes(Pane finalPane) {
                BracketNode nodeFinal0 = createBracketNode("", 162, 300, 70, 0);
                BracketNode nodeFinal1 = createBracketNode("", 75, 400, 70, 0);
                BracketNode nodeFinal2 = createBracketNode("", 250, 400, 70, 0);
                nodeFinal0.setName(currentBracket.getBracket().get(0));
                nodeFinal1.setName(currentBracket.getBracket().get(1));
                nodeFinal2.setName(currentBracket.getBracket().get(2));
                addNodesToPane(finalPane, nodeFinal0, nodeFinal1, nodeFinal2);
        }

        private BracketNode createBracketNode(String teamName, int x, int y, int rX, int rY) {
                BracketNode node = new BracketNode(teamName, x, y, rX, rY);
                node.setOnMouseClicked(clicked);
                node.setOnMouseDragEntered(enter);
                node.setOnMouseDragExited(exit);
                return node;
        }

        private void addNodesToPane(Pane pane, BracketNode... nodes) {
                for (BracketNode node : nodes) {
                        pane.getChildren().add(node);
                        bracketMap.put(node, nodeMap.size());
                        nodeMap.put(nodeMap.size(), node);
                }
        }

        private void setNodeStyles() {
                for (BracketNode node : nodeMap.values()) {
                        node.setStyle("-fx-border-color: darkblue");
                }
        }

        /**
         * Creates the graphical representation of a subtree.
         */
        private class Root extends Pane {
                private final int location;

                public Root(int location) {
                        this.location = location;
                        createNodePairs(420, 200, 100, 20, 0, 0);
                        createNodePairs(320, 119, 100, 200, 1, 0);
                        createNodePairs(220, 60, 100, 100, 2, 200);
                        createNodePairs(120, 35, 100, 50, 4, 100);
                        createNodePairs(20, 25, 100, 25, 8, 50);
                }

                private void createSingleNode(int iX, int iY, int iXO, int y) {
                        BracketNode last = new BracketNode("", iX, y - 20, iXO, 20);
                        nodes.add(last);
                        getChildren().addAll(new Line(iX, iY, iX + iXO, iY), last);
                        last.setName(currentBracket.getBracket().get(location));
                        bracketMap.put(last, location);
                        nodeMap.put(location, last);
                        setNodeEventListeners(last);
                }

                private void createNodePairs(int iX, int iY, int iXO, int iYO, int num, int increment) {
                        if (num == 0 && increment == 0) {
                                createSingleNode(iX, iY, iXO, iY - 20);
                        } else {
                                int y = iY;
                                ArrayList<BracketNode> aNodeList = new ArrayList<>();
                                for (int i = 0; i < num; i++) {
                                        aNodeList.addAll(createNodePair(iX, iXO, iYO, y));
                                        y += increment;
                                }
                                ArrayList<Integer> tmpHelp = helper(location, num);
                                for (int j = 0; j < aNodeList.size(); j++) {
                                        aNodeList.get(j).setName(currentBracket.getBracket().get(tmpHelp.get(j)));
                                        bracketMap.put(aNodeList.get(j), tmpHelp.get(j));
                                        nodeMap.put(tmpHelp.get(j), aNodeList.get(j));
                                }
                        }
                }

                private List<BracketNode> createNodePair(int iX, int iXO, int iYO, int y) {
                        Point2D topLeft = new Point2D(iX, y);
                        Point2D topRight = new Point2D(iX + iXO, y);
                        Point2D bottomLeft = new Point2D(iX, y + iYO);
                        Point2D bottomRight = new Point2D(iX + iXO, y + iYO);

                        BracketNode nodeTop = new BracketNode("", iX, y - 20, iXO, 20);
                        BracketNode nodeBottom = new BracketNode("", iX, y + (iYO - 20), iXO, 20);

                        Line top = new Line(topLeft.getX(), topLeft.getY(), topRight.getX(), topRight.getY());
                        Line bottom = new Line(bottomLeft.getX(), bottomLeft.getY(), bottomRight.getX(), bottomRight.getY());
                        Line right = new Line(topRight.getX(), topRight.getY(), bottomRight.getX(), bottomRight.getY());

                        getChildren().addAll(top, bottom, right, nodeTop, nodeBottom);

                        setNodeEventListeners(nodeTop);
                        setNodeEventListeners(nodeBottom);

                        return Arrays.asList(nodeTop, nodeBottom);
                }

                private void setNodeEventListeners(BracketNode node) {
                        node.setOnMouseClicked(clicked);
                        node.setOnMouseEntered(enter);
                        node.setOnMouseExited(exit);
                }
        }


        /**
         * The BracketNode model for the Graphical display of the "Bracket"
         */
        private static class BracketNode extends Pane {
                private String teamName;
                private final Label name;

                /**
                 * Creates a BracketNode with,
                 *
                 * @param teamName The name if any
                 * @param x        The starting x location
                 * @param y        The starting y location
                 * @param rX       The width of the rectangle to fill pane
                 * @param rY       The height of the rectangle
                 */
                public BracketNode(String teamName, int x, int y, int rX, int rY) {
                        this.setLayoutX(x);
                        this.setLayoutY(y);
                        this.setMaxSize(rX, rY);
                        this.teamName = teamName;
                        Rectangle rect = new Rectangle(rX, rY);
                        rect.setFill(Color.TRANSPARENT);
                        name = new Label(teamName);
                        name.setTranslateX(5);
                        getChildren().addAll(name, rect);
                }

                /**
                 * @return teamName The teams name.
                 */
                public String getName() {
                        return teamName;
                }

                /**
                 * @param teamName The name to assign to the node.
                 */
                public void setName(String teamName) {
                        this.teamName = teamName;
                        name.setText(teamName);
                }
        }
}
