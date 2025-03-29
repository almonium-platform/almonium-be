package com.almonium.learning.book.mapper;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.dto.response.BookDetails;
import com.almonium.learning.book.dto.response.BookDto;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.BookWithTranslationStatus;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BookMapper {
    @Mapping(target = "hasTranslation", ignore = true)
    @Mapping(target = "hasParallelTranslation", ignore = true)
    @Mapping(target = "isTranslation", ignore = true)
    @Mapping(target = "progressPercentage", ignore = true)
    BookDto toDto(Book book);

    BookDto toDto(BookWithTranslationStatus book);

    @Mapping(target = "availableLanguages", ignore = true)
    BookDetails toDetailsDto(BookWithTranslationStatus book);

    // New method that handles languages too
    default BookDetails toDetailsDto(BookWithTranslationStatus book, List<Language> languages) {
        BookDetails details = toDetailsDto(book);
        details.setAvailableLanguages(languages);
        return details;
    }

    List<BookDto> toDto(List<BookWithTranslationStatus> book);

    List<BookDto> toBookDtos(List<Book> entities);
}
