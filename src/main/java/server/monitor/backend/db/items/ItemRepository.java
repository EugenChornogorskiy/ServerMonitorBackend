package server.monitor.backend.db.items;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByMaterial(String material);
}