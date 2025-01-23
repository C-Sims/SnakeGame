/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package snakeproject;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author horabaixa
 */
public class SnakeProject extends Application {
    
    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    private static final String APPLE_IMAGE = "/img/apple.png";
    private static final String BODY_BOTTOMLEFT_IMAGE = "/img/body_bottomleft.png";
    private static final String BODY_BOTTOMRIGHT_IMAGE = "/img/body_bottomright.png";
    private static final String BODY_HORIZONTAL_IMAGE = "/img/body_horizontal.png";
    private static final String BODY_TOPLEFT_IMAGE = "/img/body_topleft.png";
    private static final String BODY_TOPRIGHT_IMAGE = "/img/body_topright.png";
    private static final String BODY_VERTICAL_IMAGE = "/img/body_vertical.png";
    private static final String HEAD_DOWN_IMAGE = "/img/head_down.png";
    private static final String HEAD_LEFT_IMAGE = "/img/head_left.png";
    private static final String HEAD_RIGHT_IMAGE = "/img/head_right.png";
    private static final String HEAD_UP_IMAGE = "/img/head_up.png";
    private static final String TAIL_DOWN_IMAGE = "/img/tail_down.png";
    private static final String TAIL_LEFT_IMAGE = "/img/tail_left.png";
    private static final String TAIL_RIGHT_IMAGE = "/img/tail_right.png";
    private static final String TAIL_UP_IMAGE = "/img/tail_up.png";
    
    
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    
    private GraphicsContext gc;
    private List<Point> snakeBody = new ArrayList<>();
    private Point snakeHead;
    private int foodX;
    private int foodY;
    private boolean gameOver;
    private int currentDirection;
   
    
    @Override
    public void start(Stage primaryStage) throws Exception{                          
             
        StackPane root = new StackPane();       
        
        // Adding one SQUARE_SIZE to the width and height so it skips one 
        // SQUARE_SIZE on the right and the bottom.
        Canvas canvasGame = new Canvas(WIDTH + SQUARE_SIZE, HEIGHT + SQUARE_SIZE);
     
        root.getChildren().add(canvasGame);
        root.setStyle("-fx-background-color: #578A34");
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.show();
        gc = canvasGame.getGraphicsContext2D();
        
        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                // code == KeyCode.RIGHT
                if(code == KeyCode.D){
                    if(currentDirection != LEFT){
                        currentDirection = RIGHT;
                    }
                // code == KeyCode.LEFT    
                }else if(code == KeyCode.A ){
                    if(currentDirection != RIGHT){
                        currentDirection = LEFT;
                    }
                }else if(code == KeyCode.W){
                    if(currentDirection != DOWN){
                        currentDirection = UP;
                    }
                }else if(code == KeyCode.S){
                    if(currentDirection != UP){
                        currentDirection = DOWN;
                    }
                }
            }
            
        });
        
        // Position of snake
        for(int i = 0; i < 3; i++){
            snakeBody.add(new Point(5, ROWS / 2));
        }
        
        snakeHead = snakeBody.get(0);
                
        spawnApple();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(130), e->run(gc)));
    }

    private void run(GraphicsContext gc){
        drawGameBackground(gc);
        drawApple(gc);
        drawSnake(gc);
        
        for(int i = snakeBody.size() -1; i >= 1; i--){
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }
        
        switch(currentDirection){
            case RIGHT:
                moveRight();
                break;
            case LEFT:
                moveLeft();
                break;
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
        }   
    }
    
    // Method that adds the background blocks where the game is played.
    private void drawGameBackground(GraphicsContext gc){
        
        // i = 1 so it skips one SQUARE_SIZE of width on the left.
        for(int i = 1; i < ROWS; i++){
            
            // j = 2 so it skips two SQUARE_SIZE of height on the top.
            for (int j = 2; j < COLUMNS; j++) {
                if((i + j) % 2 == 0){
                    gc.setFill(Color.web("AAD751"));
                }else{
                    gc.setFill(Color.web("A2D149")); 
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }
    
    private void spawnApple(){
        
        // Point where the continue skips to.
        start:
        while(true){
            foodX = (int) (Math.random() * ROWS );
            foodY = (int) (Math.random() * COLUMNS);
            
            // If apple appears outside the zone of the game, start next while loop.
            if(foodY == 0 || foodY == 1 || foodY == COLUMNS - 1 || foodX == 0 || foodX == ROWS - 1){
                    continue;
            }
            
            for(Point snake : snakeBody){
                
                // If apple collisions with snake body, start a new loop for the while loop, 
                // instead of the for loop, using start: and continue start;
                if(snake.getX() == foodX && snake.getY() == foodY){
                    continue start;                               
                }                
                
            }
            break;
        }
        
    }
    
    private void drawApple(GraphicsContext gc){        
        Image foodImage = new Image(APPLE_IMAGE);
        gc.drawImage(foodImage, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }
    
    private void drawSnake(GraphicsContext gc){
        gc.setFill(Color.web("4674E9"));
        gc.fillRoundRect(snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 35, 35);
        
        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE,
                    SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20);
        }
        
    }
    
    private void moveRight(){
        snakeHead.x++;
    }
    
    private void moveLeft(){
        snakeHead.x--;
    }
    
    private void moveUp(){
        snakeHead.y--;
    }
    
    private void moveDown(){
        snakeHead.y++;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
