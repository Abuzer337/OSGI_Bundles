package com.example.osgi.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class InfoServlet extends HttpServlet {

    private final String userName = "Лаврентьев Ефим Николаевич";
    private final String aboutMe = "Люблю Java, OSGi и аэродинамику.";
    private final String university = "ITMO-best of the best";
    private long startTime = System.currentTimeMillis();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        long uptime = (System.currentTimeMillis() - startTime)/1000;

        resp.getWriter().println("<html><head><title>Info about me</title></head><body>");
        resp.getWriter().println("<h1>Информация</h1>");
        resp.getWriter().println("<p>Имя: " + userName + "</p>");
        resp.getWriter().println("<p>О себе: " + aboutMe + "</p>");
        resp.getWriter().println("<p>О себе: " + university + "</p>");
        resp.getWriter().println("<p>Время работы сервера (сек): " + uptime + "</p>");
        resp.getWriter().println("<hr>");
        resp.getWriter().println("<p>Доступно по адресу <a href='/snake'>/snake</a></p>");
        resp.getWriter().println("</body></html>");
    }
}
