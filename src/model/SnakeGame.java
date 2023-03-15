package model;

import model.Game;
import strategies.Strategy;
import utils.*;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class SnakeGame extends Game {
    private final ArrayList<Snake> initialSnakes;
    private final ArrayList<Item> initialItems;

    private ArrayList<Snake> snakes;
    private ArrayList<Item> items;

    private ArrayList<AgentAction> nextMoves;
    private final Strategy strategy;

    private final boolean withWalls;
    private final int sizeX;
    private final int sizeY;

    private int score;
    private boolean won;

    public SnakeGame(int maxTurn, ArrayList<FeaturesSnake> snakes, ArrayList<FeaturesItem> items, boolean withWalls, int sizeX, int sizeY, Strategy strategy) {
        super(maxTurn);
        this.initialSnakes = new ArrayList<>();
        this.initialItems = new ArrayList<>();
        this.nextMoves = new ArrayList<>();
        this.snakes = new ArrayList<>();
        this.items = new ArrayList<>();
        this.withWalls = withWalls;
        this.strategy = strategy;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        for (FeaturesSnake snake : snakes) {
            this.initialSnakes.add(SnakeFactory.featureSnakeToSnake(snake));
            this.nextMoves.add(snake.getLastAction());
        }

        for (FeaturesItem item : items)
            this.initialItems.add(new Item(item));
}

    public ArrayList<Snake> getInitialSnakes() { return this.initialSnakes; }
    public ArrayList<Snake> getSnakes() { return this.snakes; }
    public ArrayList<Item> getItems() { return  this.items; }

    public ArrayList<AgentAction> getNextMoves() { return this.nextMoves; }

    public boolean getWithWalls() { return this.withWalls; }
    public int getSizeX() { return this.sizeX; }
    public int getSizeY() { return this.sizeY; }

    public int getScore() {
        return score;
    }
    public boolean isWon() {
        return won;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public ArrayList<FeaturesSnake> getFeaturesSnakes() {
        ArrayList<FeaturesSnake> snakes = new ArrayList<>();

        for (Snake snake : this.snakes)
            snakes.add(new FeaturesSnake(snake.getPositions(), snake.getLastAction(), snake.getColorSnake(), snake.isInvincible(), snake.isSick()));

        return snakes;
    }

    public ArrayList<FeaturesItem> getFeaturesItems() {
        ArrayList<FeaturesItem> items = new ArrayList<>();

        for (Item item : this.items)
            items.add(new FeaturesItem(item.getX(), item.getY(), item.getItemType()));

        return items;
    }

    public void initializeGame() {
        this.turn = 0;
        this.score = 0;
        this.time = 100;
        this.gameOver = false;
        this.isRunning = false;

        this.snakes = new ArrayList<Snake>();
        this.items = new ArrayList<Item>();

        for (Snake snake : initialSnakes)
            this.snakes.add(new Snake(snake));

        for (Item item : initialItems)
            this.items.add(new Item(item));

        setChanged();
        notifyObservers();
    }

    protected void takeTurn() {
        this.strategy.move(this);

        setChanged();
        notifyObservers();
    }

    public boolean gameContinue() {
        return turn != maxturn && snakes.size() > 0;
    }

    protected void gameOver() {
        System.out.println("Game Over!");

        this.gameOver = true;
        this.won = turn == maxturn;
    }
}
