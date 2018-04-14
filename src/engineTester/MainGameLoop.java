package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();

        RawModel model = OBJLoader.loadObjModel("Dragon", loader);


        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("lemon")));
        ModelTexture texture = staticModel.getTexture();
        texture.setShineDamper(10);
        texture.setReflectivity(1);

        Entity entity = new Entity(staticModel, new Vector3f(0, -5, -25), 0, 0, 0, 1);
        Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));
        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer();
        while (!Display.isCloseRequested()) {
            entity.increaseRotation(0, 1, 0);
            camera.move();
            renderer.processEntity(entity);
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}
