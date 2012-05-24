package com.me.gotoandplay;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

//http://www.gotoandplay.it/_articles/2007/02/game_tutorial_part1.php
public class GotoAndPlay implements ApplicationListener {
	Texture heroImage;
	Texture coinImage;
	Rectangle hero;
	Circle coin;

	Camera camera;
	SpriteBatch batch;
	float power = 20;
	float xspeed = 0;
	float yspeed = 0;
	float friction = 0.95f;
	float gravity = 0.1f;
	float thrust = 0.75f;
	float wind = 0.1f;

	@Override
	public void create() {
		heroImage = new Texture(Gdx.files.internal("hero.png"));
		coinImage = new Texture(Gdx.files.internal("coin.png"));

		camera = new OrthographicCamera();
		((OrthographicCamera) camera).setToOrtho(false, 800, 480);

		batch = new SpriteBatch();

		hero = new Rectangle();
		hero.x = 800 / 2 - 48 / 2;
		hero.y = 400;
		hero.width = 32;
		hero.height = 32;

		coin = new Circle(700, 400, 16);
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
		batch.draw(coinImage, coin.x, coin.y);
		batch.draw(heroImage, hero.x, hero.y);
		batch.end();

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			xspeed -= power * Gdx.graphics.getDeltaTime();
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			xspeed += power * Gdx.graphics.getDeltaTime();
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			yspeed -= power * thrust * Gdx.graphics.getDeltaTime();
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			yspeed += power * thrust * Gdx.graphics.getDeltaTime();
		}
		xspeed += wind;
		xspeed *= friction;
		yspeed -= gravity;

		hero.x += xspeed;
		hero.y += yspeed;

	    if (hero.x < 0 || hero.x > camera.viewportWidth || hero.y < 0 || hero.y > camera.viewportHeight) {
	        xspeed = 0;
	        yspeed = 0;
			hero.x = 800 / 2 - 48 / 2;
			hero.y = 400;
	    }
		if (hero.contains(coin.x + 8, coin.y + 8)) {
			coin.x = (float) (Math.random() * 400 + 50);
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
