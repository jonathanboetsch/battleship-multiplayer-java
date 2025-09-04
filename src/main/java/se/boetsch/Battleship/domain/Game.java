package se.boetsch.Battleship.domain;

import se.boetsch.Battleship.dto.Status;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private static final Set<SessionId> sessionIds = ConcurrentHashMap.newKeySet();
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
        this.playerIds.add(playerId);
    }

    public boolean isFinished() {
        return getStatus() == Status.FINISHED;
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

    public Status getStatus() {

        if (winner) return Status.FINISHED;
        if (playerIds.size() < 2) return Status.WAITING_FOR_PLAYERS;
        if (!allShipsPlaced()) return Status.PLACEMENT_PHASE;
        return Status.IN_PROGRESS;
    }

    private boolean allShipsPlaced() {
        return playerSets.values().stream().allMatch(PlayerSet::isShipsPlacementComplete);
    }


}
