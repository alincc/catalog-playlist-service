package no.nb.microservices.catalogplaylist.core.playlist.service;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
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
        Playlist playlist;
        ItemResource item = itemRepository.getItem(sesamId);

        if (!hasPlaylist(item)) {
            return null;
        }

        playlist = getSimplePlaylist(item);

        if (item.getMetadata().getRelatedItems() != null) {
            playlist.setPlaylists(addConstituent(item));
            playlist.setHosts(addHost(item));
        }
        return playlist;
    }

    private boolean hasPlaylist(ItemResource item) {
        List<String> mediatypes = Arrays.asList("radio","musikk");
        for (String mediatype : item.getMetadata().getMediaTypes()) {
            if (mediatypes.contains(mediatype.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private Playlist getSimplePlaylist(ItemResource item) {
        Playlist playlist = new Playlist();
        if (item.getAccessInfo().isDigital()) {
            playlist.hasMediafile(true);
            playlist.setUrn(item.getMetadata().getIdentifiers().getUrns().get(0));
            playlist.setUrns(item.getMetadata().getIdentifiers().getUrns());
        }
        if ("all".equalsIgnoreCase(item.getAccessInfo().getViewability())) {
            playlist.hasAccess(true);
        } else {
            playlist.hasAccess(false);
        }

        playlist.setSesamId(item.getMetadata().getIdentifiers().getSesamId());
        playlist.setTitle(item.getMetadata().getTitleInfo().getTitle());
        playlist.setMediatype(String.join(";",item.getMetadata().getMediaTypes()));

        return playlist;
    }

    private List<Playlist> addConstituent(ItemResource item) {
        List<Playlist> tracks = new ArrayList<>();
        tracks.add(getSimplePlaylist(item));

        if (item.getMetadata().getRelatedItems().getConstituents() == null) {
            return tracks;
        }
        for (ItemResource itemResource : item.getMetadata().getRelatedItems().getConstituents()) {
            Playlist subtrack = getSimplePlaylist(itemResource);
            // partnumber
            tracks.add(subtrack);
        }
        return tracks;
    }

    private List<Playlist> addHost(ItemResource item) {
        List<Playlist> tracks = new ArrayList<>();
        if (item.getMetadata().getRelatedItems().getHosts() == null) {
            return tracks;
        }
        for (ItemResource itemResource : item.getMetadata().getRelatedItems().getHosts()) {
            Playlist subtrack = getSimplePlaylist(itemResource);
            // partnumber
            tracks.add(subtrack);
        }
        return tracks;
    }
}
