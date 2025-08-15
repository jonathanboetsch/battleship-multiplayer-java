package se.boetsch.Battleship.domain;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Game {

    private static final Set<SessionId> sessionIds = ConcurrentHashMap.newKeySet();
    private final Random randomGen = new Random();
    private SessionId sessionId;
    private Set<CurrentPlayer> players;
    private Map<CurrentPlayer, PlayerSet> playerSets;
    private Map<CurrentPlayer, Boolean> playersTurn = new ConcurrentHashMap<>(2);
    private int numberOfRemainingShips;

    public Game() {
        this.players = ConcurrentHashMap.newKeySet();
        this.playerSets = new ConcurrentHashMap<>();
        this.sessionId = new SessionId();
        sessionIds.add(sessionId);
    }

    public Game(CurrentPlayer player) {
        this();
        this.players.add(player);
        playersTurn.put(player, false);
    }

    public int getNumberOfRemainingShips() {
        return numberOfRemainingShips;
    }

    public void setNumberOfRemainingShips(int numberOfRemainingShips) {
        this.numberOfRemainingShips = numberOfRemainingShips;
    }

    public boolean isFinished() {
        return numberOfRemainingShips == 0;
    }


    public Map<CurrentPlayer, PlayerSet> getPlayerSets() {
        return playerSets;
    }

    public void setPlayerSets(Map<CurrentPlayer, PlayerSet> playerSets) {
        this.playerSets = playerSets;
    }


    public SessionId getSessionId() {
        return sessionId;
    }

    public void setSessionId(SessionId sessionId) {
        this.sessionId = sessionId;
    }

    public Set<CurrentPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(Set<CurrentPlayer> players) {
        checkNumberOfPlayers(players.size());
        this.players = players;
    }


    public void addPlayer(CurrentPlayer player) {
        checkNumberOfPlayers(players.size() + 1);
        this.players.add(player);
        this.playerSets.put(player, new PlayerSet(sessionId));
        playersTurn.put(player, false);
    }

    private void checkNumberOfPlayers(int number) {
        if (number > 2) throw new RuntimeException("Cannot add more than 2 players");
    }

    public PlayerSet getPlayerSetForPlayer(CurrentPlayer player) {
        if (!this.playerSets.containsKey(player)) throw new RuntimeException("Player not found in the game");
        else return this.playerSets.get(player);
    }

    public Map<CurrentPlayer, Boolean> getPlayersTurn() {
        return playersTurn;
    }

    public void setPlayersTurn(Map<CurrentPlayer, Boolean> playersTurn) {
        this.playersTurn = playersTurn;
    }

    public void changeTurn() {
        for (Map.Entry<CurrentPlayer, Boolean> entry : playersTurn.entrySet()) {
            playersTurn.put(entry.getKey(), !entry.getValue());
        }

    }
}
