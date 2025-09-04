package se.boetsch.Battleship.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import se.boetsch.Battleship.domain.ShipModel;
import se.boetsch.Battleship.domain.ShipWithPlacement;
import se.boetsch.Battleship.dto.ShipWithPlacementDTO;

@Service
public class ShipWithPlacementMapper {

    private final ShipService shipService;
    public ShipWithPlacementMapper(ShipService shipService) {
        this.shipService = shipService;
    }


    public ShipWithPlacementDTO toDTO(ShipWithPlacement s) {
        return new ShipWithPlacementDTO(s.getName(),
                s.getStartPosition(),
                s.getOrientation());
    }

    public ShipWithPlacement toDomain(@Valid ShipWithPlacementDTO ship) {
        ShipModel s = shipService.getShipByName(ship.name());
        return new ShipWithPlacement(s,
                ship.startPosition(),
                ship.orientation());
    }
}
