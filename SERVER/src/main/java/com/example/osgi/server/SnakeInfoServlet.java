package com.example.osgi.server;

import com.example.osgi.snake.SnakeService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SnakeInfoServlet extends HttpServlet {

    private final long startTime = System.currentTimeMillis();

    private final SnakeService snakeService;

    public SnakeInfoServlet(SnakeService snakeService) {
        this.snakeService = snakeService;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        long uptime = (System.currentTimeMillis() - startTime) / 1000;
        int currentScore = snakeService.getScore(); // Получаем актуальный счет!

        resp.getWriter().println("<html><head><title>Snake Info</title></head><body>");
        resp.getWriter().println("<h1>Информация об игре</h1>");
        resp.getWriter().println("<p>Текущий счет: " + currentScore + "</p>");
        resp.getWriter().println("<p>Время работы сервера: " + uptime + " сек</p>");
        resp.getWriter().println("</body></html>");
    }

}
