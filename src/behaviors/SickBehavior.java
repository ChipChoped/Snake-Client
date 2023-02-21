package behaviors;

import utils.AgentAction;
import utils.Item;
import utils.Position;
import utils.Snake;

import java.util.ArrayList;

public class SickBehavior extends Behavior {
    @Override
    protected boolean moveIfNotEliminated(Snake snake, Position position, AgentAction lastAction, ArrayList<Snake> otherSnakes, ArrayList<Item> items, int sizeX, int sizeY, boolean withWalls) {
        if (!isEliminated(snake, position, otherSnakes, sizeX, sizeY, withWalls)) {
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
