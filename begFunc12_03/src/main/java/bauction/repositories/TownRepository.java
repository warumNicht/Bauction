package bauction.repositories;

import bauction.domain.entities.productRelated.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<Town,String> {

    Optional<Town> findByName(String name);
}
