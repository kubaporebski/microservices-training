package jporebski.microservices.resource_service;

import org.springframework.data.repository.CrudRepository;

public interface ResourceRepository extends CrudRepository<Resource, Integer> {
    
}
