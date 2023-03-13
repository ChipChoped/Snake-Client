package view;

import controllers.ControllerSnakeGame;
import org.json.JSONObject;
import strategies.InteractiveStrategy;
import strategies.RandomStrategy;
import strategies.Strategy;
import utils.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import static functions.LogIn.logIn;

public class ViewMenu {
    public ViewMenu(Socket socket, User user) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Snake Menu");
        frame.setSize(new Dimension(500, 300));

        Dimension windowSize = frame.getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;
        frame.setLocation(dx,dy);

        JPanel mainPanel = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        mainPanel.setLayout(new GridLayout(2, 1));
        bottomPanel.setLayout(new GridLayout(1, 3));

        JLabel userLabel = new JLabel("Welcome " + user.username() + "!", JLabel.CENTER);
        JButton profileButton = new JButton("Profile \u0004");
        JButton logOutButton = new JButton("Log out");
        JButton playButton = new JButton("Play!");

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String url = "http://localhost:8080/Snake/user/" + user.username();
                Runtime runtime = Runtime.getRuntime();

                try {
                    runtime.exec("xdg-open " + url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                /*try {
                    JSONObject jsonResponse = logIn(socket, usernameTextField.getText(), String.valueOf(passwordField.getPassword()));

                    if (jsonResponse.get("type").equals("id")) {
                        User user = new User(jsonResponse.getInt("id"), usernameTextField.getText());
                        frame.dispose();
                        ViewGameMenu viewGameMenu = new ViewGameMenu(user);
                    }
                    else if (jsonResponse.get("type").equals("error")) {
                        errorLabel.setText((String) jsonResponse.get("message"));
                        errorLabel.setForeground(Color.red);
                    }
                } catch (IOException | NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }*/
            }
        });

        /*playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Strategy chosenStrategy = null;

                switch (chosenMode) {
                    case MANUAL:
                        chosenStrategy = new InteractiveStrategy();
                        break;
                    case RANDOM:
                        chosenStrategy = new RandomStrategy();
                }

                numberOfTurns = Integer.parseInt(turnField.getText());

                frame.dispose();

                try {
                    ControllerSnakeGame controller = new ControllerSnakeGame(numberOfTurns, chosenStrategy, chosenMapPath);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });*/

        topPanel.add(userLabel);

        bottomPanel.add(profileButton);
        bottomPanel.add(logOutButton);
        bottomPanel.add(playButton);

        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);

        frame.add(mainPanel);

        frame.setVisible(true);
    }
}
