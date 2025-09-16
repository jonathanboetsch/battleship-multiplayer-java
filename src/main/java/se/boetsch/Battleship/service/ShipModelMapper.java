package se.boetsch.Battleship.service;

import org.springframework.stereotype.Service;
import se.boetsch.Battleship.domain.ShipModel;
import se.boetsch.Battleship.dto.ShipToPlaceDTO;

@Service
public class ShipModelMapper {
    public ShipToPlaceDTO toPlaceDTO(ShipModel m) {
        return new ShipToPlaceDTO(m.getName(), m.getSize());
    }
}
