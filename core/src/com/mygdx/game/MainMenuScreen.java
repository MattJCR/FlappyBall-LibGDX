/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

public class MainMenuScreen implements Screen {
        final Flappy game;
	OrthographicCamera camera;
        Texture backgroudI;
        long time;
	public MainMenuScreen(final Flappy gam) {
		game = gam;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
                time = TimeUtils.millis();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                backgroudI = new Texture(Gdx.files.internal("background.png"));  
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
                game.batch.draw(backgroudI, 0, 0);
		game.font.draw(game.batch, "Welcome to Flappy Ball!!! ", 100, 150);
                if (TimeUtils.millis() - time > 1000) {
                    game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
		}else{
                    game.font.draw(game.batch, "Loading...", 100, 100);
                }
		game.batch.end();
		if (Gdx.input.isTouched() && TimeUtils.millis() - time > 1000) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
            backgroudI.dispose();
	}
}
