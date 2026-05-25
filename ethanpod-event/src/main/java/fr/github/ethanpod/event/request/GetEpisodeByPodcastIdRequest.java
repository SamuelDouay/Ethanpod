package fr.github.ethanpod.event.request;

import fr.github.ethanpod.core.UserDataRequest;

public class GetEpisodeByPodcastIdRequest {
    private final UserDataRequest userDataRequest;

    public GetEpisodeByPodcastIdRequest(UserDataRequest userDataRequest) {
        this.userDataRequest = userDataRequest;
    }

    public UserDataRequest getUserDataRequest() {
        return userDataRequest;
    }
}
