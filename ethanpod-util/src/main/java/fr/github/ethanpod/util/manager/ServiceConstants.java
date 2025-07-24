package fr.github.ethanpod.util.manager;

public enum ServiceConstants {
    NAVIGATION_SERVICE("navigation"), INBOX_SERVICE("inbox"), QUEUE_SERVICE("queue");
    private final String name;

    ServiceConstants(String name) {
        this.name = name;
    }

    public static ServiceConstants fromName(String name) {
        for (ServiceConstants service : ServiceConstants.values()) {
            if (service.getName().equals(name)) {
                return service;
            }
        }
        throw new IllegalArgumentException("Unknown service: " + name);
    }

    public String getName() {
        return name;
    }
}
