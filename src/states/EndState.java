package states;

import model.Game;

public class EndState implements State {
    Game game;

    public EndState(Game game) {
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
        throw new UnsupportedOperationException();
    }
}
