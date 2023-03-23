package owl.home.KTE.test.model.client;
/**
 * Класс клиента(покупателя).
 * Содержит имя клиента и персональные скидки
 */

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "client_table")
@Getter @Setter @Builder @EqualsAndHashCode(exclude = "id") @AllArgsConstructor @NoArgsConstructor
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private long id;
    @Column(name = "client_name")
    /**
     * Имя клиента
     */
    private String name;
    /**
     * Персональная скидка клиента №1
     */
    @Column(name = "personal_discount1")
    private int personalDiscount1;
    /**
     * Персональная скидка клента №2
     */
    @Column(name = "personal_discount2")
    private int personalDiscount2;
}
