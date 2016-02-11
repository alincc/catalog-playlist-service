package no.nb.microservices.catalogplaylist.core.playlist.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nb.microservices.catalogitem.rest.model.*;
import no.nb.microservices.catalogplaylist.core.item.repository.CatalogItemRepository;
import no.nb.microservices.catalogplaylist.core.model.Playlist;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlaylistServiceTest {
    @InjectMocks
    PlaylistService playlistService;

    @Mock
    CatalogItemRepository catalogItemRepository;

    @Test
    public void whenMusicHasRelatedItemsPlaylistShouldNotBeEmpty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File itemFile = new File(Paths.get(getClass().getResource("/json/item1.json").toURI()).toString());
        ItemResource item = mapper.readValue(itemFile, ItemResource.class);
        when(catalogItemRepository.getItem("1234","relatedItems")).thenReturn(item);
        Playlist playlist = playlistService.findById("1234");
        assertTrue("Playlist should not be empty", !playlist.getPlaylists().isEmpty());
    }

    @Test
    public void whenMusicHasNoRelatedItemsPlaylistsShouldBeNull() throws Exception {
        ItemResource item = new ItemResource();
        item.setMetadata(new Metadata());
        item.getMetadata().setIdentifiers(new Identifiers());
        item.getMetadata().getIdentifiers().setUrn("urn1234");
        item.setAccessInfo(new AccessInfo());
        item.getAccessInfo().setDigital(true);
        item.setMediatypes(Arrays.asList("Musikk"));
        when(catalogItemRepository.getItem("1234","relatedItems")).thenReturn(item);
        Playlist playlist = playlistService.findById("1234");
        assertNull("Playlists should be null", playlist.getPlaylists());
    }

    @Test
    public void whenWrongMediatypeResponseShouldBeNull() throws Exception {
        ItemResource item = new ItemResource();
        item.setMetadata(new Metadata());
        item.setMediatypes(Arrays.asList("random","mediatypes"));
        when(catalogItemRepository.getItem("1234","relatedItems")).thenReturn(item);
        Playlist pl = playlistService.findById("1234");
        assertNull("playlist should be null",pl);
    }

    @Test
    public void whenRadioHasStreamingInfoPlaylistsShouldNotBeEmpty() {
        ItemResource item = new ItemResource();
        item.setMetadata(new Metadata());
        item.setMediatypes(Arrays.asList("Radio"));
        item.getMetadata().setIdentifiers(new Identifiers());
        item.getMetadata().getIdentifiers().setUrn("urn1234");
        item.setAccessInfo(new AccessInfo());
        item.getAccessInfo().setDigital(true);
        item.getMetadata().setStreamingInfo(Arrays.asList(new StreamingInfo("id1",1,10), new StreamingInfo("id2",2,30)));
        when(catalogItemRepository.getItem("1234", "relatedItems")).thenReturn(item);
        Playlist playlist = playlistService.findById("1234");
        assertTrue("Playlists should not be empty", !playlist.getPlaylists().isEmpty());
    }
}
