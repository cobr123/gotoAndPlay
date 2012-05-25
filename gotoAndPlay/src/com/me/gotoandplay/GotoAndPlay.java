package com.me.gotoandplay;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

//http://www.gotoandplay.it/_articles/2007/02/game_tutorial_part1.php
public class GotoAndPlay implements ApplicationListener {
	Texture coinImage;
	static Texture heroImage;
	Circle coin;
	BitmapFont font;

	private Client client = new Client();
	private CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList();

	Camera camera;
	SpriteBatch batch;
	float downconstant = 0.96f;
	float wind = 0.1f;
	int score = 0;

	@Override
	public void create() {
		coinImage = new Texture(Gdx.files.internal("coin.png"));
		heroImage = new Texture(Gdx.files.internal("hero.png"));
		
		font = new BitmapFont();
		font.setScale(2);

		camera = new OrthographicCamera();
		((OrthographicCamera) camera).setToOrtho(false, 480, 320);

		batch = new SpriteBatch();

		coin = new Circle(camera.viewportWidth / 3,
				camera.viewportHeight * 2 / 3, 16);

		client.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				Message msg = (Message) object;
				if (msg.type == MsgType.CHPOS) {
					Position pos = (Position) msg.data;
					Player player = getPlayer(pos.playerId);
					if (player == null) {
						player = new Player();
						player.id = pos.playerId;
						players.add(player);
					}
					player.x = pos.x;
					player.y = pos.y;
				}

				if (msg.type == MsgType.REMOVE) {
					Remove remove = (Remove) object;
					Player player = getPlayer(remove.playerId);
					if (player != null)
						players.remove(player);
				}
			}

			public void disconnected(Connection connection) {
				System.exit(0);
			}
		});

        Network.register(client.getKryo());
        client.start();
        connect();
	}

    public void connect() {
       try {
          client.connect(8000, "localhost", Network.port);
       } catch (IOException ex) {
          throw new RuntimeException(ex);
       }
    }
	private Player getPlayer(int id) {
		for (Player player : players)
			if (player.id == id)
				return player;
		return null;
	}

	@Override
	public void dispose() {
		coinImage.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(coinImage, coin.x, coin.y);
		for (Player player : players) {
			player.draw(batch, heroImage);
		}
		font.draw(batch, String.valueOf(score), 10, camera.viewportHeight - 10);
		batch.end();
		
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			sendKey(Keys.LEFT, Gdx.graphics.getDeltaTime());
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			sendKey(Keys.RIGHT, Gdx.graphics.getDeltaTime());
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			sendKey(Keys.DOWN, Gdx.graphics.getDeltaTime());
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			sendKey(Keys.UP, Gdx.graphics.getDeltaTime());
		}
	}

	private void sendKey(int keyCode, float delta) {
		if (!client.isConnected()) {
			return;
		}
		//System.out.println("keyCode="+keyCode);
		Key key = new Key();
		key.delta = delta;
		key.keyCode = keyCode;
		client.sendTCP(key);
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
