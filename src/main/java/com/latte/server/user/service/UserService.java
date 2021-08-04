package com.latte.server.user.service;

import com.latte.server.user.domain.User;
import com.latte.server.user.dto.UserRequestDto;
import com.latte.server.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Minky on 2021-07-27
 */

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class UserService {
    private final UserRepository userRepository;

    public Long create(UserRequestDto userRequestDto) {
        User newUser = userRepository.save(userRequestDto.toEntity());
        return newUser.getId();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
