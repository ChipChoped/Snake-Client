package model;

import java.util.Observable;

public abstract class Game extends Observable implements Runnable {
    protected int turn; // Nombre de tours du jeu
    protected int maxturn; // Nombre maximum de tours du jeu prédéfinie à sa création
    protected boolean isRunning; // false : Jeu en pause
    protected Thread thread;
    protected long time;
    protected boolean gameOver;

    public Game(int maxturn) {
        this.maxturn = maxturn;
        this.gameOver = false;
    }

    public int getTurn() { return this.turn; }

    public boolean isGameOver() {
        return gameOver;
    }

    public abstract void initializeGame();
    protected abstract void takeTurn();
    public abstract boolean gameContinue();
    protected abstract void gameOver();

    // Remise à 0 des valeurs du jeu
    protected void init() {
        this.turn = 0;
        this.isRunning = true;
        this.initializeGame();

        setChanged();
        notifyObservers();
    }

    // Fait avancer les tours en vérifiant l'état du jeu
    public void step() {
        this.turn++;

        if (this.gameContinue())
            this.takeTurn();
        else {
            this.isRunning = false;
            gameOver();
        }

        setChanged();
        notifyObservers();
    }

    // Met le jeu en pause
    public void pause() {
        this.isRunning = false;
        setChanged();
        notifyObservers();
    }

    public void setSpeed(int time) {
        this.time = 1000 / time;
    }

    // Fait tourner le jeu
    public void run() {
        while(this.isRunning) {
            this.step();
            try {
                Thread.sleep(this.time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Lancement du jeu dans un thread
    public void launch() {
        this.isRunning = true;
        setChanged();
        notifyObservers();

        this.thread = new Thread(this);
        thread.start();
    }
}
