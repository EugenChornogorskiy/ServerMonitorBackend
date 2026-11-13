package server.monitor.backend.db.claims;

import server.monitor.backend.services.ClaimService;

import java.util.List;

public record ClaimResponse(Long id, List<ClaimService.ItemToSend> items) {}
