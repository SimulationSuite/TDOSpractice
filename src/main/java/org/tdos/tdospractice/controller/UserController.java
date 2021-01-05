package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.config.TDOSSessionListener;
import org.tdos.tdospractice.entity.UserEntity;
import org.tdos.tdospractice.service.ClassService;
import org.tdos.tdospractice.service.SecurityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.service.UserService;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.utils.OnlineStudent;

import static org.tdos.tdospractice.utils.Constants.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClassService classService;

    @Autowired
    private SecurityService securityService;

    @GetMapping(value = "/test")
    public void test() {
        System.out.println(userService.findAll());
    }

    @PostMapping(value = "/login")
    public Response<Map<String, Object>> login(@RequestParam(value = "id") String id,
                                         @RequestParam(value = "password") String password, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        UserEntity user = userService.findUserByIdAndPwd(id,password);
        if(user == null){
            return Response.error("用户名或密码错误");
        }map.put("user_id",user.getId());
        map.put("role_id",user.getRoleID());
        session.setAttribute("user_id",user.getId());
        return Response.success(map);
    }

    @PostMapping("/createToken")
    public Response<String> createToken(@RequestParam(value = "id") String id) {
        return Response.success(securityService.createJWT(id));
    }

    @GetMapping("/online")
    public Response<Integer> online() {
        List<HttpSession> sessions = TDOSSessionListener.sessions.values().stream().filter(x -> !StringUtils.isEmpty(x.getAttribute("user_id"))).collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(x -> ((String) x.getAttribute("user_id"))))), ArrayList::new)
        );
        List<String> ids = sessions.stream()
                .map(session -> (String) session.getAttribute("user_id")).collect(Collectors.toList());
        return Response.success(ids.size());
    }

    @GetMapping("/online_user")
    public Response<List<OnlineStudent>> onlineUser() {
        List<HttpSession> sessions = TDOSSessionListener.sessions.values().stream().filter(x -> !StringUtils.isEmpty(x.getAttribute("user_id"))).collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(x -> ((String) x.getAttribute("user_id"))))), ArrayList::new)
        );
        List<String> ids = sessions.stream()
                .map(session -> (String) session.getAttribute("user_id")).collect(Collectors.toList());
        return Response.success(userService.getOnlineStudents(ids));
    }

    @GetMapping(value = "/search_user")
    public Response<List> searchUser(@RequestParam(value = "search", required = false) String search,
                             @RequestHeader(JWT_HEADER_KEY)String jwt) {
        String ownerID = securityService.getUserId(jwt);
        List list = userService.searchUser(ownerID, search);
        return Response.success(list);
    }

    @DeleteMapping(value = "/delete_user")
    public Response<String> deleteUser(@RequestBody List<String> list,
                                       @RequestHeader(JWT_HEADER_KEY) String jwt) {
        String ownerID = securityService.getUserId(jwt);
        return Response.success(userService.deleteUser(list, ownerID));
    }
}

