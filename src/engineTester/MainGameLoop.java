package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();

        //New OBJ Parser
        objConverter.ModelData treeData = OBJFileLoader.loadOBJ("tree");
        RawModel treeModel = loader.loadToVAO(treeData.getVertices(), treeData.getTextureCoords(), treeData.getNormals(), treeData.getIndices());
        TexturedModel treeFinal = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("tree")));

        objConverter.ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
        RawModel grassModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(), grassData.getNormals(), grassData.getIndices());
        TexturedModel grassFinal = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("grassTexture")));
        grassFinal.getTexture().setHasTransparency(true);
        grassFinal.getTexture().setUseFakeLighting(true);

        objConverter.ModelData lowPolyTreeData = OBJFileLoader.loadOBJ("lowPolyTree");
        RawModel lowPolyTreeModel = loader.loadToVAO(lowPolyTreeData.getVertices(), lowPolyTreeData.getTextureCoords(), lowPolyTreeData.getNormals(), lowPolyTreeData.getIndices());
        TexturedModel lowPolyTreeFinal = new TexturedModel(lowPolyTreeModel, new ModelTexture(loader.loadTexture("lowPolyTree")));
        grassFinal.getTexture().setHasTransparency(true);
        grassFinal.getTexture().setUseFakeLighting(true);

        //Old OBJ Parser
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), new ModelTexture(loader.loadTexture("fern")));
        fern.getTexture().setHasTransparency(true);

        List<Entity> entities = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < 500; i++) {
            entities.add(new Entity(treeFinal, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 3));
            entities.add(new Entity(lowPolyTreeFinal, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 0.3f));
            entities.add(new Entity(grassFinal, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 1));
            entities.add(new Entity(fern, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 0.6f));
        }

/*
        ModelTexture texture = staticModel.getTexture();
        texture.setShineDamper(10);
        texture.setReflectivity(1);

        Entity entity = new Entity(staticModel, new Vector3f(30, 0, 30), 0, 0, 0, 1);
        */

        Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));
        Camera camera = new Camera();



        //Terrain
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain1 = new Terrain(0, -1, loader, texturePack, blendMap);
        Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap);

        MasterRenderer renderer = new MasterRenderer();
        while (!Display.isCloseRequested()) {
            //entity.increaseRotation(0, 1, 0);
            camera.move();

            renderer.processTerrain(terrain2);
            renderer.processTerrain(terrain1);
            for (Entity entity : entities) {
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
