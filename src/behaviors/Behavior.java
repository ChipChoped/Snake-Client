package behaviors;

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

    protected boolean isEliminated(Snake snake, Position position, ArrayList<Snake> otherSnakes, int sizeX, int sizeY, boolean withWalls) {
        if (withWalls && (position.getX() == 0 || position.getY() == 0 ||
                position.getX() == sizeX - 1 || position.getY() == sizeY - 1)) {
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

    protected ArrayList<Item> eatApple(Snake snake, ArrayList<Item> items, int pItem, int sizeX, int sizeY, boolean withWalls) {
        int x;
        int y;
        int border = 0;
        Random randApple = new Random();
        ArrayList<Item> newItems = new ArrayList<>();
        ArrayList<Position> unavailablePositions = new ArrayList<>();

        if (withWalls)
            border = 1;

        for (Item item : items)
            unavailablePositions.add(new Position(item.getX(), item.getY()));

        unavailablePositions.addAll(snake.getPositions());

        do {
            x = new Random().nextInt(sizeX + border - 1) - border;
            y = new Random().nextInt(sizeY + border - 1) - border;
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
                x = new Random().nextInt(sizeX) + border;
                y = new Random().nextInt(sizeY) + border;
            } while (unavailablePositions.contains(new Position(x, y)));

            newItems.add(new Item(x, y, type));
        }

        return newItems;
    }

    protected boolean onItem(Snake snake, Position position, ArrayList<Item> items, int pItem, int sizeX, int sizeY, boolean withWalls) {
        for (Item item : items)
            if (item.getX() == position.getX() && item.getY() == position.getY()) {
                switch (item.getItemType()) {
                    case APPLE:
                        ArrayList<Item> itemsGenerated = eatApple(snake, items,pItem, sizeX, sizeY, withWalls);
                        items.addAll(itemsGenerated);
                        items.remove(item);
                        return true;
                    case SICK_BALL:
                        snake.setBehavior(new SickBehavior());
                        items.remove(item);
                        return true;
                    case INVINCIBILITY_BALL:
                        snake.setBehavior(new InvincibleBehavior());
                        items.remove(item);
                        return true;
                    case BOX:
                        Random randBox = new Random();
                        if (randBox.nextBoolean())
                            snake.setBehavior(new SickBehavior());
                        else
                            snake.setBehavior(new InvincibleBehavior());

                        items.remove(item);
                        return true;
                }
            }

        return false;
    }

    public boolean moveAgent(Snake snake, AgentAction action, ArrayList<Snake> otherSnakes, ArrayList<Item> items, int sizeX, int sizeY, boolean withWalls) {
        if (!snake.getBehavior().effectStillOn())
            snake.setBehavior(new NormalBehavior());

        if (!isLegalMove(snake, action))
            action = snake.getLastAction();

        AgentAction lastAction = snake.getLastAction();
        Position position = new Position(snake.getPositions().get(0));

        int move = 1;

        switch (action) {
            case MOVE_UP:
                if (!withWalls && position.getY() == 0)
                    move = -sizeY + 1;
                position.setY(position.getY() - move);
                lastAction = AgentAction.MOVE_UP;
                break;
            case MOVE_DOWN:
                if (!withWalls && position.getY() == sizeY - 1)
                    move = -sizeY + 1;
                position.setY(position.getY() + move);
                lastAction = AgentAction.MOVE_DOWN;
                break;
            case MOVE_LEFT:
                if (!withWalls && position.getX() == 0)
                    move = -sizeX + 1;
                position.setX(position.getX() - move);
                lastAction = AgentAction.MOVE_LEFT;
                break;
            case MOVE_RIGHT:
                if (!withWalls && position.getX() == sizeX - 1)
                    move = -sizeX + 1;
                position.setX(position.getX() + move);
                lastAction = AgentAction.MOVE_RIGHT;
                break;
        }

        return moveIfNotEliminated(snake, position, lastAction, otherSnakes, items, sizeX, sizeY, withWalls);
    }

    protected abstract boolean moveIfNotEliminated(Snake snake, Position position, AgentAction lastAction, ArrayList<Snake> otherSnakes, ArrayList<Item> items, int sizeX, int sizeY, boolean withWalls);
    public abstract Behaviors getBehaviorType();
}
