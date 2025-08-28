package se.boetsch.Battleship.dto;

import java.util.List;

public record GameDTO (long id, Status status, List<PlayerDTO> players) {}
