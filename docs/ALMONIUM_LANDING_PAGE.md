# Almonium landing page: skeleton, draft copy, and rules

**Date:** 2026-07-28
**Constraint:** must be server-rendered or statically generated. An Angular SPA
will not rank and will feel slow to a cold visitor.

---

## The rules

**1. Five seconds to comprehension.** A stranger must know what Almonium does
before scrolling. One sentence, one moving image, one button.

**2. Show the product.** Real screen recordings beat illustrations. Your
product is beautiful; hiding it behind stock art wastes your best asset. Every
section gets one visual of the actual thing.

**3. One idea per section.** If a section needs two sentences to explain what
it shows, split it or cut it.

**4. One call to action, repeated.** Not "sign up" and "join Discord" and
"read the blog" and "follow us." Repeat the same button four times down the
page.

**5. Specificity beats adjectives.** "Forty-two books in six languages, aligned
paragraph by paragraph" beats "an extensive library." Never write "seamless,"
"powerful," or "revolutionary."

**6. Lead with what is rare and true.** Not with what took the longest to
build. Your auth system and settings do not appear on this page at all.

**7. The founder story replaces testimonials.** You have no users to quote.
You do have a story nobody else can tell. Put a photo and three sentences near
the bottom.

**8. Do not hide the price.** Hidden pricing reads as expensive and makes
people leave to check.

**9. Answer the objection at the point it arises.** "Do I need an account?"
belongs under the reader demo, not in a FAQ nobody scrolls to.

**10. Length: medium, not short.** Short pages work for products people already
understand. Nobody knows what Almonium is, so you need eight or nine sections.
The failure mode is filler sections, not length.

**11. Never claim you invented parallel reading.** Beelinguapp has four million
downloads. Claim what you do with it instead.

---

## Section skeleton

Your proposed order was reader, bookshelf, social, Discover, review, story. I
would reorder so that the first three sections deliver value before asking for
anything, and put social later because it means nothing to a visitor who has
no friends on the platform yet.

---

### 1. Hero

**Visual:** looping screen recording, roughly eight seconds, of the parallel
reader scrolling with paragraphs staying aligned and a phrase lighting up in
matching colours on both sides. No cursor, no chrome, no UI noise.

**Copy:**

> # Read anything. Keep every word.
>
> Almonium turns the books you read into the vocabulary you actually use.
> Read in your target language with the translation beside it, tap any word to
> understand it, and let Almonium remember it until you can use it yourself.
>
> **[ Start reading, no account needed ]**
> Free forever for reading. 42 books, 6 languages.

Replace the numbers with real ones. If it is nine books, say nine books. A
small honest number beats a vague large claim.

---

### 2. Live demo strip

Directly under the hero, before any feature talk.

**Copy:**

> ### Try it now. Paste a German sentence.
>
> [ input field, prefilled with a real sentence ]
>
> No sign-up, no email.

**Behaviour:** returns the real Discover sheet for one word in that sentence,
inline on the page. This is the single highest-converting element you can
build, because it moves someone from reading about the product to using it in
one action.

---

### 3. The bookshelf

**Visual:** a real shelf of real covers, warm cream ground, in your Libre
Baskerville voice. Not a grid of placeholder rectangles.

**Copy:**

> ### Books, side by side
>
> Every book is aligned paragraph by paragraph, so the translation never
> drifts out of step. Read the whole thing free, in your browser, without an
> account.
>
> [ Sherlock Holmes · EN/DE ]  [ Frankenstein · EN/UA ]  [ Die Verwandlung · DE/EN ]
>
> **[ Browse all books ]**

Every cover links to the public reader page. Those pages are your SEO surface,
so the shelf doubles as internal linking.

---

### 4. Understand any word

**Visual:** GIF of selecting a word inside the reader and the Discover sheet
rising over the text without losing your place. Show the sheet's default state,
then the expansion.

**Copy:**

