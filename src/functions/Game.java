package functions;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.*;
import view.PanelSnakeGame;
import view.ViewCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class Game {
    public static PanelSnakeGame initGame(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream out = new PrintStream(socket.getOutputStream());
        JSONObject jsonRequest = new JSONObject();
        JSONObject jsonResponse;

        do {
            jsonRequest.put("type", "start-game");
            out.println(jsonRequest);
            System.out.println(socket.toString());
            jsonResponse = new JSONObject(in.readLine());
            System.out.println("???");
        } while (!jsonResponse.get("type").equals("return-start-game"));

        int width = jsonResponse.getInt("map-width");
        int height = jsonResponse.getInt("map-height");

        boolean walls[][] = new boolean[width][height];

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                walls[i][j] = false;

        JSONObject jsonSnake = jsonResponse.getJSONObject("snake");
        JSONArray jsonHead = jsonSnake.getJSONArray("head");
        Position head = new Position(jsonHead.getInt(0), jsonHead.getInt(1));
        ArrayList<Position> positions = new ArrayList<>();
        positions.add(head);

        AgentAction action = null;

        switch (jsonSnake.get("last-action").toString()) {
            case "up":
                action = AgentAction.MOVE_UP;
                break;
            case "down":
                action = AgentAction.MOVE_DOWN;
                break;
            case "left":
                action = AgentAction.MOVE_LEFT;
                break;
            case "right":
                action = AgentAction.MOVE_RIGHT;
                break;
        }

        ViewCommand.nextAction = action;

        FeaturesSnake snake = new FeaturesSnake(positions, action, ColorSnake.Green,
                jsonSnake.getBoolean("isInvincible"), jsonSnake.getBoolean("isSick"));
        ArrayList<FeaturesSnake> snakes = new ArrayList<>();
        snakes.add(snake);

        JSONArray jsonItems = jsonResponse.getJSONArray("items");
        ArrayList<FeaturesItem> items = new ArrayList<>();

        for (int i = 0; i < jsonItems.length(); i++) {
            ItemType type = null;
            JSONObject jsonItem = (JSONObject) jsonItems.get(i);

            switch (jsonItem.get("type").toString()) {
                case "apple":
                    type = ItemType.APPLE;
                    break;
                case "box":
                    type = ItemType.BOX;
                    break;
                case "sick":
                    type = ItemType.SICK_BALL;
                    break;
                case "invincibility":
                    type = ItemType.INVINCIBILITY_BALL;
                    break;
            }

            JSONArray coordinates = jsonItem.getJSONArray("coordinates");
            FeaturesItem item = new FeaturesItem(coordinates.getInt(0), coordinates.getInt(1), type);
            items.add(item);
        }

        return new PanelSnakeGame(width, height, walls, snakes, items);
    }

    public static void updateGame(PanelSnakeGame panelSnakeGame, JSONObject jsonResponse) {
        JSONObject jsonSnake = jsonResponse.getJSONObject("snake");
        JSONArray jsonHead = jsonSnake.getJSONArray("head");
        Position head = new Position(jsonHead.getInt(0), jsonHead.getInt(1));
        ArrayList<Position> positions = new ArrayList<>();
        positions.add(head);

        AgentAction action = null;

        switch (jsonSnake.get("last-action").toString()) {
            case "up":
                action = AgentAction.MOVE_UP;
                break;
            case "down":
                action = AgentAction.MOVE_DOWN;
                break;
            case "left":
                action = AgentAction.MOVE_LEFT;
                break;
            case "right":
                action = AgentAction.MOVE_RIGHT;
                break;
        }

        FeaturesSnake snake = new FeaturesSnake(positions, action, ColorSnake.Green,
                jsonSnake.getBoolean("isInvincible"), jsonSnake.getBoolean("isSick"));
        ArrayList<FeaturesSnake> snakes = new ArrayList<>();
        snakes.add(snake);

        JSONArray jsonItems = jsonResponse.getJSONArray("items");
        ArrayList<FeaturesItem> items = new ArrayList<>();

        for (int i = 0; i < jsonItems.length(); i++) {
            ItemType type = null;
            JSONObject jsonItem = (JSONObject) jsonItems.get(i);

            switch (jsonItem.get("type").toString()) {
                case "apple":
                    type = ItemType.APPLE;
                    break;
                case "box":
                    type = ItemType.BOX;
                    break;
                case "sick":
                    type = ItemType.SICK_BALL;
                    break;
                case "invincibility":
                    type = ItemType.INVINCIBILITY_BALL;
                    break;
            }

            JSONArray coordinates = jsonItem.getJSONArray("coordinates");
            FeaturesItem item = new FeaturesItem(coordinates.getInt(0), coordinates.getInt(1), type);
            items.add(item);
        }

        panelSnakeGame.updateInfoGame(snakes, items);
    }

    public static void play(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream out = new PrintStream(socket.getOutputStream());
        JSONObject jsonRequest;
        JSONObject jsonResponse;

        // do {
            jsonRequest = new JSONObject();
            jsonRequest.put("type", "play");
            out.println(jsonRequest);
            // jsonResponse = new JSONObject(in.readLine());
        // } while (!jsonResponse.get("type").equals("return-play"));
    }

    public static void restart(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream out = new PrintStream(socket.getOutputStream());
        JSONObject jsonRequest;
        JSONObject jsonResponse;

        // do {
            jsonRequest = new JSONObject();
            jsonRequest.put("type", "restart");
            out.println(jsonRequest);
            // jsonResponse = new JSONObject(in.readLine());
        // } while (!jsonResponse.get("type").equals("return-restart"));
    }

    public static void pause(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream out = new PrintStream(socket.getOutputStream());
        JSONObject jsonRequest;
        JSONObject jsonResponse;

        // do {
            jsonRequest = new JSONObject();
            jsonRequest.put("type", "pause");
            out.println(jsonRequest);
            // jsonResponse = new JSONObject(in.readLine());
        // } while (!jsonResponse.get("type").equals("return-pause"));
    }
}
