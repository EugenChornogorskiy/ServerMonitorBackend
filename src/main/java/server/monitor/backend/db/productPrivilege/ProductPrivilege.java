package server.monitor.backend.db.productPrivilege;

import jakarta.persistence.*;
import server.monitor.backend.db.privileges.Privilege;
import server.monitor.backend.db.productItems.ProductItem;
import server.monitor.backend.db.products.Product;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

public class ProductPrivilege {
    @EmbeddedId
    private ProductItem.Key id;
    @ManyToOne(fetch = FetchType.LAZY) @MapsId("productId")
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY) @MapsId("privilegeId")
    private Privilege privilege;
    private Duration durationDays;

    @Embeddable
    public record Key(Long productId, Long itemId) implements Serializable {}

    public ProductPrivilege(Duration durationDays, Privilege privilege, Product product, ProductItem.Key id) {
        this.durationDays = durationDays;
        this.privilege = privilege;
        this.product = product;
        this.id = id;
    }

    public ProductItem.Key getId() {
        return id;
    }

    public void setId(ProductItem.Key id) {
        this.id = id;
    }

    public Duration getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Duration durationDays) {
        this.durationDays = durationDays;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
