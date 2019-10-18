package engineTester;

import collision.AABB;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();
        TextMaster.init(loader);

        MasterRenderer renderer = new MasterRenderer(loader);
        List<Entity> entities = new ArrayList<>();
        List<Entity> normalEntities = new ArrayList<>();

        AABB box = new AABB(new Vector3f(0,0,0), new Vector3f(1,1,1));
        AABB box2 = new AABB(new Vector3f(1,0,0), new Vector3f(1,1,1));

//        if(box.getCollision(box2)) {
//            System.out.println("intersect");
//        } else {
//            System.out.println("not intersecting");
//        }

        FontType font = new FontType(loader.loadTexture("arial"), new File("res/arial.fnt"));
        GUIText text = new GUIText("Jabadaba duu!", 1, font, new Vector2f(0, 0.95f), 1f, true);
        text.setColour(0,1,0);


        RawModel model = OBJLoader.loadObjModel("dragon", loader);
        RawModel model2 = OBJLoader.loadObjModel("stall", loader);
        TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("blank")));
        TexturedModel texturedModel2 = new TexturedModel(model2, new ModelTexture(loader.loadTexture("stallTexture")));
        ModelTexture texture = texturedModel.getTexture();
        texture.setShineDamper(10);
        texture.setReflectivity(1);


        Light light = new Light(new Vector3f(20000,20000,20000), new Vector3f(1,1,1));
        List<Light> lights = new ArrayList<>();
        lights.add(light);
        Light light2 = new Light(new Vector3f(109,10,-70), new Vector3f(2,0,0), new Vector3f(1, 0.01f, 0.002f));
        lights.add(light2);


        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
        Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap2");
        Terrain terrain3 = new Terrain(0, 0, loader, texturePack, blendMap, "heightmap2");
        Terrain terrain4 = new Terrain(-1, 0, loader, texturePack, blendMap, "heightmap2");

        Entity entity = new Entity(texturedModel, new Vector3f(65,terrain.getHeightOfTerrain(65, -25),-25),0,0,0,1);
        Entity entity2 = new Entity(texturedModel2, new Vector3f(95,terrain.getHeightOfTerrain(95, -50),-50),0,120,0,1);

        TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp")));
        lamp.getTexture().setHasTransparency(true);
        lamp.getTexture().setUseFakeLighting(true);
        Entity lampEntity = new Entity(lamp, new Vector3f(109,terrain.getHeightOfTerrain(109, -70),-70), 0, 0, 0, 1);
        entities.add(lampEntity);

        TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("tree", loader), new ModelTexture(loader.loadTexture("tree")));
        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassModel")));
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        ModelTexture fernAtlas = new ModelTexture(loader.loadTexture("fernAtlas"));
        fernAtlas.setNumberOfRows(2);
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), fernAtlas);
        fern.getTexture().setHasTransparency(true);

        TexturedModel barrel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader), new ModelTexture(loader.loadTexture("barrel")));
        barrel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
        barrel.getTexture().setShineDamper(10);
        barrel.getTexture().setReflectivity(0.5f);

        Random random = new Random();
        for(int i=0;i<500;i++){
            if (i % 2 == 0) {
                float x = random.nextFloat() * 800 - 400;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 0.6f));
            }
            if (i % 5 == 0) {
                float x = random.nextFloat() * 800 - 400;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(tree, new Vector3f(x, y, z), 0, 0, 0, 5));

                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * -600;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(grass, new Vector3f(x, y, z), 0, 0, 0, 1));
            }
            if (i % 50 == 0) {
                float x = random.nextFloat() * 800 - 400;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x, z) + 2.5f;
                normalEntities.add(new Entity(barrel, new Vector3f(x, y, z), 0, 0, 0, 0.4f));
            }
        }

        RawModel bunnyModel = OBJLoader.loadObjModel("person", loader);
        RawModel bunnyModel2 = OBJLoader.loadObjModel("stanfordBunny", loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("playerTexture")));
        TexturedModel stanfordBunny2 = new TexturedModel(bunnyModel2, new ModelTexture(loader.loadTexture("blank")));

        Player player = new Player(stanfordBunny, new Vector3f(75, 0, -5), 0, 180, 0, 0.5f);
        Player player2 = new Player(stanfordBunny2, new Vector3f(80, 0, -5), 0, 180, 0, 1);

        Camera camera = new Camera(player);

        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.80f, 0.95f), new Vector2f(0.20f, 0.20f));
        guis.add(gui);

        GuiRenderer guiRenderer = new GuiRenderer(loader);

        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

        while(!Display.isCloseRequested()) {
            entity.increaseRotation(0,1,0);
            camera.move();
            player.move(terrain, Keyboard.KEY_W, Keyboard.KEY_S, Keyboard.KEY_A, Keyboard.KEY_D, Keyboard.KEY_SPACE);
            player2.move(terrain, Keyboard.KEY_UP, Keyboard.KEY_DOWN, Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT, Keyboard.KEY_NUMPAD0);

            picker.update();
            Vector3f terrainPoint = picker.getCurrentTerrainPoint();
            if(terrainPoint != null && Mouse.isButtonDown(0)) {
                lampEntity.setPosition(terrainPoint);
                light2.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y + 15, terrainPoint.z));

            }

            //game logic
            renderer.processEntity(player);
            renderer.processEntity(player2);
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processTerrain(terrain3);
            renderer.processTerrain(terrain4);

            fullEntity(loader, renderer, "lowPolyTree", "lowPolyTree", -5, 0, -50, 0, 0, 0, 5, 1, 1);

            for(Entity trees:entities){
                renderer.processEntity(trees);
            }

            for(Entity fancyEntities : normalEntities) {
                renderer.processNormalMapEntity(fancyEntities);
            }

            renderer.processEntity(entity);
            renderer.processEntity(entity2);
            renderer.render(lights, camera);
            guiRenderer.render(guis);
            TextMaster.render();
            DisplayManager.updateDisplay();

        }

        TextMaster.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

    public static void fullEntity(Loader loader, MasterRenderer renderer, String fileName, String texture, int dX, int dY, int dZ, int rotX, int rotY, int rotZ, int scale, int shine, int reflect) {
        RawModel model = OBJLoader.loadObjModel(fileName, loader);
        TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture(texture)));
        ModelTexture modelTexture = texturedModel.getTexture();
        modelTexture.setShineDamper(shine);
        modelTexture.setReflectivity(reflect);
        Entity entity = new Entity(texturedModel, new Vector3f(dX,dY,dZ),rotX,rotY,rotZ,scale);
        renderer.processEntity(entity);
    }

}
