package owl.home.KTE.test.model.check;
/**
 * Класс Чека содержит дату создания, номер,
 * список товаров, клиента (покупателя), и итогувую стоимость всего списка товаров с учетом скидок
 */

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.product.ProductForCheck;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "check_table")
@Getter @Setter  @Builder @AllArgsConstructor @NoArgsConstructor
public class Check implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /**
     * Номер чека
     */
    @Column(name = "number")
    private long number;
    /**
     * Дата создания
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;
    /**
     * Список покупок
     */
    @OneToMany(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "check_id")
    private Set<ProductForCheck> shoppingList;
    /**
     * Клиент
     */
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;
    /**
     * Итоговая стоимость
     */
    @Column(name = "final_price")
    private double finalPrice;
}
