package se.boetsch.Battleship.domain;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Game {

    private static final Set<SessionId> sessionIds = ConcurrentHashMap.newKeySet();
    private final Random randomGen = new Random();
    private SessionId sessionId;
    private Set<Long> playerIds;
    private Map<Long, PlayerSet> playerSets;
    private Map<Long, Boolean> playersTurn = new ConcurrentHashMap<>(2);
    boolean winner = false;

    public Game() {
        this.playerIds = ConcurrentHashMap.newKeySet();
        this.playerSets = new ConcurrentHashMap<>(2);
        this.sessionId = new SessionId();
        sessionIds.add(sessionId);
    }

    public Game(Long playerId) {
        this();

    public int getNumberOfRemainingShips() {
        return numberOfRemainingShips;
    }

    public void setNumberOfRemainingShips(int numberOfRemainingShips) {
        this.numberOfRemainingShips = numberOfRemainingShips;
        this.playerIds.add(playerId);
    }

    public boolean isFinished() {
        return numberOfRemainingShips == 0;
    }


    public Map<Long, PlayerSet> getPlayerSets() {
        return playerSets;
    }

    public void setPlayerSets(Map<Long, PlayerSet> playerSets) {
        this.playerSets = playerSets;
    }


    public SessionId getSessionId() {
        return sessionId;
    }

    public void setSessionId(SessionId sessionId) {
        this.sessionId = sessionId;
    }

    public Set<Long> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(Set<Long> ids) {
        checkNumberOfPlayers(ids.size());
        playerIds = ids;
    }


    public void addPlayer(CurrentPlayer player) {
        checkNumberOfPlayers(playerIds.size() + 1);
        playerIds.add(player.getPlayer().getId());
        playerSets.put(player.getPlayer().getId(), new PlayerSet(sessionId));
        playersTurn.put(player.getPlayer().getId(), false);
    }

    private void checkNumberOfPlayers(int number) {
        if (number > 2) throw new RuntimeException("Cannot add more than 2 players");
    }

    public PlayerSet getPlayerSetForPlayer(CurrentPlayer player) {
        Long id = player.getPlayer().getId();
        if (!playerSets.containsKey(id)) throw new RuntimeException("Player not found in the game");
        else return playerSets.get(id);
    }

    public Map<Long, Boolean> getPlayersTurn() {
        return playersTurn;
    }

    public void setPlayersTurn(Map<Long, Boolean> playersTurn) {
        this.playersTurn = playersTurn;
    }

    public void changeTurn() {
        for (Map.Entry<Long, Boolean> entry : playersTurn.entrySet()) {
            playersTurn.put(entry.getKey(), !entry.getValue());
        }

    }
}
