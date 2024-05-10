package kaem0n.meetoo.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kaem0n.meetoo.entities.Board;
import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.entities.UserFollow;
import kaem0n.meetoo.enums.UserGender;
import kaem0n.meetoo.exceptions.BadRequestException;
import kaem0n.meetoo.exceptions.NotFoundException;
import kaem0n.meetoo.exceptions.UnauthorizedException;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.user.*;
import kaem0n.meetoo.repositories.BoardDAO;
import kaem0n.meetoo.repositories.UserDAO;
import kaem0n.meetoo.repositories.UserFollowDAO;
import kaem0n.meetoo.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserDAO ud;
    @Autowired
    private BoardDAO bd;
    @Autowired
    private UserFollowDAO ufd;
    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    private JWTTools tools;
    @Autowired
    private Cloudinary c;

    public User register(UserRegistrationDTO payload) {
        if (ud.existsByEmail(payload.email())) throw new BadRequestException("Email " + payload.email() + " is already being used.");
        else if (ud.existsByUsername(payload.username())) throw new BadRequestException("Username " + payload.username() + " is not available.");
        else {
            User newUser = ud.save(new User(payload.email(), payload.username(), bcrypt.encode(payload.password())));
            Board board = bd.save(new Board());
            newUser.setBoard(board);
            return ud.save(newUser);
        }
    }

    public UserLoginResponseDTO login(UserLoginDTO payload) {
        User found = this.findByUsername(payload.username());
        if (bcrypt.matches(payload.password(), found.getPassword())) return new UserLoginResponseDTO(tools.createToken(found));
        else throw new UnauthorizedException("Invalid credentials, try again.");
    }

    public User findById(UUID id) {
        return ud.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public User findByUsername(String username) {
        return ud.findByUsername(username).orElseThrow(() -> new NotFoundException("Invalid credentials, try again."));
    }

    public Page<User> findAll(int page, int size, String sort) {
        if (size > 50) size = 50;
        Pageable p = PageRequest.of(page, size, Sort.by(sort));
        return ud.findAll(p);
    }

    public User updateInfo(UUID id, UserInfoUpdateDTO payload) {
        User found = this.findById(id);

        if (payload.name() != null) found.setName(payload.name());
        if (payload.surname() != null) found.setSurname(payload.surname());
        if (payload.birthday() != null) found.setBirthday(LocalDate.parse(payload.birthday()));
        if (payload.proPicUrl() != null) found.setProPicUrl(payload.proPicUrl());
        if (payload.gender() != null) found.setGender(UserGender.valueOf(payload.gender()));
        if (payload.occupation() != null) found.setOccupation(payload.occupation());
        if (payload.bio() != null) found.setBio(payload.bio());
        if (!payload.hobbies().isEmpty()) found.setHobbies(payload.hobbies());

        return ud.save(found);
    }

    public GenericResponseDTO changeEmail(UUID id, UserEmailChangeDTO payload) {
        User found = this.findById(id);

        if (ud.existsByEmail(payload.email()) && !Objects.equals(found.getEmail(), payload.email())) throw new BadRequestException("Email " + payload.email() + " is already being used.");
        else if (!ud.existsByEmail(payload.email()) && !Objects.equals(found.getEmail(), payload.email())) found.setEmail(payload.email());

        ud.save(found);

        return new GenericResponseDTO("Email successfully changed.");
    }

    public GenericResponseDTO changeUsername(UUID id, UserUsernameChangeDTO payload) {
        User found = this.findById(id);

        if (ud.existsByUsername(payload.username()) && !Objects.equals(found.getUsername(), payload.username())) throw new BadRequestException("Username " + payload.username() + " is not available.");
        else if (!ud.existsByUsername(payload.username()) && !Objects.equals(found.getUsername(), payload.username())) found.setUsername(payload.username());

        ud.save(found);

        return new GenericResponseDTO("Username successfully changed.");
    }

    public GenericResponseDTO changeMyPassword(UUID id, UserPasswordChangeDTO payload) {
        User found = this.findById(id);

        if (bcrypt.matches(found.getPassword(), payload.oldPassword())) found.setPassword(bcrypt.encode(payload.newPassword()));

        ud.save(found);

        return new GenericResponseDTO("Password successfully changed.");
    }

    public GenericResponseDTO changePassword(UUID id, AdminPasswordChangeDTO payload) {
        User found = this.findById(id);

        found.setPassword(bcrypt.encode(payload.newPassword()));

        ud.save(found);

        return new GenericResponseDTO("Password successfully changed.");
    }

    public void deleteAccount(UUID id) {
        User found = this.findById(id);

        bd.delete(found.getBoard());
        ud.delete(found);
    }

    public GenericResponseDTO handlePlatformBan(UUID id) {
        User found = this.findById(id);

        found.setAccountNonLocked(!found.isAccountNonLocked());
        ud.save(found);

        if (!found.isAccountNonLocked()) return new GenericResponseDTO("User ID '" + id + "' account permanently banned from the platform.");
        else return new GenericResponseDTO("User ID '" + id + "' account account lock has been lifted.");
    }

    public List<User> findBySearchQuery(String query) {
        return ud.searchUsers(query);
    }

    public GenericResponseDTO changeProPic(UUID id, MultipartFile img) throws IOException {
        User found = this.findById(id);
        String defaultProPic = "https://res.cloudinary.com/kaem0n/image/upload/v1714550501/default_user_icon_nm5w0s.png";
        String proPic = found.getProPicUrl();

        if (Objects.equals(proPic, defaultProPic)) {
            String url = (String) c.uploader().upload(img.getBytes(), ObjectUtils.emptyMap()).get("url");
            found.setProPicUrl(url);
        } else {
            String imageID = proPic.substring(proPic.lastIndexOf("/") + 1, proPic.lastIndexOf("."));
            c.uploader().destroy(imageID, ObjectUtils.emptyMap());
            String url = (String) c.uploader().upload(img.getBytes(), ObjectUtils.emptyMap()).get("url");
            found.setProPicUrl(url);
        }

        ud.save(found);
        return new GenericResponseDTO("Profile picture successfully updated.");
    }

    public GenericResponseDTO removeProPic(UUID id) throws IOException {
        User found = this.findById(id);
        String defaultProPic = "https://res.cloudinary.com/kaem0n/image/upload/v1714550501/default_user_icon_nm5w0s.png";
        String proPic = found.getProPicUrl();

        if (!Objects.equals(proPic, defaultProPic)) {
            String imageID = proPic.substring(proPic.lastIndexOf("/") + 1, proPic.lastIndexOf("."));
            c.uploader().destroy(imageID, ObjectUtils.emptyMap());
            found.setProPicUrl(defaultProPic);

            ud.save(found);
            return new GenericResponseDTO("Profile picture successfully removed.");
        } else throw new NotFoundException("Custom profile picture not found.");
    }

    public UserFollow findFollow(UUID followerID, UUID followedID) {
        User follower = this.findById(followerID);
        User followed = this.findById(followedID);

        return ufd.findByFollowerAndFollowed(follower, followed)
                .orElseThrow(() -> new NotFoundException(follower.getUsername() + " doesn't follow " + followed.getUsername() + "."));
    }

    public GenericResponseDTO handleFollow(UUID followerID, UUID followedID) {
        if (!Objects.equals(followerID.toString(), followedID.toString())) {
            User follower = this.findById(followerID);
            User followed = this.findById(followedID);
            List<UserFollow> follows = follower.getFollowingList();
            boolean isAlreadyFollowing = false;

            for (UserFollow follow : follows) {
                UUID followedUserID = follow.getFollowed().getId();
                if (Objects.equals(followedUserID.toString(), followedID.toString())) {
                    isAlreadyFollowing = true;
                    break;
                }
            }

            if (isAlreadyFollowing) {
                ufd.delete(this.findFollow(followerID, followedID));
                return new GenericResponseDTO("You stopped following " + followed.getUsername() + ".");
            } else {
                ufd.save(new UserFollow(follower, followed));
                return new GenericResponseDTO("You are now following " + followed.getUsername() + ".");
            }
        } else throw new BadRequestException("You can't follow yourself.");
    }

    public List<UserEssentialsDTO> getFollowingList(UUID id) {
        User found = this.findById(id);
        List<UserFollow> follows = found.getFollowingList();
        List<UserEssentialsDTO> followedUsersData = new ArrayList<>();

        for (UserFollow follow : follows) {
            User followedUser = follow.getFollowed();
            followedUsersData.add(new UserEssentialsDTO(followedUser.getId(), followedUser.getUsername(), followedUser.getProPicUrl()));
        }

        return followedUsersData;
    }

    public List<UserEssentialsDTO> getFollowedByList(UUID id) {
        User found = this.findById(id);
        List<UserFollow> follows = found.getFollowedByList();
        List<UserEssentialsDTO> followerUsersData = new ArrayList<>();

        for (UserFollow follow : follows) {
            User follower = follow.getFollower();
            followerUsersData.add(new UserEssentialsDTO(follower.getId(), follower.getUsername(), follower.getProPicUrl()));
        }

        return followerUsersData;
    }
}
