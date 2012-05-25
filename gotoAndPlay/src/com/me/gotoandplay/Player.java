package com.me.gotoandplay;

import java.awt.Color;
import java.awt.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Player {
	Rectangle hero;
	int id;
	float x=0, y=0;
	float width = 32, height = 32;
	int  keyPressed;
	float xspeed = 0;
	float yspeed = 0;
	float delta;

	public Player() {
		hero = new Rectangle();
		//hero.x = camera.viewportWidth / 2 - 48 / 2;
		//hero.y = camera.viewportHeight * 2/3;
		hero.width = width;
		hero.height = height;
	}

	public void draw(SpriteBatch batch, Texture heroImage) {
		batch.draw(heroImage, x, y);
	}
}
