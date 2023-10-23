package jporebski.microservices.resource_service;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.stereotype.Component;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;

@Component
public class Mpeg3MetadataReader {

    /**
     * Extract valuable information from a MP3 file.
     * Information retrieved: title, artist(s), album, release date, and duration [seconds]
     *
     * @param data - mp3 file contents as a byte array
     * @return object which contains interesting information about provided mp3 file
     */
    public Mpeg3Metadata read(byte[] data) {

        try (InputStream bais = new ByteArrayInputStream(data)) {
            var metadata = new Metadata();
            var parser = new Mp3Parser();
            parser.parse(bais, new DefaultHandler(), metadata, new ParseContext());

            var name = metadata.get("dc:title");
            var artist = metadata.get("xmpDM:artist");
            var album = metadata.get("xmpDM:album");
            var year = Integer.valueOf(getOrZero(metadata, "xmpDM:releaseDate"));
            var durationSeconds = (int)Float.parseFloat(getOrZero(metadata, "xmpDM:duration"));

            return new Mpeg3Metadata(name, artist, album, year, durationSeconds);

        } catch (Exception ex) {
            throw new MetadataException(ex);
        }
    }

    private static String getOrZero(Metadata metadata, String key) {
        return Objects.toString(metadata.get(key), "0");
    }

    public record Mpeg3Metadata(String name, String artist, String album, Integer year, Integer length) {

    }

    public static class MetadataException extends RuntimeException {

        public MetadataException(Exception ex) {
            super(ex);
        }
    }

}
