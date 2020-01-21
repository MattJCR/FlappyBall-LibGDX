/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EndGameScreen implements Screen {
        final Gotas game;
	OrthographicCamera camera;
        int points;
        Texture backgroudI;
        Music musicLose;
        long time;
	public EndGameScreen(final Gotas gam, int p) {
		game = gam;
                points = p;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
                musicLose = Gdx.audio.newMusic(Gdx.files.internal("gameoverlaugh.mp3"));
                musicLose.setLooping(false);
                musicLose.play();
                time = TimeUtils.millis();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                backgroudI = new Texture(Gdx.files.internal("gameover.jpg")); 
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
                game.batch.draw(backgroudI, 0, 0);
		game.font.draw(game.batch, "END GAME!!! ", 100, 150);
		game.font.draw(game.batch, "SCORE = " + points, 100, 100);
                if (TimeUtils.millis() - time > 1000) {
                    game.font.draw(game.batch, "Tap anywhere to begin!", 100, 50);
		}else{
                    game.font.draw(game.batch, "Saving score...", 100, 50);
                }
		game.batch.end();
		if (Gdx.input.isTouched() && TimeUtils.millis() - time > 1000) {
			game.setScreen(new MainMenuScreen(game));
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
            musicLose.dispose();
	}
}