> ### Tap a word. Understand it properly.
>
> Not a dictionary dump. Almonium shows the sense that fits this sentence,
> how common the word is, how it sounds, and which words it usually travels
> with.
>
> Speak more than one language? See the meaning in all of them. Sometimes
> Ukrainian catches a nuance that English flattens.

That last line is the polyglot hook. It costs one sentence and it makes your
target user feel seen.

---

### 5. Review that catches what you actually got wrong

This is your wow section. Give it the most space.

**Visual:** GIF of a review card, a wrong answer typed in, and the feedback
appearing: *"**verlassen** means to leave. You were thinking of **überlassen**,
to hand over. Here they are side by side."*

**Copy:**

> ### Most apps say "wrong." Almonium says which word you were thinking of.
>
> If you answer with the meaning of a different word you are learning,
> Almonium recognises it, shows you both, and schedules the two together until
> you stop mixing them up.
>
> It also rewords the answer every time, so you learn the word instead of
> memorising the shape of the card.

Two sentences, two genuine differentiators, one image. This section alone is
worth more than your entire games roadmap.

---

### 6. The story at the end

**Visual:** the end-of-session screen with a short generated passage, the
words you missed highlighted inside it.

**Copy:**

> ### Finish with a story built from what you missed
>
> Every review session ends with a short piece of writing that quietly uses
> the words you just got wrong, in new sentences, at your level.

---

### 7. Send words to a friend

Now it means something, because the reader understands what a word is in
Almonium.

**Visual:** a shared pack opening on a phone, previewing without an account.

**Copy:**

> ### Words are better shared
>
> Send a word, a phrase, or a whole list from a chapter you just read. Your
> friend can open it, read the context you saved, and keep the ones they want.
> They do not need an account to look.

---

### 8. Why Almonium is different

Honest comparison. Do not build a competitor table with red crosses; it reads
as insecure and invites correction.

**Copy:**

> ### What Almonium does that other tools do not
>
> - Shows a word's meaning in every language you already speak, not just one
> - Knows the difference between understanding a word and being able to use it
> - Notices which words you confuse with each other
> - Gives you the same book at the level you can actually read
> - Keeps the whole history of a word: where you met it, how often, what
>   tripped you up

Five bullets, all true, none claiming to have invented reading.

---

### 9. Who built this

**Visual:** your photo. A real one.

**Copy:**

> I took my English to C2 on my own, using about a dozen disconnected tools.
> Then I started German and hit the same wall. A dictionary that does not know
> what I am reading, flashcards that do not know what I understood, a reader
> that forgets everything the moment I close it.
>
> Almonium is the tool I wanted. I use it every day.
>
> — Yevhenii, Kyiv

Three short paragraphs, first person, no company voice. This converts better
than any feature list you could write, because it is the only thing on the page
a competitor cannot copy.

---

### 10. Pricing

Show the free tier first and make it genuinely usable. Reading should be free
forever; that is what feeds your SEO pages and your word of mouth.

> **Free** · Read every book, look up any word, keep 100 words
> **Premium** · Everything, synced across devices, your own books
> **Founding member** · Lock this price for good

Set Premium at your real target number. Grandfather early users explicitly and
say so on the page; it converts hesitation into urgency without a fake
countdown.

---

### 11. Final call to action

Same button, same words as the hero.

> ### Start with one page of a book.
>
> **[ Start reading, no account needed ]**

---

## What to leave off

- The chat, however good it is
- Games, until one exists
- Anything about your tech stack
- Streaks and heatmaps
- A newsletter signup competing with the main button
- "Trusted by" logos you do not have
- Any adjective doing the work a screenshot should do

---

## Build order

1. Hero with the reader GIF, plus the final call to action. Ship it today, even
   with nothing below it.
2. The bookshelf with real links to public reader pages.
3. Sections 4 and 5. The confusion GIF is the one to spend an afternoon
   perfecting.
4. Founder section and pricing.
5. The live paste demo, which is engineering work rather than page work.

Sections 6, 7, and 8 can wait a week. The page is useful the moment step 1
exists, and right now you have nothing.
