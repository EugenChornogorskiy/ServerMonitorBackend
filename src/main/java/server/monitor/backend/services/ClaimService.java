package server.monitor.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import server.monitor.backend.db.claims.ClaimRequest;
import server.monitor.backend.db.claims.ClaimResponse;
import server.monitor.backend.db.purchases.Purchase;
import server.monitor.backend.db.purchases.PurchaseRepository;
import server.monitor.backend.db.users.User;
import server.monitor.backend.db.users.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ClaimService {
    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;
    private final long claimTimeoutMinutes;

    public ClaimService(UserRepository userRepository, PurchaseRepository purchaseRepository, @Value("${claim.timeout-minutes:5}") long claimTimeoutMinutes) {
        this.userRepository = userRepository;
        this.purchaseRepository = purchaseRepository;
        this.claimTimeoutMinutes = claimTimeoutMinutes;
    }
    public ClaimResponse claim(ClaimRequest claimRequest) {
        User user = userRepository.findByPlayer_id(claimRequest.player_uuid()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Player is not linked"));
        Purchase purchase = purchaseRepository.findByUserIdAndCode(user.getId(),claimRequest.code()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Purchase not found"));
        if (purchase.getStatus() == Purchase.PurchaseStatus.CLOSED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Purchase already claimed or delivered");
        }
        if (purchase.getStatus() == Purchase.PurchaseStatus.CLAIMED) {
            LocalDateTime deadline = purchase.getClaimed_at().plusMinutes(claimTimeoutMinutes);
            if (LocalDateTime.now().isBefore(deadline)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Delivery in progress, try again later");
            }
        }
        List<ItemToSend> items = purchase.getProduct().getItems().stream().map(i -> new ItemToSend(i.getMaterial(),i.getAmount())).toList();
        return new ClaimResponse(purchase.getId(),items);
    }
    public void close(Long purchase_id) {
        Purchase purchase = purchaseRepository.findById(purchase_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Purchase not found"));
        purchase.setStatus(Purchase.PurchaseStatus.CLOSED);
    }
    public record ItemToSend(String material, int amount) {}
}
