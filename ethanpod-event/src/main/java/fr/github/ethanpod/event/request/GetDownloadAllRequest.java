package fr.github.ethanpod.event.request;

import fr.github.ethanpod.core.UserDataRequest;

public class GetDownloadAllRequest extends AbstractPaginatedRequest {
    public GetDownloadAllRequest(UserDataRequest userDataRequest) {
        super(userDataRequest);
    }
}
