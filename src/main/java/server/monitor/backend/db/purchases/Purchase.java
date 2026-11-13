package server.monitor.backend.db.purchases;

import jakarta.persistence.*;
import server.monitor.backend.db.products.Product;
import server.monitor.backend.db.users.User;

import java.time.Instant;
import java.time.LocalDateTime;

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

    public PurchaseStatus getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Instant getClaimed_at() {
        return claimed_at;
    }

    public void setClaimed_at(Instant claimed_at) {
        this.claimed_at = claimed_at;
    }

    public Instant getOpened_at() {
        return opened_at;
    }

    public void setOpened_at(Instant opened_at) {
        this.opened_at = opened_at;
    }

    public Instant getClosed_at() {
        return closed_at;
    }

    public void setClosed_at(Instant closed_at) {
        this.closed_at = closed_at;
    }

    public void setStatus(PurchaseStatus status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public enum PurchaseStatus {
        OPEN,
        CLOSED,
        CLAIMED
    }
}
