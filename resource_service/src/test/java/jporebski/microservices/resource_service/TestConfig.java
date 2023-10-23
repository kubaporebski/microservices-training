package jporebski.microservices.resource_service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public MainService.SongServiceAppInterface getSongServiceAppInterface() {
        return new MainService.SongServiceAppInterface() {
            @Override
            public SongAddResponse add(SongAddRequest request) {
                return new SongAddResponse(1);
            }
        };
    }

}
