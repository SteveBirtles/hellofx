import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Main extends Application {

    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 768;

    static Set<KeyCode> keysPressed = new HashSet<>();

    @Override
    public void start(Stage stage) {

        FrameRegulator fr = new FrameRegulator();
        Random rnd = new Random();

        Group root = new Group();
        Scene scene = new Scene(root);
        Canvas canvas = new Canvas();

        stage.setTitle("JavaFX Canvas Demo");
        stage.setResizable(false);
        //stage.setFullScreen(true);
        stage.setScene(scene);
        stage.setOnCloseRequest(we -> {
            System.out.println("Close button was clicked!");
            System.out.println("Terminating Application...");
            System.exit(0);
        });
        stage.show();
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> keysPressed.add(event.getCode()));
        scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> keysPressed.remove(event.getCode()));

        canvas.setWidth(WINDOW_WIDTH);
        canvas.setHeight(WINDOW_HEIGHT);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.WHITE);
        gc.setFont(new Font("Arial", 14));

        Image image = new Image("sprite.png");

        Set<Sprite> sprites = new HashSet<>();

        for (int i = 0; i <= 100; i++)
        {
            Sprite s = new Sprite(rnd.nextInt(WINDOW_WIDTH), rnd.nextInt(WINDOW_HEIGHT), 32, image);
            s.setVelocity(rnd.nextDouble()*100-50, rnd.nextDouble()*100-50);
            sprites.add(s);

        }

        new AnimationTimer() {
            @Override
            public void handle(long now) {

                /* INPUT */

                for(KeyCode k : keysPressed)
                {
                    if (k == KeyCode.ESCAPE) {
                        System.out.println("Terminating Application...");
                        System.exit(0);
                    }
                }

                /* PROCESS */

                for (Sprite s: sprites)
                {
                    s.update(fr.getFrameLength());
                }
                Sprite.clearUpExired(sprites);

                /* OUTPUT */

                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

                for (Sprite s : sprites)
                {
                    gc.drawImage(s.getImage(), s.getX() - s.getImage().getWidth() / 2, s.getY() - s.getImage().getHeight() / 2);
                }
                fr.updateFPS(now, gc);

            }
        }.start();

    }

    public static void main(String[] args) {
        launch();
    }

}
