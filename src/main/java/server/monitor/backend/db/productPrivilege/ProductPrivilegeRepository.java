package server.monitor.backend.db.productPrivilege;

import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import server.monitor.backend.db.items.Item;
import server.monitor.backend.db.privileges.Privilege;
import server.monitor.backend.db.productItems.ProductItem;
import server.monitor.backend.db.products.Product;

import java.io.Serializable;

public interface ProductPrivilegeRepository extends JpaRepository<ProductPrivilege, Long> {
}
