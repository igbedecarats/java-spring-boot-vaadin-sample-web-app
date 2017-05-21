package org.fi.uba.ar.ai.locations.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LocationRepositoryTest {

  @Autowired
  private LocationRepository repository;

  @Autowired
  private TestEntityManager testEntityManager;

  @Test
  public void shouldSave() {
    Location gravityFalls = new Location("Gravity Falls", LocationArea.GBA_SUR, new Coordinate());
    repository.save(gravityFalls);
    Location savedLocation = testEntityManager.find(Location.class, gravityFalls.getId());
    assertThat(savedLocation).isEqualTo(gravityFalls);
  }

}
