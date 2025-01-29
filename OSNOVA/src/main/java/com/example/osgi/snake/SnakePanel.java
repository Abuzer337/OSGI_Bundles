package com.example.osgi.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SnakePanel extends JPanel implements ActionListener {

    private final int CELL_SIZE = 10;
    private final int WIDTH = 50;
    private final int HEIGHT = 50;

    private Timer timer;
    private List<Point> snake;
    private Point food;
    private Point specialFood; // спец-еда
    private Direction direction;
    private boolean gameOver;
    private boolean paused;
    private int score;

    public SnakePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        setPreferredSize(new Dimension(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE));
        startGame();
    }

    public void startGame() {
        snake = new ArrayList<>();
        snake.add(new Point(WIDTH/2, HEIGHT/2));
        snake.add(new Point(WIDTH/2-1, HEIGHT/2));
        snake.add(new Point(WIDTH/2-2, HEIGHT/2));

        direction = Direction.RIGHT;
        gameOver = false;
        paused = false;
        score = 0;

        spawnFood();
        spawnSpecialFood(); // шанс на спец

        timer = new Timer(100, this);
        timer.start();
    }

    public void togglePause() {
        paused = !paused;
    }

    private void spawnFood() {
        int x = (int)(Math.random() * WIDTH);
        int y = (int)(Math.random() * HEIGHT);
        food = new Point(x, y);
    }
    private SnakeService snakeService;

    public void setSnakeService(SnakeService snakeService) {
        this.snakeService = snakeService;
    }

    private void updateScore() {
        if (snakeService != null) {
            snakeService.updateScore(score);
        }
    }

    private void spawnSpecialFood() {
        // 30% шанс появиться
        if (Math.random() < 0.3) {
            int x = (int)(Math.random() * WIDTH);
            int y = (int)(Math.random() * HEIGHT);
            specialFood = new Point(x, y);
        } else {
            specialFood = null;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!gameOver) {
            // рисуем основную еду (красная)
            g.setColor(Color.RED);
            g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

            // спец-еда (синяя)
            if (specialFood != null) {
                g.setColor(Color.BLUE);
                g.fillRect(specialFood.x * CELL_SIZE, specialFood.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }

            // рисуем змейку
            // пусть голова ярко-жёлтая, тело зелёное
            for (int i = 0; i < snake.size(); i++) {
                Point p = snake.get(i);
                if (i == 0) {
                    // голова
                    g.setColor(Color.YELLOW);
                } else {
                    g.setColor(Color.GREEN);
                }
                g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }

            // инфо
            g.setColor(Color.WHITE);
            g.drawString("Score: " + score, 10, 10);
            if (paused) {
                g.drawString("PAUSED", 100, 10);
            }

        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("GAME OVER!", WIDTH * CELL_SIZE/2 - 60, HEIGHT * CELL_SIZE/2);
            g.drawString("Score: " + score, WIDTH * CELL_SIZE/2 - 40, HEIGHT * CELL_SIZE/2 + 30);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !paused) {
            moveSnake();
            checkCollision();
        }
        repaint();
    }

    private void moveSnake() {
        Point head = snake.get(0);
        int nx = head.x;
        int ny = head.y;

        switch(direction) {
            case UP:    ny--; break;
            case DOWN:  ny++; break;
            case LEFT:  nx--; break;
            case RIGHT: nx++; break;
        }

        Point newHead = new Point(nx, ny);
        snake.add(0, newHead);

        // проверяем еду
        if (newHead.equals(food)) {
            score++;
            spawnFood();
            spawnSpecialFood();
        } else if (specialFood != null && newHead.equals(specialFood)) {
            score += 5;
            specialFood = null;
        } else {
            snake.remove(snake.size()-1);
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);
        // границы
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            gameOver = true;
            timer.stop();
            return;
        }
        // самопересечение
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
                timer.stop();
                return;
            }
        }
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
                case KeyEvent.VK_UP:
                    if (direction != Direction.DOWN) direction = Direction.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != Direction.UP) direction = Direction.DOWN;
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != Direction.RIGHT) direction = Direction.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != Direction.LEFT) direction = Direction.RIGHT;
                    break;
            }
        }
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private static class Point {
        int x, y;
        Point(int x, int y){ this.x=x; this.y=y;}
        @Override
        public boolean equals(Object o){
            if (o instanceof Point){
                Point other = (Point) o;
                return this.x == other.x && this.y == other.y;
            }
            return false;
        }
    }
}
