package fr.github.ethanpod.event.request;

public class GetPodcastByIdRequest {
    private final Integer id;

    public GetPodcastByIdRequest(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

}
