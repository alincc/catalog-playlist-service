package no.nb.microservices.catalogplaylist.core.playlist.service;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogitem.rest.model.StreamingInfo;
import no.nb.microservices.catalogplaylist.core.item.repository.CatalogItemRepository;
import no.nb.microservices.catalogplaylist.core.model.Playlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PlaylistService implements IPlaylistService {
    private final CatalogItemRepository itemRepository;

    @Autowired
    public PlaylistService(CatalogItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Playlist findById(String sesamId) {
        ItemResource item = itemRepository.getItem(sesamId, "relatedItems");
        List<String> mediatypes = item.getMetadata().getMediaTypes();
        if (mediatypes.contains("Musikk")) {
            return getMusicPlaylist(item);
        } else if (mediatypes.contains("Radio")) {
            return getRadioPlaylist(item);
        } else {
            return null;
        }
    }

    private Playlist getRadioPlaylist(ItemResource item) {
        Playlist playlist = getSimplePlaylist(item);
        List<Playlist> tracks = new ArrayList<>();
        if (item.getMetadata().getStreamingInfo() != null) {
            for (StreamingInfo streamingInfo : item.getMetadata().getStreamingInfo()) {
                Playlist pl = new Playlist();
                pl.hasMediafile(true);
                pl.setMediatype("radio");
                pl.setSesamId(playlist.getSesamId());
                pl.setUrn(playlist.getUrn());
                pl.setSuburn(streamingInfo.getIdentifier());
                pl.setTitle(playlist.getTitle());
                pl.hasAccess(playlist.hasAccess());
                tracks.add(pl);
            }
        }
        playlist.setPlaylists(tracks);
        return playlist;
    }

    private Playlist getMusicPlaylist(ItemResource item) {
        Playlist playlist = getSimplePlaylist(item);

        if (item.getRelatedItems() != null) {
            playlist.setPlaylists(addConstituent(item));
            playlist.setHosts(addHost(item));
        }
        return playlist;
    }

    private Playlist getSimplePlaylist(ItemResource item) {
        Playlist playlist = new Playlist();
        if (item.getAccessInfo().isDigital()) {
            playlist.hasMediafile(true);
            playlist.setUrn(item.getMetadata().getIdentifiers().getUrn());
        } else {
            playlist.hasMediafile(false);
        }
        if ("all".equalsIgnoreCase(item.getAccessInfo().getViewability())) {
            playlist.hasAccess(true);
        } else {
            playlist.hasAccess(false);
        }

        playlist.setSesamId(item.getMetadata().getIdentifiers().getSesamId());
        playlist.setTitle(item.getTitle());
        playlist.setMediatype(String.join(";",item.getMetadata().getMediaTypes()));

        return playlist;
    }

    private List<Playlist> addConstituent(ItemResource item) {
        List<Playlist> tracks = new ArrayList<>();
//        tracks.add(getSimplePlaylist(item));

        if (item.getRelatedItems().getConstituents() == null) {
            return tracks;
        }
        for (ItemResource itemResource : item.getRelatedItems().getConstituents()) {
            Playlist subtrack = getSimplePlaylist(itemResource);
            // partnumber
            tracks.add(subtrack);
        }
        return tracks;
    }

    private List<Playlist> addHost(ItemResource item) {
        List<Playlist> tracks = new ArrayList<>();
        if (item.getRelatedItems().getHosts() == null) {
            return tracks;
        }
        for (ItemResource itemResource : item.getRelatedItems().getHosts()) {
            Playlist subtrack = getSimplePlaylist(itemResource);
            // partnumber
            tracks.add(subtrack);
        }
        return tracks;
    }
}
