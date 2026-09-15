package com.almonium.user.core.dto.response;

/**
 * @param socialEmails whether connection request and acceptance emails and pushes are sent
 * @param bookEmails whether the reading mails and pushes are sent: a translation asked for, or a book suggested, is ready
 */
public record NotificationPreferencesDto(boolean socialEmails, boolean bookEmails) {}
