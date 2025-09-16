package se.boetsch.Battleship.domain;

import lombok.Getter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class SessionId {

    @Getter
    private static Set<SessionId> sessionIds = ConcurrentHashMap.newKeySet();

    private static final AtomicLong seq = new AtomicLong(1); // start at 1
    @Getter
    private final long id;

    public SessionId() {
        this.id = retrieveNewSessionId();
    }

    @Override
    public String toString() {
        return Long.toString(id);
    }

    public static void setSessionIds(Set<SessionId> sessionIds) {
        SessionId.sessionIds = sessionIds;
    }

    // Limit set to 500 games
    private long retrieveNewSessionId() {
        return seq.getAndIncrement();
    }

}
