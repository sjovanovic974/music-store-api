package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostProductDTO {

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @Size(min = 0, max = 350)
    private String description;

    @NotNull
    @Min(1)
    @Max(1000)
    private BigDecimal unitPrice;

    @NotNull
    @Size(min = 9, max = 50)
    private String imageUrl;

    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active;

    @NotNull
    @Min(0)
    @Max(100)
    private int unitsInStock;

    @NotNull
    @Size(min=1, max=50)
    private String artist;

    @NotNull
    private String category;
}
