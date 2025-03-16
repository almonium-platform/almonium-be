package com.almonium.learning.book.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.learning.book.dto.response.BookDto;
import com.almonium.learning.book.mapper.BookMapper;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.book.repository.LearnerBookProgressRepository;
import com.almonium.user.core.model.entity.User;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional
public class BookService {
    LearnerFinder learnerFinder;

    BookRepository bookRepository;
    LearnerBookProgressRepository learnerBookProgressRepository;

    BookMapper bookMapper;

    public List<BookDto> getBooks() {
        return bookMapper.toBookDtos(bookRepository.findAll());
    }

    public List<BookDto> getBooksInLanguage(Language language) {
        return bookMapper.toBookDtos(bookRepository.findByLanguage(language));
    }

    public List<BookDto> getBooksWithProgressInLanguage(User user, Language language) {
        UUID learnerId = learnerFinder.findLearner(user, language).getId();
        return bookMapper.toBookDtosWithProgress(
                bookRepository.findBooksWithProgressByLanguageAndLearnerId(language, learnerId));
    }

    public void deleteBookProgress(User user, Language language, UUID bookId) {
        UUID learnerId = learnerFinder.findLearner(user, language).getId();
        learnerBookProgressRepository.deleteByLearnerIdAndBookId(learnerId, bookId);
    }
}
