package com.redditspider.web.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet.ContextListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;

/**
 * Context for metrics.
 */
public class MetricsContextListener extends ContextListener {
    private MetricRegistry registry;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext parentApplicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        this.registry = parentApplicationContext.getBean(MetricRegistry.class);
        super.contextInitialized(event);
    }

    @Override
    protected MetricRegistry getMetricRegistry() {
        return registry;
    }
}
