package fr.github.ethanpod.event.request;

import fr.github.ethanpod.core.UserDataRequest;

public class GetEpisodeAllRequest extends AbstractPaginatedRequest {

    public GetEpisodeAllRequest(UserDataRequest userDataRequest) {
        super(userDataRequest);
    }
}
