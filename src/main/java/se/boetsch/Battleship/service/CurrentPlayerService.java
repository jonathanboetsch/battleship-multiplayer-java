package se.boetsch.Battleship.service;

import io.netty.util.internal.ConcurrentSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.boetsch.Battleship.domain.CurrentPlayer;
import se.boetsch.Battleship.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CurrentPlayerService {

    @Autowired
    private PlayerService playerService;

    private static Set<CurrentPlayer> players = ConcurrentHashMap.newKeySet();

    public CurrentPlayer create(String name) {
        CurrentPlayer player;
        if (playerService.isNameExisting(name)) {
            player = new CurrentPlayer(playerService.getByName(name));
        } else {
            Player playerToSave = new Player(name);
            playerService.save(playerToSave);
            player = new CurrentPlayer(playerToSave);
        }
            players.add(player);
            return player;
    }

    public static Optional<CurrentPlayer> findByName(String name) {
        return players.stream()
                .filter(p -> p.getPlayer().getName().equals(name))
                .findFirst();
    }

}
