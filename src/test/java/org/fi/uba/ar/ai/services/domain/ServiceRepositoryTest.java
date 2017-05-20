package org.fi.uba.ar.ai.services.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.LinkedHashSet;
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

    Service service = new Service("House Cleaning!", "I'll clean your House like a boos :)",
        categoryCleaning, new LinkedHashSet<>(Arrays.asList(subCategoryHouse)));

    serviceRepository.save(service);

    Service result = testEntityManager.find(Service.class, service.getId());
    assertThat(result).isEqualTo(service);
    assertThat(result.getSubCategories()).hasSize(1);
    assertThat(result.getSubCategories()).contains(subCategoryHouse);
  }
}
