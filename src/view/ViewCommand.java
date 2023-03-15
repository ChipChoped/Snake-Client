package view;

import behaviors.Behaviors;
import controllers.ControllerSnakeGame;
import model.SnakeGame;
import states.EndState;
import utils.Snake;
import utils.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

import static functions.Request.*;

public class ViewCommand implements Observer {
    protected User user;
    protected Socket socket;
    protected ControllerSnakeGame controller;

    protected JFrame frame;
    protected JLabel scoreLabel = new JLabel("Score : 0", JLabel.CENTER);
    protected JLabel turnNumberLabel = new JLabel("Turn : 0", JLabel.CENTER);

    protected boolean playerAlive = true;

    protected ImageIcon heartIcon;
    protected ImageIcon sickHeartIcon;
    protected ImageIcon invincibleHeartIcon;

    protected JLabel heartLabel;

    protected Behaviors playerBehavior = Behaviors.NORMAL;

    public ViewCommand(Observable obs, Socket socket, User user, ControllerSnakeGame controller) {
        obs.addObserver(this);

        this.user = user;
        this.socket = socket;
        this.controller = controller;

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
        JPanel turnCommandPanel = new JPanel();
        JPanel turnSliderPanel = new JPanel();
        JPanel gameInfoPanel = new JPanel();

        mainPanel.setLayout(new GridLayout(2, 1));
        gameCommandsPanel.setLayout(new GridLayout(1, 4));
        turnCommandPanel.setLayout(new GridLayout(1, 2));
        turnSliderPanel.setLayout(new GridLayout(2, 1));
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

        this.heartLabel = new JLabel(heartIcon);

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                restartButton.setEnabled(false);
                pauseButton.setEnabled(false);
                playButton.setEnabled(true);
                stepButton.setEnabled(true);

                controller.restart();

                playerAlive = true;
                heartLabel.setIcon(finalHeartIcon);
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                playButton.setEnabled(false);
                stepButton.setEnabled(false);
                restartButton.setEnabled(true);
                pauseButton.setEnabled(true);

                controller.play();
            }
        });

        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                restartButton.setEnabled(true);
                controller.step();

                if (controller.getState() instanceof EndState) {
                    playButton.setEnabled(false);
                    stepButton.setEnabled(false);
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                restartButton.setEnabled(true);
                pauseButton.setEnabled(false);
                playButton.setEnabled(true);
                stepButton.setEnabled(true);

                controller.pause();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                closeGame();
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                closeGame();
            }
        });

        gameCommandsPanel.add(restartButton);
        gameCommandsPanel.add(playButton);
        gameCommandsPanel.add(stepButton);
        gameCommandsPanel.add(pauseButton);

        JLabel turnSliderLabel = new JLabel("Number of turns per second", JLabel.CENTER);

        JSlider turnSlider = new JSlider();
        turnSlider.setMinimum(1);
        turnSlider.setMaximum(10);
        turnSlider.setMajorTickSpacing(1);
        turnSlider.setPaintTicks(true);
        turnSlider.setPaintLabels(true);

        turnSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                controller.setSpeed(turnSlider.getValue());
            }
        });

        gameInfoPanel.add(turnNumberLabel);
        gameInfoPanel.add(exitButton);
        gameInfoPanel.add(scoreLabel);
        gameInfoPanel.add(heartLabel);

        turnSliderPanel.add(turnSliderLabel);
        turnSliderPanel.add(turnSlider);

        turnCommandPanel.add(turnSliderPanel);
        turnCommandPanel.add(gameInfoPanel);

        mainPanel.add(gameCommandsPanel);
        mainPanel.add(turnCommandPanel);
        frame.add(mainPanel);

        frame.setVisible(true);
    }

    private void closeGame() {
        System.gc();

        for (Window window : Window.getWindows())
            window.dispose();

        ViewGameMenu viewGameMenu = new ViewGameMenu(this.socket, this.user);
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

    private void updatePlayersLives(ArrayList<Snake> snakes) {
        boolean snakeAlive = snakes.size() == 1;

        if (this.playerAlive) {
            if (!snakeAlive)
                this.playerAlive = false;

            if (this.playerAlive && snakeAlive) {
                Behaviors behavior = snakes.get(0).getBehavior().getBehaviorType();

                if (behavior != this.playerBehavior) {
                    this.playerBehavior = behavior;
                    updatePlayerBehavior(behavior);
                }
            }
        }
    }

    public void update(Observable observable, Object o) {
        SnakeGame game = (SnakeGame) observable;
        scoreLabel.setText("Score : " + game.getScore());
        turnNumberLabel.setText("Turn : " + game.getTurn());

        updatePlayersLives(game.getSnakes());

        if (game.isGameOver()) {
            try {
                saveGame(this.socket, user.ID(), game.isWon(), game.getScore());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
