package com.woopaca.knoo.repository;

import com.woopaca.knoo.entity.Post;
import com.woopaca.knoo.entity.User;
import com.woopaca.knoo.entity.attr.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByPostCategory(PostCategory postCategory, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.writer = :user ORDER BY p.id DESC")
    List<Post> findByWriter(@Param("user") User user, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN p.comments c " +
            "WHERE c.writer = :user ORDER BY p.id DESC")
    List<Post> findByCommentWriter(@Param("user") User user, Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN p.postLikes pl " +
            "WHERE pl.user = :user ORDER BY pl.id DESC")
    List<Post> findByLikeUser(@Param("user") User user, Pageable pageable);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<Post> findPostById(Long postId);

    @Query("SELECT p FROM Post p LEFT JOIN p.scraps s WHERE s.user = :user ORDER BY s.scrapDate DESC")
    Page<Post> findUserScrapPosts(@Param("user") User user, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postTitle LIKE %:keyword% AND p.postContent LIKE %:keyword%")
    Page<Post> searchByTitleAndContent(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postTitle LIKE %:keyword%")
    Page<Post> searchByTitle(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postContent LIKE %:keyword%")
    Page<Post> searchByContent(@Param("keyword") String keyword, Pageable pageable);
}
