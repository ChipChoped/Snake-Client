package view;

import behaviors.Behaviors;
import controllers.ControllerSnakeGame;
import model.SnakeGame;
import states.EndState;
import utils.ColorSnake;
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

    protected ArrayList<JLabel> player1HeartLabels = new ArrayList<>();
    protected ArrayList<JLabel> player2HeartLabels = new ArrayList<>();

    protected boolean player1Alive = true;
    protected boolean player2Alive = false;

    protected int numberOfPlayers = 1;

    protected ImageIcon heartIcon;
    protected ImageIcon sickHeartIcon;
    protected ImageIcon invincibleHeartIcon;

    protected Behaviors player1Behavior = Behaviors.NORMAL;
    protected Behaviors player2Behavior = Behaviors.NORMAL;

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
        JPanel player1Panel = new JPanel();
        JPanel player2Panel = new JPanel();
        JPanel lives1Panel = new JPanel();
        JPanel lives2Panel = new JPanel();

        playersPanel.setLayout(new GridLayout(1, 2));

        mainPanel.setLayout(new GridLayout(2, 1));
        gameCommandsPanel.setLayout(new GridLayout(1, 4));
        turnCommandPanel.setLayout(new GridLayout(1, 2));
        turnSliderPanel.setLayout(new GridLayout(2, 1));
        gameInfoPanel.setLayout(new GridLayout(2, 1));
        player1Panel.setLayout(new GridLayout(2, 1));

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

        JLabel player1Label = new JLabel("Player 1", JLabel.CENTER);
        this.player1HeartLabels.add(new JLabel(heartIcon));
        this.player1HeartLabels.add(new JLabel(heartIcon));
        this.player1HeartLabels.add(new JLabel(heartIcon));

        lives1Panel.add(this.player1HeartLabels.get(0));
        lives1Panel.add(this.player1HeartLabels.get(1));
        lives1Panel.add(this.player1HeartLabels.get(2));

        player1Panel.add(player1Label);
        player1Panel.add(lives1Panel);

        playersPanel.add(player1Panel);

        if (this.controller.getGame().getInitialSnakes().size() >= 2) {
            player2Panel.setLayout(new GridLayout(2, 1));

            JLabel player2Label = new JLabel("Player 2", JLabel.CENTER);
            this.player2HeartLabels.add(new JLabel(heartIcon));
            this.player2HeartLabels.add(new JLabel(heartIcon));
            this.player2HeartLabels.add(new JLabel(heartIcon));

            lives2Panel.add(this.player2HeartLabels.get(0));
            lives2Panel.add(this.player2HeartLabels.get(1));
            lives2Panel.add(this.player2HeartLabels.get(2));

            player2Panel.add(player2Label);
            player2Panel.add(lives2Panel);

            playersPanel.add(player2Panel);

            this.numberOfPlayers = 2;
            this.player2Alive = true;
        }
        else
            playersPanel.setLayout(new GridLayout(2, 1));

        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                restartButton.setEnabled(false);
                pauseButton.setEnabled(false);
                playButton.setEnabled(true);
                stepButton.setEnabled(true);

                controller.restart();

                player1Alive = true;

                player1HeartLabels.add(new JLabel(finalHeartIcon));
                player1HeartLabels.add(new JLabel(finalHeartIcon));
                player1HeartLabels.add(new JLabel(finalHeartIcon));

                lives1Panel.add(player1HeartLabels.get(0));
                lives1Panel.add(player1HeartLabels.get(1));
                lives1Panel.add(player1HeartLabels.get(2));

                if (numberOfPlayers == 2) {
                    player2Alive = true;

                    player2HeartLabels.add(new JLabel(finalHeartIcon));
                    player2HeartLabels.add(new JLabel(finalHeartIcon));
                    player2HeartLabels.add(new JLabel(finalHeartIcon));

                    lives2Panel.add(player2HeartLabels.get(0));
                    lives2Panel.add(player2HeartLabels.get(1));
                    lives2Panel.add(player2HeartLabels.get(2));
                }
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

    private void updatePlayerBehavior(Behaviors behavior, ArrayList<JLabel> hearts) {
        switch (behavior) {
            case NORMAL:
                for (JLabel heart : hearts)
                    heart.setIcon(this.heartIcon);
                break;
            case SICK:
                for (JLabel heart : hearts)
                    heart.setIcon(this.sickHeartIcon);
                break;
            case INVINCIBLE:
                for (JLabel heart : hearts)
                    heart.setIcon(this.invincibleHeartIcon);
                break;
        }
    }

    private void updatePlayersLives(ArrayList<Snake> snakes) {
        boolean greenSnakeAlive = false;
        boolean redSnakeAlive = false;

        for (Snake snake : snakes) {
            if (snake.getColorSnake() == ColorSnake.Green)
                greenSnakeAlive = true;
            else if (snake.getColorSnake() == ColorSnake.Red)
                redSnakeAlive = true;
        }

        if (this.player1Alive) {
            if (!greenSnakeAlive) {
                this.player1HeartLabels.get(this.player1HeartLabels.size() - 1).setVisible(false);
                this.player1HeartLabels.remove(this.player1HeartLabels.size() - 1);

                if (this.player1HeartLabels.size() == 0) {
                    this.player1Alive = false;
                }
                else
                    this.controller.getGame().getSnakes().add(0, new Snake(this.controller.getGame().getInitialSnakes().get(0)));
            }

            if (this.player1Alive && greenSnakeAlive) {
                Behaviors behavior = snakes.get(0).getBehavior().getBehaviorType();

                if (behavior != this.player1Behavior) {
                    this.player1Behavior = behavior;
                    updatePlayerBehavior(behavior, this.player1HeartLabels);
                }
            }
        }

        if (this.numberOfPlayers == 2) {
            if (this.player2Alive) {
                if (!redSnakeAlive) {
                    this.player2HeartLabels.get(this.player2HeartLabels.size() - 1).setVisible(false);
                    this.player2HeartLabels.remove(this.player2HeartLabels.size() - 1);

                    if (this.player2HeartLabels.size() == 0)
                        this.player2Alive = false;
                    else
                        if (this.controller.getGame().getSnakes().size() > 1)
                            this.controller.getGame().getSnakes().add(1, new Snake(this.controller.getGame().getInitialSnakes().get(1)));
                        else {
                            this.controller.getGame().getSnakes().add(new Snake(this.controller.getGame().getInitialSnakes().get(1)));
                        }
                }

                if (this.player2Alive && redSnakeAlive) {
                    Behaviors behavior;

                    if (snakes.size() == 1)
                         behavior = snakes.get(0).getBehavior().getBehaviorType();
                    else
                        behavior = snakes.get(1).getBehavior().getBehaviorType();

                    if (behavior != this.player2Behavior) {
                        this.player2Behavior = behavior;
                        updatePlayerBehavior(behavior, this.player2HeartLabels);
                    }
                }
            }
        }
    }

    public void update(Observable observable, Object o) {
        SnakeGame game = (SnakeGame) observable;
        turnNumberLabel.setText("Turn : " + game.getTurn());
        updatePlayersLives(game.getSnakes());

        //this.frame.repaint();
    }
}
