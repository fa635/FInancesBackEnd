package com.fa.finances.services.implementaions;


import com.fa.finances.dto.UserDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.models.User;
import com.fa.finances.repositories.UserRepository;
import com.fa.finances.requests.UserRequest;
import com.fa.finances.services.interfaces.IUserService;
import com.fa.finances.utils.UserMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    public Long create(UserRequest req) throws FinancesException {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new FinancesException("Email already in use");
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .passwordHash(req.getPassword())
                .role("USER")
                .build();

        userRepository.save(user);
        return user.getId();
    }

    @Override
    public void update(Long id, UserRequest req) throws FinancesException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new FinancesException("User not found"));

        user.setName(req.getName());
        user.setEmail(req.getEmail());
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) throws FinancesException {
        if (!userRepository.existsById(id)) {
            throw new FinancesException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
        		.map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getById(Long id) throws FinancesException {
    	return UserMapper.toDTO(userRepository.findById(id)
                .orElseThrow(() -> new FinancesException("User not found")));
    }

    @Override
    public UserDTO getByEmail(String email) throws FinancesException {
    	return UserMapper.toDTO(userRepository.findByEmail(email)
                .orElseThrow(() -> new FinancesException("User not found")));
    }
}

