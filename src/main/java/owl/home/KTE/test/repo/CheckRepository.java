package owl.home.KTE.test.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import owl.home.KTE.test.model.check.Check;

import java.util.Date;
import java.util.List;


@Repository
public interface CheckRepository extends JpaRepository<Check, Long> {
    List<Check> findCheckByClientId(long clientId);

    /**
     * Запрос для сохранения чека с использованием специального генератора чисал
     * @param date - дата
     * @param price - стоимость
     * @param clientId - id клиента
     * @return - id чека
     */
    @Modifying
    @Query(value = "INSERT INTO check_table " +
            "(\"date\", final_price, number, client_id) " +
            "VALUES (:date, :price, nextval('ktetest_seq'), :clientId)",
            nativeQuery = true)
    int saveWithSequence(@Param("date") String date,
                         @Param("price") double price,
                         @Param("clientId")long clientId);

    @Modifying
    @Query(value = "ALTER SEQUENCE ktetest_seq RESTART WITH 100", nativeQuery = true)
    void alterSequence();
}
