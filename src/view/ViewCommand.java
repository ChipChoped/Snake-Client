package view;

import behaviors.Behaviors;
import behaviors.InvincibleBehavior;
import controllers.ControllerSnakeGame;
import model.SnakeGame;
import org.json.JSONObject;
import states.EndState;
import utils.AgentAction;
import utils.FeaturesSnake;
import utils.Snake;
import utils.User;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

import static functions.Game.*;
import static functions.Request.*;

public class ViewCommand extends Observable {
    protected User user;
    protected Socket socket;

    protected JFrame frame;
    protected JLabel scoreLabel = new JLabel("Score : 0", JLabel.CENTER);
    protected JLabel turnNumberLabel = new JLabel("Turn : 0", JLabel.CENTER);

    protected boolean playerAlive = true;

    protected ImageIcon heartIcon;
    protected ImageIcon sickHeartIcon;
    protected ImageIcon invincibleHeartIcon;

    protected JLabel heartLabel;

    protected Behaviors playerBehavior = Behaviors.NORMAL;

    protected BufferedReader in;
    protected PrintStream out;

    public static AgentAction nextAction;

    public ViewCommand(Socket socket, User user) throws IOException {
        this.user = user;
        this.socket = socket;

        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintStream(socket.getOutputStream());

        frame = new JFrame();
        frame.setTitle("Snake Command Panel");
        frame.setSize(new Dimension(700, 330));

        Dimension windowSize = frame.getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2 + 350;
        frame.setLocation(dx,dy);

        JPanel mainPanel = new JPanel();
        JPanel gameCommandsPanel = new JPanel();
        JPanel gameInfoPanel = new JPanel();

        mainPanel.setLayout(new GridLayout(2, 1));
        gameCommandsPanel.setLayout(new GridLayout(1, 3));
        gameInfoPanel.setLayout(new GridLayout(2, 2));

        Icon restartIcon = new ImageIcon("icons/icon_restart.png");
        Icon playIcon = new ImageIcon("icons/icon_play.png");
        Icon stepIcon = new ImageIcon("icons/icon_step.png");
        Icon pauseIcon = new ImageIcon("icons/icon_pause.png");

        heartIcon = new ImageIcon("images/heart.png");
        Image heartImage = heartIcon.getImage();
        Image resizedHeartImage = heartImage.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
        heartIcon = new ImageIcon(resizedHeartImage);
        ImageIcon finalHeartIcon = heartIcon;

        sickHeartIcon = new ImageIcon("images/sickHeart.png");
        Image sickHeartImage = sickHeartIcon.getImage();
        Image resizedSickHeartImage = sickHeartImage.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
        sickHeartIcon = new ImageIcon(resizedSickHeartImage);

        invincibleHeartIcon = new ImageIcon("images/invincibleHeart.png");
        Image invincibleHeartImage = invincibleHeartIcon.getImage();
        Image resizedInvincibleHeartImage = invincibleHeartImage.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
        invincibleHeartIcon = new ImageIcon(resizedInvincibleHeartImage);

        JButton restartButton = new JButton(restartIcon);
        JButton playButton = new JButton(playIcon);
        JButton stepButton = new JButton(stepIcon);
        JButton pauseButton = new JButton(pauseIcon);
        JButton exitButton = new JButton("Exit");

        restartButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stepButton.setEnabled(false);

        this.heartLabel = new JLabel(heartIcon);

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                restartButton.setEnabled(false);
                pauseButton.setEnabled(false);
                playButton.setEnabled(true);

                try {
                    restart(socket);
                    frame.requestFocus();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                playerAlive = true;
                heartLabel.setIcon(finalHeartIcon);
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                playButton.setEnabled(false);
                restartButton.setEnabled(true);
                pauseButton.setEnabled(true);

                try {
                    play(socket);
                    frame.requestFocus();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                restartButton.setEnabled(true);
                pauseButton.setEnabled(false);
                playButton.setEnabled(true);

                try {
                    pause(socket);
                    frame.requestFocus();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    closeGame();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                try {
                    closeGame();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        gameCommandsPanel.add(restartButton);
        gameCommandsPanel.add(playButton);
        gameCommandsPanel.add(pauseButton);

        gameInfoPanel.add(turnNumberLabel);
        gameInfoPanel.add(exitButton);
        gameInfoPanel.add(scoreLabel);
        gameInfoPanel.add(heartLabel);

        mainPanel.add(gameCommandsPanel);
        mainPanel.add(gameInfoPanel);
        frame.add(mainPanel);

        frame.setVisible(true);

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {}

            @Override
            public void keyPressed(KeyEvent keyEvent) {
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

                switch(keyEvent.getKeyCode()) {
                    case KeyEvent.VK_UP, KeyEvent.VK_Z:
                        ViewCommand.nextAction = AgentAction.MOVE_UP;
                        break;
                    case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                        ViewCommand.nextAction = AgentAction.MOVE_DOWN;
                        break;
                    case KeyEvent.VK_LEFT, KeyEvent.VK_Q:
                        ViewCommand.nextAction = AgentAction.MOVE_LEFT;
                        break;
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                        ViewCommand.nextAction = AgentAction.MOVE_RIGHT;
                        break;
                }
            }
        });

        frame.requestFocusInWindow();

        new Test().execute();
    }

    private void closeGame() throws IOException {
        System.gc();

        for (Window window : Window.getWindows())
            window.dispose();

        pause(socket);
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("type", "exit");
        this.out.println(jsonRequest);

        ViewGameMenu.frame.setVisible(true);
    }

    private void updatePlayerBehavior(Behaviors behavior) {
        switch (behavior) {
            case NORMAL:
                this.heartLabel.setIcon(this.heartIcon);
                break;
            case SICK:
                this.heartLabel.setIcon(this.sickHeartIcon);
                break;
            case INVINCIBLE:
                this.heartLabel.setIcon(this.invincibleHeartIcon);
                break;
        }
    }

    private void updatePlayersLives(ArrayList<FeaturesSnake> snakes) {
        boolean snakeAlive = snakes.size() == 1;

        if (this.playerAlive) {
            if (!snakeAlive)
                this.playerAlive = false;

            if (this.playerAlive && snakeAlive) {
                Behaviors behavior;

                if (snakes.get(0).isInvincible())
                    behavior = Behaviors.INVINCIBLE;
                else if (snakes.get(0).isSick())
                    behavior = Behaviors.SICK;
                else
                    behavior = Behaviors.NORMAL;

                if (behavior != this.playerBehavior) {
                    this.playerBehavior = behavior;
                    updatePlayerBehavior(behavior);
                }
            }
        }
    }

    public void updateGame(JSONObject jsonResponse) {
        scoreLabel.setText("Score : " + jsonResponse.get("score").toString());
        turnNumberLabel.setText("Turn : " + jsonResponse.get("turn").toString());
        updatePlayersLives(getSnakes(jsonResponse));

        setChanged();
        notifyObservers(jsonResponse);
    }

    /*public void update(Observable observable, Object o) {
        SnakeGame game = (SnakeGame) observable;
        scoreLabel.setText("Score : " + game.getScore());
        turnNumberLabel.setText("Turn : " + game.getTurn());

        updatePlayersLives(game.getSnakes());

        *//*if (game.isGameOver()) {
            try {
                saveGame(this.socket, user.ID(), game.isWon(), game.getScore());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }*//*
    }*/

    class Test extends SwingWorker {
        @Override
        protected Object doInBackground() throws Exception {
            while (true){
                try {
                    JSONObject jsonResponse = new JSONObject(in.readLine());

                    switch ((String) jsonResponse.get("type")) {
                        case "update-game":
                            updateGame(jsonResponse);

                            JSONObject jsonRequest = new JSONObject();
                            jsonRequest.put("type", "next-action");
                            jsonRequest.put("next-action", ViewCommand.nextAction.toString());

                            out.println(jsonRequest);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}