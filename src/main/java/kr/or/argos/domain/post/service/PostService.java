package kr.or.argos.domain.post.service;

import java.util.List;
import kr.or.argos.domain.post.dto.PostCreation;
import kr.or.argos.domain.post.entity.Post;
import kr.or.argos.domain.post.repository.PostRepository;
import kr.or.argos.domain.post.util.IdentifierUtil;
import kr.or.argos.domain.user.entity.User;
import kr.or.argos.domain.user.service.UserService;
import kr.or.argos.global.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserService userService;

  @Transactional
  public Post createPost(String remoteUser, PostCreation request) {
    User user = userService.getUser(remoteUser);
    Post post = Post.builder()
        .title(request.getTitle())
        .content(request.getContent())
        .user(user)
        .announcement(request.isAnnouncement())
        .build();
    while (post.getIdentifier() == null || postRepository.existsByIdentifier(
        post.getIdentifier())) {
      post.setIdentifier(IdentifierUtil.generateIdentifier(8));
    }
    return postRepository.save(post);
  }

  @Transactional(readOnly = true)
  public List<Post> getPosts(int page, int size) {
    return postRepository.findAll(PageRequest.of(page, size, Sort.by(Direction.DESC, "createdAt")))
        .getContent();
  }

  @Transactional(readOnly = true)
  public Post getPost(String postId) {
    return postRepository.findByIdentifier(postId)
        .orElseThrow(() -> new InvalidRequestException("Post id " + postId + " does not exist"));
  }

  @Transactional(readOnly = true)
  public List<Post> getAnnouncements() {
    return postRepository.findAllByAnnouncementIsTrue();
  }

  @Transactional
  public Post updateContent(String remoteUser, String postId, String content) {
    Post post = getPost(postId);
    if (!post.getUser().getUsername().equals(remoteUser)) {
      throw new InvalidRequestException("You are not the author of this post");
    } else {
      post.setContent(content);
      return postRepository.save(post);
    }
  }

  @Transactional
  public Post updateHits(String remoteUser, String postId) {
    User user = userService.getUser(remoteUser);
    Post post = getPost(postId);
    if (post.getHits().contains(user)) {
      post.getHits().remove(user);
    } else {
      post.getHits().add(user);
    }
    return postRepository.save(post);
  }

  @Transactional
  public void deletePost(String remoteUser, String postId) {
    Post post = getPost(postId);
    if (!post.getUser().getUsername().equals(remoteUser)) {
      throw new InvalidRequestException("You are not the author of this post");
    } else {
      postRepository.delete(getPost(postId));
    }
  }
}