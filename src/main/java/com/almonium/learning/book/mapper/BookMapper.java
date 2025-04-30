package com.almonium.learning.book.mapper;

import com.almonium.learning.book.dto.response.BookDetails;
import com.almonium.learning.book.dto.response.BookDto;
import com.almonium.learning.book.dto.response.BookLanguageVariant;
import com.almonium.learning.book.dto.response.TranslationOrderDto;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.BookDetailsProjection;
import com.almonium.learning.book.model.entity.BookFavorite;
import com.almonium.learning.book.model.entity.BookMiniProjection;
import com.almonium.learning.book.model.entity.TranslationOrder;
import java.util.List;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BookMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "userId", source = "user.id")
    TranslationOrderDto toDto(TranslationOrder order);

    @Mapping(target = "hasTranslation", ignore = true)
    @Mapping(target = "hasParallelTranslation", ignore = true)
    @Mapping(target = "isTranslation", ignore = true)
    @Mapping(target = "progressPercentage", ignore = true)
    BookDto toDto(Book book);

    BookDto toDto(BookDetailsProjection book);

    BookLanguageVariant toDto(BookMiniProjection projection);

    List<BookLanguageVariant> toMiniDto(List<BookMiniProjection> projection);

    @Mapping(target = "languageVariants", ignore = true)
    @Mapping(target = "orderLanguage", ignore = true)
    @Mapping(target = "favorite", ignore = true)
    BookDetails toDetailsDto(BookDetailsProjection book);

    // New method that handles languages too
    default BookDetails toDetailsDto(
            BookDetailsProjection book,
            List<BookLanguageVariant> languageVariants,
            Optional<TranslationOrder> orderLanguage,
            Optional<BookFavorite> favorite) {
        BookDetails details = toDetailsDto(book);
        details.setLanguageVariants(languageVariants);
        orderLanguage.ifPresent(order -> details.setOrderLanguage(order.getLanguage()));
        favorite.ifPresent(fav -> details.setFavorite(true));
        return details;
    }

    List<BookDto> toDto(List<BookDetailsProjection> book);

    List<BookDto> toBookDtos(List<Book> entities);
}
