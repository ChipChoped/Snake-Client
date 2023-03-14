package behaviors;

import model.SnakeGame;
import utils.AgentAction;
import utils.Item;
import utils.Position;
import utils.Snake;

import java.util.ArrayList;

public class SickBehavior extends Behavior {
    @Override
    protected boolean moveIfNotEliminated(SnakeGame game, Snake snake, Position position, AgentAction lastAction, ArrayList<Snake> otherSnakes) {
        if (!isEliminated(game, snake, position, otherSnakes)) {
            for (int i = snake.getPositions().size() - 1; i > 0; i--)
                snake.getPositions().set(i, snake.getPositions().get(i-1));

            snake.getPositions().set(0, position);
            snake.setLastAction(lastAction);

            return true;
        }
        else
            return false;
    }

    @Override
    public Behaviors getBehaviorType() {
        return Behaviors.SICK;
    }
}
