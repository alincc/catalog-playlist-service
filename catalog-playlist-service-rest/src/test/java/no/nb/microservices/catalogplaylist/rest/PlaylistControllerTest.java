package no.nb.microservices.catalogplaylist.rest;

import no.nb.microservices.catalogplaylist.config.ApplicationSettings;
import no.nb.microservices.catalogplaylist.core.model.Playlist;
import no.nb.microservices.catalogplaylist.core.playlist.service.PlaylistService;
import no.nb.microservices.catalogplaylist.exception.PlaylistNotFoundException;
import no.nb.microservices.catalogplaylist.model.Rss;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlaylistControllerTest {
    private PlaylistController playlistController;

    @Mock
    PlaylistService playlistService;

    @Before
    public void setup() {
        ApplicationSettings settings = new ApplicationSettings();
        settings.setStreamService("http://localhost:80");
        playlistController = new PlaylistController(playlistService, new RssPlaylistResourceAssembler(settings));
    }

    @Test
    public void whenPlaylistIsFoundStatusShouldBeSuccessful() throws Exception {
        Playlist playlist = new Playlist();
        playlist.setPlaylists(new ArrayList<>());
        when(playlistService.findById("1234")).thenReturn(playlist);
        ResponseEntity<Rss> entity = playlistController.showJwPlayerRss("1234", null, null);
        assertEquals(HttpStatus.OK,entity.getStatusCode());
    }

    @Test(expected = PlaylistNotFoundException.class)
    public void whenPlaylistIsNullExceptionShouldBeThrown() throws Exception {
        when(playlistService.findById("1234")).thenReturn(null);
        playlistController.showJwPlayerRss("1234",null,null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenIllegalIdExceptionShouldBeThrown() throws Exception {
        playlistController.showJwPlayerRss("!1324","1","2");
    }

}
