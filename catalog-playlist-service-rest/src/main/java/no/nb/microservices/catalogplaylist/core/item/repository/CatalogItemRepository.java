package no.nb.microservices.catalogplaylist.core.item.repository;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("catalog-item-service")
public interface CatalogItemRepository {
    @RequestMapping(value = "/catalog/v1/items/{id}", method = RequestMethod.GET)
    ItemResource getItem(@PathVariable("id") String id, @RequestParam(value = "expand" , defaultValue = "relatedItems") String expand);
}
