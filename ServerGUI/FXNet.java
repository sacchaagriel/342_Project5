import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.effect.Reflection;
import javafx.scene.effect.Bloom;

public class FXNet extends Application {

    private boolean isServer = true;

    private NetworkConnection conn = createServer();
    private TextArea guesses = new TextArea();
    private Text guessesLabel, welcomeText;
    private Text mysteryNumLabel;//label
    private TextArea mysteryNum; //the actual number for mystery number
    private Text clientNamesLabel;
    private TextArea clientNames;
    private Text winnerLabel;
    private TextArea winner;
    private Text portLabel;
    private TextArea portNum;


    private Parent createContent() {

        Reflection reflection = new Reflection();
        reflection.setFraction(0.7);
        welcomeText = new Text("WELCOME TO MyStErY NUMBER!");
        welcomeText.setEffect(reflection);
        welcomeText.setFill(Color.GHOSTWHITE);
        welcomeText.setFont(new Font("Comic Sans MS Bold",40));

        mysteryNumLabel = new Text("Mystery Number: ");
        mysteryNumLabel.setFill(Color.GHOSTWHITE);
        mysteryNumLabel.setFont(new Font("Comic Sans MS Bold",30));
        Bloom bloom = new Bloom();
        bloom.setThreshold(.1);
        mysteryNumLabel.setEffect(bloom);
        mysteryNum = new TextArea();
        mysteryNum.setMaxSize(10,10);

        portLabel = new Text("Port Number: ");
        portLabel.setFill(Color.PURPLE);
        portLabel.setFont(new Font("Comic Sans MS Bold",20));
        portNum = new TextArea();
        portNum.setPrefSize(120,20);

        guessesLabel = new Text("Guesses: ");
        guessesLabel.setFill(Color.PURPLE);
        guessesLabel.setFont(new Font("Comic Sans MS Bold",36));
        guesses.setPrefSize(150,150);

        clientNamesLabel = new Text("Players: ");
        clientNamesLabel.setFill(Color.PURPLE);
        clientNamesLabel.setFont(new Font("Comic Sans MS Bold",36));
        clientNames = new TextArea();
        clientNames.setPrefSize(150,150);

        winnerLabel = new Text("Winner: ");
        winnerLabel.setFill(Color.DARKGREEN);
        winnerLabel.setFont(new Font("Comic Sans MS Bold",36));
        winner = new TextArea();
        winner.setPrefSize(150,20);



        BackgroundImage myBI= new BackgroundImage(new Image("bgg.png",800,700,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        BorderPane root = new BorderPane();


        //welcome box and port
        HBox port = new HBox(10,portLabel, portNum);
        VBox top = new VBox(30,welcomeText, port);

        VBox right = new VBox(20, guessesLabel,guesses);

        VBox center = new VBox(10, mysteryNumLabel,mysteryNum);
        center.setAlignment(Pos.BOTTOM_CENTER);

        VBox left = new VBox(20, clientNamesLabel, clientNames);
        HBox bottom = new HBox(10, winnerLabel,winner);

        VBox bot = new VBox(100,center, bottom);

        BorderPane pane = new BorderPane();
        pane.setCenter(bot);
        root.setTop(top);
        root.setBottom(pane);

        root.setRight(right);
        root.setLeft(left);


        root.setBackground(new Background(myBI));
        root.setPadding(new Insets(0,50,10,50));
        root.setPrefSize(800, 700);


        return root;



    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();

    }

    @Override
    public void init() throws Exception{
        conn.startConn();
        }

    @Override
    public void stop() throws Exception{
        conn.closeConn();
    }

    private Server createServer() {
        return new Server(5555, data-> {
            Platform.runLater(()->{
                guesses.appendText(data.toString() + "\n");
            });
        });
    }


}
