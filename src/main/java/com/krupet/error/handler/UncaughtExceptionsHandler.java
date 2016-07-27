package com.krupet.error.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The class UncaughtExceptionsHandler. Handles all unhandled exceptions.
 */
@ControllerAdvice
public class UncaughtExceptionsHandler {

    public static final Logger LOG = LogManager.getLogger(UncaughtExceptionsHandler.class);

    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        String errorMessage = " ********Uncaught_Exception********\n" +
                " Error during the request to the URL " + request.getRequestURI() + ".\n" +
                ex.getMessage() + ".\n" +
                " ********Uncaught_Exception********\n";
        LOG.error(errorMessage);
        try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMessage);
        } catch (IOException e) {
            LOG.error("Can't handle Exception.", e);
        }
    }
}
