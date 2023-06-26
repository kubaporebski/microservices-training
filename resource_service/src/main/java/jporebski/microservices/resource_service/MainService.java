package jporebski.microservices.resource_service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/")
public class MainService {

    private final ResourceRepository repository;
    private final Mpeg3Validator validator;
    private final Mpeg3MetadataReader metadataReader;

    @Value("${songservice.url}")
    private String songServiceUrl;

    @Autowired
    public MainService(ResourceRepository repository, Mpeg3Validator validator, Mpeg3MetadataReader metadataReader)
    {
        this.repository = repository;
        this.validator = validator;
        this.metadataReader = metadataReader;
    }

    @PostMapping("/resources")
    public ResponseEntity<Resource> add(@RequestBody byte[] inputFileContents) throws Exception {
        if (!validator.quickValidate(inputFileContents))
            return ResponseEntity.badRequest().build();

        var inputFile = new Resource(inputFileContents);
        var saved = repository.save(inputFile);

        if (songServiceUrl != null && !songServiceUrl.isEmpty()) {

            var metadata = metadataReader.read(inputFileContents);
            var postRequestJson = new Gson().toJson(PostSongServiceRequest.of(metadata, saved));

            var request = HttpRequest.newBuilder()
                    .uri(new URI(songServiceUrl))
                    .header("Content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(postRequestJson))
                    .build();

            HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            // var response = new Gson().fromJson(jsonResponse.body(), PostSongServiceResponse.class);
        }

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
        Arrays.stream(ids.split(",")).map(Integer::valueOf).forEach(id -> {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                deleted.add(id);
            }
        });

        return ResponseEntity.ok(new DeleteResponse(deleted));
    }

    public record PostSongServiceRequest(String name, String artist, String album, Integer year, Integer length, Integer resourceId) {

        public static PostSongServiceRequest of(Mpeg3MetadataReader.Mpeg3Metadata metadata, Resource saved) {
            return new PostSongServiceRequest(
                    metadata.name(), metadata.album(), metadata.album(), metadata.year(), metadata.length(),
                    saved.getId());
        }
    }

    public record DeleteResponse(Set<Integer> ids) {
    }

    public record PostSongServiceResponse(Integer id) {
    }

}
