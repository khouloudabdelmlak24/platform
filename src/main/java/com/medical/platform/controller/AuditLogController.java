package com.medical.platform.controller;

import com.medical.platform.entity.AuditLog;
import com.medical.platform.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAuditLogs(
            @RequestParam(defaultValue = "50") int limit
    ) {
        List<AuditLog> logs = auditLogRepository
                .findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "timestamp")))
                .getContent();
        return ResponseEntity.ok(logs);
    }
}