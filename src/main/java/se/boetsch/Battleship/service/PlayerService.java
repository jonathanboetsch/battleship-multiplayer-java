package se.boetsch.Battleship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.boetsch.Battleship.entity.Player;
import se.boetsch.Battleship.repository.PlayerRepository;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public boolean isNameExisting(String name) {
        return playerRepository.findByName(name).isPresent();
    }

    public Player getByName(String name) {
        try {
            return playerRepository.findByName(name).get();
        } catch (Exception e) {
            throw new RuntimeException("Player not found with name: " + name);
        }
    }

    public void save(Player playerToSave) {
        playerRepository.save(playerToSave);
    }
}
