package fr.github.ethanpod.event.request;

import fr.github.ethanpod.core.UserDataRequest;

public class GetHistoryAllRequest extends AbstractPaginatedRequest {

    public GetHistoryAllRequest(UserDataRequest userDataRequest) {
        super(userDataRequest);
    }
}
