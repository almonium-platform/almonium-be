package com.almonium.learning.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.user.core.exception.BadUserRequestActionException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BookEditionSelectionTest {
    @Test
    void selectsExactEditionAndRejectsAnotherWorkOrSelf() {
        BookRepository repository = mock(BookRepository.class);
        PublishedBookContentService content = mock(PublishedBookContentService.class);
        BookService service = new BookService(null, content, repository, null, null, null, null);
        Book primary = book("b2", "frankenstein");
        Book original = book("original", "frankenstein");
        when(repository.findByEditionSlug("b2")).thenReturn(Optional.of(primary));
        when(repository.findByEditionSlug("original")).thenReturn(Optional.of(original));
        when(content.parallelTextFor(primary, original)).thenReturn(new byte[] {1});
        assertThat(service.getPublicParallelEdition("b2", "original")).containsExactly((byte) 1);
        assertThatThrownBy(() -> service.getPublicParallelEdition("b2", "b2"))
                .isInstanceOf(BadUserRequestActionException.class);
        original.setWorkSlug("different");
        assertThatThrownBy(() -> service.getPublicParallelEdition("b2", "original"))
                .isInstanceOf(BadUserRequestActionException.class);
        verify(content, times(1)).parallelTextFor(primary, original);
    }

    private Book book(String slug, String work) {
        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setEditionSlug(slug);
        book.setWorkSlug(work);
        return book;
    }
}
