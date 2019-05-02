import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.File;

import static java.awt.Color.white;
import static javafx.scene.text.FontWeight.BOLD;


public class Client5 extends Application{

    NetworkConnection connection;
    MediaPlayer mediaPlayer;
    Stage myStage;
    Scene scene1, scene2;

    BorderPane pane;
    TextField port, IP, playerGuess, playerName;
    TextArea numbersGuessed, playersConnected;
    Button instr, submit, quit, playAgain, connect, join, mute, unmute;
    Text welcome, winner, guess, numsGuessed, mysteryNum, info, enterName;

    int portNumber;
    String hostName;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Client GUI");
        
        String path = new File("src/music5.mp3").getAbsolutePath();
        Media musicFile = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(musicFile);
        mediaPlayer.setAutoPlay(false);
        mediaPlayer.setVolume(0.1);

        myStage = primaryStage;
        primaryStage.setResizable(false);

        // Set up the welcome text
        welcome = new Text("Welcome to Mystery Number!");
        Font welcfont = new Font("Verdana",25);
        welcome.setFont(welcfont);
        welcome.setStyle("-fx-font-weight: bold");

        // Use this to set the gui for which player they are
        Text playerNum = new Text("You are player: ");

        Font playFont = new Font("Verdana",18);
        playerNum.setFont(playFont);

        enterName = new Text("Enter Name:");
        enterName.setStyle("-fx-font-weight: bold");
        
        playerName = new TextField("Math Wiz");
        playerName.setDisable(true);
        // Heemani: Adding proper placement for playerName Textfield
        //playerName.setMaxWidth(150);
        //playerName.setTranslateY(100);
        //playerName.setTranslateX(76);
        info = new Text();

        // Set up the pane
        pane = new BorderPane();
        pane.setPadding(new Insets(60));

        scene1 = new Scene(pane, 688,688);

        // Set up the central VBox
        numsGuessed = new Text("The numbers guessed so far:");
        numsGuessed.setStyle("-fx-font-weight: bold");
        numsGuessed.setTranslateY(20);
        numsGuessed.setTranslateX(50);
        numbersGuessed = new TextArea();
        numbersGuessed.setTranslateY(20);
        numbersGuessed.setTranslateX(50);
        // Heemani: added wrapText feature
        numbersGuessed.setWrapText(true);
        numbersGuessed.setPrefHeight(200);
        numbersGuessed.setMaxWidth(200);
        numbersGuessed.setEditable(false);

        winner = new Text("No Winners Yet...");
        winner.setStyle("-fx-font-weight: bold");
        //winner.setTranslateY(20);

