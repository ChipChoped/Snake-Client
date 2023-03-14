package behaviors;

import model.SnakeGame;
import utils.*;

import java.util.ArrayList;

public class InvincibleBehavior extends Behavior {
    @Override
    protected boolean moveIfNotEliminated(SnakeGame game, Snake snake, Position position, AgentAction lastAction, ArrayList<Snake> otherSnakes) {
        if (!isEliminated(game, snake, position, otherSnakes)) {
            onItem(game, snake, position,100);

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
