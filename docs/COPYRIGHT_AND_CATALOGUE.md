# Book catalogue, copyright engine, and reading by country

Almonium makes graded and translated editions in Ukraine, hosts them on a
server in Germany, and markets them first to German readers. Making an
edition is an adaptation, so a work may be prepared only if it is free in
**both** Ukraine and Germany. Whether a reader may open a published edition
then depends on the reader's country as well.

Three pieces implement this:

| Piece | Package | What it does |
| --- | --- | --- |
| Catalogue | `learning.catalog` | Every notable book (about 386,000 works), metadata only, imported from Wikidata and Project Gutenberg. Search lands on it. |
| Copyright engine | `learning.rights.engine` | Works out, per country and year, whether a work is free, protected, or unknown, from its publication year and its authors' lives. |
| Reading rights | `learning.rights.service` | Which countries each shelf work is hidden in; the reader's country from their IP address; the publish-time check; /ops takedowns and the report button. |

## The catalogue

`CatalogImportService` builds the catalogue in one transaction:

1. **Wikidata, through QLever** (`app.catalog.sparql-url`). A work is any item
   that is an instance of literary work, novel, novella, short story, poem,
   play, poetry or story collection, fairy tale, epic poem, book or written
   work, and that has at least one Wikipedia article. Five flat SPARQL
   queries: works (sitelink count as popularity, language, earliest
   publication or inception year), work→author pairs, authors (name, birth,
   death, "mort pour la France" as award Q3324507), titles in the product
   languages plus `mul` plus the work's own language, and Gutenberg ids
   (P2034). The whole fetch takes about 35 seconds.
2. **Project Gutenberg's catalogue file** (`pg_catalog.csv`). Each text is
   matched to a Wikidata work by Gutenberg id, else by exact title (any
   language) plus every author surname. A match fills a death year Wikidata
   lacks. Texts nothing matches become their own works (one per title and first
   author), with popularity 0.

QLever quirk: comparing `wikibase:sitelinks` directly drops every row;
`BIND(?links + 0 AS ?n) FILTER(?n > 0)` works.

Rows keep their id across imports (matched by Wikidata id, or by Gutenberg id
for Gutenberg-only works). `work_slug` links a catalogue work to the shelf and
is never overwritten by an import, only filled. Works an import no longer
produces are deleted unless linked.

`catalog_title` holds every title, every author name, and one "title + author"
row per work, folded (`TitleFolding`: lower case, no accents or apostrophes,
punctuation as spaces) and indexed with `pg_trgm`. Search (`GET
/public/catalog/search?q=`) ranks exact titles first, then quarter-point bands
of word similarity, the better-known work first within a band. Queries take
5–20 ms on the full catalogue.

The import runs on the first start against an empty catalogue, on the 2nd of
every month at 04:30 UTC, and on demand from `POST /ops/catalog/import`. The
production status of every work is recounted nightly, since it only changes as
the year turns.

## The copyright engine

`CountryTerms` holds each country's term as of 2026. Most of the world,
including Ukraine, Germany, and the rest of the EU, is life plus 70, which is
also the default for any unlisted country. Special cases:

| Country | Rule |
| --- | --- |
| United States (and territories) | Published before 1978: 95 years from publication (renewal lapses ignored). Otherwise life + 70. |
| France (and overseas departments) | Life + 70, and 30 more for an author declared "mort pour la France". |
| Spain | Authors who died in 1987 or before: life + 80. |
| Ukraine | Life + 70, counted from rehabilitation for a repressed author rehabilitated after death. |
| Russia | Life + 70; + 4 for authors who died in 1941 or later (wartime work); from rehabilitation where it applies. |
| Canada, New Zealand | Life + 50 for authors who died in 1971 or before, otherwise life + 70. |
| Japan | Life + 50 for authors who died in 1957 or before (the 1967 cutoff pulled back for the wartime extension on Allied works), otherwise life + 70. |
| South Korea | Life + 50 for authors who died in 1962 or before, otherwise life + 70. |
| Life + 50 | BY, CN, EG, HK, IR, KE, MO, MY, NP, PH, PK, SA, TH, TW, VN, ZA |
| Life + 60 | BD, IN, VE |
| Life + 75 / 80 / 95 / 99 / 100 | GT, HN, VC, WS / CO / JM / CI / MX |

**The table errs long where a country is uncertain.** A term guessed too long
only hides a book; one guessed too short publishes one. It was written from
general knowledge, not from a legal review: have a lawyer confirm the rules
for Ukraine, Germany and the main markets before relying on it.

