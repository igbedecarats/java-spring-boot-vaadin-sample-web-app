package org.fi.uba.ar.ai.services.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.maps.model.OpeningHours.Period.OpenClose.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import org.fi.uba.ar.ai.locations.domain.Coordinate;
import org.fi.uba.ar.ai.locations.domain.Location;
import org.fi.uba.ar.ai.locations.domain.LocationArea;
import org.fi.uba.ar.ai.users.domain.User;
import org.fi.uba.ar.ai.users.domain.UserMother;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ServiceRepositoryTest {

  @Autowired
  private ServiceRepository serviceRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @Test
  public void shouldSaveService() {

    ServiceCategory categoryCleaning = ServiceCategoryMother.createCleanning();
    testEntityManager.persist(categoryCleaning);
    ServiceSubCategory subCategoryHouse = new ServiceSubCategory("House", categoryCleaning);
    ServiceSubCategory subCategoryOffice = new ServiceSubCategory("Office", categoryCleaning);
    categoryCleaning.addSubCategories(
        new LinkedHashSet<>(Arrays.asList(subCategoryHouse, subCategoryOffice)));
    testEntityManager.persist(categoryCleaning);
    User john = UserMother.createJohnDoe();
    testEntityManager.persist(john);
    Location gravityFalls = new Location("Gravity Falls", LocationArea.GBA_SUR, new Coordinate());
    testEntityManager.persist(gravityFalls);

    Service service = new Service("House Cleaning!", "I'll clean your House like a boos :)",
        john, gravityFalls, categoryCleaning, subCategoryHouse,
        "10:00", "18:00", DayOfWeek.MONDAY.ordinal(), DayOfWeek.FRIDAY.ordinal());

    serviceRepository.save(service);

    Service result = testEntityManager.find(Service.class, service.getId());
    assertThat(result).isEqualTo(service);
    assertThat(result.getSubCategory()).isEqualTo(subCategoryHouse);
    assertThat(result.getProvider()).isEqualTo(john);
    assertThat(result.getLocalStartTime()).isEqualTo(LocalTime.parse("10:00"));
    assertThat(result.getLocalEndTime()).isEqualTo(LocalTime.parse("18:00"));
    assertThat(result.getLocalizedStartDay()).isEqualTo("Lunes");
    assertThat(result.getLocalizedEndDay()).isEqualTo("Viernes");
  }
}
