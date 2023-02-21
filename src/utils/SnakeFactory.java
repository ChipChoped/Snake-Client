package utils;

import java.util.ArrayList;

public class SnakeFactory {
    public static Snake newSnake(ArrayList<Position> positions, AgentAction lastAction, ColorSnake colorSnake, boolean isInvincible, boolean isSick) {
        return new Snake(positions, lastAction, colorSnake, isInvincible, isSick);
    }

    public static Snake featureSnakeToSnake(FeaturesSnake snake) {
        return new Snake(snake.getPositions(), snake.getLastAction(), snake.getColorSnake(), snake.isInvincible(), snake.isSick());
    }
}
