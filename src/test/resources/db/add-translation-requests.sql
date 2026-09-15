INSERT INTO user_core (id, username, email, email_verified, registered, setup_step)
VALUES ('01956ce2-34fa-71f2-97b1-dacd41dbab00', 'asker', 'asker@email.com', true, current_timestamp, 'COMPLETED');

INSERT INTO book (id, edition_slug, work_slug, source_hash, title, author, publication_year,
                  language, original_language, word_count, cefr_level, edition_type)
VALUES ('01956ce2-34fa-71f2-97b1-dacd41dbab01', 'effi-briest-de', 'effi-briest', 'sha-effi',
        'Effi Briest', 'Theodor Fontane', 1895, 'DE', 'DE', 120000, 'B2', 'original');
