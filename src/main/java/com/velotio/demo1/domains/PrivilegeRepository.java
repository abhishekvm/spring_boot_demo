package com.velotio.demo1.domains;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "privileges", path = "privileges")
public interface PrivilegeRepository extends PagingAndSortingRepository<Privilege, Long> {
    Privilege findByName(@Param("name") String name);
}
