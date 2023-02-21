package states;

import model.Game;

public class RestartState implements State {
    Game game;

    public RestartState(Game game) {
        this.game = game;
    }

    public void restart() {
        throw new UnsupportedOperationException();
    }

    public void step() { game.step(); }

    public void play() { game.launch(); }

    public void pause() {
        throw new UnsupportedOperationException();
    }
}
