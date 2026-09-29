package com.medical.platform.service;

import com.medical.platform.dto.AuthResponse;
import com.medical.platform.dto.LoginRequest;
import com.medical.platform.dto.RegisterRequest;
import com.medical.platform.entity.Doctor;
import com.medical.platform.entity.Patient;
import com.medical.platform.entity.Role;
import com.medical.platform.entity.User;
import com.medical.platform.repository.DoctorRepository;
import com.medical.platform.repository.PatientRepository;
import com.medical.platform.repository.UserRepository;
import com.medical.platform.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuditService auditService;

    public AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un compte existe déjà avec cet email");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setEnabled(true);

        userRepository.save(user);

        if (request.getRole() == Role.PATIENT) {
            Patient patient = new Patient();
            patient.setUser(user);
            patientRepository.save(patient);
        } else if (request.getRole() == Role.DOCTOR) {
            Doctor doctor = new Doctor();
            doctor.setUser(user);
            doctorRepository.save(doctor);
        }

        auditService.log(user, "REGISTER", "User", "Inscription réussie (rôle : " + request.getRole() + ")", httpRequest);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        auditService.log(user, "LOGIN", "User", "Connexion réussie", httpRequest);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}