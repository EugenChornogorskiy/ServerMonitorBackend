package server.monitor.backend.db.items;

import jakarta.persistence.*;
import server.monitor.backend.db.products.Product;


@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    private String material;
    private int amount;
    private String description;
    private String item_name;

    public Item(Long id, Product product, String material, int amount, String description, String item_name) {
        this.id = id;
        this.product = product;
        this.material = material;
        this.amount = amount;
        this.description = description;
        this.item_name = item_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }
}
