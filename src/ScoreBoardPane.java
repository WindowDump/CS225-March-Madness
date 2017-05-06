//package marchmadness;

import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.Callback;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sarah on 5/2/17.
 * @author Sarah Higgins and Ying Sun
 * ScoreBoardPane class is the class the displays the Scoreboard from the Main GUI.
 * It shows all of the Player's names and their scores.
 */
public class ScoreBoardPane{

    /**
     * attributes contributed by Ying Sun
     */
    private static Map<String, Integer> scores = new HashMap<String, Integer>();
    private static final int MAX_PLAYER_NUMBER = 16;

    /**
     * attributes contributed by Sarah Higgins
     */
    public static final String Column1MapKey = "PlayerName";
    public static final String Column2MapKey = "Player Score";



    

   
    public TableView _start() {
  

        final Label label = new Label("March Madness");
        label.setFont(new Font("Arial", 20));

        /**
         * TableColumn userNameCol is the column on the left side of the table
         */
        TableColumn<Map, String> userNameCol = new TableColumn<>("Username");
        userNameCol.setMinWidth(140);
        userNameCol.setMaxWidth(140);
        userNameCol.setStyle("-fx-border-width: 3px");
        userNameCol.setCellValueFactory(new MapValueFactory(Column1MapKey));
        userNameCol.setSortable(false);
        userNameCol.setSortType(TableColumn.SortType.DESCENDING); //sorts column from highest to lowest

        /**
         * TableColumn totalPtsCol is the column on the right side of the table
         */
        TableColumn<Map, String> totalPtsCol = new TableColumn<>("Total Points");
        totalPtsCol.setMinWidth(140);
        totalPtsCol.setMaxWidth(140);
        totalPtsCol.setStyle("-fx-border-width: 3px");
        totalPtsCol.setCellValueFactory(new MapValueFactory(Column2MapKey));
        totalPtsCol.setSortable(false);
        totalPtsCol.setSortType(TableColumn.SortType.DESCENDING); //sorts column from highest to lowest

        /**
         * TableView table_view is what the user sees in the GUI. This creates the table.
         */
        TableView table_view = new TableView<>(generateDataInMap());
        table_view.setEditable(true);
        table_view.getSelectionModel().setCellSelectionEnabled(true);
        table_view.getColumns().setAll(userNameCol, totalPtsCol);
        Callback<TableColumn<Map, String>, TableCell<Map, String>> cellFactoryForMap = new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
            @Override
            public TableCell call(TableColumn p) {
                return new TextFieldTableCell(new StringConverter() {
                    @Override
                    public String toString(Object t) {
                        return t.toString();
                    }
                    @Override
                    public Object fromString(String string) {
                        return string;
                    }
                });
            }
        };
        userNameCol.setCellFactory(cellFactoryForMap);
        totalPtsCol.setCellFactory(cellFactoryForMap);

        final VBox vbox = new VBox();
        vbox.setSpacing(6);
        vbox.setPadding(new Insets(12, 0, 0, 12));
        vbox.getChildren().addAll(label, table_view);

        return table_view;
    }


    private ObservableList<Map> generateDataInMap() {
        int max = 16;
        ObservableList<Map> allData = FXCollections.observableArrayList();
        for (int i = 1; i < max; i++) {
            Map<String, String> scoresRow = new HashMap<>();

            String value1 = "player" + i;
            String value2 = "score" + i;

            scoresRow.put(Column1MapKey, value1);
            scoresRow.put(Column2MapKey, value2);

            allData.add(scoresRow);
        }
        return allData;
    }

    //Ying's code, method addPlayer adds a player to the Bracket
    public void addPlayer(String name, int score) {
        try {
            if (scores == null) {
                scores = new HashMap<String, Integer>();
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
        scores = new HashMap<String, Integer>();
    }



}
