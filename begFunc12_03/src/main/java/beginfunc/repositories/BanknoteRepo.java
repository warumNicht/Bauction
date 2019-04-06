package beginfunc.repositories;

import beginfunc.domain.entities.productRelated.products.BankNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BanknoteRepo extends JpaRepository<BankNote, String> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM BankNote b " +
            "WHERE b.id LIKE :id")
    void deleteById(@Param(value = "id") String id);
}
