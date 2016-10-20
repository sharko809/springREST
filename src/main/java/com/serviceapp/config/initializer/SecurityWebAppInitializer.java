package com.serviceapp.config.initializer;

import com.serviceapp.filter.EncodingFilter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import javax.servlet.ServletContext;

/**
 * Class for mapping SpringSecurityFilterChain
 */
public class SecurityWebAppInitializer extends AbstractSecurityWebApplicationInitializer {

    private static final String ENCODING_FILTER = "encodingFilter";

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
        servletContext.addFilter(ENCODING_FILTER, EncodingFilter.class);
    }

    /*
    Class without overridden methods represents the same configuration as following xml

    <filter>
       <filter-name>springSecurityFilterChain</filter-name>
       <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>

    <filter-mapping>
       <filter-name>springSecurityFilterChain</filter-name>
       <url-pattern>/*</url-pattern>
       <dispatcher>ERROR</dispatcher>
       <dispatcher>REQUEST</dispatcher>
    </filter-mapping>
*/

}
