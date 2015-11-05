package no.nb.microservices.catalogplaylist.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistResource extends ResourceSupport {
    private String sesamId;
    private String title;
    private String alternativeTitle;
    private String urn;
    private List<String> urns;
    private List<String> sesamIds;
    private String mediatype;
    private String group;
    private String partNumber;
    private List<PlaylistResource> tracks;
    private List<PlaylistResource> hosts;

    public String getSesamId() {
        return sesamId;
    }

    public void setSesamId(String sesamId) {
        this.sesamId = sesamId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlternativeTitle() {
        return alternativeTitle;
    }

    public void setAlternativeTitle(String alternativeTitle) {
        this.alternativeTitle = alternativeTitle;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public List<String> getUrns() {
        return urns;
    }

    public void setUrns(List<String> urns) {
        this.urns = urns;
    }

    public List<String> getSesamIds() {
        return sesamIds;
    }

    public void setSesamIds(List<String> sesamIds) {
        this.sesamIds = sesamIds;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public List<PlaylistResource> getTracks() {
        return tracks;
    }

    public void setTracks(List<PlaylistResource> tracks) {
        this.tracks = tracks;
    }

    public void addTracks(PlaylistResource resource) {
        if (this.getTracks() == null) {
            this.tracks = new ArrayList<>();
        }
        this.tracks.add(resource);
    }

    public List<PlaylistResource> getHosts() {
        return hosts;
    }

    public void setHosts(List<PlaylistResource> hosts) {
        this.hosts = hosts;
    }

    public void addHost(PlaylistResource resource) {
        if (this.getHosts() == null) {
            this.hosts = new ArrayList<>();
        }
        this.hosts.add(resource);
    }
}
