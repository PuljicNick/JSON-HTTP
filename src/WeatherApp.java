/**
 * Created by oj on 4/23/15.
 */

import http_request.HttpRequestTask;
import http_request.HttpResponseHandler;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import json.JSONException;
import json.JSONObject;

public class WeatherApp extends Application {
    private BorderPane root;
    private Stage stage;
    private Scene scene;
    private ChoiceBox<String> cityChooser;
    private Label nameLabel, humidityLabel, descriptionLabel;
    private Text tempText, titleText;
    private ImageView weatherIcon;
    private GridPane weatherGrid;
    private ProgressIndicator progressIndicator;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        root = new BorderPane();
        scene = new Scene(root, 500, 550); //width and height of application
        stage.setScene(scene);
        stage.setTitle("Canadian Weather");  //text for the title bar of the window

        stage.setResizable(false);
        scene.getStylesheets().add("weather.css");

        //initialization:
        titleText = new Text("Canadaian\nWeather");
        cityChooser = new ChoiceBox<>();
        weatherGrid = new GridPane();
        nameLabel = new Label();
        tempText = new Text();
        descriptionLabel = new Label();
        humidityLabel = new Label();
        weatherIcon = new ImageView();
        progressIndicator = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);

        Font.loadFont("Frijole-Regular.ttf",10);
        weatherGrid.addRow(0, new Label("Select a City:"), cityChooser);
        weatherGrid.addRow(1, new Label("City:"), nameLabel);
        weatherGrid.addRow(2, new Label("Weather:"), descriptionLabel);
        weatherGrid.addRow(4, new Label("Humidity:"), humidityLabel);

        cityChooser.getItems().addAll("Toronto", "Vancouver", "Montreal", "Whitehorse", "Calgary", "Winnipeg", "Halifax", "Ottawa", "Yellowknife", "Saskatoon");
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHalignment(HPos.RIGHT);
        weatherGrid.getColumnConstraints().add(0, cc);
        weatherIcon.setPreserveRatio(true);
        weatherIcon.setFitWidth(280);

        root.setTop(new HBox(titleText));
        root.setCenter(new HBox(weatherGrid));
        root.setBottom(new HBox(new StackPane(weatherIcon, tempText)));

        cityChooser.getSelectionModel().selectFirst();
        cityChooser.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> getWeatherData());

        getWeatherData();

        stage.show();
    }

    private void getWeatherData() {
        String city = cityChooser.getItems().get(cityChooser.getSelectionModel().getSelectedIndex());
        System.out.println(city);
        root.setCenter(progressIndicator);
        root.getBottom().setVisible(false);

        HttpRequestTask.get("http://api.openweathermap.org/data/2.5/weather?q=" +city + "&units=metric", new HttpResponseHandler() {
            @Override
            public void handle(String response) {
                System.out.println(response); //just to see

                if (response != null) {
                    try {
                        JSONObject jo = new JSONObject(response);
                        nameLabel.setText(jo.getString("name"));

                        JSONObject weather = jo.getJSONArray("weather").getJSONObject(0);

                        //   weatherIcon.setImage(new Image("http://openweathermap.org/img/w/" + weather.getString("icon") + ".png"));

                        descriptionLabel.setText(weather.getString("main") + " - " + weather.getString("description"));


                        JSONObject main = jo.getJSONObject("main");
                        System.out.println(main.names());
                        tempText.setText(main.getInt("temp") + "\u00b0");
                        humidityLabel.setText(main.getDouble("humidity")+"");

                        weatherIcon.setImage(new Image("weather_icons/" + weather.getString("icon") + ".png"));
                        root.setCenter(new HBox(weatherGrid));
                        root.getBottom().setVisible(true);

                        FadeTransition ft = new FadeTransition(new Duration(1500), root.getBottom());
                        ft.setFromValue(0);
                        ft.setToValue(1);
                        ft.play();
                    } catch (JSONException e) {
                        nameLabel.setText("Data Error");
                    }
                } else titleText.setText("Connection\nError");
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}