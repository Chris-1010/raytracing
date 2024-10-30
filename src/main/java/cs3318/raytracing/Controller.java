package cs3318.raytracing;

import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class Controller {
    public ImageView renderedImage;
    private Stage stage;
    private Driver sceneToRender;
    boolean finished = false;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void run() {
        long time = System.currentTimeMillis();
        for (int j = 0; j < sceneToRender.height; j += 1) {
            for (int i = 0; i < sceneToRender.width; i += 1) {
                sceneToRender.renderPixel(i, j);
            }
        }
        renderedImage.setImage(sceneToRender.getRenderedImage());
        time = System.currentTimeMillis() - time;
        System.err.println("Rendered in "+(time/60000)+":"+((time%60000)*0.001));
        finished = true;
    }


    public void startRayTrace(ActionEvent actionEvent) {
        sceneToRender = new Driver((int) renderedImage.getFitWidth(),
                                   (int) renderedImage.getFitHeight(),
                "resources/SceneToRender.txt");
        this.run();
    }
}
