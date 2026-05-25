package fr.github.ethanpod.event.request;

import fr.github.ethanpod.core.UserDataRequest;

public class GetInboxAllRequest extends AbstractPaginatedRequest {

    public GetInboxAllRequest(UserDataRequest userDataRequest) {
        super(userDataRequest);
    }
}
