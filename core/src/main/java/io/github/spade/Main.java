package io.github.spade;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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

    @Override
    public void create() {
        mapDimensions = new Vector2(500, 500);
        viewport = new ScreenViewport();
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
    }

    private void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
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

        batch.setProjectionMatrix(viewport.getCamera().combined);
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
