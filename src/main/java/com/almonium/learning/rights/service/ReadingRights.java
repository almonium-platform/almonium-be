package com.almonium.learning.rights.service;

import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.rights.exception.UnavailableInCountryException;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * The reader-facing gate: may the current request's country read this work. Lists drop what the country may not
 * read, so a blocked book is simply absent from the shelf; opening one directly answers 451.
 */
@Component
@RequiredArgsConstructor
public class ReadingRights {
    private final WorkRightsService workRightsService;
    private final ReaderLocation readerLocation;

    public boolean mayRead(String workSlug) {
        return workRightsService.readable(workSlug, readerLocation.country());
    }

    public Book requireReadable(Book book) {
        if (!mayRead(book.getWorkSlug())) {
            throw new UnavailableInCountryException();
        }
        return book;
    }

    public <T> List<T> readable(List<T> items, Function<T, String> workSlug) {
        String country = readerLocation.country();
        return items.stream()
                .filter(item -> workRightsService.readable(workSlug.apply(item), country))
                .toList();
    }
}
