package server.monitor.backend.db.privileges;

import jakarta.persistence.*;
import server.monitor.backend.db.products.Product;


@Entity
@Table(name = "privileges")
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    private String name;
    private String description;
    private PrivilageType type;

    public enum PrivilageType {
        PREMIUM,
        GOD,
        ADMIN
    }
}
