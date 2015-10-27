package no.nb.microservices.catalogplaylist.core.playlist.service;

import no.nb.microservices.catalogplaylist.core.model.Playlist;

public interface IPlaylistService {
    Playlist findById(String sesamId);
}
