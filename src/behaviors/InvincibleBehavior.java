package behaviors;

import utils.*;

import java.util.ArrayList;

public class InvincibleBehavior extends Behavior {
    @Override
    protected boolean moveIfNotEliminated(Snake snake, Position position, AgentAction lastAction, ArrayList<Snake> otherSnakes, ArrayList<Item> items, int sizeX, int sizeY, boolean withWalls) {
        if (!isEliminated(snake, position, otherSnakes, sizeX, sizeY, withWalls)) {
            onItem(snake, position, items, 100, sizeX, sizeY, withWalls);

            for (int i = snake.getPositions().size() - 1; i > 0; i--)
                snake.getPositions().set(i, snake.getPositions().get(i-1));

            snake.getPositions().set(0, position);
            snake.setLastAction(lastAction);
        }

        return true;
    }

    @Override
    public Behaviors getBehaviorType() {
        return Behaviors.INVINCIBLE;
    }
}
