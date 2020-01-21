/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
  	final Gotas game;
        final float GRAVITY = -8f;
        final float MAX_VELOCITY = 70f;
        float yVelocity = 0;
        int sizeTube = 0;
        int space = 540;
	Texture tubeImageDown;
        Texture tubeImageUp;
	Texture flappyImage;
        Texture backgroudI;
	Sound pointSound,wingSound;
	Music musicBackground;
	OrthographicCamera camera;
	Rectangle flappy;
	Array<Rectangle> tubedropsDown,tubedropsUp;
	long lastDropTime;
        long lastJumpTime;
	int dropsGathered;
        int pointsDrops;
        int dropsFails;
        boolean levelUp;
        int levelChange, lastLevel;
        int dificulty;
        boolean godMode = false;
        double multiA = 2.2,multiB = 1.8;
	public GameScreen(final Gotas gam) {
		this.game = gam;
                levelUp = false;
		// load the images for the droplet and the bucket, 64x64 pixels each
		tubeImageDown = new Texture(Gdx.files.internal("bottomtube.png"));
                tubeImageUp = new Texture(Gdx.files.internal("uptube.png"));
		flappyImage = new Texture(Gdx.files.internal("ball.png"));
                backgroudI = new Texture(Gdx.files.internal("background.jpg"));   
		// load the drop sound effect and the rain background "music"
		musicBackground = Gdx.audio.newMusic(Gdx.files.internal("march_of_the_spoons.mp3"));
                pointSound =  Gdx.audio.newSound(Gdx.files.internal("coin.mp3"));
                wingSound = Gdx.audio.newSound(Gdx.files.internal("sfx_wing.ogg"));
		musicBackground.setLooping(true);
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// create a Rectangle to logically represent the bucket
		flappy = new Rectangle();
		flappy.x = 30; // center the bucket horizontally
		flappy.y = 480 / 2 - 64 / 2; // bottom left corner of the bucket is 20 pixels above
						// the bottom screen edge
		flappy.width = flappyImage.getWidth();
		flappy.height = flappyImage.getHeight();
                
                pointsDrops = 0;
                dropsFails = 0;
                lastLevel = 0;
                dificulty = 0;
                levelChange = lastLevel + 10 + MathUtils.random(0, 20);
		// create the raindrops array and spawn the first raindrop
		tubedropsDown = new Array<Rectangle>();
                tubedropsUp = new Array<Rectangle>();
	}

	private void spawnTubedropDown() {
		Rectangle tubedrop = new Rectangle();
		tubedrop.x = 800;
		tubedrop.y = MathUtils.random(-400, -100);
                sizeTube = (int) tubedrop.y;
		tubedrop.width = tubeImageDown.getWidth();
		tubedrop.height = tubeImageDown.getHeight();
		tubedropsDown.add(tubedrop);
		lastDropTime = TimeUtils.nanoTime();
	}
        private void spawnTubedropUp() {
		Rectangle tubedrop = new Rectangle();
		tubedrop.x = 800;
		tubedrop.y = sizeTube + space;
		tubedrop.width = tubeImageUp.getWidth();
		tubedrop.height = tubeImageUp.getHeight();
		tubedropsUp.add(tubedrop);
		lastDropTime = TimeUtils.nanoTime();
	}
        

	@Override
	public void render(float delta) {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// tell the camera to update its matrices.
		camera.update();
		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);
                
		// begin a new batch and draw the bucket and
		// all drops
		game.batch.begin();
                game.batch.draw(backgroudI, 0, 0);
		game.batch.draw(flappyImage, flappy.x, flappy.y);
		for (Rectangle tubedrop : tubedropsDown) {
			game.batch.draw(tubeImageDown, tubedrop.x, tubedrop.y);
		}
                for (Rectangle tubedrop : tubedropsUp) {
			game.batch.draw(tubeImageUp, tubedrop.x, tubedrop.y, tubedrop.getWidth(),tubedrop.getHeight());
		}
                game.font.draw(game.batch, "SCORE = " + pointsDrops, 700, 450);
		game.batch.end();
                if (!levelUp && lastLevel > levelChange) {
                    levelUp = true;
                    levelChange = lastLevel + 10 + MathUtils.random(0, 20);
                    tubeImageDown = new Texture(Gdx.files.internal("uptubeX.png"));
                    tubeImageUp = new Texture(Gdx.files.internal("bottomtubeX.png"));
                    dificulty = levelChange;
                }else if (levelUp && lastLevel > levelChange) {
                    levelUp = false;
                    levelChange = lastLevel + 10 + MathUtils.random(0, 20);
                    tubeImageDown = new Texture(Gdx.files.internal("bottomtube.png"));
                    tubeImageUp = new Texture(Gdx.files.internal("uptube.png"));
                    dificulty = levelChange;
                }else{
                    lastLevel = pointsDrops;
                }
		// process user input
		if (Gdx.input.isTouched() && TimeUtils.nanoTime() - lastJumpTime > 150000000 ) {
                    yVelocity = yVelocity + MAX_VELOCITY * 4;
                    lastJumpTime = TimeUtils.nanoTime();
                    wingSound.play(1);
		}
		if (Gdx.input.isKeyPressed(Keys.UP) && TimeUtils.nanoTime() - lastJumpTime > 150000000 ){
                    yVelocity = yVelocity + MAX_VELOCITY * 4;
                    lastJumpTime = TimeUtils.nanoTime();
                    wingSound.play(1);
                }
                
                
		yVelocity = yVelocity + GRAVITY;
                float yChange = yVelocity * delta;
                flappy.y = flappy.y + yChange;
		// make sure the bucket stays within the screen bounds
		if (flappy.y < 0){
                    flappy.y = 0;
                    if (!godMode) {
                        game.setScreen(new EndGameScreen(game,pointsDrops));
                        dispose();
                    }
                }
		if (flappy.y > 480){
                    flappy.y = 480;
                    if (!godMode) {
                        game.setScreen(new EndGameScreen(game,pointsDrops));
                        dispose();
                    }
                }
			

		// check if we need to create a new raindrop
		if (TimeUtils.nanoTime() - lastDropTime > 2000000000){
                    spawnTubedropDown();
                    spawnTubedropUp();
                }
		

		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we play back
		// a sound effect as well.
		Iterator<Rectangle> iterDown = tubedropsDown.iterator();
                Iterator<Rectangle> iterUp = tubedropsUp.iterator();
		while (iterDown.hasNext() || iterUp.hasNext()) {
                    if (iterDown.hasNext()) {
                        Rectangle tubedropDown = iterDown.next();
                        if (levelUp) {
                            tubedropDown.x -= (200 + (dificulty/1.5*multiA)) * Gdx.graphics.getDeltaTime();
                        }else{
                            tubedropDown.x -= (200 + (dificulty/1.5*multiB)) * Gdx.graphics.getDeltaTime();
                        }
                        if (tubedropDown.x + 64 < 0){
                            pointSound.play(1);
                            iterDown.remove();
                        }
                        if (tubedropDown.overlaps(flappy)) {
                            if (!godMode) {
                                game.setScreen(new EndGameScreen(game,pointsDrops));
                                dispose();
                            }
				
			}
                    }
                    if (iterUp.hasNext()) {
                        Rectangle tubedropUp = iterUp.next();
                        if (levelUp) {
                            tubedropUp.x -= (200 + (dificulty/1.5*multiB)) * Gdx.graphics.getDeltaTime();
                        }else{
                            tubedropUp.x -= (200 + (dificulty/1.5*multiA)) * Gdx.graphics.getDeltaTime();
                        }
                        if (tubedropUp.x + 64 < 0){
                            iterUp.remove();
                            pointsDrops+= 5;
                        }
                        if (tubedropUp.overlaps(flappy)) {
                            if (!godMode) {
                                game.setScreen(new EndGameScreen(game,pointsDrops));
                                dispose();
                            }
			}
                    }	
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		musicBackground.play();
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
            tubeImageDown.dispose();
            flappyImage.dispose();
            pointSound.dispose();
            musicBackground.dispose();
            tubeImageUp.dispose();
            backgroudI.dispose();
            wingSound.dispose();

	}

}
