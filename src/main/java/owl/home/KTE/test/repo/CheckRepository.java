package owl.home.KTE.test.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.home.KTE.test.model.check.Check;

import java.util.List;


@Repository
public interface CheckRepository extends JpaRepository<Check, Long> {
    List<Check> findCheckByClientId(long clientId);
}
