package view;

import controllers.ControllerSnakeGame;
import model.SnakeGame;
import utils.AgentAction;
import utils.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ViewSnakeGame implements Observer, KeyListener {
    protected ControllerSnakeGame controller;
    protected PanelSnakeGame panelSnakeGame;
    JFrame frame = new JFrame();

    public ViewSnakeGame(Observable obs, Socket socket, User user, ControllerSnakeGame controller, PanelSnakeGame panelSnakeGame) {
        obs.addObserver(this);
        this.controller = controller;
        this.panelSnakeGame = panelSnakeGame;

        frame.setTitle("Snake");
        frame.setSize(new Dimension(panelSnakeGame.getSizeX() * 40, panelSnakeGame.getSizeY() * 40));
        frame.setVisible(true);
        frame.addKeyListener(this);
        frame.requestFocus();

        Dimension windowSize = frame.getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.width / 2;
        frame.setLocation(dx,dy);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.gc();

                for (Window window : Window.getWindows())
                    window.dispose();

                ViewGameMenu viewGameMenu = new ViewGameMenu(socket, user);
            }
        });

        try {
            frame.add(this.panelSnakeGame);
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        this.controller.getGame().initializeGame();
    }

    public void keyPressed(KeyEvent keyEvent) {
        switch(keyEvent.getKeyCode()) {
            case KeyEvent.VK_UP:
                controller.getGame().getNextMoves().set(0, AgentAction.MOVE_UP);
                break;
            case KeyEvent.VK_DOWN:
                controller.getGame().getNextMoves().set(0, AgentAction.MOVE_DOWN);
                break;
            case KeyEvent.VK_LEFT:
                controller.getGame().getNextMoves().set(0, AgentAction.MOVE_LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                controller.getGame().getNextMoves().set(0, AgentAction.MOVE_RIGHT);
                break;
            case KeyEvent.VK_Z:
                controller.getGame().getNextMoves().set(1, AgentAction.MOVE_UP);
                break;
            case KeyEvent.VK_S:
                controller.getGame().getNextMoves().set(1, AgentAction.MOVE_DOWN);
                break;
            case KeyEvent.VK_Q:
                controller.getGame().getNextMoves().set(1, AgentAction.MOVE_LEFT);
                break;
            case KeyEvent.VK_D:
                controller.getGame().getNextMoves().set(1, AgentAction.MOVE_RIGHT);
                break;
        }
    }

    public void keyTyped(KeyEvent keyEvent) {}
    public void keyReleased(KeyEvent keyEvent) {}

    public void update(Observable observable, Object o) {
        SnakeGame game = (SnakeGame) observable;
        this.panelSnakeGame.updateInfoGame(game.getFeaturesSnakes(), game.getFeaturesItems());
        frame.repaint();
        frame.requestFocus();
    }
}
