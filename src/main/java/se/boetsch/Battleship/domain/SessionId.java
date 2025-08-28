package se.boetsch.Battleship.domain;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class SessionId {

    private static Set<SessionId> sessionIds = ConcurrentHashMap.newKeySet();

    private final AtomicLong seq = new AtomicLong(1); // start at 1
    private final long id;

    public SessionId() {
        this.id = retrieveNewSessionId();
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }

    public static Set<SessionId> getSessionIds() {
        return sessionIds;
    }

    public static void setSessionIds(Set<SessionId> sessionIds) {
        SessionId.sessionIds = sessionIds;
    }

    public static Random getRandomGen() {
        return randomGen;
    }

    public static void setRandomGen(Random randomGen) {
        SessionId.randomGen = randomGen;
    }

    public int getId() {
        return id;
    }

    // Limit set to 500 games
    private long retrieveNewSessionId() {
        return seq.getAndIncrement();
    }

}
