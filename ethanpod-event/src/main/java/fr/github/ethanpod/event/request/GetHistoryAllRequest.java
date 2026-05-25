package fr.github.ethanpod.event.request;

import fr.github.ethanpod.core.UserDataRequest;

public class GetHistoryAllRequest {
    private final UserDataRequest userDataRequest;

    public GetHistoryAllRequest(UserDataRequest userDataRequest) {
        this.userDataRequest = userDataRequest;
    }

    public UserDataRequest getUserDataRequest() {
        return userDataRequest;
    }
}
