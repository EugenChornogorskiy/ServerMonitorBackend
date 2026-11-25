package server.monitor.backend.db.purchases;

import jakarta.persistence.*;
import server.monitor.backend.db.products.Product;
import server.monitor.backend.db.users.User;

import java.time.Instant;

@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private PurchaseStatus status;

    private Instant claimed_at;
    private Instant opened_at;
    private Instant closed_at;
    private String code;

    public Purchase(Long id, User user, Product product, server.monitor.backend.db.purchases.Purchase.PurchaseStatus status, Instant claimed_at, Instant opened_at, Instant closed_at, String code) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.status = status;
        this.claimed_at = claimed_at;
        this.opened_at = opened_at;
        this.closed_at = closed_at;
        this.code = code;
    }

    public PurchaseStatus getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public Instant getClaimed_at() {
        return claimed_at;
    }

    public void setClaimed_at(Instant claimed_at) {
        this.claimed_at = claimed_at;
    }

    public void setClosed_at(Instant closed_at) {
        this.closed_at = closed_at;
    }

    public void setStatus(PurchaseStatus status) {
        this.status = status;
    }

    public enum PurchaseStatus {
        OPEN,
        CLOSED,
        CLAIMED
    }
}
