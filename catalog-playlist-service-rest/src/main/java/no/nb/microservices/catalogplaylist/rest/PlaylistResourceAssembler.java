package no.nb.microservices.catalogplaylist.rest;

import no.nb.microservices.catalogplaylist.core.model.Playlist;
import no.nb.microservices.catalogplaylist.model.PlaylistResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class PlaylistResourceAssembler extends ResourceAssemblerSupport<Playlist, PlaylistResource> {

    public PlaylistResourceAssembler() {
        super(PlaylistController.class, PlaylistResource.class);
    }

    public PlaylistResource toResource(Playlist playlist) {
        PlaylistResource resource = createResourceWithId(playlist.getSesamId(), playlist);
        resource.setSesamId(playlist.getSesamId());
        resource.setTitle(playlist.getTitle());
        resource.setUrn(playlist.getUrn());
        resource.setMediatype(playlist.getMediatype());
        resource.setGroup(playlist.getGroup());
        resource.setSesamIds(playlist.getSesamIds());
        resource.setPartNumber(playlist.getPartNumber());
        resource.setAlternativeTitle(playlist.getAlternativeTitle());

        if (playlist.getPlaylists() != null) {
            for (Playlist subTrack : playlist.getPlaylists()) {
                resource.addTracks(new PlaylistResourceAssembler().toResource(subTrack));

            }
        }
        if (playlist.getHosts() != null) {
            for (Playlist host : playlist.getHosts()) {
                resource.addHost(new PlaylistResourceAssembler().toResource(host));

            }
        }

        if (playlist.hasMediafile() && playlist.hasAccess()) {
            resource.add(linkTo(methodOn(PlaylistController.class).showJwPlayerRss(playlist.getSesamId(), null, null)).withRel("jwPlayer"));
        }

        return resource;
    }
}
