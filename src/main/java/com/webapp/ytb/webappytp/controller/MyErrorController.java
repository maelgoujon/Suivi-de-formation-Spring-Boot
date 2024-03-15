package com.webapp.ytb.webappytp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MyErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        return processError(request);
    }

    @PostMapping("/error")
    public String handleErrorPost(HttpServletRequest request) {
        return processError(request);
    }

    private String processError(HttpServletRequest request) {
        // Récupérer des détails sur l'erreur à partir de la requête
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "errors/error403"; // Page d'erreur personnalisée pour le statut 403
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "errors/error404"; // Page d'erreur personnalisée pour le statut 404
            }
        }

        return "/errors/error"; // Page d'erreur générale
    }
}