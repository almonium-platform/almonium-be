package com.almonium.learning.book.dto.response;

import java.util.List;

public record BookshelfViewDto(List<BookDto> continueReading, List<BookDto> recommended) {}
