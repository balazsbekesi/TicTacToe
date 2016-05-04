package com.epam.training.TicTacToe;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

public class Game {

	private static final String URL = "http://10.0.9.82:8080/xoxo/";
	private static final String NAME = "Balazs & JDani v2";

	private static String uuid;
	private static String gid;
	private static int myType = 0;
	private static boolean gameON = true;

	public static void main(String[] args) throws Exception {
		System.out.println("started...");
		JSONObject reg = regPost();
		uuid = reg.getString("uuid");
		String temp = reg.getString("type");
		if (temp.equals("O")) {
			myType = 1;
		} else if (temp.equals("X")) {
			myType = 2;
		}
		gid = reg.getString("gid");

		boolean enemyStartedMyFirstRound = true;

		System.out.println("Got uuid: " + uuid);
		System.out.println("Got gid: " + gid);
		System.out.println("Got type: " + reg.getString("type"));

		GameMap game = new GameMap();
		Strategy strategy = new Strategy(game);

		while (gameON) {
			JSONObject myTurn = isMyTurnPost();

			if (myTurn.getInt("statusCode") == 100) {
				gameON = false;
			}

			if (myTurn.getBoolean("isMyTurn") && myTurn.getInt("statusCode") != 100) {
				int x = (Integer) myTurn.getJSONObject("lastMove").get("x");
				int y = (Integer) myTurn.getJSONObject("lastMove").get("y");
				String t = (String) myTurn.getJSONObject("lastMove").get("t");

				int type = 0;
				if (t.equals("O")) {
					type = 1;
				} else if (t.equals("X")) {
					type = 2;
				}

				boolean isFirst = myTurn.getBoolean("first");


				// csak akkor teszi bele, ha nem az első rakás.
				if (!isFirst) {
					game.add(new Field(new Point(x, y), 2));
				}

				Point nextMove = strategy.nextStep();

//				System.out.println("My next move before: " + nextMove);

				nextMove.x = nextMove.x - GameMap.offsetX;
				nextMove.y = nextMove.y - GameMap.offsetY;

//				System.out.println("My next move: " + nextMove);

				game.add(new Field(nextMove, 1));
				System.out.println(game.gameMap.size());

				nextMovePost(nextMove.x, nextMove.y);
				enemyStartedMyFirstRound = false;

				Thread.sleep(500);
			} else if (!myTurn.getBoolean("isMyTurn") && myTurn.getInt("statusCode") != 100) {
				System.out.println("Not my turn, waiting for opponent...");
				Thread.sleep(500);
			}
		}
	}

	public static JSONObject regPost() throws Exception {
		JSONObject jsonObj = null;

		URL url = new URL(URL + "reg");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Content-Type", "application/json");

		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write("{\"name\":\"" + NAME + "\"}");
		writer.close();

		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer res = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				res.append(line);
			}
			reader.close();

			jsonObj = new JSONObject(res.toString());
		} else {
			System.out.println("Reg HTTP response code: " + connection.getResponseCode());
		}

		return jsonObj;
	}

	public static JSONObject isMyTurnPost() throws Exception {
		JSONObject jsonObj = null;

		URL url = new URL(URL + "ismyturn");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Content-Type", "application/json");

		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write("{\"uuid\":\"" + uuid + "\"}");
		writer.close();

		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer res = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				res.append(line);
			}
			reader.close();

			jsonObj = new JSONObject(res.toString());
		}

		return jsonObj;
	}

	public static void nextMovePost(int x, int y) throws Exception {
		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpPost request = new HttpPost(URL + "put");
		StringEntity params = new StringEntity("{\"x\":\"" + x + "\",\"y\":\"" + y + "\",\"uuid\":\"" + uuid + "\"}");
		request.addHeader("content-type", "application/x-www-form-urlencoded");
		request.setEntity(params);
		httpClient.execute(request);
	}

}
