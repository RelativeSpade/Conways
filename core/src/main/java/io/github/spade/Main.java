package io.github.spade;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main extends ApplicationAdapter {
    private Pixmap map;
    private Texture texture;
    private SpriteBatch batch;
    private ScreenViewport viewport;
    private Stage stage;
    private Vector2 center;
    private Boolean started;
    private ArrayList<Vector2> LivingCells;
    private Vector2 mapDimensions;
    private OrthographicCamera camera;

    @Override
    public void create() {
        mapDimensions = new Vector2(500, 500);
        viewport = new ScreenViewport();
        camera = new OrthographicCamera();
        stage = new Stage(viewport);
        LivingCells = new ArrayList<>();
        started = false;

        map = new Pixmap((int) mapDimensions.x, (int) mapDimensions.y, Pixmap.Format.RGBA8888);
        map.setColor(1, 1, 1, 1);
        map.fill();

        center = new Vector2((float) Gdx.graphics.getWidth() / 2 - (float) map.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2 - (float) map.getHeight() / 2);

        // Create a texture from the pixmap
        texture = new Texture(map);
        batch = new SpriteBatch();
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        center.set((float) Gdx.graphics.getWidth() / 2 - (float) map.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2 - (float) map.getHeight() / 2);

        camera.setToOrtho(false, width, height);
        camera.update();

        camera.position.set(width / 2f, height / 2f, 0);
    }


    private void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        // Create a new InputMultiplexer to handle both stage input and custom input
        InputMultiplexer multiplexer = new InputMultiplexer();

        // Add the stage as an input processor
        multiplexer.addProcessor(stage);

        // Add your custom input adapter for map dragging and zooming
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                if (amountY > 0) {
                    if (camera.zoom == 0.025f) {
                        camera.zoom = 0.05f;
                    } else {
                        camera.zoom += 0.1f;
                    }
                } else if (amountY < 0) {
                    if (camera.zoom - 0.1f > 0.1f) {
                        camera.zoom -= 0.1f;
                    } else {
                        if (camera.zoom == 0.05f) {
                            camera.zoom = 0.025f;
                        } else if (camera.zoom != 0.025f) {
                            camera.zoom = 0.05f;
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                camera.position.x -= (Gdx.input.getDeltaX() * camera.zoom);
                camera.position.y += (Gdx.input.getDeltaY() * camera.zoom);
                return true;
            }
        });

        // Set the multiplexer as the input processor
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void logic() {
        if (!started){
            started = true;
            for(int i = 0 ; i < (((int) mapDimensions.x * (int) mapDimensions.y)*.1) ; i++){
                Random rand = new Random();
                int x = rand.nextInt(map.getWidth());
                int y = rand.nextInt(map.getWidth());
                LivingCells.add(i,new Vector2(x, y));
            }
        }

        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());

        // Convert screen coordinates to world coordinates based on the camera's position and zoom
        Vector3 worldCoordinates = camera.unproject(new Vector3(mousePos.x, mousePos.y, 0));

        // Adjust the position by centering the camera
        Vector2 trueMousePos = new Vector2(Math.round(worldCoordinates.x - center.x), Math.round(worldCoordinates.y - center.y));

        trueMousePos.y = map.getHeight() - trueMousePos.y; // Invert the y-axis because LibGDX draws starting from top left and we loop from bottom left.


        for (int y = 0; y < mapDimensions.x; y++) {
            for (int x = 0; x < mapDimensions.y; x++) {
                map.drawPixel(x, y, Color.rgba8888(1.0f, 0.0f, 1.0f, 1));
            }
        }

        for (Vector2 livingCell : LivingCells) {
            map.drawPixel((int) livingCell.x, (int) livingCell.y, Color.rgba8888(1.0f, 1.0f, 1.0f, 1));
        }

        // Update the texture from the pixmap
        texture.draw(map, 0, 0);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(texture, center.x, center.y);

        batch.end();
    }

    @Override
    public void dispose() {
        map.dispose();
        texture.dispose();
        batch.dispose();
        stage.dispose();
    }
}
