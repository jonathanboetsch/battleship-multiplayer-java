package se.boetsch.Battleship.service;

import org.springframework.stereotype.Service;
import se.boetsch.Battleship.domain.CurrentPlayer;
import se.boetsch.Battleship.entity.Player;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CurrentPlayerService {

    private final PlayerService playerService;
    // player id as key preferred over its name since it is more stable and not subject to collision
    private final ConcurrentHashMap<Long, CurrentPlayer> players = new ConcurrentHashMap<>();
    // player name mapped to its id to help CurrentPlayer retrieval by name
    private final ConcurrentHashMap<String, Long> idByName = new ConcurrentHashMap<>();

    public CurrentPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    private static String clean(String name) {
        return name == null ? "" : name.trim().toLowerCase();
    }

    public CurrentPlayer create(String name) {
        String playerName = clean(name);
        CurrentPlayer player;
        if (playerName.isEmpty()) {
            throw new IllegalArgumentException("Player name is empty");
        }
        if (playerService.isNameExisting(name)) {
            player = new CurrentPlayer(playerService.getByName(name));
        } else {
            Player playerToSave = new Player(name);
            playerService.save(playerToSave);
            player = new CurrentPlayer(playerToSave);
        }
        players.put(player.getPlayer().getId(), player);
        idByName.put(player.getName(), player.getPlayer().getId());
        return player;
    }

    public Optional<CurrentPlayer> findByName(String name) {
        Optional<Long> playerId = Optional.ofNullable(idByName.get(name));
        return playerId.map(players::get);
    }

    public Optional<CurrentPlayer> findById(Long playerId) {
        return players.values().stream().filter(p -> p.getPlayer()
                .getId() == playerId).findAny();
    }

    public void addPlayer(CurrentPlayer player) {
        players.put(player.getPlayer().getId(), player);
        idByName.put(player.getPlayer().getName(), player.getPlayer().getId());
    }
}
