package server.monitor.backend.db.items;

import jakarta.persistence.*;
import server.monitor.backend.db.products.Product;


@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String material;
    private String description;
    private String item_name;

    public Item(Long id, String material,String description, String item_name) {
        this.id = id;
        this.material = material;
        this.description = description;
        this.item_name = item_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterial() {
        return material;
    }
}
