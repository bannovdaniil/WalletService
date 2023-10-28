package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ylab.model.Action;
import ru.ylab.service.ActionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ActionController {
    private final ActionService actionService;

    @GetMapping(value = "/api/action", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Action>> getActionList() {
        List<Action> actionList = actionService.findAll();
        return ResponseEntity.ok(
                actionList
        );
    }
}
