package owl.home.KTE.test.model.check;


import lombok.*;
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
    @Column(name = "number")
    @SequenceGenerator(sequenceName = "KTEtest", allocationSize = 1, name = "KTEtestSeq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KTEtestSeq")
    private long number;
    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "check_number")
    private Set<ProductForCheck> shoppingList;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;
    @Column(name = "final_price")
    private double finalPrice;
}
