package states;

import model.Game;

public class PlayState implements State {
    Game game;

    public PlayState(Game game) {
        this.game = game;
    }

    public void restart() {
        game.initializeGame();
    }

    public void step() {
        throw new UnsupportedOperationException();
    }

    public void play() {
        throw new UnsupportedOperationException();
    }

    public void pause() {
        game.pause();
    }
}
