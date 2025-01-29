package com.example.osgi.snake;

public interface SnakeService {
    void startGame();
    String getGameStatus();  // Метод для получения статуса игры
    int getScore();          // Метод для получения текущего счёта

    void updateScore(int newScore);

}
