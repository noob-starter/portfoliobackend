package com.backend.portfolio.controllers.test;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.backend.portfolio.exceptions.custom.ErrorResponse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom error controller to handle errors that are not caught by GlobalExceptionHandler
 * This acts as a safety net to prevent application crashes
 */
@Controller
@Slf4j
public class ErrorTestController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public ErrorTestController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            httpStatus = HttpStatus.valueOf(statusCode);
        }

        // Get error attributes for more details
        WebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> errorAttrs = errorAttributes.getErrorAttributes(webRequest, 
            org.springframework.boot.web.error.ErrorAttributeOptions.defaults());

        String message = errorMessage != null ? errorMessage.toString() : 
                        errorAttrs.get("message") != null ? errorAttrs.get("message").toString() : 
                        "An error occurred";

        String uri = requestUri != null ? requestUri.toString() : 
                    errorAttrs.get("path") != null ? errorAttrs.get("path").toString() : 
                    "Unknown";

        log.error("Error controller invoked - Status: {}, URI: {}, Message: {}", 
                httpStatus.value(), uri, message);

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(getCustomMessage(httpStatus, uri))
                .build();

        return new ResponseEntity<>(error, httpStatus);
    }

    /**
     * Get custom error message based on status code
     */
    private String getCustomMessage(HttpStatus status, String uri) {
        return switch (status) {
            case NOT_FOUND -> "The requested endpoint does not exist: " + uri;
            case METHOD_NOT_ALLOWED -> "HTTP method not allowed for: " + uri;
            case UNAUTHORIZED -> "Authentication is required to access this resource";
            case FORBIDDEN -> "You don't have permission to access this resource";
            case BAD_REQUEST -> "Invalid request. Please check your request and try again";
            case TOO_MANY_REQUESTS -> "Rate limit exceeded. Please try again later";
            case SERVICE_UNAVAILABLE -> "Service is temporarily unavailable. Please try again later";
            default -> "An error occurred while processing your request";
        };
    }
}

