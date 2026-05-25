package fr.github.ethanpod.event.request;

import fr.github.ethanpod.core.UserDataRequest;

public class GetEpisodeAllRequest {
    private final UserDataRequest userDataRequest;

    public GetEpisodeAllRequest(UserDataRequest userDataRequest) {
        this.userDataRequest = userDataRequest;
    }

    public UserDataRequest getUserDataRequest() {
        return userDataRequest;
    }
}
