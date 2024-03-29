package com.obaidahmed.twitter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.obaidahmed.twitter.config.JwtProvider;
import com.obaidahmed.twitter.exception.UserException;
import com.obaidahmed.twitter.model.User;
import com.obaidahmed.twitter.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	public User findUserById(Long userId) throws UserException {
		User user = userRepository.findById(userId)
				.orElseThrow(()-> new UserException("User not found with is" +userId));
		return user;
	}
	
	public User findUserProfileByJwt(String jwt) throws UserException{
		try {
	        String email = jwtProvider.getEmailFromToken(jwt);
	        User user = userRepository.findByEmail(email);

	        if (user == null) {
	            throw new UserException("User not found with email: " + email + " for JWT: " + jwt);
	        }

	        return user;
	    } catch (Exception e) {
	        throw new UserException("Error processing JWT: " + e.getMessage());
	    }
	}
	
	public User updateUser(Long userId, User req) throws UserException {
		User user = findUserById(userId);
		
		if (req.getFullName()!=null) {
			user.setFullName(req.getFullName());	
		}
		if (req.getFullName()!=null) {
			user.setFullName(req.getFullName());	
		}
		if (req.getImage()!=null) {
			user.setImage(req.getImage());
		}
		if (req.getBackgroundImage()!=null) {
			user.setBackgroundImage(req.getBackgroundImage());
		}
		if (req.getBirthDate()!=null) {
			user.setBirthDate(req.getBirthDate());
		}
		if (req.getLocation()!=null) {
			user.setLocation(req.getLocation());
		}
		if (req.getBio()!=null) {
			user.setBio(req.getBio());
		}
		if(req.getWebsite()!=null) {
			user.setWebsite(req.getWebsite());
		}
		
		return userRepository.save(user);
		
	}
	
	public User followUser(long userId, User user) throws UserException {
		User followToUser = findUserById(userId);
		if(user.getFollowings().contains(followToUser)&&followToUser.getFollowers().contains(user)) {
			user.getFollowings().remove(followToUser);
			followToUser.getFollowers().remove(user);
		} else {
			user.getFollowings().add(followToUser);
			followToUser.getFollowers().add(user);
		}
		userRepository.save(followToUser);
		userRepository.save(user);
		return followToUser;
	}
	
	public List<User> searchUser(String query) {
		return userRepository.searchUser(query);
	}

}
