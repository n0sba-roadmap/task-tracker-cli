package project.controller;

import project.service.Service;

public class ControllerImpl implements Controller {

    private final Service service;

    public ControllerImpl(Service service) {
        this.service = service;
    }

    @Override
    public void command(String[] args) {
        this.service.handleCommand(args);
    }
    
}
