package server.monitor.backend.db.claims;

import java.util.UUID;

public record ClaimRequest(UUID player_uuid, String code) {}
