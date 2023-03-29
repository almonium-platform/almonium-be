package com.linguatool.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.linguatool.configuration.security.oauth2.user.OAuth2UserInfo;
import com.linguatool.configuration.security.oauth2.user.OAuth2UserInfoFactory;
import com.linguatool.exception.friend.FriendshipNotAllowedException;
import com.linguatool.exception.friend.FriendshipNotFoundException;
import com.linguatool.exception.auth.OAuth2AuthenticationProcessingException;
import com.linguatool.exception.auth.UserAlreadyExistsAuthenticationException;
import com.linguatool.model.dto.Friend;
import com.linguatool.model.dto.*;
import com.linguatool.model.dto.external_api.request.CardCreationDto;
import com.linguatool.model.dto.external_api.request.CardDto;
import com.linguatool.model.dto.external_api.request.CardUpdateDto;
import com.linguatool.model.dto.external_api.request.TagDto;
import com.linguatool.model.entity.lang.*;
import com.linguatool.model.entity.user.*;
import com.linguatool.model.mapping.CardMapper;
import com.linguatool.repository.*;
import com.linguatool.util.GeneralUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.linguatool.model.entity.user.FriendshipStatus.FRIENDS;
import static com.linguatool.model.entity.user.FriendshipStatus.PENDING;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    CardTagRepository cardTagRepository;
    FriendshipRepository friendshipRepository;
    PasswordEncoder passwordEncoder;
    CardMapper cardMapper;
    ExampleRepository exampleRepository;
    CardRepository cardRepository;
    TranslationRepository translationRepository;
    TagRepository tagRepository;
    LanguageRepository languageRepository;
    CardSuggestionRepository cardSuggestionRepository;

    @Transactional
    public CardDto getCardById(Long id) {
        return cardMapper.cardEntityToDto(cardRepository.getById(id));
    }

    @Transactional
    public void changeUsername(String username, Long id) {
        if (!userRepository.existsByUsername(username))
            userRepository.changeUsername(username, id);
    }

    @Override
    @Transactional(value = "transactionManager")
    public User registerNewUser(final SignUpRequest signUpRequest) throws UserAlreadyExistsAuthenticationException {
        if (signUpRequest.getUserID() != null && userRepository.existsById(signUpRequest.getUserID())) {
            throw new UserAlreadyExistsAuthenticationException("User with User id " + signUpRequest.getUserID() + " already exist");
        } else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsAuthenticationException("User with email id " + signUpRequest.getEmail() + " already exist");
        } else if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UserAlreadyExistsAuthenticationException("User with username " + signUpRequest.getUsername() + " already exist");
        }
        User user = buildUser(signUpRequest);
        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setTargetLanguages(Set.of(languageRepository.getEnglish()));
        user.setModified(now);
        user = userRepository.save(user);
        userRepository.flush();
        return user;
    }

    private User buildUser(final SignUpRequest formDTO) {
        User user = new User();
        user.setUsername(formDTO.getUsername());
        user.setEmail(formDTO.getEmail());
        user.setPassword(passwordEncoder.encode(formDTO.getPassword()));
        final HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(Role.ROLE_USER));
        user.setRoles(roles);
        user.setProfilePicLink(formDTO.getProfilePicLink());
        user.setProvider(formDTO.getSocialProvider().getProviderType());
        user.setEnabled(true);
        user.setProviderUserId(formDTO.getProviderUserId());
        return user;
    }

    @Override
    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<FriendInfo> findFriendByEmail(final String email) {
        Optional<Friend> friendOptional = userRepository.findFriendByEmail(email);
        return friendOptional.map(FriendInfo::new);
    }


    @Override
    @Transactional
    public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
        if (!StringUtils.hasLength(oAuth2UserInfo.getName())) {
            throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
        } else if (!StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        SignUpRequest userDetails = toUserRegistrationObject(registrationId, oAuth2UserInfo);
        User user = findUserByEmail(oAuth2UserInfo.getEmail());
        if (user != null) {
            if (!user.getProvider().equals(registrationId) && !user.getProvider().equals(SocialProvider.LOCAL.getProviderType())) {
                throw new OAuth2AuthenticationProcessingException(
                        "Looks like you're signed up with " + user.getProvider() + " account. Please use your " + user.getProvider() + " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(userDetails);
        }

        return LocalUser.create(user, attributes, idToken, userInfo);
    }

    @Transactional
    public void renameTagForUser(User user, Tag tag, String proposedName) {
        if (tag.getText().equals(Tag.normalizeText(proposedName))) {
            return;
        }
        Set<CardTag> foundTaggedCards = cardTagRepository.getByUserAndTag(user, tag);

        if (foundTaggedCards.isEmpty()) {
            return;
        }

        Optional<Tag> tagOptional = tagRepository.findByText(proposedName);
        Tag proposedTag;
        if (tagOptional.isPresent()) {
            proposedTag = tagOptional.get();
        } else {
            proposedTag = new Tag(proposedName);
            tagRepository.save(proposedTag);
        }
        foundTaggedCards.forEach(cardTag -> {
            cardTag.setTag(proposedTag);
            cardTagRepository.save(cardTag);
        });
    }

    @Transactional
    public UserInfo buildUserInfo(LocalUser localUser) {
        List<String> roles = localUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        User user = localUser.getUser();
        List<String> tags = cardTagRepository.getUsersTags(user).stream().map(r -> tagRepository.getById(r).getText()).collect(Collectors.toList());
        List<String> targetLangs = user.getTargetLanguages().stream().map(t -> t.getCode().getCode()).collect(Collectors.toList());
        List<String> fluentLangs = user.getFluentLanguages().stream().map(t -> t.getCode().getCode()).collect(Collectors.toList());
        return new UserInfo(
                user.getId().toString(),
                user.getUsername(),
                user.getEmail(),
                user.getUiLanguage().getCode(),
                user.getProfilePicLink(),
                user.getBackground(),
                roles,
                tags,
                targetLangs,
                fluentLangs);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setProfilePicLink(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteAccount(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public void createCard(User user, CardCreationDto dto) {
        Card card = cardMapper.cardDtoToEntity(dto, languageRepository);
        card.setCreated(LocalDateTime.now());
        card.setUpdated(LocalDateTime.now());
        user.addCard(card);

        List<Example> examples = card.getExamples();
        examples.forEach(e -> e.setCard(card));

        List<Translation> translations = card.getTranslations();
        translations.forEach(t -> t.setCard(card));
        List<CardTag> cardTags = new ArrayList<>();
        TagDto[] tags = dto.getTags();
        Arrays.stream(tags).forEach(t -> {
            CardTag cardTag = new CardTag();
            cardTag.setCard(card);
            cardTag.setUser(card.getOwner());

            Optional<Tag> tagOptional = tagRepository.findByTextNormalized(t.getText());
            if (tagOptional.isPresent()) {
                cardTag.setTag(tagOptional.get());
            } else {
                Tag tag = new Tag(t.getText());
                tagRepository.save(tag);
                cardTag.setTag(tag);
            }

            cardTags.add(cardTag);
        });
//
        cardRepository.save(card);

        translationRepository.saveAll(translations);
        exampleRepository.saveAll(examples);
        cardTagRepository.saveAll(cardTags);

        userRepository.save(user);
        log.info("Created card {} for user {}", card, user);
    }

    @Transactional
    public void cloneCard(Card entity, User user) {
        Card card = cardMapper.copyCardDtoToEntity(cardMapper.cardEntityToDto(entity), languageRepository);

        user.addCard(card);

        List<Example> examples = card.getExamples();
        examples.forEach(e -> e.setCard(card));

        List<Translation> translations = card.getTranslations();
        List<CardTag> cardTags = new ArrayList<>();

        translations.forEach(t -> t.setCard(card));
        Set<CardTag> cardTagsExtracted = entity.getCardTags();
        for (CardTag ct : cardTagsExtracted) {
            cardTags.add(CardTag.builder()
                    .id(new CardTagPK(card.getId(), ct.getId().getTagId()))
                    .card(card)
                    .user(user)
                    .tag(ct.getTag())
                    .build());
        }
        cardRepository.save(card);
        translationRepository.saveAll(translations);
        exampleRepository.saveAll(examples);
        cardTagRepository.saveAll(cardTags);
        userRepository.save(user);
        log.info("Cloned card {} for user {}", card, user);
    }

    private SignUpRequest toUserRegistrationObject(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        return SignUpRequest.builder()
                .providerUserId(oAuth2UserInfo.getId())
                .email(oAuth2UserInfo.getEmail())
                .profilePicLink(oAuth2UserInfo.getImageUrl())
                .socialProvider(GeneralUtils.toSocialProvider(registrationId))
                .password("changeit")
                .build();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Collection<FriendInfo> getUsersFriends(long id) {
        List<Object[]> pairList = friendshipRepository.getUsersFriendsIdsAndStatuses(id);

        List<FriendInfo> result = new ArrayList<>();
        pairList.forEach(objects -> {
            long userId = ((BigInteger) objects[0]).longValue();
            FriendshipStatus friendshipStatus = (FriendshipStatus.fromString((String) objects[1]));
            boolean isFriendRequester = (Boolean) objects[2];
            Optional<Friend> friendOptional = userRepository.findAllById(userId);
            friendOptional.ifPresent(friend -> result.add(new FriendInfo(friend, friendshipStatus, isFriendRequester)));
        });
        return result;
    }

    @Transactional
    public List<CardDto> getSuggestedCards(User user) {
        return cardSuggestionRepository.getByRecipient(user)
                .stream()
                .map(sug -> {
                    CardDto dto = cardMapper.cardEntityToDto(sug.getCard());
                    dto.setUserId(sug.getSender().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(value = "transactionManager")
    public List<CardDto> getUsersCards(User user) {
        return cardRepository.findAllByOwner(user).stream().map(cardMapper::cardEntityToDto).collect(Collectors.toList());
    }

    @SneakyThrows
    @Transactional
    public Friendship cancelFriendship(Long actionInitiatorId, Long actionAcceptorId) {
        assert (!actionInitiatorId.equals(actionAcceptorId));
        friendshipRepository.getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId).orElseThrow(()
                -> new FriendshipNotFoundException());

        friendshipRepository.deleteFriendshipByIds(actionInitiatorId, actionAcceptorId);
        return null;
    }

    @SneakyThrows
    public void unblockFriendship(Long requesterId, Long requesteeId) {
        this.cancelFriendship(requesterId, requesteeId);
    }

    @SneakyThrows
    public Friendship createFriendshipRequest(long actionInitiatorId, long actionAcceptorId) {
        assert (actionInitiatorId != actionAcceptorId);
        User recipient = userRepository.getById(actionAcceptorId);

        Optional<Friendship> friendshipOptional = friendshipRepository.getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId);
        if (friendshipOptional.isPresent()) {
            Friendship existingFriendship = friendshipOptional.get();

            if (Long.valueOf(actionAcceptorId).equals(existingFriendship.whoDeniesFriendship())) {
                throw new FriendshipNotAllowedException("User limited your ability to send requests!");
            }

            throw new Exception(String.format("Friendship between %s and %s already exists, status: %s",
                    actionInitiatorId, actionAcceptorId, existingFriendship.getFriendshipStatus()));
        }
        if (recipient.isFriendshipRequestsBlocked()) {
            throw new FriendshipNotAllowedException("User doesn't accept friendship requests!");
        }

        Friendship friendship = new Friendship();
        LocalDateTime now = LocalDateTime.now();
        friendship.setFriendshipStatus(PENDING);
        friendship.setCreated(now);
        friendship.setUpdated(now);
        friendship.setRequesterId(userRepository.getById(actionInitiatorId).getId());
        friendship.setRequesteeId(userRepository.getById(actionAcceptorId).getId());
        friendshipRepository.save(friendship);
        return friendship;
    }


    @SneakyThrows
    public Friendship approveFriendshipRequest(long actionInitiatorId, long actionAcceptorId) {
        assert (actionInitiatorId != actionAcceptorId);

        Optional<Friendship> friendshipOptional = friendshipRepository.getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId);
        friendshipOptional.orElseThrow(() -> new FriendshipNotFoundException());

        Friendship friendship = friendshipOptional.get();
        assert friendship.getFriendshipStatus().equals(PENDING);

        LocalDateTime now = LocalDateTime.now();
        friendship.setFriendshipStatus(FriendshipStatus.FRIENDS);
        friendship.setUpdated(now);
        friendshipRepository.save(friendship);
        return friendship;
    }

    @Transactional
    public void ddd(CardTag ct, Card card, User recipient) {
        CardTag cardTag = CardTag.builder()
                .id(new CardTagPK(card.getId(), ct.getTag().getId()))
                .user(recipient)
                .card(cardRepository.getById(20L))
                .tag(ct.getTag())
                .build();
        cardTagRepository.save(cardTag);
    }

    @Transactional
    public void lol(CardAcceptanceDto dto) {
//        Card card = cardRepository.getById(85L);
//        Card card = cardRepository.getById(dto.getCardId());
//        System.out.println(card.getExamples());
    }

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MappingJackson2HttpMessageConverter springMvcJacksonConverter;

    @Autowired
    ObjectMapper mapper;

    @Transactional
    public void declineSuggestion(CardAcceptanceDto dto, User recipient) {
        cardSuggestionRepository
                .deleteBySenderAndRecipientAndCard(
                        userRepository.getById(dto.getSenderId()),
                        recipient,
                        cardRepository.getById(dto.getCardId())
                );
    }

    @Transactional
    public void acceptSuggestion(CardAcceptanceDto dto, User recipient) {
        User sender = userRepository.getById(dto.getSenderId());
        Card card = cardRepository.getById(dto.getCardId());
        cloneCard(card, recipient);
        CardSuggestion cardSuggestion = cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card);
        cardSuggestionRepository.delete(cardSuggestion);
    }


    public boolean suggestCard(CardSuggestionDto dto, User sender) {
        Card card = cardRepository.getById(dto.getCardId());
        User recipient = userRepository.getById(dto.getRecipientId());
        //TODO  notifications
        //TODO check if has access
        if (cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card) == null) {
            cardSuggestionRepository.save(new CardSuggestion(sender, recipient, card));
            return true;
        } else {
            return false;
        }
    }

    @SneakyThrows
    @Transactional
    public Friendship blockUser(long actionInitiatorId, long actionAcceptorId) {
        assert (actionInitiatorId != actionAcceptorId);

        Optional<Friendship> friendshipOptional = friendshipRepository.getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId);
        friendshipOptional.orElseThrow(() -> new FriendshipNotFoundException());
        Friendship friendship = friendshipOptional.get();

        assert friendship.getFriendshipStatus().equals(FRIENDS) || friendship.getFriendshipStatus().equals(PENDING);

        LocalDateTime now = LocalDateTime.now();
        friendship.setUpdated(now);
        friendship.setFriendshipStatus(friendship.getRequesterId().equals(actionInitiatorId)
                ? FriendshipStatus.FST_BLOCKED_SND
                : FriendshipStatus.SND_BLOCKED_FST
        );
        friendshipRepository.save(friendship);
        return friendship;
    }

    @Transactional
    public void editFriendship(FriendshipCommandDto dto) {
        switch (dto.getAction()) {
            case REQUEST:
                this.createFriendshipRequest(dto.getIdInitiator(), dto.getIdAcceptor());
                break;
            case ACCEPT:
                this.approveFriendshipRequest(dto.getIdInitiator(), dto.getIdAcceptor());
                break;
            case BLOCK:
                this.blockUser(dto.getIdInitiator(), dto.getIdAcceptor());
                break;
            case UNBLOCK:
                this.unblockFriendship(dto.getIdInitiator(), dto.getIdAcceptor());
            case CANCEL:
            case REJECT:
            case UNFRIEND:
                this.cancelFriendship(dto.getIdInitiator(), dto.getIdAcceptor());
                break;
        }
    }

    @Transactional
    public List<CardDto> searchByEntry(String entry, User user) {
        return cardRepository.findAllByOwnerAndEntryLikeIgnoreCase(user, '%' + entry.trim().toLowerCase() + '%')
                .stream().map(cardMapper::cardEntityToDto).collect(Collectors.toList());
    }

    @Transactional
    public void updateCard(CardUpdateDto dto, User user) {
        Card entity = cardRepository.getById(dto.getId());
        cardMapper.update(dto, entity, languageRepository);

        Arrays.stream(dto.getTr_del()).forEach(i -> translationRepository.deleteById((long) i));
        Arrays.stream(dto.getEx_del()).forEach(i -> exampleRepository.deleteById((long) i));

        Arrays.stream(dto.getTranslations()).forEach(translationDto -> {
            Long id = translationDto.getId();
            if (id != null) {
                Translation translation = translationRepository.getById(id);
                translation.setTranslation(translationDto.getTranslation());
                translationRepository.save(translation);
            } else {
                translationRepository.save(Translation.builder()
                        .card(entity)
                        .translation(translationDto.getTranslation())
                        .build());
            }
        });
        Arrays.stream(dto.getExamples()).forEach(exampleDto -> {
            Long id = exampleDto.getId();
            if (id != null) {
                Example example = exampleRepository.getById(id);
                example.setExample(exampleDto.getExample());
                example.setTranslation(exampleDto.getTranslation());
                exampleRepository.save(example);
            } else {
                exampleRepository.save(Example.builder()
                        .card(entity)
                        .example(exampleDto.getExample())
                        .translation(exampleDto.getTranslation())
                        .build());
            }
        });

        HashSet<String> dtoTagSet = Arrays
                .stream(dto.getTags())
                .map(TagDto::getText)
                .collect(Collectors.toCollection(HashSet::new));

        HashSet<String> cardTagSet = entity.getCardTags()
                .stream()
                .map(cardTag -> cardTag.getTag().getText())
                .collect(Collectors.toCollection(HashSet::new));

        Set<String> added = Sets.difference(dtoTagSet, cardTagSet);
        Set<String> deleted = Sets.difference(cardTagSet, dtoTagSet);

        for (String tagText : deleted) {
            CardTag cardTag = cardTagRepository.getByCardAndText(entity, tagText);
            cardTagRepository.delete(cardTag);
            entity.removeCardTag(cardTag);
        }

        for (String tagText : added) {
            Optional<Tag> tagOptional = tagRepository.findByText(tagText);

            Tag tag = tagOptional.orElseGet(() -> {
                Tag createdTag = new Tag(tagText);
                tagRepository.save(createdTag);
                return createdTag;
            });

            CardTag cardTag = CardTag.builder()
                    .user(user)
                    .tag(tag)
                    .card(entity)
                    .id(new CardTagPK(entity.getId(), tag.getId()))
                    .build();
            cardTagRepository.save(cardTag);
//            entity.addCardTag(cardTag);
//            tag.addCardTag(cardTag);
        }
        entity.setUpdated(LocalDateTime.now());
        cardRepository.save(entity);
    }

    @Transactional
    public CardDto getCardByHash(String hash) {
        Card card = cardRepository.getByGeneratedId(hash).orElseThrow();
        return cardMapper.cardEntityToDto(card);
    }

    @Transactional
    public void setTargetLangs(LangCodeDto dto, User user) {
        Set<LanguageEntity> languages = new HashSet<>();
        Arrays.stream(dto.getCodes()).forEach(
                code -> languages.add(
                        languageRepository.findByCode(Language.fromString(code)).orElseThrow()));
        user.setTargetLanguages(languages);
        userRepository.save(user);
    }

    @Transactional
    public void setFluentLangs(LangCodeDto dto, User user) {
        Set<LanguageEntity> languages = new HashSet<>();
        Arrays.stream(dto.getCodes()).forEach(
                code -> languages.add(
                        languageRepository.findByCode(Language.fromString(code)).orElseThrow()));
        user.setFluentLanguages(languages);
        userRepository.save(user);
    }

    @Transactional
    public List<CardDto> getUsersCardsOfLang(String code, User user) {
        LanguageEntity language = languageRepository.findByCode(Language.fromString(code)).orElseThrow();
        return cardRepository
                .findAllByOwnerAndLanguage(user, language)
                .stream()
                .map(cardMapper::cardEntityToDto)
                .collect(Collectors.toList());
    }
}