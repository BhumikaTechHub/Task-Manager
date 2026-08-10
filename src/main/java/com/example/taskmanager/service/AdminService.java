package com.example.taskmanager.service;

import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {

        this.userRepository = userRepository;

    }
    private static final Logger logger =
            LoggerFactory.getLogger(AdminService.class);

    /**
     * Get all registered users
     */
    public List<User> getAllUsers() {

        return userRepository.findAll();

    }

    /**
     * Delete user
     */
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)

                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : " + id
                        ));

        userRepository.delete(user);

        logger.warn(
                "Admin deleted user {}",
                user.getEmail()
        );

    }

}