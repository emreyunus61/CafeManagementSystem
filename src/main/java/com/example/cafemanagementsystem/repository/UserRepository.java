package com.example.cafemanagementsystem.repository;

import com.example.cafemanagementsystem.dto.UserDto;
import com.example.cafemanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByEmailId(@Param("email") String email);

    List<UserDto> getAllUser();

    @Transactional
    @Modifying // Bu annotasyon, metotun veritabanında bir değişiklik gerçekleştirdiğini belirtir. Genellikle güncelleme, silme vb.
    Integer updateStatus(@Param("status") String status,@Param("id") Integer id);

    List<String> getAllAdmin();
    User findByEmail(String email);
}
