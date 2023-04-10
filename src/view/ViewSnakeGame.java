package view;

import org.json.JSONObject;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import static functions.Game.pause;
import static functions.Game.updateGame;

public class ViewSnakeGame implements Observer {
    public static JFrame frame = new JFrame();

    private PanelSnakeGame panelSnakeGame;
    private AgentAction nextAction;

    private BufferedReader in;
    private PrintStream out;

    public ViewSnakeGame(Observable observable, Socket socket, User user, PanelSnakeGame panelSnakeGame, AgentAction nextAction) throws IOException {
        observable.addObserver(this);

        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintStream(socket.getOutputStream());

        this.panelSnakeGame = panelSnakeGame;
        this.nextAction = nextAction;

        frame.setTitle("Snake");
        frame.setSize(new Dimension(panelSnakeGame.getSizeX() * 40, panelSnakeGame.getSizeY() * 40));
        frame.setVisible(true);

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

                try {
                    pause(socket);
                    PrintStream out = new PrintStream(socket.getOutputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("type", "exit");
                out.println(jsonRequest);

                ViewGameMenu.frame.setVisible(true);
            }
        });

        try {
            frame.add(this.panelSnakeGame);
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void update(Observable observable, Object o) {
        updateGame(this.panelSnakeGame, (JSONObject) o);
        frame.repaint();

        System.out.println(o);
        System.out.println(this.panelSnakeGame.featuresSnakes.get(0).getPositions().get(0).getY());
    }
}
