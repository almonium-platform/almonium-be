package com.almonium.user.core.dto.request;

/** @param socialEmails whether connection request and acceptance emails are sent */
public record ProfileNotificationsRequest(boolean socialEmails) {}
