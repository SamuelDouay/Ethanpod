package fr.github.ethanpod.event.request;

import fr.github.ethanpod.core.UserDataRequest;

public class GetQueueAllRequest extends AbstractPaginatedRequest {

    public GetQueueAllRequest(UserDataRequest userDataRequest) {
        super(userDataRequest);
    }
}
