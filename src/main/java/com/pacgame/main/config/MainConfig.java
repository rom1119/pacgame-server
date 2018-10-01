package com.pacgame.main.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        SecurityConfiguration.class,
        MvcConfigurer.class,
        AuthServerOAuth2Config.class,
        OAuth2ResourceServerConfig.class
})
public class MainConfig {

    @Bean
    public ModelMapper modelMapper()
    {
        return new ModelMapper();
    }

}
