package com.example.osgi.snake;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import java.awt.*;

@Component(service = SnakeService.class, immediate = true, scope = ServiceScope.SINGLETON)
public class SnakeServiceImpl implements SnakeService {

    private String gameStatus = "Stopped"; // Статус игры
    private int score = 0;                 // Текущий счёт

    @Override
    public void startGame() {
        gameStatus = "Running";
        score = 0;
        System.out.println("Game started!");

        // Запускаем GUI в отдельном потоке (важно для Swing)
        EventQueue.invokeLater(() -> {
            SnakeGame game = new SnakeGame();
            game.setVisible(true);
        });
    }

    @Override
    public String getGameStatus() {
        return gameStatus;
    }

    @Override
    public int getScore() {
        return score;
    }
    @Override
    public void updateScore(int newScore) {
        this.score = newScore;
    }

}
