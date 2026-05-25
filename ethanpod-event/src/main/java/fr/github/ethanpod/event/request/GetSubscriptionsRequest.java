package fr.github.ethanpod.event.request;

import fr.github.ethanpod.core.UserDataRequest;

public class GetSubscriptionsRequest extends AbstractPaginatedRequest {

    public GetSubscriptionsRequest(UserDataRequest userDataRequest) {
        super(userDataRequest);
    }

}
