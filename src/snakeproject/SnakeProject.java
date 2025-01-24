/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package snakeproject;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author horabaixa
 */
public class SnakeProject extends Application {

    private final int WIDTH = 800;
    private final int HEIGHT = WIDTH;
    private final int ROWS = 20;
    private final int COLUMNS = ROWS;
    private final int SQUARE_SIZE = WIDTH / ROWS;
    private final String APPLE_IMAGE = "/img/apple.png";
    private final String BODY_BOTTOMLEFT_IMAGE = "/img/body_bottomleft.png";
    private final String BODY_BOTTOMRIGHT_IMAGE = "/img/body_bottomright.png";
    private final String BODY_HORIZONTAL_IMAGE = "/img/body_horizontal.png";
    private final String BODY_TOPLEFT_IMAGE = "/img/body_topleft.png";
    private final String BODY_TOPRIGHT_IMAGE = "/img/body_topright.png";
    private final String BODY_VERTICAL_IMAGE = "/img/body_vertical.png";
    private final String HEAD_DOWN_IMAGE = "/img/head_down.png";
    private final String HEAD_LEFT_IMAGE = "/img/head_left.png";
    private final String HEAD_RIGHT_IMAGE = "/img/head_right.png";
    private final String HEAD_UP_IMAGE = "/img/head_up.png";
    private final String TAIL_DOWN_IMAGE = "/img/tail_down.png";
    private final String TAIL_LEFT_IMAGE = "/img/tail_left.png";
    private final String TAIL_RIGHT_IMAGE = "/img/tail_right.png";
    private final String TAIL_UP_IMAGE = "/img/tail_up.png";

