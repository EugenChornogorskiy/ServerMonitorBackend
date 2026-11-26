package server.monitor.backend.db.products;

import jakarta.persistence.*;
import server.monitor.backend.db.items.Item;
import server.monitor.backend.db.privileges.Privilege;
import server.monitor.backend.db.productItems.ProductItem;
import server.monitor.backend.db.productPrivilege.ProductPrivilege;
import server.monitor.backend.db.users.User;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true)
    private List<ProductItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true)
    private List<ProductPrivilege> privileges = new ArrayList<>();

    private long price;

    public Product(Long id, User user, List<ProductItem> items, long price,List<ProductPrivilege> privileges) {
        this.id = id;
        this.user = user;
        this.price = price;
        this.items = items;
        this.privileges = privileges;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductItem> getItems() {
        return items;
    }
    public List<ProductPrivilege> getPrivileges() {
        return privileges;
    }
}
