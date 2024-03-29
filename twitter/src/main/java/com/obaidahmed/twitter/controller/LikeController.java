package com.obaidahmed.twitter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obaidahmed.twitter.dto.LikeDto;
import com.obaidahmed.twitter.exception.UserException;
import com.obaidahmed.twitter.mapper.LikeDtoMapper;
import com.obaidahmed.twitter.model.Like;
import com.obaidahmed.twitter.model.User;
import com.obaidahmed.twitter.service.LikeService;
import com.obaidahmed.twitter.service.UserService;

@RestController
@RequestMapping("/api")
public class LikeController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LikeService likeService;
	
	@PostMapping("/{tweetId}/likes")
	public ResponseEntity<LikeDto> likeTweet(@PathVariable long tweetId, 
			@RequestHeader("Authorization") String jwt) throws UserException{
		
		User user = userService.findUserProfileByJwt(jwt);
		Like like = likeService.likeTweet(tweetId, user);
		
		LikeDto likeDto = LikeDtoMapper.toLikeDto(like, user);
		
		return new ResponseEntity<LikeDto>(likeDto, HttpStatus.CREATED);
		
	}
	
	@GetMapping("/tweet/{tweetId}")
	public ResponseEntity<List<LikeDto>> getAllLikes(@PathVariable long tweetId, 
			@RequestHeader("Authorization") String jwt) throws UserException{
		
		User user = userService.findUserProfileByJwt(jwt);
		List<Like>  like= likeService.getAllLikes(tweetId);

		
		List<LikeDto> likeDto = LikeDtoMapper.toLikeDtos(like, user);
		
		return new ResponseEntity<>(likeDto, HttpStatus.CREATED);
		
	}
	
	

}
