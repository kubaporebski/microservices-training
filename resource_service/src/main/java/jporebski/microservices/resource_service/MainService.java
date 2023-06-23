package jporebski.microservices.resource_service;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class MainService {

    private final ResourceRepository repository;

    private final Mpeg3Validator validator;

    @Autowired
    public MainService(ResourceRepository repository, Mpeg3Validator validator)
    {
        this.repository = repository;
        this.validator = validator;
    }

    @PostMapping("/resources")
    public ResponseEntity<Resource> add(@RequestBody byte[] inputFileContents)
    {
        if (!validator.quickValidate(inputFileContents))
            return ResponseEntity.badRequest().build();

        var inputFile = new Resource(inputFileContents);
        var saved = repository.save(inputFile);

        return ResponseEntity.ok(new Resource(saved.getId()));
    }

    @GetMapping("/resources/{id}")
    public ResponseEntity<byte[]> get(@PathVariable("id") Integer id)
    {
        var data = repository.findById(id);
        if (data.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(data.get().getContents());
    }

    @DeleteMapping("/resources")
    public ResponseEntity<DeleteResponse> delete(@RequestParam("id") String ids)
    {
        if (ids == null || ids.isEmpty())
            return ResponseEntity.badRequest().build();

        var deleted = new HashSet<Integer>();
        Arrays.stream(ids.split(",")).map(id -> Integer.valueOf(id)).forEach(id -> {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                deleted.add(id);
            }
        });

        return ResponseEntity.ok(new DeleteResponse(deleted));
    }

    public static class DeleteResponse
    {
        private final Set<Integer> ids;

        public DeleteResponse(Set<Integer> ids) {
            this.ids = ids;
        }

        public Set<Integer> getIds() {
            return ids;
        }
    }

}
