package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.UserAdminBusinessService;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.ResourceNotFoundException;
import com.upgrad.quora.service.exception.UnauthorizedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserAdminBusinessService userAdminBusinessService;

    @RequestMapping(method = RequestMethod.DELETE, path = "/user/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> userDetails(@PathVariable("userId") String uuid, @RequestHeader("authorization") final String authorization) throws AuthenticationFailedException, UserNotFoundException, ResourceNotFoundException, UnauthorizedException {

        final UserDeleteResponse userDeleteResponse=new UserDeleteResponse();

        String accessToken = authorization.split("Bearer ")[1];
        String userid=userAdminBusinessService.deleteUser(uuid,accessToken);

        userDeleteResponse.id(userid).status("USER SUCCESSFULLY DELETED");

        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);

    }

}
