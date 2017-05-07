import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javafx.util.StringConverter;
import java.util.ArrayList;
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
    private Map<Bracket, Integer> scores = new HashMap<Bracket, Integer>();
    private static final int MAX_PLAYER_NUMBER = 16;
    private TableView table = new TableView<Bracket>();
    private ObservableList<Bracket> data =
            FXCollections.observableArrayList();

    /**
     * ScoreBoardPane constructor
     */
    public ScoreBoardTable() {
        TableView table = new TableView<Bracket>();
        ObservableList<Bracket> data =
                FXCollections.observableArrayList();
        /**
         * TableColumn userNameCol is the column on the left side of the table.
         * userNameCol.setCellValueFactory() passes the data to the TableView object, which is
         *                                   automatically sorted with the TableColumn.SortType.DESCENDING
         *                                   code line.
         */
        TableColumn userNameCol = new TableColumn("Username");
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
        TableColumn totalPtsCol = new TableColumn("Total Points");
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
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getColumns().setAll(userNameCol, totalPtsCol);
    }

    public TableView start() {
        return table;
    }

    //Ying's code, method addPlayer adds a player to the Bracket
    public void addPlayer(Bracket name, int score) {
        try {
            if (scores == null) {
                scores = new HashMap<Bracket, Integer>();
            }
            //only allow to update the existing player score or add new player if there
            //is less than 16 players
            if (scores.get(name) != null || scores.size() < MAX_PLAYER_NUMBER) {
                scores.put(name, score);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Ying's code, method clears the players from the Bracket
    public void clearPlayers() {
        scores = new HashMap<Bracket, Integer>();
    }

}
