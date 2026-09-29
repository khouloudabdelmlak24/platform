package com.medical.platform.service;

import com.medical.platform.entity.AuditLog;
import com.medical.platform.entity.User;
import com.medical.platform.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    /**
     * Enregistre un événement. "user" peut être null (ex : tentative de login ratée).
     * "request" peut être null si l'adresse IP n'est pas disponible à cet endroit.
     */
    public void log(User user, String action, String resource, String details, HttpServletRequest request) {
        AuditLog entry = new AuditLog();
        entry.setUser(user);
        entry.setAction(action);
        entry.setResource(resource);
        entry.setDetails(details);
        entry.setTimestamp(LocalDateTime.now());
        entry.setIpAddress(request != null ? request.getRemoteAddr() : null);

        auditLogRepository.save(entry);
    }
}