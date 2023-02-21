package strategies;

import model.SnakeGame;
import utils.Snake;

import java.util.ArrayList;

public class InteractiveStrategy implements Strategy {
    public void move(SnakeGame game) {
        ArrayList<Snake> otherSnakes;
        ArrayList<Snake> eleminatedSnakes = new ArrayList<>();
        boolean eliminated = false;
        int maxIt = game.getSnakes().size();

        if (maxIt >= 2)
            maxIt = 2;
        else
            maxIt = 1;

        for (int i = 0; i < maxIt; i++) {
            otherSnakes = (ArrayList<Snake>) game.getSnakes().clone();
            otherSnakes.remove(i);
            otherSnakes.removeAll(eleminatedSnakes);

            if (eleminatedSnakes.contains(game.getSnakes().get(i)))
                i++;
            else {
                eliminated = !game.getSnakes().get(i).getBehavior().moveAgent(game.getSnakes().get(i), game.getNextMoves().get(i), otherSnakes, game.getItems(), game.getSizeX(), game.getSizeY(), game.getWithWalls());

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
