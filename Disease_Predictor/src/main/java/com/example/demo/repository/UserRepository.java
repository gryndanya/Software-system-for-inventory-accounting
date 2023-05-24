package com.example.demo.repository;

import com.example.demo.model.Stock;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail(String email);

    @Query(value = "SELECT c FROM User c WHERE c.firstName LIKE CONCAT('%',:keyword,'%') or c.lastName LIKE CONCAT('%',:keyword,'%')")
    List<User> search(@Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query("update User u set u.email = :email," +
            "u.firstName = :first_name," +
            "u.lastName = :last_name," +
            "u.password = :password," +
            "u.role = :role " +
            "where u.id = :id")
    void update(@Param("id") long id,
        @Param("email") String email,
        @Param("first_name") String first_name,
        @Param("last_name") String last_name,
        @Param("password") String password,
        @Param("role") String role);
}
