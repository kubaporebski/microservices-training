package jporebski.microservices.song_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/")
public class MainService {

    private final SongRepository repository;

    @Autowired
    public MainService(SongRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/songs")
    public ResponseEntity<AddResponse> add(@RequestBody Song song) {
        var saved = repository.save(song);
        return ResponseEntity.ok(new AddResponse(saved.getId()));
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<Song> get(@PathVariable("id") Integer id) {
        var song = repository.findById(id);
        if (song.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(song.get());
    }

    @DeleteMapping("/songs")
    public ResponseEntity<DeleteResponse> delete(@RequestParam("id") String ids) {
        if (ids == null || ids.isEmpty() || ids.length() >= 200)
            return ResponseEntity.badRequest().build();

        var deleted = new HashSet<Integer>();
        Arrays.stream(ids.split(",")).map(Integer::valueOf).forEach(id -> {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                deleted.add(id);
            }
        });

        return ResponseEntity.ok(new DeleteResponse(deleted));
    }

    public record DeleteResponse(Set<Integer> ids) { }

    public record AddResponse(int id) { }


}
