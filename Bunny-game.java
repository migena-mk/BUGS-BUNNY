package com.example.BunnyGame;


import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;


public class HelloApplication extends Application {
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 720;
    private static final int CHARACTER_SIZE = 80;
    private static final int CARROT_SIZE = 10;
    private static int USER_SCORE = 1;

    int RANDOMFOOD;
    private long lastCollisionTime;

    private ImageView rabbit;
    private  ImageView food;
    private Pane root;
    private Random random;
    private Label scoreLabel;

    Image rabbitImageLeft = new Image("C:\\Users\\User\\Projects\\RabbitGame\\src\\Images\\ourBunnyLeft.png");
    Image rabbitImageRight = new Image("C:\\Users\\User\\Projects\\RabbitGame\\src\\Images\\OurBunnyRight.png");
    Image rabbitImageForward = new Image("C:\\Users\\User\\Projects\\RabbitGame\\src\\Images\\ourBunnyForward.png");
    Image rabbitImageBackView = new Image("C:\\Users\\User\\Projects\\RabbitGame\\src\\Images\\ourBunnyBackView.png");
    Image backgroundImage = new Image("C:\\Users\\User\\Projects\\RabbitGame\\src\\Images\\background.png");

    private final String lossImagePath= "C:\\Users\\User\\Projects\\RabbitGame\\src\\Images\\GameOverBunny.jpg";
    private final String winImagePath = "C:\\Users\\User\\Projects\\RabbitGame\\src\\Images\\BunnyWonDisplay.jpg";
    private final Image gameLogo = new Image("C:\\Users\\User\\Projects\\RabbitGame\\src\\Images\\logoRabbitGame.png");

    @Override
    public void start(Stage primaryStage) {

        root = new Pane(); //Node ku do te mbahet lepuri carrota zjarri etc
        root.setPadding(new Insets(10));

        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImg);
        root.setBackground(background);

        rabbit = new ImageView(rabbitImageForward);
        rabbit.setFitHeight(CHARACTER_SIZE);
        rabbit.setFitWidth(CHARACTER_SIZE);
        rabbit.setTranslateX((WINDOW_WIDTH - CHARACTER_SIZE) / 2);
        rabbit.setTranslateY((WINDOW_HEIGHT - CHARACTER_SIZE) / 2);
        root.getChildren().add(rabbit);

