package com.example.osgi.snake;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SnakeGame extends JFrame {
    private SnakePanel snakePanel;

    public SnakeGame() {
        setTitle("Snake (Colorful Edition)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        snakePanel = new SnakePanel();
        add(snakePanel);

        // Меню
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        // New Game
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener((ActionEvent e) -> snakePanel.startGame());
        gameMenu.add(newGameItem);

        // Pause/Resume
        JMenuItem pauseItem = new JMenuItem("Pause/Resume");
        pauseItem.addActionListener((ActionEvent e) -> snakePanel.togglePause());
        gameMenu.add(pauseItem);

        // Exit
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener((ActionEvent e) -> this.dispose());
        gameMenu.add(exitItem);

        menuBar.add(gameMenu);

        setJMenuBar(menuBar);

        setSize(500, 500);
        setLocationRelativeTo(null);
    }
}
