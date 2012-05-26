package com.me.gotoandplay;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class GameServer extends Server {
	public static final int gameWidth = 480;
	public static final int gameHeight = 320;

	float power = 20;
	float upconstant = 0.75f;
	float friction = 0.95f;
	float gravity = 0;//0.1f;

	private CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<Player>();
	private Random random = new Random();

	public GameServer() throws IOException {
		addListener(new Listener() {
			public void connected(Connection connection) {
				int id = connection.getID();

				// Send new connection all the existing players.
				for (Player player : players)
					sendToTCP(id, getPosition(player));

				// Add new player.
				Player player = new Player();
				player.id = connection.getID();
				player.x = 100;//random.nextInt((int) (gameWidth - player.width));
				player.y = 100;//random.nextInt((int) (gameHeight - player.height));
				players.add(player);

				// Send new player to everyone.
				sendToAllTCP(getPosition(player));
			}

			public void disconnected(Connection connection) {
				players.remove(getPlayer(connection.getID()));

				Remove remove = new Remove();
				remove.playerId = connection.getID();
				Message msg = new Message();
				msg.type = MsgType.REMOVE;
				msg.data = remove;
				sendToAllTCP(msg);
			}

			public void received(Connection connection, Object object) {
				Player player = getPlayer(connection.getID());
				if (player == null)
					return;
				if (object instanceof Key) {
					Key key = (Key) object;
					player.keyPressed = key.keyCode;
					player.delta = key.delta;
				}
			}
		});
		Network.register(getKryo());
		bind(Network.port);
		start();

		new Thread() {
			public void run() {
				while (true) {
					tick(48);
					try {
						Thread.sleep(48);
					} catch (InterruptedException ignored) {
					}
				}
			}
		}.start();
	}

	Message getPosition(Player player) {
		Position position = new Position();
		position.playerId = player.id;
		position.x = player.x;
		position.y = player.y;
		Message msg = new Message();
		msg.type = MsgType.CHPOS;
		msg.data = position;
		return msg;
	}

	private Player getPlayer(int id) {
		for (Player player : players)
			if (player.id == id)
				return player;
		return null;
	}

	void tick(int delta) {
		for (Player player : players) {
			// boolean moved = false;
			switch (player.keyPressed) {
			case Keys.LEFT:
				player.xspeed -= power * player.delta;
				break;
			case Keys.RIGHT:
				player.xspeed += power * player.delta;
				break;
			case Keys.DOWN:
				player.yspeed -= power * upconstant * player.delta;
				break;
			case Keys.UP:
				player.yspeed += power * upconstant * player.delta;
				break;
			}

			// xspeed += wind;
			player.xspeed *= friction;
			player.yspeed -= gravity;

			if (player.x < 0) {
				player.xspeed = -player.xspeed;
				player.x = 0;
			}
			if (player.y < 0) {
				player.yspeed = -player.yspeed;
				player.y = 0;
			}
			if (player.x + player.width > gameWidth) {
				player.xspeed = -player.xspeed;
				player.x = gameWidth - player.width;
			}
			if (player.y + player.height > gameHeight) {
				player.yspeed = -player.yspeed;
				player.y = gameHeight - player.height;
			}
			player.x += player.xspeed;
			player.y += player.yspeed;
			
				player.keyPressed = 0;
				//System.out.println(player.id +" " + player.x+" "+player.y);
			// if (hero.contains(coin.x + 8, coin.y + 8)) {
			// coin.x = (float) (Math.random() * 400 + 50);
			// ++score;
			// }

			// if (moved)
			sendToAllTCP(getPosition(player));
		}
	}

	public static void main(String[] args) throws IOException {
		Log.DEBUG();
		new GameServer();
	}

}
