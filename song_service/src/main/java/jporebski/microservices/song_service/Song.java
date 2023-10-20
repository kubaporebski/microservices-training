package jporebski.microservices.song_service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import jakarta.persistence.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Entity
@Table
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String artist;

    @Column(nullable = false)
    private String album;

    /** Seconds */
    @Column(nullable = false)
    @JsonDeserialize(using = SongLengthJson.Deserializer.class)
    @JsonSerialize(using = SongLengthJson.Serializer.class)
    private int length;

    @Column(nullable = false)
    private Integer year;

    /** External Id of a binary data. */
    @Column
    private Integer resourceId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * Serialization and deserialization of the song duration.
     * It is made for human-readable format of a song length.
     * Example:
     *  Song duration in seconds | String-formatted
     *                        27 | 00:27
     *                       121 | 02:01
     *                      3601 | 01:00:01
     */
    public final static class SongLengthJson {

        public static final class Deserializer extends JsonDeserializer<Integer> {

            @Override
            public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
                var length = jsonParser.getValueAsString();
                return deserializeFromString(length);
            }

            public static Integer deserializeFromString(String length) {
                if (length == null) {
                    return -1;
                } else {
                    if (length.contains(":")) {
                        var parts = Arrays.stream(length.split(":")).map(Integer::valueOf).toList();
                        int hours = 0, minutes = 0, seconds = 0;
                        if (parts.size() == 2) {
                            minutes = parts.get(0);
                            seconds = parts.get(1);
                        } else if (parts.size() == 3) {
                            hours = parts.get(0);
                            minutes = parts.get(1);
                            seconds = parts.get(2);
                        }

                        return hours * 3600 + minutes * 60 + seconds;
                    } else {
                        return Integer.valueOf(length);
                    }
                }
            }
        }

        public static final class Serializer extends JsonSerializer<Integer> {

            @Override
            public void serialize(Integer value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(serializeToString(value));
            }

            public static String serializeToString(Integer value) {
                var sb = new StringBuilder();

                if (value >= 3600)
                    sb.append(pad0(value / 3600)).append(":");

                if (value >= 60)
                    sb.append(pad0((value / 60) % 60)).append(":");

                sb.append(pad0(value % 60));

                // when a piece is less than minute long, let's return duration in the format: `00:ss`
                if (value < 60)
                    sb.insert(0, "00:");

                return sb.toString();
            }

            private static String pad0(Integer value) {
                return String.format("%02d", value);
            }
        }
    }
}
