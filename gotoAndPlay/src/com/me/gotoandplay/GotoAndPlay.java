package com.me.gotoandplay;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class GotoAndPlay implements ApplicationListener {
	Texture heroImage;
	Rectangle hero;
	
	Camera camera;
	SpriteBatch batch;
	
	@Override
	public void create() {
		heroImage = new Texture(Gdx.files.internal("hero.png"));
		
		camera = new OrthographicCamera();
		((OrthographicCamera) camera).setToOrtho(false, 800, 480);
		
		batch = new SpriteBatch();
		
		hero = new Rectangle();
		hero.x = 800 / 2 - 48 / 2;
		hero.y = 20;
		hero.width = 32;
		hero.height = 32;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(heroImage, hero.x, hero.y);
		batch.end();
		
		if(Gdx.input.isKeyPressed(Keys.LEFT)){
			hero.x -= 200 * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			hero.x += 200 * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)){
			hero.y -= 200 * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Keys.UP)){
			hero.y += 200 * Gdx.graphics.getDeltaTime();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
