package fr.github.ethanpod.event.request;

import fr.github.ethanpod.core.UserDataRequest;

public class GetEpisodeByPodcastIdRequest extends AbstractPaginatedRequest {

    public GetEpisodeByPodcastIdRequest(UserDataRequest userDataRequest) {
        super(userDataRequest);
    }
}

