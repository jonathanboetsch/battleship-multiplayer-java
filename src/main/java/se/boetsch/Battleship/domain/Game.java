package se.boetsch.Battleship.domain;

import lombok.Getter;
import lombok.Setter;
import se.boetsch.Battleship.dto.Status;
import se.boetsch.Battleship.exception.GameException;
import se.boetsch.Battleship.exception.PlayerRetrievalException;
import se.boetsch.Battleship.exception.PlayerSetRetrievalException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
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
        playerSets.put(playerId, new PlayerSet(sessionId));
    }

    public boolean isFinished() {
        return getStatus() == Status.FINISHED;
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

    public PlayerSet getPlayerSetForPlayerWithId(long playerId) {
        try {
            return playerSets.get(playerId);
        } catch (Exception e) {
            throw new PlayerSetRetrievalException(playerId);
        }
    }

    public PlayerSet getPlayerSetForPlayerWithId(Long playerId) {
        try {
            return playerSets.get(playerId);
        } catch (Exception e) {
            throw new GameException("Could not retrieve player set for player with id: " + playerId);
        }
    }

    public void changeTurn() {
        playersTurn.replaceAll((k, v) -> !v);

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

    public Long getOtherPlayerId(long playerId) {
        return playerIds.stream().filter(id -> id != playerId).findFirst().orElseThrow(() -> new PlayerRetrievalException("Adversary not found yet"));
    }


}
