package com.almonium.learning.rights.controller;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.learning.rights.dto.request.CatalogLinkRequest;
import com.almonium.learning.rights.dto.request.RightsOverrideRequest;
import com.almonium.learning.rights.dto.response.RightsOverrideRow;
import com.almonium.learning.rights.dto.response.RightsReportRow;
import com.almonium.learning.rights.dto.response.WorkRightsRow;
import com.almonium.learning.rights.dto.response.WorkRightsView;
import com.almonium.learning.rights.service.RightsOperationsService;
import com.almonium.user.core.model.entity.User;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * /ops → Rights. A takedown is one call: POST /overrides with the work, {@code *} or a country, BLOCK, and a reason;
 * readers stop seeing the book within a minute.
 */
@RestController
@RequestMapping("/ops/rights")
@RequiredArgsConstructor
public class OperationsRightsController {
    private final RightsOperationsService rightsOperationsService;

    @GetMapping("/reports")
    public ResponseEntity<List<RightsReportRow>> reports() {
        return ResponseEntity.ok(rightsOperationsService.reports());
    }

    @PostMapping("/reports/{id}/dismiss")
    @RequireRecentLogin
    public ResponseEntity<RightsReportRow> dismiss(@Auth User operator, @PathVariable UUID id) {
        return ResponseEntity.ok(rightsOperationsService.dismiss(operator, id));
    }

    @PostMapping("/overrides")
    @RequireRecentLogin
    public ResponseEntity<RightsOverrideRow> override(
            @Auth User operator, @Valid @RequestBody RightsOverrideRequest request) {
        return ResponseEntity.ok(rightsOperationsService.override(operator, request));
    }

    @DeleteMapping("/overrides/{id}")
    @RequireRecentLogin
    public ResponseEntity<Void> removeOverride(@PathVariable UUID id) {
        rightsOperationsService.removeOverride(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/works")
    public ResponseEntity<List<WorkRightsRow>> works() {
        return ResponseEntity.ok(rightsOperationsService.works());
    }

    @GetMapping("/works/{workSlug}")
    public ResponseEntity<WorkRightsView> work(@PathVariable String workSlug) {
        return ResponseEntity.ok(rightsOperationsService.work(workSlug));
    }

    @PutMapping("/works/{workSlug}/catalog")
    @RequireRecentLogin
    public ResponseEntity<WorkRightsView> link(
            @PathVariable String workSlug, @Valid @RequestBody CatalogLinkRequest request) {
        return ResponseEntity.ok(rightsOperationsService.link(workSlug, request.catalogWorkId()));
    }

    @PostMapping("/recompute")
    @RequireRecentLogin
    public ResponseEntity<Map<String, Integer>> recompute() {
        return ResponseEntity.ok(Map.of("works", rightsOperationsService.recomputeAll()));
    }
}
