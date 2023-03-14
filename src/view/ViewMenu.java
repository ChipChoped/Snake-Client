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

import static functions.Logs.logIn;
import static functions.Logs.logOut;

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
        JLabel errorLabel = new JLabel();
        JButton profileButton = new JButton("Profile \u0004");
        JButton logOutButton = new JButton("Log out");
        JButton playButton = new JButton("Play!");

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String url = "http://localhost:8080/Snake/user/" + user.username();


                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(new URI(url));
                } catch (IOException | URISyntaxException | UnsupportedOperationException e) {
                    try {
                        Runtime runtime = Runtime.getRuntime();
                        runtime.exec("xdg-open " + url);
                    } catch (IOException e_) {
                        e.printStackTrace();
                        e_.printStackTrace();
                    }
                }
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    JSONObject jsonResponse = logOut(socket, user.ID());

                    if (jsonResponse.get("type").equals("return-log-out")) {
                        frame.dispose();
                        ViewLogIn viewLogIn = new ViewLogIn(socket);
                    }
                    else if (jsonResponse.get("type").equals("error")) {
                        errorLabel.setText((String) jsonResponse.get("message"));
                        errorLabel.setForeground(Color.red);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                frame.dispose();

                try {
                    ControllerSnakeGame controller = new ControllerSnakeGame(500, new InteractiveStrategy(), System.getProperty("user.dir") + "/layout/arenaNoWall.lay");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        topPanel.add(userLabel);
        topPanel.add(errorLabel);

        bottomPanel.add(profileButton);
        bottomPanel.add(logOutButton);
        bottomPanel.add(playButton);

        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);

        frame.add(mainPanel);

        frame.setVisible(true);
    }
}
