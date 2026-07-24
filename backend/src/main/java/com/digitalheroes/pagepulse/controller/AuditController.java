package com.digitalheroes.pagepulse.controller;

import com.digitalheroes.pagepulse.dto.AuditRequest;
import com.digitalheroes.pagepulse.dto.AuditResponse;
import com.digitalheroes.pagepulse.service.AuditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @PostMapping
    public ResponseEntity<AuditResponse> audit(@Valid @RequestBody AuditRequest request) {
        return ResponseEntity.ok(auditService.audit(request));
    }
}