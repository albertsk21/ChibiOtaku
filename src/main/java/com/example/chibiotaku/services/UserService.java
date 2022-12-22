package com.example.chibiotaku.services;

import com.example.chibiotaku.domain.dtos.UserDto;

public interface UserService {

     void registerUser(UserDto userDto);
     UserDto findUserByUsername(String username);

}
