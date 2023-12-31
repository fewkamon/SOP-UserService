package com.example.demo.controller;
import com.example.demo.pojo.*;
import com.example.demo.repository.JwtService;
import com.example.demo.repository.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/api")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @RequestMapping(value ="/login", method = RequestMethod.POST)
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO wz) {
        System.out.println(wz.getEmail());
        System.out.println(wz.getPassword());
        User data = this.userService.CheckLogin(wz.getEmail(), wz.getPassword());
        if (data == null) {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", "error");
            responseData.put("message", "อีเมลหรือรหัสผ่านไม่ถูกต้อง");
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(responseData);
        } else {
            String token = jwtService.generateToken(data);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", "success");
            responseData.put("message", "อันนี้คือเข้าสู่ระบบแล้ว");
            responseData.put("token", token);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseData);
        }
    }

    @RequestMapping(value ="/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody String requestBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            RegisterDTO registerDTO = objectMapper.readValue(requestBody, RegisterDTO.class);
            String lastName = registerDTO.getInfo().getLast_name();
            String firstName = registerDTO.getInfo().getFirst_name();
            System.out.println(firstName);
            Date currentDate = new Date();

            User data = userService.findUserWithEmail(registerDTO.getEmail());
            if (data == null) {
                User wzNew = new User(null, registerDTO.getEmail(), registerDTO.getPassword(), false, currentDate, currentDate, registerDTO.getInfo());
                userService.addUser(wzNew);
                User ddddd = this.userService.CheckLogin(registerDTO.getEmail(), registerDTO.getPassword());
                String token = jwtService.generateToken(ddddd);

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("status", "success");
                responseData.put("message", "สมัครสำเร็จ");
                responseData.put("token", token);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseData);
            } else {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("status", "error");
                responseData.put("message", "มีคนสมัครอีเมลนี้ไปแล้ว");
                return ResponseEntity.status(409).contentType(MediaType.APPLICATION_JSON).body(responseData);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", "error");
            responseData.put("message", "เกิดข้อผิดพลาดจากระบบ");
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(responseData);

        }
    }

    @RequestMapping(value ="/info", method = RequestMethod.PUT)
    public Optional<User> changeInfoUser(HttpServletRequest request, @RequestBody User wz) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String[] token = authorizationHeader.split(" ");
            Claims claims = jwtService.parseToken(token[1]);
            Optional<User> user = this.userService.Me(claims.getSubject().toString());
            User userupdate = user.get();
            userupdate.setInfo(wz.getInfo());
            this.userService.updateUser(userupdate);
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value ="/me", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null) {
                String[] token = authorizationHeader.split(" ");
                if (token.length > 1) {
                    Claims claims = jwtService.parseToken(token[1]);

                    Optional<User> userOptional = userService.Me(claims.getSubject().toString());
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();

                        ObjectMapper objectMapper = new ObjectMapper();
                        String userJson = objectMapper.writeValueAsString(user);

                        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(userJson);
                    } else {
                        return ResponseEntity.status(404).body("User not found");
                    }
                }
            }
            return ResponseEntity.status(401).body("Invalid token");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    @RequestMapping(value ="/user/{id}", method = RequestMethod.GET)
    public Optional<User> getUserWithId(@PathVariable("id") String id, HttpServletRequest request) {
        try {
            System.out.println(id);
            return this.userService.Me(id.toString());
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value ="/me", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null) {
                String[] token = authorizationHeader.split(" ");
                if (token.length > 1) {
                    Claims claims = jwtService.parseToken(token[1]);
                    this.userService.deleteUser(claims.getSubject().toString());
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("status", "success");
                    responseData.put("message", "ทำการลบสมาชิกเรียบร้อย");
                    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseData);
                }
            }
            return ResponseEntity.status(401).body("Invalid token");
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", "error");
            responseData.put("message", "ไม่สามารถทำการลบได้");
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(responseData);
        }




    }

}
