package server.monitor.backend.db.productItems;

import jakarta.persistence.*;
import server.monitor.backend.db.items.Item;
import server.monitor.backend.db.products.Product;

import java.io.Serializable;

public class ProductItem {
    @EmbeddedId
    private Key id;
    @ManyToOne(fetch = FetchType.LAZY) @MapsId("productId") private Product product;
    @ManyToOne(fetch = FetchType.LAZY) @MapsId("itemId")    private Item item;
    private int amount;

    @Embeddable
    public record Key(Long productId, Long itemId) implements Serializable {}

    public ProductItem(Key id, Product product, Item item, int amount) {
        this.id = id;
        this.product = product;
        this.item = item;
        this.amount = amount;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Key getId() {
        return id;
    }

    public void setId(Key id) {
        this.id = id;
    }
}
