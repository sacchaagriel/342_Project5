import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public class Server5 extends Application {

    NetworkConnection connection;
    private TextArea guesses = new TextArea();
    private Text guessesLabel, welcomeText;
    private Button ServerOn;
    private Text mysteryNumLabel;//label
    private TextArea mysteryNum; //the actual number for mystery number
    private Text clientNamesLabel;
    private TextArea playerNames;
    private Text winnerLabel;
    private TextArea winner;
    private Text portLabel;
    private TextField portNumber;
    int portNum;
    String pChoices;
    String players;
    Boolean isWinner = false;

    private Parent createContent() {
        try {
                InputStream inputStream = getClass().getResourceAsStream("CS.wav");
                AudioStream audioStream = new AudioStream(inputStream);
                AudioPlayer.player.start(audioStream);
            } catch (Exception e) {
                // handle exception
        }
        
        ServerOn = new Button("Server On");
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
        mysteryNum.setEditable(false);
        mysteryNum.setMaxSize(10,10);

        portLabel = new Text("Port Number: ");
        portLabel.setFill(Color.LIMEGREEN);
        portLabel.setFont(new Font("Arial Rounded MT Bold",20));
        //portNumber = new TextArea();
        portNumber = new TextField("5555");
        portNumber.setPrefSize(120,20);

        guessesLabel = new Text("Guesses: ");
        guessesLabel.setFill(Color.RED);
        guessesLabel.setFont(new Font("Arial Rounded MT Bold",36));
        guesses.setPrefSize(200,300);
        // Heemani: Changed it so text wraps around
        guesses.setWrapText(true);
        guesses.setEditable(false);

        clientNamesLabel = new Text("Players: ");
        clientNamesLabel.setFill(Color.BLUE);
        clientNamesLabel.setFont(new Font("Arial Rounded MT Bold",36));
        playerNames = new TextArea("No players connected!!");
        playerNames.setPrefSize(150,250);
        playerNames.setEditable(false);

        winnerLabel = new Text("Winner: ");
        winnerLabel.setFill(Color.YELLOW);
        winnerLabel.setFont(new Font("Arial Rounded MT Bold",36));
        winner = new TextArea();
        winner.setPrefSize(150,20);
        winner.setEditable(false);

        System.out.println(Font.getFontNames());

        BackgroundImage myBI= new BackgroundImage(new Image("bgg.png",800,700,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        BorderPane root = new BorderPane();

        //welcome box and port
     //   HBox port = new HBox(10, portNumber, ServerOn);
        HBox port = new HBox(10,portLabel, portNumber, ServerOn);
        VBox left = new VBox(20, clientNamesLabel, playerNames);
        VBox top = new VBox(30,welcomeText, port, left);

        VBox right = new VBox(20, guessesLabel,guesses);

        VBox center = new VBox(10, mysteryNumLabel,mysteryNum);
        center.setAlignment(Pos.BOTTOM_CENTER);

        
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
     //   root.setCenter(port);
        ServerOn.setOnAction(event-> {
            portNum = Integer.parseInt(portNumber.getText());
            portNumber.clear();


            try {
                //data is what we received from the other end.
                connection = new NetworkConnection(portNum, data -> {           //data->{ } defines the code for accept()
                    Platform.runLater(() -> {   //gives the control back to the UI thread
                        if((NetworkConnection.numClients >= 4) && (!isWinner)) {
                        if (data.toString().startsWith("*** ")) {
                            isWinner = true;
                            winner.setText(data.toString());
                            for (int i = 0; i < connection.clientList.size(); i++) {
                                connection.clientList.get(i).sendMsg("WINNER: " + data);
                                connection.clientList.get(i).sendMsg("Mystery Number: " + connection.mystery);
                                mysteryNum.setText(connection.mystery);
                            }
                        }
                    }

                    players = "Players Online: \n";
                    if(connection.clientList.size() > 0) {
                        players += connection.clientNames.get(0);
                        playerNames.setText(players);
                        for (int i = 1; i < connection.clientNames.size(); i++) {
                            players += "\n" + connection.clientNames.get(i);
                            playerNames.setText(players);
                        }
                    }
                    
                    if(connection.clientList.size() == 0) {
                        playerNames.setText("No clients connected.");
                    }

                    for (int i = 0; i < connection.clientList.size(); i++) {
                        connection.clientList.get(i).sendMsg(players);
                    }

                    if((NetworkConnection.numClients >= 4) && (connection.clientChoices.size() > 0)) {
                        pChoices = "Numbers Guessed: \n";
                        pChoices += connection.clientChoices.get(0);
                        guesses.setText(pChoices);
                        for (int i = 1; i < connection.clientChoices.size(); i++) {
                            pChoices += ", " + connection.clientChoices.get(i);
                            guesses.setText(pChoices);
                        }
                    }

                    for (int i = 0; i < connection.clientList.size(); i++) {
                        connection.clientList.get(i).sendMsg(pChoices);
                    }

                    });
                });

                connection.startConn();

            }
            catch (Exception e) {
                System.out.println("Exception in Server Gui");
            }
        });
        return root;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("SERVER");
        primaryStage.setScene(new Scene(createContent(), 760, 500));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception{
        connection.closeConn();
    }


}
