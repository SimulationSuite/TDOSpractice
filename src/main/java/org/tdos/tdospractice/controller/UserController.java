package org.tdos.tdospractice.controller;

import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.body.DeleteIdList;
import org.tdos.tdospractice.config.TDOSSessionListener;
import org.tdos.tdospractice.entity.ClassEntity;
import org.tdos.tdospractice.entity.UserEntity;
import org.tdos.tdospractice.mapper.ClassMapper;
import org.tdos.tdospractice.mapper.UserMapper;
import org.tdos.tdospractice.service.SecurityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.service.UserService;
import org.tdos.tdospractice.body.ModifyUser;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.body.UpdateUserPas;
import org.tdos.tdospractice.utils.OnlineStudent;
import org.tdos.tdospractice.utils.PageTool;

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
    private ClassMapper classMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SecurityService securityService;

    @GetMapping(value = "/test")
    public void test(@RequestBody ClassEntity classEntity) {
        int a = classMapper.insertClassByUser(classEntity);
        String aa = classEntity.getId();
        System.out.println(aa);
    }

    @PostMapping(value = "/login")
    public Response<Map<String, Object>> login(@RequestParam(value = "id") String id,
                                         @RequestParam(value = "password") String password, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        UserEntity user = userMapper.findUserByIdAndPwd(id,password);
        if(user == null){
            return Response.error("用户名或密码错误");
        }map.put("user_id",user.getId());
        map.put("role_id",user.getRoleID());
        map.put("user_name",user.getName());
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
        public Response<Page<OnlineStudent>> onlineUser(@RequestParam(value = "per_page") Integer perPage,
                                                    @RequestParam(value = "page") Integer page) {
        List<HttpSession> sessions = TDOSSessionListener.sessions.values().stream().filter(x -> !StringUtils.isEmpty(x.getAttribute("user_id"))).collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(x -> ((String) x.getAttribute("user_id"))))), ArrayList::new)
        );
        List<String> ids = sessions.stream()
                .map(session -> (String) session.getAttribute("user_id")).collect(Collectors.toList());
        return Response.success(PageTool.getPageList(userService.getOnlineStudents(ids), page, perPage));
    }

    @GetMapping(value = "/search_user")
    public Response<PageInfo<UserEntity>> searchUser(@RequestParam(value = "search", required = false) String search,
                                     @RequestParam(value = "type", required = true) int type,
                                     @RequestParam(value = "classes", required = false) String classesId,
                                     @RequestParam(value = "page", required = true) int page,
                                     @RequestParam(value = "per_page", required = true) int per_page,
                                    @RequestHeader(JWT_HEADER_KEY)String jwt) {
        String ownerID = securityService.getUserId(jwt);
        if(StringUtils.hasText(classesId)){
            ClassEntity classes = classMapper.findClassByClassesId(classesId);
            if (classes == null) {
                return Response.error("class name is not exist");
            }
        }
        PageInfo<UserEntity> list = userService.searchUser(ownerID, search,type,classesId,page,per_page);
        return Response.success(list);
    }

    @DeleteMapping(value = "/delete_user")
    public Response<String> deleteUser(@RequestBody DeleteIdList deleteIdList,
                                       @RequestHeader(JWT_HEADER_KEY) String jwt) {
        String ownerID = securityService.getUserId(jwt);
        int type =2;
        Pair<Boolean, String> pair = userService.deleteUser(deleteIdList.deleteIdList,type, ownerID);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }

    @PostMapping(value = "/modify_user")
    public Response<String> modifyUser(@RequestBody @Validated ModifyUser modifyUser,
                                       @RequestHeader(JWT_HEADER_KEY) String jwt) {
        String ownerID = securityService.getUserId(jwt);
        Pair<Boolean, String> pair = userService.modifyUser(ownerID, modifyUser);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }

    @PostMapping(value = "/update_userPassword")
    public Response<String> updateUserPassword(@RequestBody UpdateUserPas updateUserPas,
                                               @RequestHeader(JWT_HEADER_KEY) String jwt) {
        String ownerID = securityService.getUserId(jwt);
        Pair<Boolean, String> pair = userService.updateUserPassword(updateUserPas.userIDs, ownerID, updateUserPas.password);
        if (pair.getKey()) {
            return Response.success(null);
        } else {
            return Response.error(pair.getValue());
        }
    }
}

