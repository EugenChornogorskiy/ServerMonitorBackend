package server.monitor.backend.services;

import java.util.UUID;

public record ClaimRequest(UUID player_uuid, String code) {}
