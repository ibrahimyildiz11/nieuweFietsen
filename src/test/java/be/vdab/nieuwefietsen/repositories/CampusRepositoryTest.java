package be.vdab.nieuwefietsen.repositories;

import be.vdab.nieuwefietsen.domain.Adres;
import be.vdab.nieuwefietsen.domain.Campus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Import(CampusRepository.class)
@Sql("/insertCampus.sql")
class CampusRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String CAMPUSSEN ="campussen";
    private final CampusRepository repository;

    public CampusRepositoryTest(CampusRepository repository) {
        this.repository = repository;
    }

    private long idVanTestCampus() {
        return jdbcTemplate.queryForObject(
                "select id from campussen where naam = 'test'", Long.class);
    }

    @Test
    void findById() {
        assertThat(repository.findById(idVanTestCampus()))
                .hasValueSatisfying(campus ->  {
                    assertThat(campus.getNaam()).isEqualTo("test");
                    assertThat(campus.getAdres().getGemeente()).isEqualTo("test");
                });
    }

    @Test
    void findByOnbestaandeId() {
        assertThat(repository.findById(-1)).isEmpty();
    }

    @Test
    void create() {
        var naam = "test";
        var adres = new Adres("test", "test", "test", "test");
        var campus = new Campus(naam, adres);
        repository.create(campus);
        assertThat(countRowsInTableWhere(CAMPUSSEN,
                "id = " + campus.getId())).isOne();
    }
}