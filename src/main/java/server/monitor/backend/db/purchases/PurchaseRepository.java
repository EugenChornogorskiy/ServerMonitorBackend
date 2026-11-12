package server.monitor.backend.db.purchases;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Purchase> findByUserIdAndCode(long id,String code);
}