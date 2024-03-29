package com.woopaca.knoo.controller;

import com.woopaca.knoo.annotation.SignIn;
import com.woopaca.knoo.controller.dto.auth.SignInUser;
import com.woopaca.knoo.controller.dto.post.PostDetailsResponseDto;
import com.woopaca.knoo.controller.dto.post.PostLikeResponseDto;
import com.woopaca.knoo.controller.dto.post.PostListResponseDto;
import com.woopaca.knoo.controller.dto.post.PostPreviewResponseDto;
import com.woopaca.knoo.controller.dto.post.PostScrapResponseDto;
import com.woopaca.knoo.controller.dto.post.PostSearchRequestDto;
import com.woopaca.knoo.controller.dto.post.UpdatePostRequestDto;
import com.woopaca.knoo.controller.dto.post.WritePostRequestDto;
import com.woopaca.knoo.entity.value.PostCategory;
import com.woopaca.knoo.service.PostService;
import com.woopaca.knoo.service.impl.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@Slf4j
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<List<PostPreviewResponseDto>> postPreviewList() {
        List<PostPreviewResponseDto> postPreviewList = postService.getPostPreviewList();
        return ResponseEntity.ok().body(postPreviewList);
    }

    @PostMapping
    public ResponseEntity<Long> writeNewPost(
            @SignIn final SignInUser signInUser,
            @RequestBody @Valid final WritePostRequestDto writePostRequestDto
    ) {
        Long postId = postService.writePost(signInUser, writePostRequestDto);
        return ResponseEntity.created(URI.create("/api/posts/" + postId)).body(postId);
    }

    @GetMapping("/{category}")
    public ResponseEntity<PostListResponseDto> writtenPostList(
            @PathVariable("category") final PostCategory postCategory,
            @RequestParam("page") @Positive(message = "유효하지 않은 페이지입니다.") final int page
    ) {
        PostListResponseDto postListResponseDto = postService.postList(postCategory, page - 1);
        return ResponseEntity.ok().body(postListResponseDto);
    }

    @GetMapping("/{category}/{postId}")
    public ResponseEntity<PostDetailsResponseDto> postDetailsInfo(
            @SignIn final SignInUser signInUser,
            @PathVariable("postId") final Long postId, @PathVariable("category") String ignore
    ) {
        PostDetailsResponseDto postDetails = postService.postDetails(signInUser, postId);
        return ResponseEntity.ok().body(postDetails);
    }

    @PutMapping
    public ResponseEntity<String> updatePostContents(
            @SignIn final SignInUser signInUser,
            @RequestParam("post_id") final Long postId,
            @RequestBody @Valid final UpdatePostRequestDto updatePostRequestDto
    ) {
        postService.updatePost(signInUser, postId, updatePostRequestDto);
        return ResponseEntity.ok().body("게시글 수정이 완료되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteOnePost(
            @SignIn final SignInUser signInUser,
            @RequestParam("post_id") final Long postId
    ) {
        postService.deletePost(signInUser, postId);
        return ResponseEntity.ok().body("게시글 삭제가 완료되었습니다.");
    }

    @PostMapping("/likes")
    public ResponseEntity<PostLikeResponseDto> likeOrCancelLikePost(
            @SignIn final SignInUser signInUser,
            @RequestParam("post_id") final Long postId
    ) {
        PostLikeResponseDto postLikeResponseDto = postService.changePostLike(signInUser, postId);
        return ResponseEntity.ok().body(postLikeResponseDto);
    }

    @PostMapping("/scraps")
    public ResponseEntity<PostScrapResponseDto> scrapOrCancelScrapPost(
            @SignIn final SignInUser signInUser,
            @RequestParam("post_id") final Long postId
    ) {
        PostScrapResponseDto postScrapResponseDto = postService.changePostScrap(signInUser, postId);
        return ResponseEntity.ok().body(postScrapResponseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<PostListResponseDto> searchPostsAllOrSpecificCategory(
            @Valid final PostSearchRequestDto postSearchRequestDto) {
        PostListResponseDto postListResponseDto = postService.searchPosts(postSearchRequestDto);
        return ResponseEntity.ok().body(postListResponseDto);
    }

    @PostMapping("/images")
    public ResponseEntity<String> uploadPostImages(
            @SignIn SignInUser signInUser,
            @RequestPart(name = "post_images") final List<MultipartFile> postImageFiles,
            @RequestParam(name = "post_id") final Long postId
    ) {
        postService.uploadPostImageFiles(postId, postImageFiles, signInUser);
        return ResponseEntity.ok().body("게시글 사진이 등록되었습니다.");
    }

    @GetMapping(value = "/images/{imageName}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> postImage(@PathVariable("imageName") final String imageName) {
        byte[] imageBytes = imageService.getPostImageBytes(imageName);
        return ResponseEntity.ok().body(imageBytes);
    }
}
