package ru.alexredby.demo.configs;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.alexredby.demo.steam.ApiTokenClass;
import ru.alexredby.demo.steam.SteamWebApiClientWithThreshold;

/**
 * Common config class for beans and maybe something else in future.
 */
@Configuration
public class AppConfig {

    /**
     * Bean of SteamWebApiClient to make request to Steam Api.
     * Note: Should be single on whole application.
     */
    @Bean
    public SteamWebApiClient getSteamWebApiClient() {
        return new SteamWebApiClientWithThreshold(ApiTokenClass.API_TOKEN);
    }
}
