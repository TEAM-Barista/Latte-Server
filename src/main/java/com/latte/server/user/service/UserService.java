package com.latte.server.user.service;

import com.latte.server.category.domain.Category;
import com.latte.server.category.repository.CategoryRepository;
import com.latte.server.common.exception.custom.NotFoundCategoryException;
import com.latte.server.common.exception.custom.NotFoundEmailException;
import com.latte.server.common.exception.custom.NotFoundUserCategoryException;
import com.latte.server.user.domain.User;
import com.latte.server.user.domain.UserCategory;
import com.latte.server.user.dto.*;
import com.latte.server.user.repository.UserCategoryRepository;
import com.latte.server.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Minky on 2021-07-27
 */

@Service
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class UserService {
    private final UserRepository userRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final CategoryRepository categoryRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Long create(UserRequestDto userRequestDto) {
        User newUser = userRepository.save(userRequestDto.toEntity());
        return newUser.getId();
    }

    public void setProfileImage(UserProfileImageUrlRequestDto userProfileImageUrlRequestDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundEmailException::new);

        user.setProfileImageUrl(userProfileImageUrlRequestDto.getProfileImageUrl());

        userRepository.save(user);
    }

    public List<UserCategoryResponseDto> getUserCategories(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundEmailException::new);

        List<UserCategory> userCategoryList = userCategoryRepository.findByUser(user);

        return UserCategoryResponseDto.listOf(userCategoryList);
    }

    public void setUserCategories(UserCategoriesRequestDto userCategoriesRequestDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundEmailException::new);

        List<Category> categoryList = categoryRepository.findByIdIn(userCategoriesRequestDto.getUserCategories());

        userCategoryRepository.saveAll(toEntities(user, categoryList));
    }

    public void setUserCategory(Long categoryId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundEmailException::new);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(NotFoundCategoryException::new);

        userCategoryRepository.save(toEntity(user, category));
    }

    public void deleteUserCategory(Long categoryId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundEmailException::new);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(NotFoundCategoryException::new);

        UserCategory userCategory = userCategoryRepository.findByUserAndCategory(user, category)
                .orElseThrow(NotFoundUserCategoryException::new);

        userCategoryRepository.deleteById(userCategory.getId());
    }

    public void setAccessNotify(UserAccessNotifyRequestDto userAccessNotifyRequestDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NotFoundEmailException::new);

        user.setAccessNotify(userAccessNotifyRequestDto.getAccessNotify());

        userRepository.save(user);
    }

    private UserCategory toEntity(User user, Category category) {
        return new UserCategory(null, user, category);
    }

    private List<UserCategory> toEntities(User user, List<Category> categoryList) {
        ArrayList<UserCategory> userCategoryList = new ArrayList();
        for (Category category : categoryList) {
            userCategoryList.add(toEntity(user, category));
        }
        return userCategoryList;
    }
}
