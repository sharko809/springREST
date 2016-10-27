package com.serviceapp.util;

import javax.servlet.http.HttpServletResponse;

/**
 * Helper class for convenient headers setting
 */
public class ResponseHelper {

    /**
     * Set headers required for successful CORS handling
     *
     * @param response http response
     */
    public static void setCorsHeader(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:63342");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, " +
                "Access-Control-Request-Method, Access-Control-Request-Headers");
    }

}
