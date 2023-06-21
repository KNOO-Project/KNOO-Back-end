package com.woopaca.knoo.repository;

import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.value.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByPostCategory(PostCategory postCategory, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.writer = :user ORDER BY p.id DESC")
    Page<Post> findByWriter(@Param("user") User user, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN p.comments c " +
            "WHERE c.writer = :user ORDER BY p.id DESC")
    Page<Post> findByCommentWriter(@Param("user") User user, Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN p.postLikes pl " +
            "WHERE pl.user = :user ORDER BY pl.id DESC")
    Page<Post> findByLikeUser(@Param("user") User user, Pageable pageable);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<Post> findPostById(Long postId);

    @Query("SELECT p FROM Post p LEFT JOIN p.scraps s WHERE s.user = :user ORDER BY s.scrapDate DESC")
    Page<Post> findUserScrapPosts(@Param("user") User user, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postTitle LIKE %:keyword% OR p.postContent LIKE %:keyword%")
    Page<Post> searchByTitleAndContent(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postTitle LIKE %:keyword%")
    Page<Post> searchByTitle(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postContent LIKE %:keyword%")
    Page<Post> searchByContent(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE (p.postTitle LIKE %:keyword% OR p.postContent LIKE %:keyword%) " +
            "AND p.postCategory = :postCategory")
    Page<Post> searchByTitleAndContentInCategory(@Param("keyword") String keyword,
                                                 @Param("postCategory") PostCategory postCategory, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postTitle LIKE %:keyword% AND p.postCategory = :postCategory")
    Page<Post> searchByTitleInCategory(@Param("keyword") String keyword, @Param("postCategory") PostCategory postCategory,
                                       Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postContent LIKE %:keyword% AND p.postCategory = :postCategory")
    Page<Post> searchByContentInCategory(@Param("keyword") String keyword, @Param("postCategory") PostCategory postCategory,
                                         Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN p.scraps s WHERE s.user = :user " +
            "AND (p.postTitle LIKE %:keyword% OR p.postContent LIKE %:keyword%) " +
            "ORDER BY s.scrapDate DESC")
    Page<Post> searchByTitleAndContentInUserScrap(@Param("keyword") String keyword, @Param("user") User authenticatedUser,
                                                  PageRequest pageRequest);

    @Query("SELECT p FROM Post p LEFT JOIN p.scraps s WHERE s.user = :user " +
            "AND p.postTitle LIKE %:keyword% " +
            "ORDER BY s.scrapDate DESC")
    Page<Post> searchByTitleInUserScrap(@Param("keyword") String keyword, @Param("user") User authenticatedUser,
                                        PageRequest pageRequest);

    @Query("SELECT p FROM Post p LEFT JOIN p.scraps s WHERE s.user = :user " +
            "AND p.postContent LIKE %:keyword% " +
            "ORDER BY s.scrapDate DESC")
    Page<Post> searchByContentInUserScrap(@Param("keyword") String keyword, @Param("user") User authenticatedUser,
                                          PageRequest pageRequest);
}
