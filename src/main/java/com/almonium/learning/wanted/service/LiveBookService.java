package com.almonium.learning.wanted.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.catalog.dto.response.TextSourceView;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.catalog.repository.CatalogWorkRepository;
import com.almonium.learning.catalog.service.TextSources;
import com.almonium.learning.rights.model.RightsStatus;
import com.almonium.learning.rights.service.ReaderLocation;
import com.almonium.learning.rights.service.ReadingRights;
import com.almonium.learning.rights.service.UnlockDates;
import com.almonium.learning.wanted.dto.request.LiveBookRequest;
import com.almonium.learning.wanted.dto.response.LiveBook;
import com.almonium.learning.wanted.model.entity.Preparation;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Reading a book that is not on the shelf, now: the reader picks a catalogue work, this deployment's processor
 * fetches a free text of the original while they wait, and in well under a minute the unadapted text is on the
 * shelf for everyone, its level marked as not judged yet. Levels, word explanations and translations still come the
 * slow way.
 *
 * <p>Only a text that is free where we make books is fetched, only from a library whose file the processor can
 * read, and never for a reader whose country still protects the work. A second reader asking for the same book
 * joins the first one's wait. One book at a time for a reader, a handful a day, and a few at once for everybody,
 * since each takes a core of the one host for up to a minute.
 */
@Service
@RequiredArgsConstructor
public class LiveBookService {
    static final String WIKISOURCE = "WIKISOURCE";

    /** The libraries whose files this deployment's processor fetches. */
    static final Set<String> FETCHED = Set.of("GUTENBERG", "STANDARD_EBOOKS", "WOLNE_LEKTURY", WIKISOURCE);

    /** How many other texts stand by for one whose library cannot be had; the processor takes five at most. */
    static final int FALLBACKS = 3;

    /** After this long a book that never finished no longer counts as arriving, so nobody is locked out by it. */
    static final Duration ARRIVAL = Duration.ofMinutes(15);

    private final CatalogWorkRepository catalogWorkRepository;
    private final BookRepository bookRepository;
    private final TextSources textSources;
    private final PreparationService preparationService;
    private final ReadingRights readingRights;
    private final UnlockDates unlockDates;
    private final ReaderLocation readerLocation;
    private final LiveBookLimiter limiter;

    @Value("${app.books.live.max-at-once:4}")
    private int maxAtOnce;

