package se.boetsch.Battleship.service;

import org.springframework.stereotype.Service;
import se.boetsch.Battleship.domain.FireResult;
import se.boetsch.Battleship.dto.CoordinatesDTO;
import se.boetsch.Battleship.dto.FireResultDTO;

@Service
public class FireResultMapper {
    public FireResultDTO toDTO(FireResult fireResult){
        return new FireResultDTO(fireResult.status(),
                new CoordinatesDTO(fireResult.coordinates().getX(), fireResult.coordinates().getY()));
    }
}
