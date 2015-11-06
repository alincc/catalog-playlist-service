package no.nb.microservices.catalogplaylist.rest;

import no.nb.microservices.catalogplaylist.core.model.Playlist;
import no.nb.microservices.catalogplaylist.model.PlaylistResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PlaylistResourceAssemblerTest {
    private PlaylistResourceAssembler assembler;

    @Before
    public void setup() {
        assembler = new PlaylistResourceAssembler();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/id1");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testPlaylistWithHosts() {
        Playlist playlist = new Playlist();
        playlist.setSesamId("sesamid1");
        playlist.setTitle("Tittel");
        playlist.setUrn("urn1");
        playlist.setMediatype("musikk");
        playlist.setGroup("");
        Playlist host = new Playlist();
        host.setSesamId("sesamid2");
        playlist.setHosts(Arrays.asList(host));
        PlaylistResource resource = assembler.toResource(playlist);
        assertEquals("Playlist should contain 1 host", 1, resource.getHosts().size());
    }

    @Test
    public void testPlaylistWithTracks() {
        Playlist playlist = new Playlist();
        playlist.hasMediafile(true);
        playlist.hasAccess(true);
        playlist.setSesamId("sesamid1");
        Playlist subtrack = new Playlist();
        subtrack.setSesamId("sesamid2");
        playlist.setPlaylists(Arrays.asList(subtrack));
        PlaylistResource resource = assembler.toResource(playlist);
        assertEquals("Playlist should contain 1 track", 1, resource.getTracks().size());
    }
}
