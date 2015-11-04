package no.nb.microservices.catalogplaylist.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Playlist implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sesamId;
    private String urn;
    private String suburn;
    private List<String> sesamIds;
    private String mediatype;
    private String title;
    private String alternativeTitle;
    private String group;
    private boolean hasMediafile;
    private boolean hasAccess;
    private int partNumber;
    private List<Playlist> playlists;
    private List<Playlist> hosts;

    public String getSesamId() {
        return sesamId;
    }

    public void setSesamId(String sesamId) {
        this.sesamId = sesamId;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getSuburn() {
        return suburn;
    }

    public void setSuburn(String suburn) {
        this.suburn = suburn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean hasMediafile() {
        if (!hasAccess && this.playlists !=  null) {
            for (Playlist playlist : this.playlists) {
                if (playlist.hasMediafile()) {
                    return playlist.hasMediafile();
                }
            }
        }
        return hasMediafile;
    }

    public void hasMediafile(boolean hasMediafile) {
        this.hasMediafile = hasMediafile;
    }

    public boolean hasAccess() {
        if (!hasAccess && this.playlists !=  null) {
            for (Playlist playlist :this.playlists) {
                if (playlist.hasAccess()) {
                    return playlist.hasAccess();
                }
            }
        }
        return hasAccess;
    }

    public void hasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public String getAlternativeTitle() {
        return alternativeTitle;
    }

    public void setAlternativeTitle(String alternativeTitle) {
        this.alternativeTitle = alternativeTitle;
    }

    public List<Playlist> getHosts() {
        return hosts;
    }

    public void setHosts(List<Playlist> hosts) {
        this.hosts = hosts;
    }

    public List<String> getSesamIds() {
        return sesamIds;
    }

    public void setSesamIds(List<String> sesamids) {
        this.sesamIds = sesamids;
    }

}
