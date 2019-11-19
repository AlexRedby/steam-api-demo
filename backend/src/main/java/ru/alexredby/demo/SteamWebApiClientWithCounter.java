package ru.alexredby.demo;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.alexredby.demo.exceptions.OutOfThresholdException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

// TODO: more javadoc and rewrite
public class SteamWebApiClientWithCounter extends SteamWebApiClient implements Countable {
    /**
     * Limitation to call to the Steam Web API per day.
     *
     * Taken from Steam Web API Terms of Use
     * @see <a href="https://steamcommunity.com/dev/apiterms">https://steamcommunity.com/dev/apiterms<a/>
     */
    public static final int THRESHOLD = -1;//100_000;

    public static final int INCREMENT_VALUE = 1;
    public static final int COUNTER_INITIAL_VALUE = 0;

    private LocalDate lastRequestDate;
    private int counter;

    public SteamWebApiClientWithCounter(String apiToken) {
        super(apiToken);
        this.counter = COUNTER_INITIAL_VALUE;
        lastRequestDate = LocalDate.now(ZoneId.of("UTC"));
    }

    @Override
    public <V> CompletableFuture<V> sendRequest(SteamWebApiRequest message) {
        try {
            increment();
        } catch (OutOfThresholdException e) {
            throw new CompletionException("Threshold reached. Can't proceed.", e);
        }
        return super.sendRequest(message);
    }

    @Override
    public int getCount() {
        return counter;
    }

    @Override
    public void reset() {
        this.counter = COUNTER_INITIAL_VALUE;
    }

    @Override
    public void increment() throws OutOfThresholdException {
        if (counter + INCREMENT_VALUE > THRESHOLD) {
            throw new OutOfThresholdException("Threshold of " + THRESHOLD + " reached his limits today. Try it tomorrow(00:00:00 UTC).");
        }
        this.counter += INCREMENT_VALUE;
    }
}
