package com.app.vasyBus.repository;

import com.app.vasyBus.dto.admin.UserManagementDTO;
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

    @Query(value = """
        SELECT
            u.user_id AS userId,
            u.name AS name,
            u.email AS email,
            u.phone AS phone,
            u.age AS age,
            u.role AS role,
            COUNT(b.booking_id)  AS totalBookings,
            COUNT(CASE WHEN b.booking_status = 'CONFIRMED' THEN 1 END) AS confirmedBookings,
            COUNT(CASE WHEN b.booking_status = 'CANCELLED' THEN 1 END) AS cancelledBookings,
            u.created_at                                            AS createdAt
        FROM app_users u
        LEFT JOIN bookings b ON b.user_id = u.user_id AND b.is_deleted = false
        GROUP BY u.user_id, u.name, u.email, u.phone, u.age, u.role, u.created_at
        ORDER BY u.created_at DESC
        """, nativeQuery = true)
    java.util.List<UserManagementDTO> findAllUsersWithStats();
}
