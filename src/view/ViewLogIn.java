package view;

import org.json.JSONObject;
import utils.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import static functions.Request.disconnect;
import static functions.Request.logIn;

public class ViewLogIn {
    public ViewLogIn(Socket socket) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Snake Game Menu");
        frame.setSize(new Dimension(500, 300));

        Dimension windowSize = frame.getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;
        frame.setLocation(dx,dy);

        JPanel mainPanel = new JPanel();
        JPanel a = new JPanel();
        JPanel b = new JPanel();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1));

        JLabel logInLabel = new JLabel("Log in to Snake!");
        JLabel errorLabel = new JLabel();
        JLabel usernameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");

        JTextField usernameTextField = new JTextField(null, 15);
        JPasswordField passwordField = new JPasswordField(null, 15);

        JButton logInButton = new JButton("Log in");

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    JSONObject jsonResponse = logIn(socket, usernameTextField.getText(), String.valueOf(passwordField.getPassword()));

                    if (jsonResponse.get("type").equals("return-log-in")) {
                        User user = new User(jsonResponse.getInt("id"), usernameTextField.getText());
                        frame.dispose();
                        ViewMenu viewMenu = new ViewMenu(socket, user);
                    }
                    else if (jsonResponse.get("type").equals("error")) {
                        errorLabel.setText((String) jsonResponse.get("message"));
                        errorLabel.setForeground(Color.red);
                    }
                } catch (IOException | NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                try {
                    disconnect(socket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        panel.add(logInLabel);
        panel.add(errorLabel);
        panel.add(usernameLabel);
        panel.add(usernameTextField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(logInButton);

        mainPanel.add(a);
        mainPanel.add(panel);
        mainPanel.add(b);

        frame.add(mainPanel);

        frame.setVisible(true);
    }
}
