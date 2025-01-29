package com.example.osgi.server;

import com.example.osgi.snake.SnakeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import java.util.Hashtable;

public class ServerActivator implements BundleActivator {

    private HttpService httpService;

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("[ServerActivator] Starting server bundle...");

        httpService = HttpUtils.getHttpService(context);
        if (httpService == null) {
            System.err.println("[ServerActivator] No HttpService available!");
            return;
        }

        // Регистрируем InfoServlet
        httpService.registerServlet("/info", new InfoServlet(), null, null);
        System.out.println("[ServerActivator] Servlet /info registered!");

        // Получаем SnakeService
        ServiceReference<SnakeService> snakeServiceRef = context.getServiceReference(SnakeService.class);
        SnakeService snakeService = context.getService(snakeServiceRef);

        // Регистрируем SnakeInfoServlet
        if (snakeService != null) {
            httpService.registerServlet("/snake", new SnakeInfoServlet(snakeService), null, null);
            System.out.println("[ServerActivator] Servlet /snake registered!");
        } else {
            System.err.println("[ServerActivator] SnakeService is not available!");
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (httpService != null) {
            httpService.unregister("/info");
            httpService.unregister("/snake");
        }
        System.out.println("[ServerActivator] Stopping server bundle...");
    }
}
