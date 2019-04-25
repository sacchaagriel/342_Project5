import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.awt.*;

import static java.awt.Color.white;
import static javafx.scene.text.FontWeight.BOLD;


public class Client5 extends Application{


    Stage myStage;
    Scene scene1, scene2;

    BorderPane pane;

    TextField port, IP, playerGuess, playersOnline;

    TextArea numbersGuessed;

    Button instr, submit, quit,  playAgain;

    Text welcome, winner, guess, numsGuessed, mysteryNum;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Client GUI");

        myStage = primaryStage;

        // Set up the welcome text
        welcome = new Text("Welcome to Mystery Number!");
        Font welcfont = new Font("Verdana",25);
        welcome.setFont(welcfont);
        welcome.setStyle("-fx-font-weight: bold");

        // Use this to set the gui for which player they are
        Text playerNum = new Text("You are player: ");

        Font playFont = new Font("Verdana",18);
        playerNum.setFont(playFont);



        // Set up the pane
        pane = new BorderPane();
        pane.setPadding(new Insets(70));

        scene1 = new Scene(pane, 700,700);

       // Set up the central VBox
        numsGuessed = new Text("The numbers guessed so far:");
        numsGuessed.setStyle("-fx-font-weight: bold");
        numsGuessed.setTranslateY(20);
        numsGuessed.setTranslateX(50);
        numbersGuessed = new TextArea();
        numbersGuessed.setTranslateY(20);
        numbersGuessed.setTranslateX(50);


        numbersGuessed.setPrefHeight(200);
        numbersGuessed.setMaxWidth(200);


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

        VBox welcomeBanner = new VBox(5, welcome,playerNum);

        pane.setTop(welcomeBanner);

        mysteryNum = new Text("Mystery Num:");
        mysteryNum.setFont(playFont);

        Text displayNum = new Text("???"); // Set this to actual number at the end
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

        playAgain = new Button("PLAY AGAIN");
        playAgain.setTextFill(Color.WHITE);
        playAgain.setStyle("-fx-background-color: #000000");



        Text onlinePlayers = new Text("Players currently online:");
        onlinePlayers.setStyle("-fx-font-weight: bold");
        TextArea playersConnected = new TextArea();
        playersConnected.setMaxWidth(150);
        //playersConnected.setTranslateY(20);

        Button connect = new Button("CONNECT");
        connect.setTextFill(Color.WHITE);
        connect.setStyle("-fx-background-color: #000000");
        VBox options = new VBox(10,ipEnter,IP,portEnter,port,connect,getInstr,instr,
                onlinePlayers,playersConnected,winner,quit, playAgain);

        pane.setRight(options);


        // User Interface/Interaction at the bottom of the page
        guess = new Text("Your Guess: ");
        guess.setStyle("-fx-font-weight: bold");
        playerGuess = new TextField();
        submit = new Button("SUBMIT");
        submit.setTextFill(Color.WHITE);
        submit.setStyle("-fx-background-color: #000000");

        HBox userStuff = new HBox(10,guess,playerGuess,submit);

        pane.setBottom(userStuff);

        VBox paneCenter = new VBox(10,numsGuessed,numbersGuessed,mysteryNum,displayNum);
        pane.setCenter(paneCenter);


        primaryStage.setScene(scene1);
        primaryStage.show();
    }
}
