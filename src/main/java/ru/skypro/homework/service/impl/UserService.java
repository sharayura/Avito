package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;


@Service
public class UserService implements UserDetailsManager {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, ImageRepository imageRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;   //todo это всё можно сделать в одну строку через Optional - вернуть user, если не null, или бросить исключение иначе
    }

    @Transactional
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @Transactional
    public boolean setPassword(NewPasswordDto newPasswordDto) {
        User currentUser = userRepository.findByUsername(getCurrentUsername());
        if (passwordEncoder.matches(newPasswordDto.getCurrentPassword(), currentUser.getPassword())) {
            currentUser.setPassword(passwordEncoder.encode(newPasswordDto.getNewPassword()));
            return true;
        }
        return false;
    }
   @Transactional(readOnly = true)
    public UserDto getUser() {
        User currentUser = userRepository.findByUsername(getCurrentUsername());
        UserDto userDto = new UserDto();
        userMapper.toUserDto(userDto, currentUser);
        return userDto;
    }

    @Transactional
    public boolean updateUser(UserDto userDto) {
        User currentUser = userRepository.findByUsername(getCurrentUsername());
        if (currentUser == null) {
            return false;
        }
        userMapper.toUser(currentUser, userDto);
        return true;  //todo это всё можно сделать в одну строку через Optional - вернуть true, если не null, или false иначе
    }

    @Transactional(readOnly = true)
    public Image getUserImage(Integer userId) {
        return userRepository.findById(userId).map(User::getImage).orElse(null);
    }

    @Transactional
    public void updateUserImage(MultipartFile file) throws IOException {
        Image image = new Image();

        image.setFileSize(file.getSize());
        image.setMediaType(file.getContentType());
        image.setData(file.getBytes());
        imageRepository.save(image);

        User user = userRepository.findByUsername(getCurrentUsername());
        user.setImage(image);

    }



    @Override
    public void createUser(UserDetails user) {
    }

    @Override
    public void updateUser(UserDetails user) {
    }

    @Override
    public void deleteUser(String username) {
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
    }

    @Override
    public boolean userExists(String username) {
        return !(userRepository.findByUsername(username) == null);
    }
}
