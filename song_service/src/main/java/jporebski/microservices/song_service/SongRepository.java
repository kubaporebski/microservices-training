package jporebski.microservices.song_service;

import org.springframework.data.repository.CrudRepository;

public interface SongRepository extends CrudRepository<Song, Integer> {

}
