package server.monitor.backend.services;

import java.util.List;

public record ClaimResponse(Long id, List<ClaimService.ItemToSend> items, List<ClaimService.PrivilegeToSend> privilege) {}
