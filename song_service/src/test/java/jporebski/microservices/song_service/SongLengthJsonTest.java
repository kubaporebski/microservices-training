package jporebski.microservices.song_service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class SongLengthJsonTest {

    @Test
    public void serializeTest() {

        Map<Integer, String> testTable = new HashMap<>();
        testTable.put(1, "00:01");
        testTable.put(11, "00:11");
        testTable.put(111, "01:51");
        testTable.put(1111, "18:31");
        testTable.put(11111, "03:05:11");

        for (Integer value : testTable.keySet()) {
            String expected = testTable.get(value);
            String actual = Song.SongLengthJson.Serializer.serializeToString(value);
            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    public void deserializeTest() {
        Map<String, Integer> testTable = new HashMap<>();
        testTable.put("00:01", 1);
        testTable.put("00:11", 11);
        testTable.put("01:51", 111);
        testTable.put("18:31", 1111);
        testTable.put("03:05:11", 11111);

        for (String length : testTable.keySet()) {
            int expected = testTable.get(length);
            int actual = Song.SongLengthJson.Deserializer.deserializeFromString(length);
            Assertions.assertEquals(expected, actual);
        }
    }

}
