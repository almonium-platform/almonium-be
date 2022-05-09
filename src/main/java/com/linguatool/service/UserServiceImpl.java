package com.linguatool.service;

import com.linguatool.dto.LocalUser;
import com.linguatool.dto.SignUpRequest;
import com.linguatool.dto.SocialProvider;
import com.linguatool.exception.OAuth2AuthenticationProcessingException;
import com.linguatool.exception.UserAlreadyExistAuthenticationException;
import com.linguatool.model.Friend;
import com.linguatool.model.user.Friendship;
import com.linguatool.model.user.Role;
import com.linguatool.model.user.Status;
import com.linguatool.model.user.User;
import com.linguatool.repo.FriendshipRepository;
import com.linguatool.repo.RoleRepository;
import com.linguatool.repo.UserRepository;
import com.linguatool.security.oauth2.user.OAuth2UserInfo;
import com.linguatool.security.oauth2.user.OAuth2UserInfoFactory;
import com.linguatool.util.GeneralUtils;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.linguatool.model.user.Status.PENDING;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final FriendshipRepository friendshipRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, FriendshipRepository friendshipRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.friendshipRepository = friendshipRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(value = "transactionManager")
    public User registerNewUser(final SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException {
        if (signUpRequest.getUserID() != null && userRepository.existsById(signUpRequest.getUserID())) {
            throw new UserAlreadyExistAuthenticationException("User with User id " + signUpRequest.getUserID() + " already exist");
        } else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistAuthenticationException("User with email id " + signUpRequest.getEmail() + " already exist");
        }
        User user = buildUser(signUpRequest);
        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setModified(now);
        user = userRepository.save(user);
        userRepository.flush();
        return user;
    }


    private User buildFriend(final Long userFrien) {
        return null; //TODO
    }

    private User buildUser(final SignUpRequest formDTO) {
        User user = new User();
        user.setUsername(formDTO.getUsername());
        user.setEmail(formDTO.getEmail());
        user.setPassword(passwordEncoder.encode(formDTO.getPassword()));
        final HashSet<Role> roles = new HashSet<Role>();
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

    @Override
    @Transactional
    public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
        if (StringUtils.isEmpty(oAuth2UserInfo.getName())) {
            throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
        } else if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
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

    private SignUpRequest toUserRegistrationObject(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        return SignUpRequest.getBuilder().addProviderUserID(oAuth2UserInfo.getId()).addusername(oAuth2UserInfo.getName()).addEmail(oAuth2UserInfo.getEmail())
            .addSocialProvider(GeneralUtils.toSocialProvider(registrationId)).addPassword("changeit").build();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Collection<Long> getUsersFriendsIds(long id) {
        return friendshipRepository
            .findAllUsersFriendships(id).stream()
            .flatMap(r -> Stream.of(r.getRequester().getId(), r.getRequestee().getId()))
            .filter(r -> r != id)
            .collect(Collectors.toList());
    }

    public Collection<Friend> getUsersFriends(long id) {

        return this
            .getUsersFriendsIds(id)
            .stream()
            .map(userRepository::findAllById)
            .flatMap(Optional::stream)
            .collect(Collectors.toList());
    }

    public void cancelFriendship(Long requesterId, Long requesteeId) {
        friendshipRepository.deleteByUsersIds(requesterId, requesteeId);
    }

    @SneakyThrows
    public Friendship createFriendshipRequest(long actionInitiatorId, long actionRecipientId) {
        User user1 = userRepository.getById(actionInitiatorId);
        if (user1.isFriendshipRequestsBlocked()) {
            return null;
        }

        Optional<Friendship> friendshipOptional = this.getFriendshipByUsersIds(actionInitiatorId, actionRecipientId);
        if (friendshipOptional.isPresent()) {
            Friendship existingFriendship = friendshipOptional.get();

            if (Status.isBlocking(existingFriendship.getStatus())) {
                return null;
            }

            throw new Exception(String.format("Friendship between %s and %s already exists ",
                actionInitiatorId, actionRecipientId));
        }

        Friendship friendship = new Friendship();
        LocalDateTime now = LocalDateTime.now();
        friendship.setStatus(PENDING);
        friendship.setCreated(now);
        friendship.setUpdated(now);
        friendship.setRequesteeId(userRepository.getById(actionInitiatorId).getId());
        friendship.setRequesterId(userRepository.getById(actionRecipientId).getId());
        friendshipRepository.save(friendship);
        return friendship;
    }


    @SneakyThrows
    public Friendship confirmFriendshipRequest(long actionInitiatorId, long actionRecipientId) {
        Optional<Friendship> friendshipOptional = this.getFriendshipByUsersIds(actionInitiatorId, actionRecipientId);
        friendshipOptional.orElseThrow(() -> new FriendshipNotFoundException(String.format("Friendship of %s and %s not found", actionInitiatorId, actionRecipientId)));
        Friendship friendship = friendshipOptional.get();

        LocalDateTime now = LocalDateTime.now();
        friendship.setStatus(Status.FRIENDS);
        friendship.setUpdated(now);
        friendshipRepository.save(friendship);
        return friendship;
    }

    public Optional<Friendship> getFriendshipByUsersIds(long id1, long id2) {
        return friendshipRepository.getFriendshipByUsersIds(id1, id2);
    }

    @SneakyThrows
    public Friendship blockUser(long actionInitiatorId, long actionRecipientId) {
        Optional<Friendship> friendshipOptional = this.getFriendshipByUsersIds(actionInitiatorId, actionRecipientId);
        friendshipOptional.orElseThrow(() -> new Exception("hello"));
        Friendship friendship = friendshipOptional.get();
        LocalDateTime now = LocalDateTime.now();
        friendship.setUpdated(now);
//        friendship.setStatus(friendship.getRequesterId().equals(actionInitiatorId)
//            ? Status.FST_BLOCKED_SND
//            : Status.SND_BLOCKED_FST
//        );
        return friendship;
    }
}
