package org.fi.uba.ar.ai.users.usecase;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import java.util.List;
import org.fi.uba.ar.ai.users.domain.User;
import org.fi.uba.ar.ai.users.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class UserInteractor {

  private UserRepository userRepository;

  public UserInteractor(@Autowired UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User find(final String username) {
    return userRepository.findByUsername(username);
  }

  public List<User> findAll() {
    return (List<User>) userRepository.findAll();
  }

  public User find(long id) {
    return userRepository.findOne(id).get();
  }

  public void delete(User user) {
    userRepository.delete(user);
  }

  public User save(User user) {
    return userRepository.save(user);
  }
}
