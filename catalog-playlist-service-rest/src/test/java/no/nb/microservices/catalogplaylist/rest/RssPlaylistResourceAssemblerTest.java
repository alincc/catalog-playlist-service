package no.nb.microservices.catalogplaylist.rest;

import no.nb.microservices.catalogplaylist.config.ApplicationSettings;
import no.nb.microservices.catalogplaylist.core.model.Playlist;
import no.nb.microservices.catalogplaylist.model.Rss;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class RssPlaylistResourceAssemblerTest {
    private RssPlaylistResourceAssembler assembler;

    @Before
    public void setup() {
        ApplicationSettings settings = new ApplicationSettings();
        settings.setStreamService("http://localhost");
        assembler = new RssPlaylistResourceAssembler(settings);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/id1");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testPlaylistWithOffsetExtent() {
        Playlist playlist = new Playlist();
        playlist.setSesamId("sesamid1");
        Playlist subtrack = new Playlist();
        subtrack.setUrn("urn2");
        subtrack.hasAccess(true);
        subtrack.hasMediafile(true);
        subtrack.setSesamId("sesamid2");
        playlist.setPlaylists(Arrays.asList(subtrack));
        Rss rss = assembler.toResource(playlist, "10", "50");
        assertNotNull(rss.getChannel().getItems());
        assertEquals("http://localhost/vod/urn2/playlist.m3u8&ssoToken=token&currSite=nbdigital&offset=10&extent=50", rss.getChannel().getItems().get(0).getJwPlayerSource().getFile());
    }

    @Test
    public void testPlaylistWithSuburn() {
        Playlist playlist = new Playlist();
        playlist.setSesamId("sesamid1");
        Playlist sub = new Playlist();
        sub.setSesamId("sesamid2");
        sub.hasMediafile(true);
        sub.hasAccess(true);
        sub.setUrn("urn1");
        sub.setSuburn("suburn1");
        playlist.setPlaylists(Arrays.asList(sub));
        Rss rss = assembler.toResource(playlist, null, null);
        assertNotNull(rss.getChannel().getItems());
        assertEquals("http://localhost/vod/urn1/playlist.m3u8&ssoToken=token&currSite=nbdigital&suburn=suburn1", rss.getChannel().getItems().get(0).getJwPlayerSource().getFile());
    }
}
