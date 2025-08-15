package se.boetsch.Battleship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.boetsch.Battleship.entity.Player;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    public Optional<Player> findByName(String name);
    public Optional<Player> findById(Long id);

}
