package com.almonium.user.core.dto.request;

/**
 * @param socialEmails whether connection request and acceptance emails and pushes are sent
 * @param bookEmails whether the reading mails and pushes are sent
 */
public record ProfileNotificationsRequest(boolean socialEmails, boolean bookEmails) {}