    private Image headUpImage;
    private Image headDownImage;
    private Image headLeftImage;
    private Image headRightImage;
    private Image tailUpImage;
    private Image tailDownImage;
    private Image tailLeftImage;
    private Image tailRightImage;
    private Image bodyVerticalImage;
    private Image bodyHorizontalImage;
    private Image bodyTopLeftImage;
    private Image bodyTopRightImage;
    private Image bodyBottomLeftImage;
    private Image bodyBottomRightImage;

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
    private int score;
    private Timeline timeline;

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {

            headUpImage = new Image(HEAD_UP_IMAGE);
            headDownImage = new Image(HEAD_DOWN_IMAGE);
            headLeftImage = new Image(HEAD_LEFT_IMAGE);
            headRightImage = new Image(HEAD_RIGHT_IMAGE);
            tailUpImage = new Image(TAIL_UP_IMAGE);
            tailDownImage = new Image(TAIL_DOWN_IMAGE);
            tailLeftImage = new Image(TAIL_LEFT_IMAGE);
            tailRightImage = new Image(TAIL_RIGHT_IMAGE);
            bodyVerticalImage = new Image(BODY_VERTICAL_IMAGE);
            bodyHorizontalImage = new Image(BODY_HORIZONTAL_IMAGE);
            bodyTopLeftImage = new Image(BODY_TOPLEFT_IMAGE);
            bodyTopRightImage = new Image(BODY_TOPRIGHT_IMAGE);
            bodyBottomLeftImage = new Image(BODY_BOTTOMLEFT_IMAGE);
            bodyBottomRightImage = new Image(BODY_BOTTOMRIGHT_IMAGE);

        } catch (Exception e) {
            System.err.println("Error loading snake images");
        }

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

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                if (code == KeyCode.D && currentDirection != LEFT) {
                    currentDirection = RIGHT;
                } else if (code == KeyCode.A && currentDirection != RIGHT) {
                    currentDirection = LEFT;
                } else if (code == KeyCode.W && currentDirection != DOWN) {
                    currentDirection = UP;
                } else if (code == KeyCode.S && currentDirection != UP) {
                    currentDirection = DOWN;
                }else if(code == KeyCode.SPACE){
                    resetGame();
                }
            }
        });

        // Position of snake
        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point(5, ROWS / 2));
        }

        snakeHead = snakeBody.get(0);

        generateApple();

        timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    // Game over text
    private void run(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", 70));
            gc.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2);
            gc.setFill(Color.WHITE);
            gc.fillText("Score: " + score, WIDTH / 3.5, HEIGHT / 1.5);
            
            return;
        }

        clearCanvas();
        drawGameBackground(gc);
        drawApple(gc);
        drawSnakeHead(gc);
        drawSnakeBody(gc);
        drawScore();

        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }

        switch (currentDirection) {
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

        gameOver();
        eatApple();

    }

    // Method that adds the background blocks where the game is played.
    private void drawGameBackground(GraphicsContext gc) {

        // i = 1 so it skips one SQUARE_SIZE of width on the left.
        for (int i = 1; i < ROWS; i++) {

            // j = 2 so it skips two SQUARE_SIZE of height on the top.
            for (int j = 2; j < COLUMNS; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("AAD751"));
                } else {
                    gc.setFill(Color.web("A2D149"));
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    private void generateApple() {

        // Point where the continue skips to.
        start:
        while (true) {
            foodX = (int) (Math.random() * ROWS);
            foodY = (int) (Math.random() * COLUMNS);

            // If apple appears outside the zone of the game, start next while loop.
            if (foodY == 0 || foodY == 1 || foodY == COLUMNS || foodX == 0 || foodX == ROWS) {
                continue;
            }

            for (Point snake : snakeBody) {

                // If apple collisions with snake body, start a new loop for the while loop, 
                // instead of the for loop, using start: and continue start;
                if (snake.getX() == foodX && snake.getY() == foodY) {
                    continue start;
                }

            }
            break;
        }

    }

    private void drawApple(GraphicsContext gc) {
        Image foodImage = new Image(APPLE_IMAGE);
        gc.drawImage(foodImage, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    private void drawSnakeHead(GraphicsContext gc) {
        Image headImage;

        // Choose the correct image for the head based on the direction
        switch (currentDirection) {
            case RIGHT:
                headImage = headRightImage;
                break;
            case LEFT:
                headImage = headLeftImage;
                break;
            case UP:
                headImage = headUpImage;
                break;
            case DOWN:
                headImage = headDownImage;
                break;
            default:
                headImage = headRightImage;
                break;
        }

        gc.drawImage(headImage, snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    private void drawSnakeBody(GraphicsContext gc) {
        for (int i = 1; i < snakeBody.size(); i++) {
            Point currentSegment = snakeBody.get(i);
            Point previousSegment = snakeBody.get(i - 1);
            Point nextSegment = (i + 1 < snakeBody.size()) ? snakeBody.get(i + 1) : null;

            // Calculate the direction of the current segment
            int currentDirection = getDirection(previousSegment, currentSegment);
            int nextDirection = (nextSegment != null) ? getDirection(currentSegment, nextSegment) : -1;

            // Draw the segment based on its direction and the direction of the next segment
            if (i == snakeBody.size() - 1) {
                // Draw the tail
                drawTail(gc, currentSegment, currentDirection);
            } else if (nextDirection == -1 || currentDirection == nextDirection) {
                // Draw a straight segment (either horizontal or vertical)
                drawStraightBody(gc, currentSegment, currentDirection);
            } else {
                // Draw a curved segment (corner)
                drawCurvedBody(gc, currentSegment, currentDirection, nextDirection);
            }
        }
    }

    private void drawStraightBody(GraphicsContext gc, Point segment, int direction) {
        Image bodyImage;
        if (direction == UP || direction == DOWN) {
            bodyImage = bodyVerticalImage;
        } else {
            bodyImage = bodyHorizontalImage;
        }
        gc.drawImage(bodyImage, segment.x * SQUARE_SIZE, segment.y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    private void drawCurvedBody(GraphicsContext gc, Point segment, int currentDirection, int nextDirection) {
        Image bodyImage = null;

        if (currentDirection == RIGHT && nextDirection == UP) {
            bodyImage = bodyTopLeftImage;
        } else if (currentDirection == LEFT && nextDirection == UP) {
            bodyImage = bodyTopRightImage;
        } else if (currentDirection == RIGHT && nextDirection == DOWN) {
            bodyImage = bodyBottomLeftImage;
        } else if (currentDirection == LEFT && nextDirection == DOWN) {
            bodyImage = bodyBottomRightImage;
        } else if (currentDirection == UP && nextDirection == RIGHT) {
            bodyImage = bodyBottomRightImage;
        } else if (currentDirection == UP && nextDirection == LEFT) {
            bodyImage = bodyBottomLeftImage;
        } else if (currentDirection == DOWN && nextDirection == RIGHT) {
            bodyImage = bodyTopRightImage;
        } else if (currentDirection == DOWN && nextDirection == LEFT) {
            bodyImage = bodyTopLeftImage;
        }

        if (bodyImage != null) {
            gc.drawImage(bodyImage, segment.x * SQUARE_SIZE, segment.y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        }
    }

    private void drawTail(GraphicsContext gc, Point tail, int direction) {
        Image tailImage;

        switch (direction) {
            case RIGHT:
                tailImage = tailRightImage;
                break;
            case LEFT:
                tailImage = tailLeftImage;
                break;
            case UP:
                tailImage = tailUpImage;
                break;
            case DOWN:
                tailImage = tailDownImage;
                break;
            default:
                tailImage = tailRightImage;
                break;
        }

        gc.drawImage(tailImage, tail.x * SQUARE_SIZE, tail.y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    private int getDirection(Point from, Point to) {
        if (to.x > from.x) {
            return RIGHT;
        }
        if (to.x < from.x) {
            return LEFT;
        }
        if (to.y > from.y) {
            return DOWN;
        }
        if (to.y < from.y) {
            return UP;
        }
        return -1;
    }

    private void moveRight() {
        snakeHead.x++;
    }

    private void moveLeft() {
        snakeHead.x--;
    }

    private void moveUp() {
        snakeHead.y--;
    }

    private void moveDown() {
        snakeHead.y++;
    }

    public void gameOver() {
        // if snake collisions with walls, game over.
        if (snakeHead.y == 0 || snakeHead.y == 1 || snakeHead.y == COLUMNS || snakeHead.x == 0 || snakeHead.x == ROWS) {
            gameOver = true;
        }
        // if snake collisions with itself, game over.
        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeHead.x == snakeBody.get(i).getX() && snakeHead.y == snakeBody.get(i).getY()) {
                gameOver = true;
                break;
            }
        }
    }

    private void resetGame() {
        snakeBody.clear();
        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point(5, ROWS / 2));
        }
        snakeHead = snakeBody.get(0);
        currentDirection = RIGHT;
        gameOver = false;
        score = 0;
        generateApple(); 
    }
    
    private void increaseSpeed() {
        timeline.stop();
        Duration newDuration = Duration.millis(Math.max(50, 130 - (score * 5))); // Adjust speed based on score
        timeline = new Timeline(new KeyFrame(newDuration, e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void eatApple() {
        if (snakeHead.getX() == foodX && snakeHead.getY() == foodY) {
            snakeBody.add(new Point(-1, -1));
            generateApple();
            score += 1;
            increaseSpeed();
        }
    }

    private void drawScore() {
        // Clear the score so the score doesn't overlay each time a new apple is eaten
        gc.setFill(Color.web("578A34"));
        gc.fillRect(0, 0, 100, 50);

        // Draw an apple 
        Image apple = new Image(APPLE_IMAGE);
        gc.drawImage(apple, 35, 17, SQUARE_SIZE, SQUARE_SIZE);

        // Draw new score
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("" + score + "\t\tPress SPACEBAR to reset", 80, 50);
    }

    private void clearCanvas() {
        gc.clearRect(0, 0, WIDTH + SQUARE_SIZE, HEIGHT + SQUARE_SIZE);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
