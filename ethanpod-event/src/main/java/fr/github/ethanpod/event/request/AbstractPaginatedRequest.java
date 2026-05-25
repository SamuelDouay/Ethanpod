package fr.github.ethanpod.event.request;

import fr.github.ethanpod.core.UserDataRequest;

public abstract class AbstractPaginatedRequest {
    private final UserDataRequest userDataRequest;

    protected AbstractPaginatedRequest(UserDataRequest userDataRequest) {
        this.userDataRequest = userDataRequest;
    }

    public UserDataRequest getUserDataRequest() {
        return userDataRequest;
    }
}
