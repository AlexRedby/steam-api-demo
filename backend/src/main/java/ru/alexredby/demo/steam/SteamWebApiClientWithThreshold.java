package ru.alexredby.demo.steam;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiRequest;
import ru.alexredby.demo.exceptions.OutOfThresholdException;
import ru.alexredby.demo.interfaces.Thresholdable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Wrapper of SteamWebApiClient with threshold of request per day.
 */
public class SteamWebApiClientWithThreshold extends SteamWebApiClient implements Thresholdable {
    /**
     * Limitation of calling the Steam Web API per day.
     *
     * Taken from Steam Web API Terms of Use.
     * See <a href="https://steamcommunity.com/dev/apiterms">https://steamcommunity.com/dev/apiterms<a/>
     */
    private static final int THRESHOLD = 100_000;

    /** Default zone id. */
    private static final ZoneId ZONE_ID = ZoneId.of("UTC");

    // TODO: mb save it with closing application
    /**
     * Holds last date of request to Steam Api.
     * Need to reset counter in a new day.
     */
    private LocalDate lastRequestDate;
    /** Holds count of request to Steam Api. */
    private int counter;

    public SteamWebApiClientWithThreshold(String apiToken) {
        super(apiToken);
        this.counter = COUNTER_INITIAL_VALUE;
        lastRequestDate = LocalDate.now(ZONE_ID);
    }

    /**
     * Overridden method from SteamWebApiClient called each time,
     * when it is make request to Steam Api. It's need to increment
     * counter and cancel requests, if threshold was reached.
     *
     * @throws CompletionException when OutOfThresholdException happens and
     *         hold it inside as a cause.
     */
    @Override
    public <V> CompletableFuture<V> sendRequest(SteamWebApiRequest message) throws CompletionException {
        try {
            incrementCounter();
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
    public int getThreshold() {
        return THRESHOLD;
    }

    @Override
    public void resetCounter() {
        this.counter = COUNTER_INITIAL_VALUE;
    }

    @Override
    public void incrementCounter() throws OutOfThresholdException {
        if (!canIncrement()) {
            throw new OutOfThresholdException(
                    "Threshold of "
                    + THRESHOLD
                    + " requests reached its limit today. Try it tomorrow(00:00:00 UTC)."
            );
        }
        this.counter += INCREMENT_VALUE;
    }

    /**
     * Decides can counter be increment or not.
     * Note: Also resets counter if it's new day.
     *
     * @return true or false as answer on question.
     */
    private boolean canIncrement() {
        LocalDate currentDate = LocalDate.now(ZONE_ID);
        // Checks if is new date and we can reset counter
        if (lastRequestDate.isBefore(currentDate)) {
            resetCounter();
            lastRequestDate = currentDate;
        }
        // Checks if next step will cross threshold
        return (this.counter + INCREMENT_VALUE) <= THRESHOLD;
    }
}
