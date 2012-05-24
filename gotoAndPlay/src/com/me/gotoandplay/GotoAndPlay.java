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

//http://www.gotoandplay.it/_articles/2007/02/game_tutorial_part1.php
public class GotoAndPlay implements ApplicationListener {
	Texture heroImage;
	Rectangle hero;
	
	Camera camera;
	SpriteBatch batch;
	float power = 20;
	float xspeed = 0;
	float yspeed = 0;
	float friction = 0.95f;
	float gravity = 0.1f;
	
	@Override
	public void create() {
		heroImage = new Texture(Gdx.files.internal("hero.png"));
		
		camera = new OrthographicCamera();
		((OrthographicCamera) camera).setToOrtho(false, 800, 480);
		
		batch = new SpriteBatch();
		
		hero = new Rectangle();
		hero.x = 800 / 2 - 48 / 2;
		hero.y = 400;
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
			xspeed -= power * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			xspeed += power * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)){
			yspeed -= power * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Keys.UP)){
			yspeed += power * Gdx.graphics.getDeltaTime();
		}
		xspeed *= friction;
		yspeed -= gravity;
		
		hero.x += xspeed;
		hero.y += yspeed;
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
