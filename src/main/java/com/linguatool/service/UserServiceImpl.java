package com.linguatool.service;

import com.linguatool.configuration.security.oauth2.user.OAuth2UserInfo;
import com.linguatool.configuration.security.oauth2.user.OAuth2UserInfoFactory;
import com.linguatool.exception.user.FriendshipNotAllowedException;
import com.linguatool.exception.user.FriendshipNotFoundException;
import com.linguatool.exception.user.OAuth2AuthenticationProcessingException;
import com.linguatool.exception.user.UserAlreadyExistAuthenticationException;
import com.linguatool.model.dto.Friend;
import com.linguatool.model.dto.FriendInfo;
import com.linguatool.model.dto.FriendshipCommandDto;
import com.linguatool.model.dto.LocalUser;
import com.linguatool.model.dto.SignUpRequest;
import com.linguatool.model.dto.SocialProvider;
import com.linguatool.model.dto.api.request.CardCreationDto;
import com.linguatool.model.dto.api.request.CardDto;
import com.linguatool.model.entity.lang.Card;
import com.linguatool.model.entity.lang.Example;
import com.linguatool.model.entity.lang.Translation;
import com.linguatool.model.entity.user.Friendship;
import com.linguatool.model.entity.user.FriendshipStatus;
import com.linguatool.model.entity.user.Role;
import com.linguatool.model.entity.user.Tag;
import com.linguatool.model.entity.user.User;
import com.linguatool.model.mapping.CardMapper;
import com.linguatool.repository.CardRepository;
import com.linguatool.repository.ExampleRepository;
import com.linguatool.repository.FriendshipRepository;
import com.linguatool.repository.LanguageRepository;
import com.linguatool.repository.RoleRepository;
import com.linguatool.repository.TagRepository;
import com.linguatool.repository.TranslationRepository;
import com.linguatool.repository.UserRepository;
import com.linguatool.util.GeneralUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

    FriendshipRepository friendshipRepository;

    PasswordEncoder passwordEncoder;

    CardMapper cardMapper;
    ExampleRepository exampleRepository;
    CardRepository cardRepository;
    TranslationRepository translationRepository;
    TagRepository tagRepository;
    LanguageRepository languageRepository;

    @Override
    @Transactional(value = "transactionManager")
    public User registerNewUser(final SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException {
        if (signUpRequest.getUserID() != null && userRepository.existsById(signUpRequest.getUserID())) {
            throw new UserAlreadyExistAuthenticationException("User with User id " + signUpRequest.getUserID() + " already exist");
        } else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistAuthenticationException("User with email id " + signUpRequest.getEmail() + " already exist");
        } else if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UserAlreadyExistAuthenticationException("User with username " + signUpRequest.getUsername() + " already exist");
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

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setUsername(oAuth2UserInfo.getName());
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
        card.setModified(LocalDateTime.now());

        List<Example> examples = card.getExamples();
        examples.forEach(e -> e.setCard(card));

        List<Translation> translations = card.getTranslations();
        translations.forEach(t -> t.setCard(card));

        Set<Tag> tags = card.getTags();
        tags.forEach(t -> {
            Optional<Tag> tagOptional = tagRepository.findByText(t.normalizeText());
            if (tagOptional.isPresent()) {
                Tag existingTag = tagOptional.get();
                t.setId(existingTag.getId());
                t.setCards(existingTag.getCards());
            } else {
                t.setCards(new HashSet<>());
            }
            t.getCards().add(card);
            card.getTags().add(t);
            user.getTags().add(t);
        });

        user.addCard(card);
        cardRepository.save(card);
        translationRepository.saveAll(translations);
        if (!examples.isEmpty()) {
            exampleRepository.saveAll(examples);
        }
        if (!tags.isEmpty()) {
            tagRepository.saveAll(tags);
        }
        userRepository.save(user);
        log.info("Created card {} for user {}", card, user);
    }

    private SignUpRequest toUserRegistrationObject(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        return SignUpRequest.builder()
                .providerUserId(oAuth2UserInfo.getId())
                .username(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
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

    public List<CardDto> getUsersCards(User user) {
        return cardRepository.findAllByOwner(user).stream().map(cardMapper::cardEntityToDto).collect(Collectors.toList());
    }

    @SneakyThrows
    @Transactional
    public Friendship cancelFriendship(Long actionInitiatorId, Long actionAcceptorId) {
        assert (!actionInitiatorId.equals(actionAcceptorId));
        friendshipRepository.getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId).orElseThrow(()
                -> new FriendshipNotFoundException(String.format("Friendship between %s and %s doesn't exist", actionInitiatorId, actionInitiatorId)));

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
        friendshipOptional.orElseThrow(() -> new FriendshipNotFoundException(String.format("Friendship of %s and %s not found", actionInitiatorId, actionAcceptorId)));

        Friendship friendship = friendshipOptional.get();
        assert friendship.getFriendshipStatus().equals(PENDING);

        LocalDateTime now = LocalDateTime.now();
        friendship.setFriendshipStatus(FriendshipStatus.FRIENDS);
        friendship.setUpdated(now);
        friendshipRepository.save(friendship);
        return friendship;
    }

    @SneakyThrows
    public Friendship blockUser(long actionInitiatorId, long actionAcceptorId) {
        assert (actionInitiatorId != actionAcceptorId);

        Optional<Friendship> friendshipOptional = friendshipRepository.getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId);
        friendshipOptional.orElseThrow(() -> new FriendshipNotFoundException(""));
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

    public List<CardDto> searchByEntry(String entry, User user) {
        return cardRepository.findAllByEntryLikeAndOwner(entry, user)
                .stream().map(cardMapper::cardEntityToDto).collect(Collectors.toList());
    }
}
