import http_request.HttpRequestTask;
import http_request.HttpResponseHandler;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import json.JSONException;
import json.JSONObject;

/**
 * Created by puljicn on 4/28/15.
 */
public class GeoipApp extends Application {
    private BorderPane root;
    private Stage stage;
    private Scene scene;
    private Label cityLabel, countryLabel, latitudeLabel, longitudeLabel, ISPLabel, IPLabel;
    private Text titleText;
    private GridPane geoGrid;
    private ProgressIndicator progressIndicator;


    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        root = new BorderPane();
        scene = new Scene(root, 500, 550); //width and height of application
        stage.setScene(scene);
        stage.setTitle("GEO IP");  //text for the title bar of the window
        stage.setResizable(false);
        scene.getStylesheets().add("geoip.css");

        //initialization:
        titleText = new Text("GEO IP");
        cityLabel = new Label();
        countryLabel = new Label();
        latitudeLabel = new Label();
        longitudeLabel = new Label();
        ISPLabel = new Label();
        geoGrid = new GridPane();
        IPLabel = new Label();
        progressIndicator = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);

        geoGrid.addRow(0, new Label("Your IP:"), IPLabel);
        geoGrid.addRow(1, new Label("Your Country:"), countryLabel);
        geoGrid.addRow(3, new Label("Your Latitude"), latitudeLabel);
        geoGrid.addRow(4, new Label("Your Longitude:"), longitudeLabel);
        geoGrid.addRow(2, new Label("Your City:"), cityLabel);
        geoGrid.addRow(5, new Label("Your ISP:"), ISPLabel);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setHalignment(HPos.RIGHT);
        geoGrid.getColumnConstraints().add(0, cc);

        root.setTop(new HBox(titleText));
        root.setCenter(new HBox(geoGrid));

        getGeoIP();


        stage.show();
    }

    private void getGeoIP() {
        //root.setCenter(progressIndicator);

        HttpRequestTask.get("http://www.telize.com/geoip", new HttpResponseHandler() {
            @Override
            public void handle(String response) {
                System.out.println(response);

                if ( response != null ){
                    try {
                        JSONObject jo = new JSONObject(response);
                        JSONObject main = jo.getJSONObject("main");
                        System.out.println(main.names());
                        IPLabel.setText(main.getDouble("ip") + "");

                        root.setCenter(new HBox(geoGrid));

                        FadeTransition ft = new FadeTransition(new Duration(1500), root.getBottom());
                        ft.setFromValue(0);
                        ft.setToValue(1);
                        ft.play();
                    } catch (JSONException e) {
                        IPLabel.setText("Data Error");
                    }
                } else titleText.setText("Connection\nError");
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