        random = new Random();
        scoreLabel();
        generateFood();


        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> {
                    root.getChildren().remove(food);
                    generateFood();
                    CheckWinOrLoss(USER_SCORE);
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("Rabbito");
        primaryStage.getIcons().add(gameLogo);
        primaryStage.setScene(scene);
        setKeyPressEventHandlers(scene);
        primaryStage.show();

        //Check collision between rabbit and the food
//        AnimationTimer gameLoop = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                checkCollision();
//                checkLastCollisionTime(now);
//            }
//        };
//        gameLoop.start();
        root.requestFocus();
    }
    private void setKeyPressEventHandlers(Scene scene)
    {
        scene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));
    }

    private void handleKeyPress(KeyCode keyCode) {
        switch (keyCode) {
            case RIGHT:
                moveCharacterX(30, rabbitImageRight);
                break;
            case LEFT:
                moveCharacterX(-30, rabbitImageLeft);
                break;
            case UP:
                moveCharacterY(-30, rabbitImageBackView);
                break;
            case DOWN:
                moveCharacterY(30, rabbitImageForward);
                break;
        }
        checkCollision();

    }
    private void moveCharacterX(double deltaX, Image newImage)
    {
        rabbit.setTranslateX(rabbit.getTranslateX() + deltaX);
        rabbit.setImage(newImage);
    }

    private void moveCharacterY(double deltaY, Image newImage)
    {
        rabbit.setTranslateY(rabbit.getTranslateY() + deltaY);
        rabbit.setImage(newImage);
    }

    private void checkCollision()
    {
        Bounds characterBounds = rabbit.getBoundsInParent();
        Bounds dotBounds = food.getBoundsInParent();

        if ((characterBounds.intersects(dotBounds))&&(RANDOMFOOD ==0))//hudhra
        {
            root.getChildren().remove(food);
            generateFood();
            USER_SCORE--;
            updateScore();
            lastCollisionTime = System.nanoTime();
        }
        else if ((characterBounds.intersects(dotBounds))&&(RANDOMFOOD==1))
        {
            root.getChildren().remove(food);
            generateFood();
            USER_SCORE++;
            updateScore();
            lastCollisionTime = System.nanoTime();
        }
        else if ((characterBounds.intersects(dotBounds))&&(RANDOMFOOD == 2))
        {
            root.getChildren().remove(food);
            generateFood();
            USER_SCORE+=2;
            updateScore();
            lastCollisionTime = System.nanoTime();
        }
    }

    //We should work in this method
    private void checkLastCollisionTime(long currentTime)
    {
        long elapsedTime = currentTime - lastCollisionTime;
        double elapsedTimeInSeconds = elapsedTime / 1_000_000_000.0; // Convert to seconds

        if (elapsedTimeInSeconds >= 20) {
            // No collision in the last 10 seconds
            USER_SCORE=0;
        }
    }

    private void generateFood()
    {
        double foodX = random.nextDouble() * (WINDOW_WIDTH - CARROT_SIZE);//Ketu marrim nje pozicion x per karroten tone, qe karrota te jete brenda kufijve zbresim nga gjatesia dritares madhesine e karrotes
        double foodY = random.nextDouble() * (WINDOW_HEIGHT - CARROT_SIZE);



        //Neve duam qe kur lepuri te haj carrot do te shtohet nje pike, kur te ha nje hudher ulet nje, nese qendron
        // ne vend per 5 sec do te ulet nje pike po ashtu

        RANDOMFOOD = random.nextInt(3);

        String dotImagePath= "";


        if (RANDOMFOOD == 0)
            dotImagePath = "C:\\Users\\User\\Projects\\RabbitGame\\src\\Images\\garlic.png";//Ky eshte rasti kur na del karrot
        else if (RANDOMFOOD == 1)
            dotImagePath = "C:\\Users\\User\\Projects\\RabbitGame\\src\\Images\\carrot.png";//Ky eshte rasti kur na del karrot
        else if (RANDOMFOOD == 2)
            dotImagePath = "C:\\Users\\User\\Projects\\RabbitGame\\src\\Images\\carrots.png";//Rasti kur eshte hudher



        Image carrotOrGarlic = new Image(dotImagePath);
        food = new ImageView(carrotOrGarlic);
        food.setFitHeight(CHARACTER_SIZE);
        food.setFitWidth(CHARACTER_SIZE);
        food.setTranslateX(foodX);
        food.setTranslateY(foodY);
        root.getChildren().add(food);
    }
    private void scoreLabel()
    {
        scoreLabel = new Label("Score: " + USER_SCORE);
        scoreLabel.setLayoutX(10);
        scoreLabel.setLayoutY(10);
        root.getChildren().add(scoreLabel);
    }

    private void updateScore()
    {
        scoreLabel.setText("Score: "+ USER_SCORE);
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        scoreLabel.setTextFill(Color.AZURE);
        scoreLabel.setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        scoreLabel.setPadding(new Insets(10));
        scoreLabel.setAlignment(Pos.TOP_LEFT);
        scoreLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.FUCHSIA, 5, 0.5, 2, 2));
        scoreLabel.setBorder(new Border(new BorderStroke(Color.BLUEVIOLET, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

    //With these two methods we simply display in the screen the result of the player
    private void displayLossImage(Pane root, String imagePath, double width, double height)
    {
        Image lossImage = new Image(imagePath);
        ImageView imageView = new ImageView(lossImage);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        imageView.setLayoutX((root.getWidth() - width) / 2);
        imageView.setLayoutY((root.getHeight() - height) / 2);
        imageView.layoutXProperty().bind(root.widthProperty().subtract(width).divide(2));
        imageView.layoutYProperty().bind(root.heightProperty().subtract(height).divide(2));
        root.getChildren().add(imageView);
    }

    private void displayWinImage(Pane root, String winImagePath, double width, double height)
    {
        Image winImage = new Image(winImagePath);
        ImageView viewWinImage = new ImageView(winImage);
        viewWinImage.setFitWidth(width);
        viewWinImage.setFitHeight(height);

        viewWinImage.setLayoutX((root.getWidth() - width) / 2);
        viewWinImage.setLayoutY((root.getHeight() - height) / 2);
        viewWinImage.layoutXProperty().bind(root.widthProperty().subtract(width).divide(2));
        viewWinImage.layoutYProperty().bind(root.heightProperty().subtract(height).divide(2));
        root.getChildren().add(viewWinImage);
    }


    //This method chmaecks if there are enought points for a win or it is losss
    //After that it will display the win or loss image
    private void CheckWinOrLoss(int userCurrentScore)
    {
        if (userCurrentScore<=0)
        {
            displayLossImage(root, lossImagePath,500, 350);
           // System.exit(0);
        } else if (userCurrentScore>=10)
        {
            displayWinImage(root, winImagePath, 600, 350);
            //System.exit(0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
