package com.itranswarp.learnjava.service;

import org.springframework.context.ApplicationEvent;

public class DatabaseReadyEvent extends ApplicationEvent {
    public DatabaseReadyEvent(Object source) {
        super(source);
    }
}