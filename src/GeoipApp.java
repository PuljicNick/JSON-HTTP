import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by puljicn on 4/28/15.
 */
public class GeoipApp extends Application {
    private BorderPane root;
    private Stage stage;
    private Scene scene;
    private Label cityLabel, stateLabel, latitudeLabel, longitudeLabel, ISPLabel;
    private Text IPText, titleText;
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
        progressIndicator = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
