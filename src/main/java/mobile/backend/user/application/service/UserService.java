package mobile.backend.user.application.service;

import lombok.RequiredArgsConstructor;
import mobile.backend.user.application.port.out.UserRepository;
import mobile.backend.user.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

}
