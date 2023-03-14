package view;

import behaviors.Behaviors;
import controllers.ControllerSnakeGame;
import model.SnakeGame;
import states.EndState;
import utils.Snake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ViewCommand implements Observer {
    protected ControllerSnakeGame controller;
    protected JFrame frame;
    protected JLabel turnNumberLabel = new JLabel("Turn : 0", JLabel.CENTER);

    protected boolean playerAlive = true;

    protected ImageIcon heartIcon;
    protected ImageIcon sickHeartIcon;
    protected ImageIcon invincibleHeartIcon;

    protected JLabel heartLabel;

    protected Behaviors playerBehavior = Behaviors.NORMAL;

    public ViewCommand(Observable obs, ControllerSnakeGame controller) {
        obs.addObserver(this);
        this.controller = controller;

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        JPanel playersPanel = new JPanel();
        JPanel playerPanel = new JPanel();

        playersPanel.setLayout(new GridLayout(1, 2));

        mainPanel.setLayout(new GridLayout(2, 1));
        gameCommandsPanel.setLayout(new GridLayout(1, 4));
        turnCommandPanel.setLayout(new GridLayout(1, 2));
        turnSliderPanel.setLayout(new GridLayout(2, 1));
        gameInfoPanel.setLayout(new GridLayout(2, 1));
        playerPanel.setLayout(new GridLayout(2, 1));

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

        restartButton.setEnabled(false);
        pauseButton.setEnabled(false);

        JLabel playerLabel = new JLabel("Player", JLabel.CENTER);

        this.heartLabel = new JLabel(heartIcon);

        playerPanel.add(playerLabel);
        playerPanel.add(this.heartLabel);

        playersPanel.add(playerPanel);

        restartButton.addActionListener(new ActionListener() {
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
            public void actionPerformed(ActionEvent event) {
                playButton.setEnabled(false);
                stepButton.setEnabled(false);
                restartButton.setEnabled(true);
                pauseButton.setEnabled(true);

                controller.play();
            }
        });

        stepButton.addActionListener(new ActionListener() {
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
            public void actionPerformed(ActionEvent event) {
                restartButton.setEnabled(true);
                pauseButton.setEnabled(false);
                playButton.setEnabled(true);
                stepButton.setEnabled(true);

                controller.pause();
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
        gameInfoPanel.add(playersPanel);

        turnSliderPanel.add(turnSliderLabel);
        turnSliderPanel.add(turnSlider);

        turnCommandPanel.add(turnSliderPanel);
        turnCommandPanel.add(gameInfoPanel);

        mainPanel.add(gameCommandsPanel);
        mainPanel.add(turnCommandPanel);
        frame.add(mainPanel);

        frame.setVisible(true);
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
        turnNumberLabel.setText("Turn : " + game.getTurn());
        updatePlayersLives(game.getSnakes());

        if (game.isGameOver())
            System.out.println(game.getScore());
    }
}
