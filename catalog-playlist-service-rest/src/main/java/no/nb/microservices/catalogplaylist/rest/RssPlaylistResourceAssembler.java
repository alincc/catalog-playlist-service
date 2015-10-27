package no.nb.microservices.catalogplaylist.rest;

import no.nb.microservices.catalogplaylist.config.ApplicationSettings;
import no.nb.microservices.catalogplaylist.core.model.Playlist;
import no.nb.microservices.catalogplaylist.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RssPlaylistResourceAssembler {
    private final ApplicationSettings settings;

    @Autowired
    public RssPlaylistResourceAssembler(ApplicationSettings settings) {
        this.settings = settings;
    }

    public Rss toResource(Playlist playlist, String offset, String extent) {
        Rss rss = new Rss();

        Channel channel = new Channel();
        rss.setChannel(channel);

        channel.setTitle(playlist.getTitle());
        channel.setDescription("Playlist");

        for (Playlist pl : playlist.getPlaylists()) {
            PlaylistResourceAssembler assembler = new PlaylistResourceAssembler();
            PlaylistResource resource = assembler.toResource(pl);

            Item item = new Item();
            item.setGuid(resource.getId().getHref());
            item.setTitle(resource.getTitle());
            item.setDescription("");
            String jwp = settings.getStreamService() + "/vod/" + resource.getUrn() + "/playlist.m3u8&ssoToken=" + "token" + "&currSite=nbdigital";
            if (offset != null) {
                jwp += "&offset=" + offset;
            }
            if (extent != null) {
                jwp += "&extent=" + extent;
            }
            if (pl.getSuburn() != null) {
                jwp += "&suburn=" + pl.getSuburn();
            }

            JwPlayerSource playerSource = new JwPlayerSource();
            playerSource.setFile(jwp);
            item.setJwPlayerSource(playerSource);
            item.setImage("http://www.nb.no/nbsok/resources/images/filmvisning_1.png");
            channel.getItems().add(item);
        }
        return rss;
    }
}
