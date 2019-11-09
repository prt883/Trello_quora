package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionBusinessService questionBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> userDetails1(final QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        final QuestionResponse questionResponse = new QuestionResponse();

        String accessToken = authorization.startsWith("Bearer ")?authorization.split("Bearer ")[1]:authorization;
        String uuid = questionBusinessService.postQuestion(accessToken, questionRequest.getContent());

        questionResponse.id(uuid).status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> userDetails1(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        String accessToken = authorization.startsWith("Bearer ")?authorization.split("Bearer ")[1]:authorization;
        List<QuestionEntity> allQuestions = questionBusinessService.getAllQuestions(accessToken);
        List<QuestionDetailsResponse> questionDetailsResponses = new ArrayList<>();
        for (QuestionEntity question : allQuestions) {
            QuestionDetailsResponse qResponse = new QuestionDetailsResponse();
            qResponse.setId(question.getUuid());
            qResponse.setContent(question.getContent());
            questionDetailsResponses.add(qResponse);
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponses, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/edit/{questionId}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editByQuestionId(@PathVariable("questionId") String questionId,final QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization) throws InvalidQuestionException, AuthorizationFailedException {

        String accessToken = authorization.startsWith("Bearer ")?authorization.split("Bearer ")[1]:authorization;
        String uuid=questionBusinessService.updateQuestion(questionId,questionRequest.getContent(),accessToken);

        QuestionEditResponse questionEditResponse=new QuestionEditResponse().id(uuid).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/delete/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestionByQuestionId(@PathVariable("questionId") String questionId,@RequestHeader("authorization") final String authorization) throws InvalidQuestionException, AuthorizationFailedException {

        String accessToken = authorization.startsWith("Bearer ")?authorization.split("Bearer ")[1]:authorization;
        String uuid=questionBusinessService.deleteQuestion(questionId,accessToken);

        QuestionDeleteResponse questionDeleteResponse=new QuestionDeleteResponse().id(uuid).status("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/all/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getQuestionsByuserId(@PathVariable("userId") String userId,@RequestHeader("authorization") final String authorization) throws UserNotFoundException, AuthorizationFailedException {

        String accessToken = authorization.startsWith("Bearer ")?authorization.split("Bearer ")[1]:authorization;
        List<QuestionEntity> allQuestions = questionBusinessService.getAllQuestionsByUserId(userId,accessToken);
        List<QuestionDetailsResponse> questionDetailsResponses = new ArrayList<>();
        for (QuestionEntity question : allQuestions) {
            QuestionDetailsResponse qResponse = new QuestionDetailsResponse();
            qResponse.setId(question.getUuid());
            qResponse.setContent(question.getContent());
            questionDetailsResponses.add(qResponse);
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponses, HttpStatus.OK);
    }
}
