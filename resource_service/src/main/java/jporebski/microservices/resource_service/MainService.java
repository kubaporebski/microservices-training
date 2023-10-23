package jporebski.microservices.resource_service;

import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/")
@EnableFeignClients
public class MainService {

    private static final Logger logger = LoggerFactory.getLogger(MainService.class);

    private final ResourceRepository repository;
    private final Mpeg3Validator validator;
    private final Mpeg3MetadataReader metadataReader;

    /**
     * Eureka client for discovery of other clients.
     */
    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    /**
     * Interface pointing directly to Song Service.
     * Contains method for adding a song.
     *
     * Interface will be created by Feign library.
     */
    @Autowired
    @Lazy
    private SongServiceAppInterface songServiceAppInterface;

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

        logger.info("Has SongServiceInterface={}", songServiceAppInterface);
        if (songServiceAppInterface != null) {

            var metadata = metadataReader.read(inputFileContents);
            var request = SongServiceAppInterface.SongAddRequest.of(metadata, saved);

            logger.info("Request={}", request);
            // here is a real HTTP call to a remote song service, we don't just merely call some method in some interface
            var response = songServiceAppInterface.add(request);
            logger.info("Response={}", response);
        } else {
            logger.warn("SongServiceInterface couldn't be created for some reason");
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

    public record DeleteResponse(Set<Integer> ids) { }


    /**
     * We use Feign Client for calling another microservice.
     * Methods must have same signature
     */
    @FeignClient("SongServiceApp")
    public interface SongServiceAppInterface {

        @PostMapping("/songs")
        SongAddResponse add(@RequestBody SongAddRequest request);


        record SongAddRequest(
                String name, String artist, String album, Integer year, Integer length, Integer resourceId) {

            static SongAddRequest of(Mpeg3MetadataReader.Mpeg3Metadata metadata, Resource saved) {
                return new SongAddRequest(
                        metadata.name(), metadata.artist(), metadata.album(), metadata.year(), metadata.length(),
                        saved.getId());
            }
        }

        record SongAddResponse(Integer id) { }
    }

}
