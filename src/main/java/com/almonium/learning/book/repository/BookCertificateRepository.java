package com.almonium.learning.book.repository;

import com.almonium.learning.book.model.entity.BookCertificate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookCertificateRepository extends JpaRepository<BookCertificate, UUID> {

    Optional<BookCertificate> findByUserIdAndBookId(UUID userId, UUID bookId);

    /** The public page's record: only one the reader has turned on, found by the address it lives at. */
    @Query("select c from BookCertificate c where c.user.username = :username"
            + " and c.book.editionSlug = :editionSlug and c.publicPage = true")
    Optional<BookCertificate> findPublic(String username, String editionSlug);
}
