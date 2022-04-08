package be.vdab.nieuwefietsen.repositories;

import be.vdab.nieuwefietsen.domain.Docent;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class DocentRepository {
    private final EntityManager manager;

    public DocentRepository(EntityManager manager) {
        this.manager = manager;
    }
    public Optional<Docent> findById(long id) {
        return Optional.ofNullable(manager.find(Docent.class, id));
    }
}
