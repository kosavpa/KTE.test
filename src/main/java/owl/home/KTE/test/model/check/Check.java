package owl.home.KTE.test.model.check;


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
    @Column(name = "number")
    private long number;
    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;
    @OneToMany(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "check_id")
    private Set<ProductForCheck> shoppingList;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;
    @Column(name = "final_price")
    private double finalPrice;
}
