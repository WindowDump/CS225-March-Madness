import javafx.application.Application;
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

import javax.security.auth.callback.Callback;
import java.util.HashMap;
import java.util.Map;

import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;

/**
 * Created by sarah on 5/2/17.
 */
public class ScoreBoardPane extends Application {

    //attributes contributed by Ying
    private static Map<String, Integer> scores = new HashMap<String, Integer>();
    private static final int MAX_PLAYER_NUMBER = 16;

    //attributes contributed by Sarah
    //private TableView table = new TableView();
    public static final String Column1MapKey = "A";
    public static final String Column2MapKey = "B";

    public ScoreBoardPane() {

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("March Madness Bracket");
        stage.setWidth(300);
        stage.setHeight(500);

        final Label label = new Label("March Madness");
        label.setFont(new Font("Arial", 20));

        TableColumn<Map, String> userNameCol = new TableColumn<>("Username");
        userNameCol.setMinWidth(130);
        userNameCol.setMaxWidth(130);
        userNameCol.setCellFactory(new MapValueFactory(Column1MapKey));


        TableColumn<Map, String> totalPtsCol = new TableColumn<>("Total Points");
        totalPtsCol.setMinWidth(130);
        totalPtsCol.setMaxWidth(130);
        totalPtsCol.setCellFactory(new MapValueFactory(Column2MapKey));


        TableView table_view = new TableView<>();

        table_view.setEditable(true);
        table_view.getSelectionModel().setCellSelectionEnabled(true);
        table_view.getColumns().setAll(userNameCol, totalPtsCol);
        /*
        javafx.util.Callback<TableColumn<Map, String>, TableCell<Map, String>>
                cellFactoryForMap = new javafx.util.Callback<TableColumn<Map, String>,
                TableCell<Map, String>>() {
            @Override
            public TableCell call(TableColumn p) {
                return new TextFieldTableCell(new StringConverter() {
                    @Override
                    public String toString() {
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
        */

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table_view);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

    /*
    private ObservableList<Map> generateDataInMap() {
        int max = 16;
        ObservableList<Map> allData = FXCollections.observableArrayList();
        for (int i = 1; i < max; i++) {
            Map<String, String> dataRow = new HashMap<>();

            String value1 = "A" + i;
            String value2 = "B" + i;

            dataRow.put(Column1MapKey, value1);
            dataRow.put(Column2MapKey, value2);

            allData.add(dataRow);
        }
        return allData;
    } */

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
