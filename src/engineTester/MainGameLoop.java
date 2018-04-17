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
import terrains.Terrain;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();

        RawModel model = OBJLoader.loadObjModel("tree", loader);


        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("tree")));
        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel",loader), new ModelTexture(loader.loadTexture("grassTexture")));
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);

        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern",loader), new ModelTexture(loader.loadTexture("fern")));
        fern.getTexture().setHasTransparency(true);

        List<Entity> entities = new ArrayList<Entity>();

        Random random = new Random();
        for(int i=0;i<500;i++) {
            entities.add(new Entity(staticModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 3));
            entities.add(new Entity(grass, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 1));
            entities.add(new Entity(fern, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 0.6f));
        }

/*
        ModelTexture texture = staticModel.getTexture();
        texture.setShineDamper(10);
        texture.setReflectivity(1);

        Entity entity = new Entity(staticModel, new Vector3f(30, 0, 30), 0, 0, 0, 1);
        */

        Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
        Camera camera = new Camera();

        Terrain terrain1 = new Terrain(0, -1, loader, new ModelTexture((loader.loadTexture("grass"))));
        Terrain terrain2 = new Terrain(-1, -1, loader, new ModelTexture((loader.loadTexture("grass"))));

        MasterRenderer renderer = new MasterRenderer();
        while (!Display.isCloseRequested()) {
            //entity.increaseRotation(0, 1, 0);
            camera.move();

            renderer.processTerrain(terrain2);
            renderer.processTerrain(terrain1);
            for(Entity entity : entities) {
                renderer.processEntity(entity);
            }

            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}
