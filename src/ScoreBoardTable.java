import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sarah on 5/2/17.
 * @author Sarah Higgins and Ying Sun
 * ScoreBoardPane class is the class the displays the Scoreboard from the Main GUI.
 * It shows all of the Player's names and their scores.
 */
public class ScoreBoardTable {

    /**
     * attributes
     */
    private Map<Bracket, Integer> scores;
    private static final int MAX_PLAYER_NUMBER = 16;
    private TableView<Bracket> table;
    private ObservableList<Bracket> data;

    /**
     * @author Sarah Higgins
     * ScoreBoardPane constructor, makes the TableView object containing
     * information about a Bracket Player and their total score
     */
    @SuppressWarnings("unchecked")
    public ScoreBoardTable() {
        table = new TableView<>();
        data = FXCollections.observableArrayList();
        scores = new HashMap<>();

        /**
         * TableColumn userNameCol is the column on the left side of the table.
         * userNameCol.setCellValueFactory() passes the data to the TableView object, which is
         *                                   automatically sorted with the TableColumn.SortType.DESCENDING
         *                                   code line.
         */
        TableColumn<Bracket, String> userNameCol = new TableColumn<>("Username");
        userNameCol.setMinWidth(140);
        userNameCol.setMaxWidth(140);
        userNameCol.setStyle("-fx-border-width: 3px");
        userNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Bracket, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Bracket, String> b) {
                return new SimpleStringProperty(b.getValue().getPlayerName());
            }
        });
        userNameCol.setSortable(false);
        userNameCol.setSortType(TableColumn.SortType.DESCENDING); //sorts column from highest to lowest

        /**
         * TableColumn totalPtsCol is the column on the right side of the table
         * totalPtsCol.setCellValueFactory() passes the data to the TableView object, which is
         *                                   automatically sorted with the TableColumn.SortType.DESCENDING
         *                                   code line.
         */
        TableColumn<Bracket, Number> totalPtsCol = new TableColumn<>("Total Points");
        totalPtsCol.setMinWidth(140);
        totalPtsCol.setMaxWidth(140);
        totalPtsCol.setStyle("-fx-border-width: 3px");
        totalPtsCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Bracket, Number>, ObservableValue<Number>>() {
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<Bracket, Number> b) {
                return new SimpleIntegerProperty(scores.get(b.getValue()));
            }
        });
        totalPtsCol.setSortable(false);
        totalPtsCol.setSortType(TableColumn.SortType.DESCENDING); //sorts column from highest to lowest

        /**
         * TableView table_view is what the user sees in the GUI. This creates the table.
         *
         */
        table.setItems(data);
        table.setEditable(false);
        //table.getSelectionModel().setCellSelectionEnabled(true);
        table.getColumns().setAll(userNameCol, totalPtsCol);
    }

    /**
     * method start
     * @return the TableView object containing all of the data for the Bracket
     */
    public TableView<Bracket> start() {
        return table;
    }

    /**
     * @author Ying Sun and Sarah Higgins
     * method addPlayer adds Bracket players to the Table
     * @param name is the Player's name
     * @param score is the Player's current score
     */
    public void addPlayer(Bracket name, int score) {
        try {
            if (scores == null) {
                scores = new HashMap<Bracket, Integer>();
            }
            //only allow to update the existing player score or add new player if there
            //is less than 16 players
            if (scores.get(name) != null || scores.size() < MAX_PLAYER_NUMBER) {
                scores.put(name, score);
                data.add(name);
                //System.out.println("added: " + name.getPlayerName() + " " + score);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @author Ying Sun and Sarah Higgins
     * method clearPlayers erases all of the players in the table from view
     */
    public void clearPlayers() {
        scores = new HashMap<Bracket, Integer>();
        data = FXCollections.observableArrayList();
    }
}