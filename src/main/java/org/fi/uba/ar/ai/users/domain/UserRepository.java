package org.fi.uba.ar.ai.users.domain;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findOne(final long id);

}
