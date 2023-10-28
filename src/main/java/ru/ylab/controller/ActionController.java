package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ylab.model.Action;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ActionController {

    @GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Action> getActionList() {
        return ResponseEntity.ok(
                new Action(
                        1L,
                        LocalDateTime.now(),
                        "asdf",
                        2L,
                        "information"
                )
        );
    }
}
