package fr.github.ethanpod.event.request;

import fr.github.ethanpod.core.UserDataRequest;

public class GetSubscriptionsRequest {
    private final UserDataRequest userDataRequest;

    public GetSubscriptionsRequest(UserDataRequest userDataRequest) {
        this.userDataRequest = userDataRequest;
    }

    public UserDataRequest getUserDataRequest() {
        return userDataRequest;
    }
}
