package owl.home.KTE.test.model.product;
/**
 * Класс рейтинга
 * набор product id - client id в нем уникален так как один клиент может оставить одну оценку одному продукту
 */

import lombok.*;
import owl.home.KTE.test.model.client.Client;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(
        name = "rating_table",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"product_id","client_id"})})
@Getter @Setter @Builder @EqualsAndHashCode(exclude = "id") @AllArgsConstructor @NoArgsConstructor
public class Rating implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    @Column(name = "amount_star")
    private int amountStar;
}