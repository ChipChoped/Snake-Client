package view;

import controllers.AbstractController;
import model.SimpleGame;

import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import java.awt.*;

public class ViewSimpleGame implements Observer {
    protected AbstractController controller;

    public ViewSimpleGame(Observable obs, AbstractController controller) {
        obs.addObserver(this);
        this.controller = controller;

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Snake");
        frame.setSize(new Dimension(700, 700));
        frame.setVisible(true);

        Dimension windowSize = frame.getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2 - 350;
        frame.setLocation(dx,dy);

        JLabel turn = new JLabel("Turn", JLabel.CENTER);
        frame.add(turn);
    }

    public void update(Observable observable, Object o) {
        SimpleGame game = (SimpleGame) o;
    }
}
