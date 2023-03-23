package owl.home.KTE.test.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import owl.home.KTE.test.model.check.Check;

import java.util.Date;
import java.util.List;


@Repository
public interface CheckRepository extends JpaRepository<Check, Long> {
    List<Check> findCheckByClientId(long clientId);
    @Modifying
    @Query(value = "INSERT INTO check_table " +
            "(\"date\", final_price, number, client_id) " +
            "VALUES ('2023-03-23', 175, nextval('ktetest_seq'), 1);",
            nativeQuery = true)
    int saveWithSequence(String date, double price, long clientId);
}
