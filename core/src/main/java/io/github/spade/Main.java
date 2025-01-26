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

import java.util.HashMap;

public class Main extends ApplicationAdapter {
    private Pixmap map;
    private Texture texture;
    private SpriteBatch batch;
    private ScreenViewport viewport;
    private Stage stage;
    private Vector2 center;
    private Boolean started = false;
    private HashMap<Integer,Vector2> LivingCells;

    @Override
    public void create() {
        viewport = new ScreenViewport();
        stage = new Stage(viewport);

        map = new Pixmap(1000, 1000, Pixmap.Format.RGBA8888);
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
        // Update the pixmap (if needed)
        if (!started){
            started = true;


        }

        for (int y = 0; y < 1000; y++) {
            for (int x = 0; x < 1000; x++) {
                map.drawPixel(x, y, Color.rgba8888(1.0f, 1.0f, 0.0f, 1));
            }
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