Missing facts widen the answer instead of guessing. An author with no recorded
death is assumed to have died between the publication and 110 years after
their birth (taken as ten years before the publication when the birth is not
recorded either). A work is free only if it is free for every death in that
window, protected only if protected for every one, and unknown otherwise.
Unknown is never shown as "under copyright" and never blocks: /ops fixes the
facts or decides.

Known gaps, handled by /ops overrides rather than by the table: human
translators' own rights (the catalogue models works, not editions), Ukraine's
and Russia's rehabilitation dates (no reliable Wikidata source; the field
exists on `CatalogAuthor` but the import leaves it empty), posthumous
first-publication rights (Germany §71), and the EU rule of the shorter term.

## Reading by country

`work_rights_block` lists the countries each shelf work is hidden in (`*` for
everywhere), recomputed by `WorkRightsService` whenever its inputs change:
after every catalogue import, on every publication, on every /ops override,
and nightly. Each application slot keeps an in-memory copy reloaded every
minute, so a takedown reaches every reader within a minute.

A work is hidden:

- **everywhere** when it is protected in a production country (Ukraine or
  Germany) and no override allows it there;
- **in a country** when the engine says it is protected there;
- as /ops overrides say, which always win.

The facts for a shelf work come from the catalogue work linked to its
`work_slug`. An unlinked work is judged by its own edition's publication year
and author name only, which still settles the US rule.

The reader's country comes from their IP address (`ReaderLocation`), looked up
in DB-IP's free IP-to-Country Lite database (`GeoIpCountries`), which is
downloaded at startup and monthly (about 8 MB, held in memory). Traefik is the
only way in and the application trusts its forwarded headers
(`server.forward-headers-strategy: framework`), so `getRemoteAddr()` is the
reader's address. An address that cannot be placed counts as
`app.rights.unknown-country` (Germany). DB-IP's licence (CC BY 4.0) requires
crediting DB-IP on the site; the frontend footer does.

Lists (the shelf, the public catalogue of editions) silently leave out what the
reader's country may not read. Opening one directly answers **451 Unavailable
For Legal Reasons**. Catalogue search marks such a work `NOT_IN_YOUR_COUNTRY`.
The sitemap leaves out only works hidden everywhere.

## Publishing

`ProductionGate` refuses a publication whose work is protected in Ukraine or
Germany: the internal publication endpoint answers 409 with the reason
("Under copyright in UA and DE until the end of 2046 (by the catalogue's
dates)."). The book processor can ask the same question before it spends
anything: `POST /internal/books/publications/rights-check`, signed like
publications, body `{workSlug, title, author, publicationYear}`, answer
`{status, freeFrom, countries, catalogWorkId, catalogTitle, catalogAuthor,
explanation}`. Unknown passes.

## Reports and takedowns

- **Report button:** `POST /public/books/{editionSlug}/rights-reports`, open to
  guests (they must leave an email), five per address per hour. Each report is
  stored and mailed to `app.rights.copyright-address`
  (`copyright@almonium.com`) at once. A report hides nothing by itself.
- **/ops → Rights** (`/ops/rights`): the report queue (dismiss, or act),
  `POST /overrides` with a work slug, `*` or a country, `BLOCK` or `ALLOW`, a
  reason and optionally the report it answers; `DELETE /overrides/{id}` hands
  the country back to the engine; `GET /works` and `/works/{slug}` show the
  facts, the verdict per country and the blocks; `PUT /works/{slug}/catalog`
  links a shelf work to the right catalogue work.

A takedown everywhere is one call, effective within a minute.

## Configuration

All keys have defaults in `application.yaml`; none needs an environment
variable.

| Key | Default |
| --- | --- |
| `app.catalog.scheduled` | `true` (`false` in tests) |
| `app.catalog.sparql-url` | `https://qlever.dev/api/wikidata` |
| `app.catalog.gutenberg-csv-url` | Gutenberg's `pg_catalog.csv` |
| `app.catalog.title-languages` | `en,mul,de,uk,ru,fr,es,it,pl,pt,nl,cs,sv` |
| `app.rights.production-countries` | `UA,DE` |
| `app.rights.unknown-country` | `DE` |
| `app.rights.geoip.enabled` | `true` (`false` in tests) |
| `app.rights.geoip.url-template` | DB-IP Lite, `%s` = `yyyy-MM` |
| `app.rights.copyright-address` | `copyright@almonium.com` |
| `app.rights.reload-interval` | `PT1M` |
