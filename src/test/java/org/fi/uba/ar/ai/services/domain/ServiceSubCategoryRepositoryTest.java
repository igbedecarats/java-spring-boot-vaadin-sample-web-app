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
public class ServiceSubCategoryRepositoryTest {

  @Autowired
  private ServiceSubCategoryRepository serviceSubCategoryRepository;

  @Autowired
  private TestEntityManager testEntityManager;


  @Test
  public void shouldSaveSubCategoriesWhenAddingToCategory() {
    ServiceCategory categoryCleaning = ServiceCategoryMother.createCleanning();
    testEntityManager.persist(categoryCleaning);
    ServiceSubCategory subCategoryHouse = new ServiceSubCategory("House", categoryCleaning);
    ServiceSubCategory subCategoryOffice = new ServiceSubCategory("Office", categoryCleaning);
    categoryCleaning.addSubCategories(
        new LinkedHashSet<>(Arrays.asList(subCategoryHouse, subCategoryOffice)));
    testEntityManager.persist(categoryCleaning);
    ServiceCategory category = testEntityManager
        .find(ServiceCategory.class, categoryCleaning.getId());
    assertThat(category.getSubCategories()).hasSize(2);
    ServiceSubCategory subCategory = serviceSubCategoryRepository.findOne(subCategoryHouse.getId());
    assertThat(subCategory.getCategory()).isEqualTo(categoryCleaning);
  }

}
