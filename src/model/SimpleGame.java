package model;

import model.Game;

public class SimpleGame extends Game {
    public SimpleGame(int maxturn) {
        super(maxturn);
    }

    public void initializeGame() {
        this.turn = 0;
        this.isRunning = false;
        this.time = 100;

        setChanged();
        notifyObservers();
    }

    protected void takeTurn() {

    }

    public boolean gameContinue() {
        return turn != maxturn;
    }

    protected void gameOver() {
        System.out.println("Game Over!");

        setChanged();
        notifyObservers();
    }
}
