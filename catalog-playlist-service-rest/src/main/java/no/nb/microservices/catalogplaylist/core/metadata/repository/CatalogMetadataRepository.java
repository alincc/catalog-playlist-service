package no.nb.microservices.catalogplaylist.core.metadata.repository;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("catalog-metadata-service")
public interface CatalogMetadataRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/catalog/metadata/{id}/mods", produces = MediaType.APPLICATION_XML_VALUE)
    Mods getModsById(@PathVariable("id") String id,
                     @RequestParam("X-Forwarded-Host") String xHost,
                     @RequestParam("X-Forwarded-Port") String xPort,
                     @RequestParam("X-Original-IP-Fra-Frontend") String xRealIp,
                     @RequestParam("amsso") String ssoToken);

}
