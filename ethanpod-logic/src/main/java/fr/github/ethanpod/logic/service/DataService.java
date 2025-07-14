package fr.github.ethanpod.logic.service;

import java.util.concurrent.ExecutorService;

public abstract class DataService {
    protected final ExecutorService executor;

    protected DataService(ExecutorService executor) {
        this.executor = executor;
    }

    abstract void refreshData();
}
