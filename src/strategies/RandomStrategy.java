package strategies;

import model.SnakeGame;
import utils.AgentAction;
import utils.Snake;

import java.util.ArrayList;
import java.util.Random;

public class RandomStrategy implements Strategy {
    public void move(SnakeGame game) {
        Random rand = new Random();
        ArrayList<Snake> otherSnakes;
        ArrayList<Snake> eleminatedSnakes = new ArrayList<>();
        boolean eliminated = false;

        for (int i = 0; i < game.getSnakes().size(); i++) {
            otherSnakes = (ArrayList<Snake>) game.getSnakes().clone();
            otherSnakes.remove(i);
            otherSnakes.removeAll(eleminatedSnakes);

            if (eleminatedSnakes.contains(game.getSnakes().get(i)))
                i++;
            else {
                switch (rand.nextInt(4)) {
                    case 0:
                        eliminated = !game.getSnakes().get(i).getBehavior().moveAgent(game, game.getSnakes().get(i), AgentAction.MOVE_UP, otherSnakes);
                        break;
                    case 1:
                        eliminated = !game.getSnakes().get(i).getBehavior().moveAgent(game, game.getSnakes().get(i), AgentAction.MOVE_DOWN, otherSnakes);
                        break;
                    case 2:
                        eliminated = !game.getSnakes().get(i).getBehavior().moveAgent(game, game.getSnakes().get(i), AgentAction.MOVE_LEFT, otherSnakes);
                        break;
                    case 3:
                        eliminated = !game.getSnakes().get(i).getBehavior().moveAgent(game, game.getSnakes().get(i), AgentAction.MOVE_RIGHT, otherSnakes);
                        break;
                }

                ArrayList<Snake> difference = new ArrayList<>(game.getSnakes());
                difference.removeAll(otherSnakes);

                if (!eliminated) {
                    difference.remove(game.getSnakes().get(i));
                }

                eleminatedSnakes.addAll(difference);
            }
        }

        game.getSnakes().removeAll(eleminatedSnakes);
    }
}
