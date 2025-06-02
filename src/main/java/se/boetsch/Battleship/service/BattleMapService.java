package se.boetsch.Battleship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.boetsch.Battleship.entity.ShipCoordinates;

@Service
public class BattleMapService {

    @Autowired
    BattleMap battleMap;

    public int getValueAtCoordinates(ShipCoordinates shipCoordinates) {
        return battleMap.getMap()[shipCoordinates.getHorizontalPos()][shipCoordinates.getVerticalPos()];
    }


}
