package se.boetsch.Battleship.domain;

import lombok.Getter;
import lombok.Setter;
import se.boetsch.Battleship.dto.Status;
import se.boetsch.Battleship.exception.*;

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
    private Status status;

    public Game() {
        this.playerIds = ConcurrentHashMap.newKeySet();
        this.playerSets = new ConcurrentHashMap<>(2);
        this.sessionId = new SessionId();
        sessionIds.add(sessionId);
        this.status=Status.WAITING_FOR_PLAYERS;
    }

    public Game(Long playerId) {
        this();
        this.playerIds.add(playerId);
        playerSets.put(playerId, new PlayerSet(sessionId));
        playersTurn.put(playerId, true);
    }

    public boolean isFinished() {
        return getStatus() == Status.FINISHED;
    }


    public void setPlayerIds(Set<Long> ids) {
        checkNumberOfPlayers(ids.size());
        playerIds = ids;
    }


    public void addPlayer(CurrentPlayer player) {
        checkPlayerAlreadyInGame(player);
        checkNumberOfPlayers(playerIds.size() + 1);
        long playerId = player.getPlayer().getId();
        playerIds.add(playerId);
        playerSets.put(playerId, new PlayerSet(sessionId));
        playersTurn.put(playerId, false);
        System.out.println("player with id " + playerId + " added to game");
    }

    private void checkPlayerAlreadyInGame(CurrentPlayer player) {
        if (playerIds.contains(player.getPlayer().getId())) {throw new GameLogicException("Player with id " + player.getPlayer().getId() + " already added to game");
        }
    }

    private void checkNumberOfPlayers(int number) {
        if (number > 2) throw new PlayerCreationException("Cannot add more than 2 players");
    }

    public PlayerSet getPlayerSetForPlayer(CurrentPlayer player) {
        Long id = player.getPlayer().getId();
        if (!playerSets.containsKey(id)) throw new PlayerRetrievalException("Player not found in the game");
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

        if (isFinished(playerSets)) return status=Status.FINISHED;
        if (playerIds.size() < 2) return status=Status.WAITING_FOR_PLAYERS;
        if (!allShipsPlaced()) return status=Status.PLACEMENT_PHASE;
        return status=Status.IN_PROGRESS;
    }

    private boolean isFinished(Map<Long, PlayerSet> playerSets) {
        for (PlayerSet playerSet : playerSets.values()) {
            if (playerSet.areAllShipsSunk()) return true;
        }
        return false;
    }

    private boolean allShipsPlaced() {
        return playerSets.values().stream().allMatch(PlayerSet::isShipsPlacementComplete);
    }

    public Long getOtherPlayerId(long playerId) {
        return playerIds.stream().filter(id -> id != playerId).findFirst().orElseThrow(() -> new PlayerRetrievalException("Adversary not found yet"));
    }


}
