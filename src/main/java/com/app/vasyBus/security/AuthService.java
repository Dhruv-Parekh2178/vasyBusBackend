package com.app.vasyBus.security;

import com.app.vasyBus.dtos.AuthResponseDTO;
import com.app.vasyBus.dtos.LoginRequestDTO;
import com.app.vasyBus.dtos.RegisterRequestDTO;
import com.app.vasyBus.exception.InvalidCredentialsException;
import com.app.vasyBus.exception.ResourceNotFoundException;
import com.app.vasyBus.exception.UserAlreadyExistsException;
import com.app.vasyBus.model.User;
import com.app.vasyBus.model.enums.Role;
import com.app.vasyBus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

     public AuthResponseDTO login(LoginRequestDTO request){
         try {
             authenticationManager.authenticate(
                     new UsernamePasswordAuthenticationToken(
                             request.getEmail(),
                             request.getPassword()
                     )
             );
         } catch (Exception ex) {
             throw new InvalidCredentialsException("Invalid email or password");
         }
         User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new ResourceNotFoundException("User not Found"));

         String token = jwtUtil.generateToken(user.getUserId(), user.getEmail());
       return new AuthResponseDTO(token , user.getName() ,user.getEmail() , user.getRole().toString());
     }

     public AuthResponseDTO register(RegisterRequestDTO request){
         if(userRepository.findByEmail(request.getEmail()).isPresent()){
             throw new UserAlreadyExistsException("User already exist");
         }

         User user = User.builder()
                 .name(request.getName())
                 .email(request.getEmail())
                 .phone(request.getPhone())
                 .age(request.getAge())
                 .password(passwordEncoder.encode(request.getPassword()))
                 .role(Role.ROLE_USER)
                 .build();

         userRepository.save(user);
         String token = jwtUtil.generateToken(user.getUserId(), user.getEmail());
         return new AuthResponseDTO(token , user.getName() ,user.getEmail() , user.getRole().toString());
     }
}
