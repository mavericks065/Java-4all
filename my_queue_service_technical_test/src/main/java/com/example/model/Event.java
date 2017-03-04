package com.example.model;

import java.util.UUID;

public class Event {

    private final UUID uuid;
    private final String value;
    private EventStatus status;

    public Event(final UUID uuid, final String value) {
        this.uuid = uuid;
        this.value = value;
        this.status = EventStatus.NEW;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getValue() {
        return value;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }
}
