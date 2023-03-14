package behaviors;

import model.SnakeGame;
import utils.*;

import java.util.ArrayList;
import java.util.Random;

public abstract class Behavior {
    private int effectTurnCount;

    public Behavior() { this.effectTurnCount = 0; }

    protected boolean effectStillOn() {
        if (this.effectTurnCount == 20)
            return false;
        else {
            this.effectTurnCount++;
            return true;
        }
    }

    protected boolean isLegalMove(Snake snake, AgentAction action) {
        return (action == AgentAction.MOVE_UP && snake.getLastAction() != AgentAction.MOVE_DOWN) ||
                (action == AgentAction.MOVE_DOWN && snake.getLastAction() != AgentAction.MOVE_UP) ||
                (action == AgentAction.MOVE_LEFT && snake.getLastAction() != AgentAction.MOVE_RIGHT ||
                (action == AgentAction.MOVE_RIGHT && snake.getLastAction() != AgentAction.MOVE_LEFT));
    }

    protected boolean isEliminated(SnakeGame game, Snake snake, Position position, ArrayList<Snake> otherSnakes) {
        if (game.getWithWalls() && (position.getX() == 0 || position.getY() == 0 ||
                position.getX() == game.getSizeX() - 1 || position.getY() == game.getSizeY() - 1)) {
            return true;
        }

        for (int i = 2; i < snake.getPositions().size() - 1; i++)
            if (Snake.collision(position, snake.getPositions().get(i)))
                return true;

        for (Snake otherSnake : otherSnakes)
            if (otherSnake.getPositions().contains(position)) {
                if (Snake.collision(position, otherSnake.getPositions().get(0)) &&
                        snake.getPositions().size() == otherSnake.getPositions().size()) {
                    otherSnakes.remove(otherSnake);
                    return true;
                } else if (snake.getPositions().size() >= otherSnake.getPositions().size()) {
                    otherSnakes.remove(otherSnake);
                    return false;
                } else {
                    return true;
                }
            }

        return false;
    }

    protected ArrayList<Item> eatApple(SnakeGame game, Snake snake, int pItem) {
        int x;
        int y;
        int border = 0;
        Random randApple = new Random();
        ArrayList<Item> newItems = new ArrayList<>();
        ArrayList<Position> unavailablePositions = new ArrayList<>();

        if (game.getWithWalls())
            border = 1;

        for (Item item : game.getItems())
            unavailablePositions.add(new Position(item.getX(), item.getY()));

        unavailablePositions.addAll(snake.getPositions());

        do {
            x = new Random().nextInt(game.getSizeX() + border - 1) - border;
            y = new Random().nextInt(game.getSizeY() + border - 1) - border;
        } while (unavailablePositions.contains(new Position(x, y)));

        unavailablePositions.add(new Position(x, y));
        newItems.add(new Item(x, y, ItemType.APPLE));

        if (randApple.nextInt(101) <= pItem) {

            ItemType type;

            int randItem = randApple.nextInt(3);

            if (randItem == 0)
                type = ItemType.SICK_BALL;
            else if (randItem == 1)
                type = ItemType.INVINCIBILITY_BALL;
            else
                type = ItemType.BOX;

            snake.getPositions().add(new Position(snake.getPositions().get(snake.getPositions().size() - 1)));

            do {
                x = new Random().nextInt(game.getSizeX()) + border;
                y = new Random().nextInt(game.getSizeY()) + border;
            } while (unavailablePositions.contains(new Position(x, y)));

            newItems.add(new Item(x, y, type));
        }

        return newItems;
    }

    protected boolean onItem(SnakeGame game, Snake snake, Position position, int pItem) {
        for (Item item : game.getItems())
            if (item.getX() == position.getX() && item.getY() == position.getY()) {
                switch (item.getItemType()) {
                    case APPLE:
                        ArrayList<Item> itemsGenerated = eatApple(game, snake, pItem);
                        game.getItems().addAll(itemsGenerated);
                        game.getItems().remove(item);
                        game.addScore(30);
                        return true;
                    case SICK_BALL:
                        snake.setBehavior(new SickBehavior());
                        game.getItems().remove(item);
                        game.addScore(-5);
                        return true;
                    case INVINCIBILITY_BALL:
                        snake.setBehavior(new InvincibleBehavior());
                        game.getItems().remove(item);
                        game.addScore(5);
                        return true;
                    case BOX:
                        Random randBox = new Random();
                        if (randBox.nextBoolean())
                            snake.setBehavior(new SickBehavior());
                        else
                            snake.setBehavior(new InvincibleBehavior());

                        game.getItems().remove(item);
                        game.addScore(10);
                        return true;
                }
            }

        return false;
    }

    public boolean moveAgent(SnakeGame game, Snake snake, AgentAction action, ArrayList<Snake> otherSnakes) {
        if (!snake.getBehavior().effectStillOn())
            snake.setBehavior(new NormalBehavior());

        if (!isLegalMove(snake, action))
            action = snake.getLastAction();

        AgentAction lastAction = snake.getLastAction();
        Position position = new Position(snake.getPositions().get(0));

        int move = 1;

        switch (action) {
            case MOVE_UP:
                if (!game.getWithWalls() && position.getY() == 0)
                    move = -game.getSizeY() + 1;
                position.setY(position.getY() - move);
                lastAction = AgentAction.MOVE_UP;
                break;
            case MOVE_DOWN:
                if (!game.getWithWalls() && position.getY() == game.getSizeY() - 1)
                    move = -game.getSizeY() + 1;
                position.setY(position.getY() + move);
                lastAction = AgentAction.MOVE_DOWN;
                break;
            case MOVE_LEFT:
                if (!game.getWithWalls() && position.getX() == 0)
                    move = -game.getSizeX() + 1;
                position.setX(position.getX() - move);
                lastAction = AgentAction.MOVE_LEFT;
                break;
            case MOVE_RIGHT:
                if (!game.getWithWalls() && position.getX() == game.getSizeX() - 1)
                    move = -game.getSizeX() + 1;
                position.setX(position.getX() + move);
                lastAction = AgentAction.MOVE_RIGHT;
                break;
        }

        return moveIfNotEliminated(game, snake, position, lastAction, otherSnakes);
    }

    protected abstract boolean moveIfNotEliminated(SnakeGame game, Snake snake, Position position, AgentAction lastAction, ArrayList<Snake> otherSnakes);
    public abstract Behaviors getBehaviorType();
}
