package com.app.vasyBus.repository;

import com.app.vasyBus.dto.user.ProfileResponseDTO;
import com.app.vasyBus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {
    Optional<User> findByEmail(String email);
    @Query(value = """
        SELECT
            u.user_id    AS userId,
            u.name       AS name,
            u.email      AS email,
            u.phone      AS phone,
            u.age        AS age,
            u.role       AS role,
            u.created_at AS createdAt
        FROM app_users u
        WHERE u.user_id = :userId
        """, nativeQuery = true)
    ProfileResponseDTO findProfileByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE app_users
        SET name = :name, phone = :phone
        WHERE user_id = :userId
        """, nativeQuery = true)
    void updateProfile(@Param("userId") Long userId,
                       @Param("name") String name,
                       @Param("phone") String phone);

    @Query(value = "SELECT COUNT(*) > 0 FROM app_users WHERE phone = :phone AND user_id != :userId",
            nativeQuery = true)
    boolean existsByPhoneAndNotUserId(@Param("phone") String phone, @Param("userId") Long userId);
}
