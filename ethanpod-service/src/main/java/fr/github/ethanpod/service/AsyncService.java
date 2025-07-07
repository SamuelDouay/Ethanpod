package fr.github.ethanpod.service;

import fr.github.ethanpod.core.thread.ThreadMessage;

public interface AsyncService {
    void initialize();

    void handleResponse(ThreadMessage message);

    void refreshData();

    void stop();

    String getServiceId();

    boolean isActive();
}