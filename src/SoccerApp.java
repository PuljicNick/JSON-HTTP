/**
 * Created by oj on 4/23/15.
 */

import http_request.HttpRequestTask;
import http_request.HttpResponseHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import json.JSONArray;
import json.JSONObject;

public class SoccerApp extends Application {
    private BorderPane root;
    private Stage stage;
    private Scene scene;
    private ChoiceBox<String> leagueChooser;
    private GridPane leagueTablePane;
    private ProgressIndicator progressIndicator;
    private Text titleText;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        root = new BorderPane();
        scene = new Scene(new ScrollPane(root), 500, 650); //width and height of application
        stage.setScene(scene);
        stage.setTitle("League Tables");  //text for the title bar of the window

        scene.getStylesheets().add("soccer.css");

        //initializations:
        titleText = new Text("Soccer League Tables");
        leagueChooser = new ChoiceBox<>();
        leagueTablePane = new GridPane();
        progressIndicator = new ProgressIndicator();

        root.setTop(new VBox(new HBox(titleText), new HBox(leagueChooser)));
        root.setCenter(progressIndicator);
        leagueChooser.setVisible(false);
        titleText.setWrappingWidth(scene.getWidth() - 20);

        HttpRequestTask.get("http://api.football-data.org/alpha/soccerseasons", new HttpResponseHandler() {
            @Override
            public void handle(String response) {
                if (response != null) {
                    System.out.println(response);
                    JSONArray a = new JSONArray(response);

                    for (int i = 0; i < a.length(); i++) {
                        JSONObject jo = (JSONObject) a.get(i);
                        leagueChooser.getItems().add(jo.getString("caption").trim()); //.substring(jo.getString("caption").indexOf(" ") + 1)
                    }

                    //      leagueChooser.getSelectionModel().selectFirst();
                    leagueChooser.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> getLeagueData());

                    leagueChooser.setVisible(true);
                    root.setCenter(null);
                } else {
                    titleText.setText("Connection Error");
                }
            }
        });

        stage.show();
    }

    private void getLeagueData() {
        root.setCenter(progressIndicator);
        int league = leagueChooser.getSelectionModel().getSelectedIndex() + 351;
        leagueChooser.setVisible(false);
        titleText.setText(leagueChooser.getItems().get(leagueChooser.getSelectionModel().getSelectedIndex()));

        HttpRequestTask.get("http://api.football-data.org/alpha/soccerseasons/" + league + "/leagueTable", new HttpResponseHandler() {
            @Override
            public void handle(String response) {
                System.out.println(response);

                leagueTablePane.getChildren().clear();

                JSONObject jo = new JSONObject(response);
                JSONArray a = jo.getJSONArray("standing");

                leagueTablePane.addRow(0, new HBox(new Label("Psn")), new HBox(new Label("Team")), new HBox(new Label("Pld")), new HBox(new Label("Diff")), new HBox(new Label("Pts")));


                for (int i = 0; i < a.length(); i++) {
                    JSONObject team = a.getJSONObject(i);
                    leagueTablePane.addRow(i + 1, new HBox(new Label(i + 1 + "")),
                            new HBox(new Label(team.getString("teamName"))),
                            new HBox(new Label(team.getInt("playedGames") + "")),
                            new HBox(new Label(team.getInt("goalDifference") + "")),
                            new HBox(new Label(team.getInt("points") + ""))
                    );
                    root.setCenter(new HBox(leagueTablePane));
                }

                leagueChooser.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}