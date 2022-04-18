package be.vdab.nieuwefietsen.repositories;

import be.vdab.nieuwefietsen.domain.Docent;
import be.vdab.nieuwefietsen.projections.AantalDocentenPerWedde;
import be.vdab.nieuwefietsen.projections.IdEnEmailAdres;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

@Repository
public class DocentRepository {
    private final EntityManager manager;

    public DocentRepository(EntityManager manager) {
        this.manager = manager;
    }
    public Optional<Docent> findById(long id) {
        return Optional.ofNullable(manager.find(Docent.class, id));
    }
    public void create(Docent docent) {
        manager.persist(docent);
    }
    public void delete(long id) {
        findById(id)
                .ifPresent(docent -> manager.remove(docent));
    }

    public List<Docent> findAll() {
        return manager.createQuery("""
            select d
            from Docent d
            order by d.wedde
            """, Docent.class).getResultList();
    }

    public List<Docent> findByWeddeBetween(BigDecimal van, BigDecimal tot) {
        return manager.createNamedQuery("Docent.findByWeddeBetween", Docent.class)
                .setParameter("van", van)
                .setParameter("tot", tot)
                .setHint("javax.persistence.loadgraph",
                        manager.createEntityGraph(Docent.MET_CAMPUS))
                .getResultList();
    }

    public List<String> findEmailAdressen() {
        return manager.createQuery("""
            select d.emailAdres
            from Docent d
            """, String.class)
                .getResultList();
    }

    public List<IdEnEmailAdres> findIdsEnEmailAdressen() {
        return manager.createQuery("""
            select new be.vdab.nieuwefietsen.projections.IdEnEmailAdres(d.id, d.emailAdres)
            from Docent d
            """, IdEnEmailAdres.class).getResultList();
    }

    public BigDecimal findGrootsteWedde() {
        return manager.createQuery("""
            select max(d.wedde)
            from Docent d
            """, BigDecimal.class)
                .getSingleResult();
    }

    public List<AantalDocentenPerWedde> findAantalDocentenPerWedde() {
        return manager.createQuery("""
            select new be.vdab.nieuwefietsen.projections.AantalDocentenPerWedde(d.wedde,count(d))
            from Docent d
            group by d.wedde
        """, AantalDocentenPerWedde.class)
                .getResultList();
    }
    public int algemeneOpslag(BigDecimal percentage) {
        return manager.createNamedQuery("Docent.algemeneOpslag")
                .setParameter("percentage", percentage)
                .executeUpdate();
    }

    public Optional<Docent> findByIdWithLock(long id) {
        return Optional.ofNullable(
                manager.find(Docent.class, id, LockModeType.PESSIMISTIC_WRITE));
    }


}
