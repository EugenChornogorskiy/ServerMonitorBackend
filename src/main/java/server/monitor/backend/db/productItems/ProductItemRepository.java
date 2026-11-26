package server.monitor.backend.db.productItems;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ProductItem> findByUserIdAndCode(long id, String code);
}
