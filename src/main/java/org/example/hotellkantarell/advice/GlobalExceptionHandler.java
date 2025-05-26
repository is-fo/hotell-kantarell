package org.example.hotellkantarell.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointer(HttpServletRequest request, Exception ex) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        return "error"; //om användaren inte är felet, då får vi lägga till felhantering för det
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handle404() {
        return "redirect:/login";
    }
}
