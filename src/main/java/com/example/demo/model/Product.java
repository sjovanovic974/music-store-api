package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(name = "UK_sku", columnNames = "sku")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"artist", "category"})
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "units_in_stock", nullable = false)
    private int unitsInStock;

    @Column(name = "date_created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(name = "last_updated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    private Artist artist;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    //fetch = FetchType.EAGER is default for @ManyToOne relationship
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private ProductCategory category;
}
