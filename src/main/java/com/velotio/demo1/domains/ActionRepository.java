package com.velotio.demo1.domains;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "actions", path = "actions")
public interface ActionRepository extends PagingAndSortingRepository<Action, Long> {
    List<Action> findByOrganization(@Param("organization") Organization organization);
}
