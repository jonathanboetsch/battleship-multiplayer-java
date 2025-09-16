package se.boetsch.Battleship.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.boetsch.Battleship.domain.*;
import se.boetsch.Battleship.entity.Player;
import se.boetsch.Battleship.entity.ShipCoordinates;

import se.boetsch.Battleship.entity.ShipOrientation;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class GameServiceTest {
    @Mock
    private ShipWithPlacementMapper shipWithPlacementMapper;
    @Mock
    private GameShipMapper gameShipMapper;
	@Mock
    private ConcurrentHashMap<Long, Game> games;
	@Mock
    private ConcurrentHashMap<Long, Game> gamesById;
	@Mock
    private CurrentPlayerService currentPlayerService;
    @Mock
    private BattleMapService battleMapService;
	@Mock
    private GameMapper gameMapper;
	@Mock
    private ShipPlacementService shipPlacementService;
	@Mock
    private ShipModelMapper shipModelMapper;
    @InjectMocks GameService gameService;

    private Game g;
    private PlayerSet ps;
    private CurrentPlayer pl;
    private ShipCoordinates coordinates;
    private GameShip s;
    private CurrentPlayerService cps;


    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        ShipService shipService = new ShipService(); // To get ShipModel
        g = new Game();
        // bm = new BattleMapService();
        games.put(g.getSessionId().getId(), g);
        gamesById.put(g.getSessionId().getId(), g);
        coordinates = new ShipCoordinates(1, 2);
        cps = new CurrentPlayerService(new PlayerService());

        s = new GameShip(
                new ShipWithPlacement(
                        shipService.getShipModels().stream()
                                .filter(m -> m.getName().equalsIgnoreCase("Battleship"))
                                .findFirst().get(),
                        coordinates,
                        ShipOrientation.VERTICAL
                )
        );

        pl = new CurrentPlayer(new Player("foo"));

    }

    @Test
    void testResolveFire() {
        g.addPlayer(pl);
        g.getPlayerSetForPlayer(pl).addPlacedShip(s);
        when(battleMapService.getShipAtCoordinatesForPlayerSet(coordinates, g.getPlayerSetForPlayer(pl)))
                .thenReturn(Optional.of(s));

        Optional<GameShip> expected = Optional.of(s);
        Optional<GameShip> actual = gameService.resolveFire(g, pl, coordinates);

        assertEquals(expected, actual);
    }

    @Test
    void testHandleHit() {

    }
}