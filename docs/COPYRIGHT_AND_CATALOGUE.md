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
| France (and overseas departments) | Life + 70. For an author declared "mort pour la France": the old life + 50, plus 30, plus the war extensions for works published by 1947 (8 years 120 days) and by 1920 (6 years 152 days), rounded up to year ends, when longer. |
| Spain | Authors who died in 1987 or before: life + 80. |
| Ukraine | Life + 70, counted from rehabilitation for a repressed author rehabilitated after death. |
| Russia | Life + 70; + 4 for authors who died in 1941 or later (wartime work); from rehabilitation where it applies. Works whose old life + 50 (+ 4) had run out by 1993 stay free. |
| Canada, New Zealand | Life + 50 for authors who died in 1971 or before, otherwise life + 70. |
| Japan | Life + 50 for authors who died in 1957 or before (the 1967 cutoff pulled back for the wartime extension on Allied works), otherwise life + 70. |
| South Korea | Life + 50 for authors who died in 1962 or before, otherwise life + 70. |
| Life + 50 | BY, CN, EG, HK, IR, KE, MO, MY, NP, PH, PK, SA, TH, TW, VN, ZA |
| Life + 60 | BD, IN, VE |
| Life + 75 / 80 / 95 / 99 / 100 | GT, HN, VC, WS / CO, GQ / JM / CI / MX |

**The table errs long where a country is uncertain.** A term guessed too long
only hides a book; one guessed too short publishes one. On 2026-10-06 it was
checked against Wikipedia's "List of copyright duration by country": no
country there has a longer life-based term than the table, and about 80 have
a shorter one (life + 50), which the table deliberately leaves at 70. It is
not a legal review: have a lawyer confirm the rules for Ukraine, Germany and
the main markets before relying on it.

Missing facts widen the answer instead of guessing. An author with no recorded
death is assumed to have died between the publication and 110 years after
their birth (taken as ten years before the publication when the birth is not
recorded either). A work is free only if it is free for every death in that
window, protected only if protected for every one, and unknown otherwise.
Unknown is never shown as "under copyright" and never blocks: /ops fixes the
facts or decides.

**Repressed authors.** Ukraine and Russia count the term of an author who was
executed or died in the purges and rehabilitated after death from the
rehabilitation, not the death: Valerian Pidmohylny (shot 1937, rehabilitated
1956) is protected in Ukraine until the end of 2026, though free in Germany
since 2008. Wikidata records no rehabilitation dates, so the import marks as
`repressed` every author of a Soviet citizenship (USSR, Ukrainian SSR, Russian
SFSR) whose manner of death is capital punishment, homicide or "unnatural
death" and who died in 1930–1953. For such an author with no recorded
rehabilitation year, the engine lets the rehabilitation be any year up to
1991, so their works come out unknown, not free, in Ukraine and Russia:
production goes ahead (unknown passes) but the processor records why, and
/ops can pin the year with an override. Other countries are unaffected.

