package se.boetsch.Battleship.domain;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionId {

    private static Set<SessionId> sessionIds = ConcurrentHashMap.newKeySet();

    private static Random randomGen = new Random();

    private final int id;

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
    private int retrieveNewSessionId() {
        for (int i = 0; i < 500; i++) {
            int newId = randomGen.nextInt(0, 500);
            if (sessionIds.contains(newId)) continue;
            return newId;
        }
        throw new RuntimeException("SessionId encountered an error: all ids already used");
    }

}
