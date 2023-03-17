package owl.home.KTE.test.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owl.home.KTE.test.model.client.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
