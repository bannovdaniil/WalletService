package ru.ylab.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.StringJoiner;

/**
 * Маппит Swagger UI из ресурсов.
 */
@RestController
@RequestMapping(value = "/swagger-ui")
@Hidden
public class SwaggerUiController {

    @RequestMapping(value = "*")
    public byte[] swaggerUiAction(HttpServletRequest req) {

        String[] pathPart = req.getPathInfo().split("/");
        StringJoiner resoursePath = new StringJoiner(File.separator);
        for (String s : pathPart) {
            resoursePath.add(s);
        }

        try (InputStream inputStream = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream(resoursePath.toString())) {

            return inputStream.readAllBytes();
        } catch (Exception ex) {
            return new byte[0];
        }
    }
}
