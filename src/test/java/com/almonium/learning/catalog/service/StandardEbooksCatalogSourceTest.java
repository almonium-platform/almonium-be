package com.almonium.learning.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StandardEbooksCatalogSourceTest {
    static final String PAGE = """
            <ol class="ebooks-list">
            <li typeof="schema:Book" about="/ebooks/jane-austen/pride-and-prejudice">
              <div class="thumbnail-container" aria-hidden="true"><a href="/ebooks/jane-austen/pride-and-prejudice"></a></div>
              <p><a href="/ebooks/jane-austen/pride-and-prejudice" property="schema:url"><span property="schema:name">Pride and Prejudice</span></a></p>
              <p class="author" typeof="schema:Person" property="schema:author"><a href="/ebooks/jane-austen" property="schema:url"><span property="schema:name">Jane Austen</span></a></p>
            </li>
            <li typeof="schema:Book" about="/ebooks/h-g-wells/the-war-of-the-worlds">
              <p><a href="/ebooks/h-g-wells/the-war-of-the-worlds" property="schema:url"><span property="schema:name">The War of the Worlds</span></a></p>
              <p class="author" typeof="schema:Person"><a href="/ebooks/h-g-wells"><span property="schema:name">H.&#160;G. Wells</span></a></p>
            </li>
            </ol>
            """;

    @Test
    void theListingGivesPathTitleAndAuthorPerBook() {
        var texts = StandardEbooksCatalogSource.parse(PAGE);
        assertThat(texts)
                .containsExactly(
                        new LibraryCatalogSource.LibraryText(
                                "STANDARD_EBOOKS",
                                "/ebooks/jane-austen/pride-and-prejudice",
                                "Pride and Prejudice",
                                "Jane Austen",
                                "en"),
                        new LibraryCatalogSource.LibraryText(
                                "STANDARD_EBOOKS",
                                "/ebooks/h-g-wells/the-war-of-the-worlds",
                                "The War of the Worlds",
                                "H.&#160;G. Wells",
                                "en"));
    }

    @Test
    void aPageWithoutBooksEndsTheListing() {
        assertThat(StandardEbooksCatalogSource.parse("<ol class=\"ebooks-list\"></ol>"))
                .isEmpty();
    }
}
