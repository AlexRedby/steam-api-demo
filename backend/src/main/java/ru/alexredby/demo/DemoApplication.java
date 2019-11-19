package ru.alexredby.demo;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public SteamWebApiClient getSteamWebApiClient() {
		return new SteamWebApiClientWithCounter(ApiTokenClass.API_TOKEN);
	}
}
