package com.almonium.learning.rights.dto.request;

import com.almonium.learning.rights.model.enums.ReporterRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * A report against a book. A guest must leave an email so /ops can answer; a member's account address is used
 * otherwise. {@code country} is the country the report is about (ISO alpha-2), blank for everywhere.
 */
public record RightsReportRequest(
        @NotNull ReporterRole role,
        @Size(max = 200) String name,
        @Email @Size(max = 320) String email,
        @Pattern(regexp = "^$|^[A-Za-z]{2}$") String country,
        @NotBlank @Size(min = 10, max = 4000) String message) {}
