package server.monitor.backend.db.privileges;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import server.monitor.backend.db.purchases.Purchase;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Purchase> findByUserIdAndCode(long id, String code);
}
