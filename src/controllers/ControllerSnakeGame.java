package controllers;

import model.SnakeGame;
import model.InputMap;
import states.*;
import strategies.Strategy;
import utils.User;
import view.PanelSnakeGame;
import view.ViewCommand;
import view.ViewSnakeGame;

import java.net.Socket;

public class ControllerSnakeGame extends AbstractController {
    private State state;

    public ControllerSnakeGame(Socket socket, User user, int maxTurn, Strategy strategy, String mapPath) throws Exception {
        InputMap map = new InputMap(mapPath);
        PanelSnakeGame panelSnakeGame = new PanelSnakeGame(map.getSizeX(), map.getSizeY(), map.get_walls(), map.getStart_snakes(), map.getStart_items());
        this.game = new SnakeGame(maxTurn, map.getStart_snakes(), map.getStart_items(), map.get_walls()[0][0], map.getSizeX(), map.getSizeY(), strategy);
        this.state = new RestartState(game);

//        ViewCommand viewCommand = new ViewCommand(this.game, socket, user, this);
//        ViewSnakeGame viewSnakeGame = new ViewSnakeGame(this.game, socket, user, this, panelSnakeGame);
    }

    public SnakeGame getGame() { return (SnakeGame) this.game; }
    public State getState() { return this.state; }

    public void restart() {
        this.state.restart();
        this.state = new RestartState(this.game);
    }

    public void step() {
        this.state.step();

        if (!(this.state instanceof StepState))
            this.state = new StepState(this.game);

        if (!game.gameContinue())
            this.state = new EndState(game);
    }

    public void play() {
        this.state.play();
        this.state = new PlayState(this.game);
    }

    public void pause() {
        this.game.pause();
        this.state = new PauseState(game);
    }

    public void setSpeed(int speed) {
        this.game.setSpeed(speed);
    }
}