Known gaps, handled by /ops overrides rather than by the table: human
translators' own rights (the catalogue models works, not editions), exact
rehabilitation dates, posthumous first-publication rights (Germany §71,
Ukraine's 70 years from a publication within 30 years of the death), and the
EU rule of the shorter term.

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

## Wanted books

A reader can want any catalogue work that is not under copyright where we make
books, in the language they would read it in (`POST /books/wants
{catalogWorkId, language}`; `DELETE /books/wants/{workId}/{language}` takes it
back, silently). Wanting is free and unmetered: it promises a place in a public
order, nothing else, and editorial still chooses what to prepare. A want for a
new book and a want for a new language of a shelf work are the same record.

`GET /books/wanted[?mine=true]` is the Wanted page: up to two works being
prepared, the queue ranked by distinct readers (ties by the first want), and
what arrived in the last 14 days. Counts are hidden below three readers, so a
lone want never reads as "1 reader". The answer to a want carries the work's
rank, for the "It's 2nd in line" toast. Catalogue search takes `language` and
then says, per hit, how many want it and whether the viewer does.

A published edition settles every standing want of its work in its language
(found through the catalogue link on its work slug), and each wanter hears
once, in the app and by email.

`/ops/demand?days=30` replaces the book-requests tab: the wanted rows (Start
moves a row to "Being prepared" and links to the editorial catalogue
prefilled; Decline drops it), searches whose top hit is under copyright (with
Wikidata's publisher, as research for a licensing conversation), and searches
that found nothing, where "Link to work" (`POST /ops/demand/aliases`) teaches
search that the query means a work. Every search is logged with its top hit;
a guest is told apart by a keyed hash of their address under a key that lives
only as long as the process, and a query the same searcher extended within a
minute counts as still typing.

`book_request` is no longer read or written. It stays in the schema so the
slot still running the previous release keeps working during a rollout; drop
it in a later release. Translation orders (the paid "add a language" flow with
its monthly allowance) are unchanged.

## Public Domain Day and the countdown

`catalog_work.production_free_from` holds the year a work becomes free where we
make books only when the facts pin it to one year (Christie's Styles: 2047),
never a lower bound (Rowling: 2068 at the earliest), so a client may count
down to 1 January of that year. `GET /public/catalog/coming-free[?year=]`
lists the best-known literary works that become free on 1 January of a year,
next year by default: for 2027, Brecht and A. A. Milne's Pooh books.

## Free texts and preparing a wanted work

The import records where each work's text can be fetched, through an API and
legally: Project Gutenberg's books (by id; EPUB 3 from
`gutenberg.org/ebooks/{id}.epub3.images`) and Wikisource pages, both about the
work and about its editions (EPUB from Wikisource's own export tool,
`ws-export.wmcloud.org`). About 225,000 catalogue works have at least one.
Each source keeps its language and, for a translation, its translators' lives
(Gutenberg's catalogue names translators with dates; Wikidata editions carry
translator P655). A source is free only when the work is free where we make
books and its translators are too; a translation whose translators are not
known is unknown, never free. A page in another language than the work's is a
translation; a work with no language recorded takes its author's native one.

`GET /ops/catalog/works/{id}/sources` lists them, free first. In /ops →
Demand, Start can name one (`{"sourceId": …}`): the backend asks the book
processor (`POST /internal/source-ingests/`) to fetch that EPUB and ingest it,
and every three minutes reads its progress back. The Wanted page shows a work
being prepared as four stages (text, chapters, level and words, on the
shelf), counted in order from the pipeline stages the processor reports. A
source under copyright cannot be chosen; the processor checks the rights
again before any paid step.

## The factory

Books are made on the owner's host, where the Codex and Claude plan accounts
are signed in; this deployment's processor cannot run the writers and judges.
So the factory lives in the book processor on that host
(`catalog/factory.py`, a systemd timer every 15 minutes), and this backend
offers it three signed calls under `/internal/books/factory`, signed exactly
like a publication:

- `POST /queue` (empty body): the wanted works nobody is preparing, ranked as
  /ops Demand ranks them (readers, then the first want), each with the best
  free text to start from: in the wanted language if a free one exists, else
  the work's own, else any free text the processor reads. Twenty rows at most.
- `POST /preparations {catalogWorkId, language, sourceId}`: the factory's
  reservation. The same checks as Start in /ops (the source is the work's and
  not under copyright, its language is one the processor reads, nothing else
  is preparing the row), but this deployment's processor is not asked: the host
  fetches the text itself. The wants move to IN_PROGRESS with nobody deciding;
  the answer is the preparation id, which the host uses as the ingest's id.
- `POST /preparations/{id}/status`: the host pushes the same status body the
  backend otherwise polls from its processor, with the production's word on
  top: `producing` while the levels and translations are made, `published`
  once promoted and published, `failed` with the production's reason when a
  gate held the book.

`preparation.reporter` says which kind a row is: `processor` rows are polled
every three minutes as before, `factory` rows are never polled. The Wanted page
and /ops Demand show both alike. Publication settles the wants as it always
did, so a factory book arrives for its readers without anyone pressing
anything. Whether there is room to start another book is the host's call,
from its plan meters (`FACTORY_RESERVE_PERCENT`, `FACTORY_MAX_PRODUCTIONS` in
the processor's environment).


## Unlock dates: when and where a work becomes free (W6, W7, W8)

Added 2026-10-22. Every catalogue hit carries three more fields: `readerCountry`
(where the request comes from, by `ReaderLocation`), `readerFreeFrom` (the year
the reader can open the work, from 1 January) and `freeIn` (up to three
countries where it is already free, best known first). The reader's year is
the later of two exact years, the production countries' and the reader's
country's, because an edition can only be made once Ukraine and Germany free
the work; it is null unless both years are exact, the one case a countdown
may count to. `UnlockDates` computes all of it from `CopyrightEngine`.

`GET /public/catalog/works/{id}/unlocks` answers for every ISO country: status
this year, the exact free year when the facts pin it down, and the rule behind
the term in a reader's words (`CountryTerms.rule`, e.g. "Author's life + 100
years"). The clients draw it as a map with a year slider on the web and as a
list grouped by year on the phone. Countries the table does not list show the
default life-plus-70 rule; the clients call a country with no exact year "Not
checked yet" rather than guessing.

A work under copyright can be wanted ahead of the day when its production free
year is within five years (`WantService.freesSoon`): "Tell me on the day" is an
ordinary want, so it sorts into the queue and is settled when the edition
arrives. The Public Domain Day list keeps only works the reader's own country
frees by that January.

The rules are a careful estimate for showing dates, not legal advice: the
table simplifies non-retroactive extensions, wartime extensions and the US
publication rule, and it needs a legal check before launch.
