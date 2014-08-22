package com.redditspider.web.metrics;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet.ContextListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;

/**
 * Context for healthchecks.
 */
public class HealthContextListener extends ContextListener {

    private HealthCheckRegistry registry;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext parentApplicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        this.registry = parentApplicationContext.getBean(HealthCheckRegistry.class);
        super.contextInitialized(event);
    }

    @Override
    protected HealthCheckRegistry getHealthCheckRegistry() {
        return registry;
    }
}
