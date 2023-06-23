package jporebski.microservices.song_service;

import org.springframework.data.repository.CrudRepository;

public interface CreationRepository extends CrudRepository<Song, Integer> {

}
