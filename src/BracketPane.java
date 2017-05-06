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
import javafx.scene.layout.StackPane;
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
     * Used to initiate the paint of the bracket nodes
     */
    private static boolean isTop = true;
    /**
     *  Maps the text "buttons" to it's respective grid-pane
     */
//    private HashMap<Text, GridPane> panes;
    private HashMap<StackPane, GridPane> panes;
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
     * Handles clicked events for BracketNode objects
     */
    private HashMap<Integer, BracketNode> bracketNodeMap;
    /**
     * Able to save the working bracket to the main data structure
     */
    private HashMap<String, Bracket> playerMap;

    /**
     * Clears the entries of a team future wins
     * @param treeNum
     */
    private void clearAbove(int treeNum){
        int nextTreeNum = (treeNum - 1) / 2;
        if (!bracketNodeMap.get(nextTreeNum).getName().isEmpty()){
            bracketNodeMap.get(nextTreeNum).setName("");
            clearAbove(nextTreeNum);
        }
    }
    /**
     * Handles mouseClicked events for BracketNode objects
     */
    private EventHandler<MouseEvent> clicked = mouseEvent -> {
        BracketNode tmp = (BracketNode) mouseEvent.getSource();
        int treeNum = tmp.getTreeNum();
        int nextTreeNum = (treeNum - 1) / 2;
        //The starting ends of the bracket (63 - 126) will cause a team to move up.
        if (treeNum >= 63) {
            //If the next node contains text, the team will be moved down and new team moved up
            if (!bracketNodeMap.get(nextTreeNum).getName().isEmpty()) {
                currentBracket.removeAbove((nextTreeNum));
                clearAbove(treeNum);
                bracketNodeMap.get(nextTreeNum).setName(tmp.getName());
                currentBracket.moveTeamUp(treeNum);
                saveBracket(currentBracket);
            }
            else{
                bracketNodeMap.get(nextTreeNum).setName(tmp.getName());
                currentBracket.moveTeamUp(treeNum);
                saveBracket(currentBracket);
            }
        }
        //treeNum < 63
        else {
            //check to see if the next node contains a team, if it does move team down and clear all the team progress from above the node
            if(!bracketNodeMap.get(nextTreeNum).getName().isEmpty()) {
                currentBracket.removeAbove(nextTreeNum);
                clearAbove(treeNum);
                bracketNodeMap.get(nextTreeNum).setName(tmp.getName());
                currentBracket.moveTeamUp(treeNum);
                saveBracket(currentBracket);
            }
            else {
                bracketNodeMap.get(nextTreeNum).setName(tmp.getName());
                currentBracket.moveTeamUp(treeNum);
                saveBracket(currentBracket);
            }
            }
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
     * TODO: Reduce. reuse, recycle!
     * Initializes the properties needed to construct a bracket.
     */
    public BracketPane(Bracket bracket, HashMap<String, Bracket> playerMap) {
        this.playerMap = playerMap;
        this.currentBracket = bracket;
        panes = new HashMap<>();
        nodes = new ArrayList<>();

        ArrayList<String> teams;
        teams = currentBracket.getBracket();
        HashMap<Integer, String> teamIndexMap = new HashMap<>();
        for (int i = 0; i < 127 ; i++) {
            teamIndexMap.put(i, teams.get(i));
        }

        // Creates "buttons" of Text objects.
//        ArrayList<Text> buttons = new ArrayList<>();
//        buttons.add(new Text("North"));
//        buttons.add(new Text("South"));
//        buttons.add(new Text("East"));
//        buttons.add(new Text("West"));
//        buttons.add(new Text("Full"));
        ArrayList<StackPane> buttons = new ArrayList<>();
        buttons.add(customButton("EAST", Color.CORAL));
        buttons.add(customButton("WEST", Color.CORAL));
        buttons.add(customButton("MIDWEST", Color.CORAL));
        buttons.add(customButton("SOUTH", Color.CORAL));
        buttons.add(customButton("FULL", Color.CORAL));

        bracketNodeMap = new HashMap<>();
        // initializes each graphical representation of sub-trees
        // adds each to the panes map
        int treeNum = 63;
        int division = 1;
        for (int j = 0; j < buttons.size()-1; j++) {
            if(division == 4){
                treeNum = 109;
            }
            GridPane tmp = new GridPane();
            tmp.add(new Root(treeNum, division, teams), 0, 0);
            tmp.setAlignment(Pos.CENTER);
            panes.put(buttons.get(j),tmp);
            treeNum += 16;
            division++;
        }

        // Fills the "full" pane (Entire bracket view)
        GridPane fullPane = new GridPane();
        GridPane gp1 = new GridPane();
        gp1.add(panes.get(buttons.get(0)), 0, 0);
        gp1.add(panes.get(buttons.get(1)), 0, 1);
        GridPane gp2 = new GridPane();
        gp2.add(panes.get(buttons.get(2)), 0, 0);
        gp2.add(panes.get(buttons.get(3)), 0, 1);
        gp2.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        fullPane.add(gp1, 0, 0);
        fullPane.add(new Rectangle(400, 400, Color.TRANSPARENT), 1, 0);
        //insert the last three Nodes (0,1,2)
        BracketNode finalNode1 = new BracketNode(teams.get(1), 70, 400, 500, 100, 1);
        bracketNodeMap.put(1, finalNode1);
        BracketNode finalNode2 = new BracketNode(teams.get(2), 70, 400, 500, 100, 2);
        bracketNodeMap.put(2, finalNode2);
        BracketNode finalNode = new BracketNode(teams.get(0), 70, 600, 500, 100, 0);
        bracketNodeMap.put(0, finalNode);
        finalNode1.setOnMouseClicked(clicked);
        finalNode2.setOnMouseClicked(clicked);
        fullPane.add(finalNode1, 1,0);
        fullPane.add(finalNode2, 2, 0);
        fullPane.add(finalNode,1,0);
        fullPane.add(gp2, 2, 0);
        //fullPane.add(new Root(), 0, 0);
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
//        for (Text t : buttons) {
        for (StackPane t : buttons) {
//            t.setTextAlignment(TextAlignment.CENTER);

            t.setOnMouseEntered(mouseEvent -> {
                t.setStyle("-fx-background-color: lightblue;");
                t.setEffect(new InnerShadow(10, Color.LIGHTCYAN));
            });
            t.setOnMouseExited(mouseEvent -> {
                t.setStyle("-fx-background-color: orange;");
//                t.setStyle();
                t.setEffect(null);
            });
            t.setOnMouseClicked(mouseEvent -> {
               // Changes center on mouse clicked
                // TODO Disable appropriate text objects from being click-able
                setCenter(null);
//                if (panes.get(t) != fullPane) {
//                    panes.get(t).setScaleX(1.5);
//                    panes.get(t).setScaleY(1.5);
//                }
                setCenter(panes.get(t));
            });
            if (this.getCenter() != null) this.getCenter().requestFocus();
        }

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
     * @param isFinalized The value to set finalized to.
     */
    public void setFinalized(boolean isFinalized) {
        finalized = isFinalized && currentBracket.isComplete();
    }

    /**
     * Returns a custom "Button" with specified
     * @param name The name of the button
     * @param color
     * @return pane The stack-pane "button"
     */
    private StackPane customButton(String name, Color color) {
        StackPane pane = new StackPane();
        Rectangle r = new Rectangle(100,50, Color.TRANSPARENT);
        Text t = new Text(name);
        t.setTextAlignment(TextAlignment.CENTER);
        pane.getChildren().addAll(r, t);
        pane.setStyle("-fx-background-color: orange;");
        return pane;
    }

    private void saveBracket(Bracket bracket){
        playerMap.put(bracket.getPlayerName(), bracket);
    }

    /**
     * Creates the graphical representation of a subtree.
     * Note, this is a vague model. TODO: MAKE MODULAR
     */
    private class Root extends Pane {

        //create the bracket by giving the method of the first index of the round of a sub bracket
        public Root(int treeNum, int division, ArrayList<String> teams) {
            createVertices(20, 25, 100, 25, 8, 50, treeNum, teams);
            if(division == 1){treeNum = 31;}
            if(division == 2){treeNum = 39;}
            if(division == 3){treeNum = 47;}
            if(division == 4){treeNum = 54;}
            createVertices(120, 35, 100, 50, 4, 100, treeNum, teams);
            if(division == 1){treeNum = 15;}
            if(division == 2){treeNum = 19;}
            if(division == 3){treeNum = 23;}
            if(division == 4){treeNum = 27;}
            createVertices(220, 60, 100, 100, 2, 200, treeNum, teams);
            if(division == 1){treeNum = 7;}
            if(division == 2){treeNum = 9;}
            if(division == 3){treeNum = 11;}
            if(division == 4){treeNum = 13;}
            createVertices(320,119,100,200,1,0, treeNum, teams);
            if(division == 1){treeNum = 3;}
            if(division == 2){treeNum = 4;}
            if(division == 3){treeNum = 5;}
            if(division == 4){treeNum = 6;}
            createVertices(420,200,100,20,0,0, treeNum, teams);

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
        private void createVertices(int iX, int iY, int iXO, int iYO, int num, int increment, int treeNum, ArrayList<String> teams) {
            int y = iY;
            //last node on a subtree, only one BracketNode will be added
            if (num == 0 && increment == 0) {
                BracketNode last = new BracketNode("", iX, y - 20, iXO, 20, treeNum);
                bracketNodeMap.put(treeNum, last);
                nodes.add(last);
                getChildren().addAll(new Line(iX,iY,iX+iXO,iY), last);
            } else {
                for (int i = 0; i < num; i++) {
                    Point2D tl = new Point2D(iX, y);
                    Point2D tr = new Point2D(iX + iXO, y);
                    Point2D bl = new Point2D(iX, y + iYO);
                    Point2D br = new Point2D(iX + iXO, y + iYO);
                    //create two BracketNode that will sit on top of the lines then add them to the hashMap to access them with a mouseEvent
                    BracketNode nTop= new BracketNode(teams.get(treeNum), iX, y - 20, iXO, 20, treeNum);
                    bracketNodeMap.put(treeNum, nTop);
                    treeNum++;
                    BracketNode nBottom = new BracketNode(teams.get(treeNum), iX, y + (iYO - 20), iXO, 20, treeNum);
                    bracketNodeMap.put(treeNum, nBottom);
                    treeNum++;
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
     * The BracketNode model for the Graphical display of the "Bracket"
     */
    private class BracketNode extends Pane {
        private String teamName;
        private Rectangle rect;
        private Label name;
        private int treeNum;

        /**
         * Creates a BracketNode with,
         * @param teamName The name if any
         * @param x The starting x location
         * @param y The starting y location
         * @param rX The width of the rectangle to fill pane
         * @param rY The height of the rectangle
         */
        public BracketNode(String teamName, int x, int y, int rX, int rY, int treeNum) {
            this.treeNum = treeNum;
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
         * @return TODO: Is this necessary?
         */
        public String getName() {
            return teamName;
        }

        /**
         * @param teamName TODO: Is this necessary?
         */
        public void setName(String teamName) {
            this.teamName = teamName;
            name.setText(teamName);
        }

        public int getTreeNum(){ return treeNum;}
    }
        /**
         * Reference to the graphical representation of the nodes within the bracket.
         */
        private static ArrayList<BracketNode> nodes;
        /**
         * Used to initiate the paint of the bracket nodes
         */
        private static boolean isTop = true;
        /**
         * Maps the text "buttons" to it's respective grid-pane
         */
        private HashMap<StackPane, Pane> panes;
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
         * Important logical simplification for allowing for code that is easier
         * to maintain.
         */
        private HashMap<BracketNode, Integer> bracketMap = new HashMap<>();
        /**
         *  Reverse of the above;
         */
        private HashMap<Integer,BracketNode> nodeMap = new HashMap<>();
        /**
         * Handles clicked events for BracketNode objects
         */
        private EventHandler<MouseEvent> clicked = mouseEvent -> {
                BracketNode n = (BracketNode) mouseEvent.getSource();
                // TODO THIS IS A CURRENT WORKAROUND FOR MISSING BRACKET ELEMENTS ( Championship game ).
                if (bracketMap.get(n) > 6) {
                        currentBracket.moveTeamUp(bracketMap.get(n));
                        nodeMap.get((bracketMap.get(n) - 1) / 2).setName(n.getName());
                }
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

        private GridPane center;

        /**
         * TODO: Reduce. reuse, recycle!
         * Initializes the properties needed to construct a bracket.
         */
        public BracketPane(Bracket currentBracket) {
                this.currentBracket = currentBracket;

                bracketMap = new HashMap<>();
                nodeMap = new HashMap<>();
                panes = new HashMap<>();
                nodes = new ArrayList<>();
                ArrayList<Root> roots = new ArrayList<>();

                center = new GridPane();

                ArrayList<StackPane> buttons = new ArrayList<>();
                buttons.add(customButton("EAST"));
                buttons.add(customButton("WEST"));
                buttons.add(customButton("MIDWEST"));
                buttons.add(customButton("SOUTH"));
                buttons.add(customButton("FULL"));

                ArrayList<GridPane> gridPanes = new ArrayList<>();

                for (int m = 0; m < buttons.size() - 1; m++) {
                        roots.add(new Root(3 + m));
                        panes.put(buttons.get(m), roots.get(m));
                }

                GridPane fullPane = new GridPane();
                GridPane gp1 = new GridPane();
                gp1.add(roots.get(0), 0, 0);
                gp1.add(roots.get(1), 0, 1);
                GridPane gp2 = new GridPane();
                gp2.add(roots.get(2), 0, 0);
                gp2.add(roots.get(3), 0, 1);
                gp2.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

                fullPane.add(gp1, 0, 0);
                fullPane.add(new Rectangle(400, 400, Color.TRANSPARENT), 1, 0);
                fullPane.add(gp2, 2, 0);
                fullPane.setAlignment(Pos.CENTER);
                panes.put(buttons.get((buttons.size() - 1)), fullPane);

                // Initializes the button grid
                GridPane buttonGrid = new GridPane();
                for (int i = 0; i < buttons.size(); i++)
                        buttonGrid.add(buttons.get(i), 0, i);
                buttonGrid.setAlignment(Pos.CENTER);

                // set default center to the button grid
                this.setCenter(buttonGrid);

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
                                center.add(panes.get(t),0,0);
                                center.setAlignment(Pos.CENTER);
                                setCenter(center);
                        });
                }

        }

        /**
         * Helpful method to retrieve our magical numbers
         * @param root the root node (3,4,5,6)
         * @param pos the position in the tree (8 (16) , 4 (8) , 2 (4) , 1 (2))
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
         * Resets the bracket-display
         */
        public void resetBracket() {
                currentBracket.resetSubtree(0);
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
         * @return true if the current-bracket is complete and the value
         * of finalized is also true.
         */
        public boolean isFinalized() {
                return currentBracket.isComplete() && finalized;
        }

        /**
         * @param isFinalized The value to set finalized to.
         */
        public void setFinalized(boolean isFinalized) {
                finalized = isFinalized && currentBracket.isComplete();
        }

        /**
         * Returns a custom "Button" with specified
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

        /**
         * Creates the graphical representation of a subtree.
         * Note, this is a vague model. TODO: MAKE MODULAR
         */
        private class Root extends Pane {

                private int location;

                public Root(int location) {
                        this.location = location;
                        createVertices(420, 200, 100, 20, 0, 0);
                        createVertices(320, 119, 100, 200, 1, 0);
                        createVertices(220, 60, 100, 100, 2, 200);
                        createVertices(120, 35, 100, 50, 4, 100);
                        createVertices(20, 25, 100, 25, 8, 50);
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
                                getChildren().addAll(new Line(iX, iY, iX + iXO, iY), last);
                                last.setName(currentBracket.getBracket().get(location));
                                bracketMap.put(last,location);
                                nodeMap.put(location, last);
                        } else {
                                ArrayList<BracketNode> aNodeList = new ArrayList<>();
                                for (int i = 0; i < num; i++) {
                                        Point2D tl = new Point2D(iX, y);
                                        Point2D tr = new Point2D(iX + iXO, y);
                                        Point2D bl = new Point2D(iX, y + iYO);
                                        Point2D br = new Point2D(iX + iXO, y + iYO);
                                        BracketNode nTop = new BracketNode("", iX, y - 20, iXO, 20);
                                        aNodeList.add(nTop);
                                        nodes.add(nTop);
                                        BracketNode nBottom = new BracketNode("", iX, y + (iYO - 20), iXO, 20);
                                        aNodeList.add(nBottom);
                                        nodes.add(nBottom);
                                        Line top = new Line(tl.getX(), tl.getY(), tr.getX(), tr.getY());
                                        Line bottom = new Line(bl.getX(), bl.getY(), br.getX(), br.getY());
                                        Line right = new Line(tr.getX(), tr.getY(), br.getX(), br.getY());
                                        getChildren().addAll(top, bottom, right, nTop, nBottom);
                                        isTop = !isTop;
                                        y += increment;
                                }
                                ArrayList<Integer> tmpHelp = helper(location, num);
                                for (int j = 0; j < aNodeList.size(); j++) {
                                        System.out.println(currentBracket.getBracket().get(tmpHelp.get(j)));
                                        aNodeList.get(j).setName(currentBracket.getBracket().get(tmpHelp.get(j)));
                                        bracketMap.put(aNodeList.get(j), tmpHelp.get(j));
                                        nodeMap.put(tmpHelp.get(j),aNodeList.get(j));
                                        System.out.println(bracketMap.get(aNodeList.get(j)));
                                }
                        }

                }
        }

        /** The BracketNode model for the Graphical display of the "Bracket" */
        private class BracketNode extends Pane {
                private String teamName;
                private Rectangle rect;
                private Label name;

                /**
                 * Creates a BracketNode with,
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
                        rect = new Rectangle(rX, rY);
                        rect.setFill(Color.TRANSPARENT);
                        name = new Label(teamName);
                        // setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
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
