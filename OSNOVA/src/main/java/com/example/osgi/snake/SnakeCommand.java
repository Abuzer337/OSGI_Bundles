package com.example.osgi.snake;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        service = SnakeCommand.class,
        property = {
                "osgi.command.scope=snake",
                "osgi.command.function=start"
        }
)
public class SnakeCommand {

    @Reference
    private SnakeService snakeService;

    public void start() {
        if (snakeService != null) {
            snakeService.startGame();
            System.out.println("GAME STARTED!");
        } else {
            System.out.println("ERROR: SNAKESERVICE NOT FOUNTED.");
        }
    }
}
