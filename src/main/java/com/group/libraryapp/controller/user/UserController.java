package com.group.libraryapp.controller.user;

import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final JdbcTemplate jdbcTemplate;

    public UserController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/user")
    public void saveUser(@RequestBody UserCreateRequest request) {
        String sql = "insert into user (name, age) values(?, ?)";
        jdbcTemplate.update(sql, request.getName(), request.getAge());
    }

    @GetMapping("/user")
    public List<UserResponse> getUsers() {
        String sql = "select * from user";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            return new UserResponse(id, name, age);
        });
    }

    @PutMapping("/user")
    public void updateUser(@RequestBody UserUpdateRequest request) {
        String readSql = "select * from user where id = ?";
        // 데이터가 존재하면 0, 존재하지 않으면 비어있음
        boolean isUserNotExist = jdbcTemplate.query(readSql, (rs, rowNum) -> 0, request.getId()).isEmpty();

        if (isUserNotExist) {
            throw new IllegalArgumentException();
        }

        String sql = "update user set name = ? where id = ?";
        jdbcTemplate.update(sql, request.getName(), request.getId());
    }

    @DeleteMapping("/user")
    public void deleteUser(@RequestParam String name) {
        String readSql = "select * from user where name = ?";
        boolean isUserNotExist = jdbcTemplate.query(readSql, (rs, rowNum) -> 0, name).isEmpty();

        if (isUserNotExist) {
            throw new IllegalArgumentException();
        }

        String sql = "delete from user where name = ?";
        jdbcTemplate.update(sql, name);
    }
}
