package com.example.osgi.server;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;

public class HttpUtils {
    public static HttpService getHttpService(BundleContext context) {
        ServiceReference<HttpService> ref = context.getServiceReference(HttpService.class);
        if (ref != null) {
            return context.getService(ref);
        }
        return null;
    }
}
