package no.nb.microservices.catalogplaylist.rest;

import no.nb.microservices.catalogplaylist.core.model.Playlist;
import no.nb.microservices.catalogplaylist.core.playlist.service.PlaylistService;
import no.nb.microservices.catalogplaylist.exception.PlaylistNotFoundException;
import no.nb.microservices.catalogplaylist.model.Rss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/catalog/v1/playlist/")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final RssPlaylistResourceAssembler rssPlaylistResourceAssembler;

    @Autowired
    public PlaylistController(PlaylistService playlistService, RssPlaylistResourceAssembler rssPlaylistResourceAssembler) {
        this.playlistService = playlistService;
        this.rssPlaylistResourceAssembler = rssPlaylistResourceAssembler;
    }

    @RequestMapping(value = "{sesamId}/jwplayer.rss", method = RequestMethod.GET)
    ResponseEntity<Rss> showJwPlayerRss(@PathVariable String sesamId,
                        @RequestParam(required = false) String offset,
                        @RequestParam(required = false) String extent) {
        Playlist playlist = playlistService.findById(sesamId);
        if (playlist == null) {
            throw new PlaylistNotFoundException("");
        }
        Rss rss = rssPlaylistResourceAssembler.toResource(playlist, offset, extent);
        return new ResponseEntity<Rss>(rss, HttpStatus.OK);
    }
}