        // Code for setting a Background Image
        BackgroundImage myBI= new BackgroundImage(new Image("math4.jpg",700,700,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        pane.setBackground(new Background(myBI));

        //*********** Code for Instructions Scene ***********************

        BorderPane pane2 = new BorderPane();
        Text instrTop = new Text("Mystery Number Game Instructions");
        instrTop.setFont(new Font("Verdana",25));
        instrTop.setStyle("-fx-font-weight: bold");
        instrTop.setTranslateY(100);
        instrTop.setTranslateX(100);

        Text gameInstructions = new Text("Once there are 4 players connected, you may begin\nto enter numbers" +
                " in your guessing box Hit SUBMIT\nto send your guess to the server. You will also see a \nlist" +
                " of numbers that have been guessed by other\nplayers that updates in real time. Use your process\nof" +
                " elimination skills to be the first one to guess\nthe number. Good luck!");

        gameInstructions.setStyle("-fx-font-weight: bold");
        gameInstructions.setFont(new Font("Verdana",18));
        gameInstructions.setTranslateX(100);
        gameInstructions.setTranslateY(175);

        Button back = new Button("BACK TO GAME");
        back.setTextFill(Color.WHITE);
        back.setStyle("-fx-background-color: #000000");
        back.setTranslateY(250);
        back.setTranslateX(300);
        back.setOnAction(e -> myStage.setScene(scene1));


        VBox Instructions = new VBox(10,instrTop,gameInstructions,back);

        pane2.setBackground(new Background(myBI));
        pane2.setCenter(Instructions);

        scene2 = new Scene(pane2,700,700);

        //************ End Code for Instructions scene ***********

        VBox welcomeBanner = new VBox(5, welcome, playerNum);

        pane.setTop(welcomeBanner);

        mysteryNum = new Text("Mystery Num: ???");
        mysteryNum.setFont(playFont);

        Text displayNum = new Text("           ???"); // Set this to actual number at the end
        displayNum.setFont(playFont);

        mysteryNum.setTranslateY(30);
        mysteryNum.setTranslateX(50);
        displayNum.setTranslateY(30);
        displayNum.setTranslateX(50);

        Text portEnter = new Text("Enter Port or use Default:");
        portEnter.setStyle("-fx-font-weight: bold");
        portEnter.setFont(new Font("Verdana",14));
        port = new TextField("5555");

        Text ipEnter = new Text("Enter IP or use Default:");
        ipEnter.setStyle("-fx-font-weight: bold");
        ipEnter.setFont(new Font("Verdana",14));
        IP = new TextField("localhost");

        Text getInstr = new Text("Click below for instructions");
        getInstr.setStyle("-fx-font-weight: bold");
        instr = new Button("INSTRUCTIONS");
        instr.setTextFill(Color.WHITE);
        instr.setStyle("-fx-background-color: #000000");
        instr.setOnAction(e -> myStage.setScene(scene2));

        quit = new Button("QUIT");
        quit.setTextFill(Color.WHITE);
        quit.setStyle("-fx-background-color: #000000");
        
        mute = new Button("MUTE");
        mute.setTextFill(Color.WHITE);
        mute.setStyle("-fx-background-color: #000000");
        mute.setDisable(true);
        
        unmute = new Button("UNMUTE");
        unmute.setTextFill(Color.WHITE);
        unmute.setStyle("-fx-background-color: #000000");

        playAgain = new Button("PLAY AGAIN");
        playAgain.setTextFill(Color.WHITE);
        playAgain.setStyle("-fx-background-color: #000000");
        
        join = new Button("JOIN");
        join.setTextFill(Color.WHITE);
        join.setStyle("-fx-background-color: #000000");
        join.setDisable(true);

        Text onlinePlayers = new Text("Players currently online:");
        onlinePlayers.setStyle("-fx-font-weight: bold");
        playersConnected = new TextArea();
        playersConnected.setMaxWidth(200);;
        playersConnected.setMinHeight(65);;
        playersConnected.setEditable(false);

        connect = new Button("CONNECT");
        connect.setTextFill(Color.WHITE);
        connect.setStyle("-fx-background-color: #000000");
        VBox options = new VBox(10,ipEnter,IP,portEnter,port,connect,getInstr,instr,
                onlinePlayers,playersConnected,winner);

        pane.setRight(options);

        // User Interface/Interaction at the bottom of the page
        guess = new Text("Your Guess: ");
        guess.setStyle("-fx-font-weight: bold");
        playerGuess = new TextField();
        submit = new Button("SUBMIT");
        submit.setTextFill(Color.WHITE);
        submit.setStyle("-fx-background-color: #000000");
        
        HBox userName = new HBox(10,enterName,playerName,join,mute,unmute,quit);
        HBox userStuff = new HBox(10,guess,playerGuess,submit, playAgain);
        VBox bottomStuff = new VBox(10,info,userName,userStuff);

        pane.setBottom(bottomStuff);

        VBox paneCenter = new VBox(10,numsGuessed,numbersGuessed,mysteryNum,displayNum);
        pane.setCenter(paneCenter);

        playerGuess.setDisable(true);
        submit.setDisable(true);

        connect.setOnAction(event-> {
        	playerName.setDisable(false);
        	join.setDisable(false);
        	
            try{
                portNumber = Integer.parseInt(port.getText());
                hostName = IP.getText();

                connection = new NetworkConnection(hostName, portNumber, data-> {
                    Platform.runLater(() -> {

                            if (data.toString().startsWith("You are player: ")) {
                                playerNum.setText(data.toString());
                            }

                            if(data.toString().startsWith("Waiting"))
                            {
                                info.setText(data.toString());
                            }

                            if (data.toString().startsWith("Players Online: \n")) {
                                playersConnected.setText(data.toString());
                            }

                            if (data.toString().equals("4 players are now connected!! \n Begin playing the game.")) {
                                info.setText(data.toString());
                                playerGuess.setDisable(false);
                                submit.setDisable(false);
                            }
                            if (data.toString().startsWith("Numbers Guessed:")) {
                                numbersGuessed.setText(data.toString());
                            }

                            if (data.toString().startsWith("Mystery Number:")) {
                                mysteryNum.setText(data.toString());
                                mysteryNum.setFont(playFont);
                            }

                            if (data.toString().startsWith("WINNER")) {
                                winner.setText(data.toString());
                                playerGuess.setDisable(true);
                                submit.setDisable(true);
                            }
                    });
                });
                connection.startConn();
            }
            catch(Exception e) {
                System.out.println("Enter the correct port and ip address.");
            }
        });

        submit.setOnAction(event-> {
            connection.send(playerGuess.getText());
        });

        join.setOnAction(event -> {
        	connection.send("* " + playerName.getText());
        });
        
        mute.setOnAction(event -> {
        	mediaPlayer.pause();
        	mute.setDisable(true);
        	unmute.setDisable(false);
        }); 
        
        unmute.setOnAction(event -> {
        	mediaPlayer.play();
        	mute.setDisable(false);
        	unmute.setDisable(true);
        }); 
        
        quit.setOnAction(event-> {
            connection.send("quit");
            connection.closeConn();         //Closes connection and ends client GUI.
            System.exit(0);
        });

        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception{
        connection.send("quit");
        connection.closeConn();
    }
}