    public LiveBook open(User reader, LiveBookRequest request) {
        CatalogWork work = catalogWorkRepository
                .findById(request.catalogWorkId())
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));
        List<TextSourceView> texts = texts(work, request.sourceDownloadUrl());
        TextSourceView text = texts.getFirst();
        Language language = PreparationService.language(text.language()).orElseThrow();

        Optional<Book> shelved = onShelf(work, language);
        if (shelved.isPresent()) {
            if (!readingRights.mayRead(work.getWorkSlug())) {
                throw new ResourceConflictException("This one can’t be read in your country yet.");
            }
            return ready(null, work, language, shelved.get().getEditionSlug());
        }
        if (unlockDates.in(work.rightsFacts(), readerLocation.country()).status() == RightsStatus.PROTECTED) {
            throw new ResourceConflictException("This one can’t be read in your country yet.");
        }
        Optional<Preparation> standing = preparationService
                .find(work.getId(), language)
                .filter(preparation -> !"failed".equals(preparation.getPhase()));
        if (standing.isPresent()) {
            // somebody asked first, a reader or the factory: this reader waits with them
            return view(preparationService.watch(standing.get().getId()).orElseThrow());
        }
        if (work.getPublicationYear() == null) {
            throw new ResourceConflictException(
                    "We can’t open this one right away: its year of publication is not known. Want it instead.");
        }
        if (preparationService.arrivingFor(reader, ARRIVAL)) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS, "One book at a time: yours is still arriving.");
        }
        if (preparationService.arriving(ARRIVAL) >= maxAtOnce) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS, "Several books are arriving right now; try again in a minute.");
        }
        if (!limiter.tryAcquire(reader.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "That is today’s share of new books; want this one and it will be made for you.");
        }
        try {
            return view(preparationService.startLive(reader, work.getId(), text, texts.subList(1, texts.size())));
        } catch (RuntimeException exception) {
            // nothing was started, so the reader keeps their place for the day
            limiter.release(reader.getId());
            throw exception;
        }
    }

    public LiveBook status(UUID preparationId) {
        return view(preparationService
                .watch(preparationId)
                .orElseThrow(() -> new EntityNotFoundException("No book is arriving under this id")));
    }

    /**
     * The text to fetch, then the ones that stand by for it. A text the reader chose stands alone: another edition
     * is another book to them. Otherwise the original's free texts in the catalogue's order, the ones a library
     * has ready as a file before Wikisource's, whose export is built on request and takes a minute for a long book.
     */
    private List<TextSourceView> texts(CatalogWork work, String chosenDownloadUrl) {
        List<TextSourceView> fetchable = textSources.forWork(work.getId()).stream()
                .filter(view -> view.status() == RightsStatus.FREE)
                .filter(view -> FETCHED.contains(view.kind()))
                .filter(view -> PreparationService.language(view.language()).isPresent())
                .toList();
        if (chosenDownloadUrl != null) {
            return List.of(fetchable.stream()
                    .filter(view -> view.downloadUrl().equals(chosenDownloadUrl))
                    .findFirst()
                    .orElseThrow(() -> new ResourceConflictException("That text can’t be opened right away.")));
        }
        List<TextSourceView> originals = fetchable.stream()
                .filter(view -> !view.translation())
                .sorted(Comparator.comparing((TextSourceView view) -> WIKISOURCE.equals(view.kind())))
                .toList();
        if (originals.isEmpty()) {
            throw new ResourceConflictException(
                    work.getProductionStatus() == RightsStatus.PROTECTED
                            ? "This one is under copyright, so we can’t offer it."
                            : "We have no free text of this one to open right away. Want it instead.");
        }
        String language = originals.getFirst().language();
        return originals.stream()
                .filter(view -> view.language().equals(language))
                .limit(1 + FALLBACKS)
                .toList();
    }

    /** The edition of the work in this language a reader would open: the original where there is one. */
    private Optional<Book> onShelf(CatalogWork work, Language language) {
        if (work.getWorkSlug() == null) {
            return Optional.empty();
        }
        return bookRepository.findByWorkSlug(work.getWorkSlug()).stream()
                .filter(book -> book.getLanguage() == language)
                .min(Comparator.comparing((Book book) -> !"original".equals(book.getEditionType())));
    }

    private LiveBook view(Preparation preparation) {
        CatalogWork work = preparation.getCatalogWork();
        String phase = preparation.getPhase();
        if ("published".equals(phase)) {
            return ready(preparation.getId(), work, preparation.getLanguage(), preparation.getEditionSlug());
        }
        LiveBook.State state;
        LiveBook.Step step = null;
        if ("failed".equals(phase)) {
            state = LiveBook.State.FAILED;
        } else if (!PreparationService.REPORTER_LIVE.equals(preparation.getReporter())) {
            state = LiveBook.State.BEING_PREPARED;
        } else {
            state = LiveBook.State.ARRIVING;
            step = step(phase, preparation.getProgress());
        }
        return new LiveBook(
                preparation.getId(),
                work.getId(),
                preparation.getLanguage(),
                state,
                step,
                null,
                state == LiveBook.State.FAILED ? reason(preparation.getError()) : null,
                preparation.getSourceKind(),
                preparation.getSourceReference());
    }

    /** {@link PreparationStages} counts the file read as the first stage done and the sentences as the second. */
    static LiveBook.Step step(String phase, Integer stagesDone) {
        int done = stagesDone == null ? 0 : stagesDone;
        if (phase == null || "queued".equals(phase) || "downloading".equals(phase)) {
            return LiveBook.Step.FETCHING;
        }
        if (done == 0) {
            return LiveBook.Step.CHAPTERS;
        }
        return done == 1 ? LiveBook.Step.SENTENCES : LiveBook.Step.SHELVING;
    }

    private static String reason(String error) {
        return error == null || error.isBlank() ? "The book could not be fetched." : error;
    }

    private LiveBook ready(UUID preparationId, CatalogWork work, Language language, String editionSlug) {
        return new LiveBook(
                preparationId, work.getId(), language, LiveBook.State.READY, null, editionSlug, null, null, null);
    }
}
