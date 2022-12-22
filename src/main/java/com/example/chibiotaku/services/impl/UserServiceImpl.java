package com.example.chibiotaku.services.impl;

import com.example.chibiotaku.constraints.ErrorMessage;
import com.example.chibiotaku.domain.dtos.UserDto;
import com.example.chibiotaku.domain.entities.RoleEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.enums.RoleUserEnum;
import com.example.chibiotaku.repo.RoleRepository;
import com.example.chibiotaku.repo.UserRepository;
import com.example.chibiotaku.services.UserService;
import com.example.chibiotaku.util.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserDto userDto) {

        UserEntity user = this.modelMapper.map(userDto,UserEntity.class);
        user.setCreated(LocalDate.now());
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        RoleEntity role = this.roleRepository.findRoleEntityByRole(RoleUserEnum.USER)
                .orElseThrow(() ->  new ObjectNotFoundException("Role with name" + RoleUserEnum.USER + "was not found"));



        user.setRoles(List.of(role));

        this.userRepository.save(user);
    }

    @Override
    public UserDto findUserByUsername(String username) {
        UserEntity  user = this.userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(ErrorMessage.USERNAME_NOT_FOUND,username)));
        return this.modelMapper.map(user,UserDto.class);
    }
}
