package fr.github.ethanpod.event.request;

import fr.github.ethanpod.core.UserDataRequest;

public class GetInboxAllRequest {
    private final UserDataRequest userDataRequest;

    public GetInboxAllRequest(UserDataRequest userDataRequest) {
        this.userDataRequest = userDataRequest;
    }

    public UserDataRequest getUserDataRequest() {
        return userDataRequest;
    }
}
