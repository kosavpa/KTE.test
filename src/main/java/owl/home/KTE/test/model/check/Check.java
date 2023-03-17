package owl.home.KTE.test.model.check;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import owl.home.KTE.test.model.client.Client;
import owl.home.KTE.test.model.product.ProductForCheck;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "check_table")
@Getter @Setter  @Builder
public class Check implements Serializable {
    @Id
    @Column(name = "number")
    @SequenceGenerator(sequenceName = "KTEtest", allocationSize = 1, name = "KTEtestSeq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KTEtestSeq")
    private long number;
    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;
    @OneToMany
    @JoinColumn(name = "check_number")
    private Set<ProductForCheck> shoppingList;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
