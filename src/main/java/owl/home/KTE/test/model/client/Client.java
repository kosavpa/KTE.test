package owl.home.KTE.test.model.client;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import owl.home.KTE.test.model.check.Check;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name = "client_table")
@Getter @Setter @Builder @EqualsAndHashCode(exclude = "id")
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private long id;
    @Column(name = "client_name")
    private String name;
    @Column(name = "personal_discount1")
    private int personalDiscount1;
    @Column(name = "personal_discount2")
    private int personalDiscount2;
    @OneToMany(
            mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Check> checks;
}
