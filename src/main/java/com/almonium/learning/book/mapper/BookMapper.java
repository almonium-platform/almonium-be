package com.almonium.learning.book.mapper;

import com.almonium.learning.book.dto.response.BookDto;
import com.almonium.learning.book.model.entity.Book;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface BookMapper {
    //    hasTranslation, hasParallelTranslation, isTranslation".
    BookDto toDto(Book book);

    List<BookDto> toBookDtos(List<Book> entities);
}
