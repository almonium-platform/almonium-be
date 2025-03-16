package com.almonium.learning.book.mapper;

import com.almonium.learning.book.dto.response.BookDto;
import com.almonium.learning.book.dto.response.LearnerBookProgressDto;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.BookWithProgress;
import com.almonium.learning.book.model.entity.LearnerBookProgress;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BookMapper {
    BookDto toDto(Book book);

    List<BookDto> toBookDtos(List<Book> entities);

    @Mapping(target = "bookId", source = "book.id")
    LearnerBookProgressDto toDto(LearnerBookProgress entity);

    List<LearnerBookProgressDto> toDtos(List<LearnerBookProgress> entities);

    List<BookDto> toBookDtosWithProgress(List<BookWithProgress> projections);

    default BookDto toBookDto(BookWithProgress projection) {
        Book book = projection.getBook();
        LearnerBookProgress progress = projection.getProgress();

        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear(),
                book.getCoverImageUrl(),
                book.getWordCount(),
                book.getRating(),
                book.getLanguage(),
                book.getLevelFrom(),
                book.getLevelTo(),
                progress != null ? progress.getProgressPercentage() : null);
    }
}
