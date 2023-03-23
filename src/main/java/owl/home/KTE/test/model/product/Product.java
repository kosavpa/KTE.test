package owl.home.KTE.test.model.product;
/**
 * Класс товара(продукта).
 * Содержит имя товара, описание товара, скидка(если она действует) и стоимость
 */

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "product_table")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long id;
    @Column(name = "product_name")
    private String name;
    /**
     * Цена товара
     */
    @Column(name = "product_price")
    private double price;
    /**
     * Скидка на товар
     */
    @Column(name = "product_discount")
    private int discount;
    /**
     * Описание товара
     */
    @Column(name = "about_product")
    private String about;
}