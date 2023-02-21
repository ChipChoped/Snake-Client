package states;

import model.Game;

public class StepState implements State {
    Game game;

    public StepState(Game game) {
        this.game = game;
    }

    public void restart() {
        game.initializeGame();
    }

    public void step() {
        game.step();
    }

    public void play() {
        game.launch();
    }

    public void pause() {
        throw new UnsupportedOperationException();
    }
}
