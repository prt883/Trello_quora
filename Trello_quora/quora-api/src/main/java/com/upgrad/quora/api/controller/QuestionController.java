package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionEditResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.json.JSONException;
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
    public ResponseEntity<QuestionResponse> userDetails1(final QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {

        final QuestionResponse questionResponse = new QuestionResponse();

        String accessToken = authorization.split("Bearer ")[1];
        String uuid = questionBusinessService.postQuestion(accessToken, questionRequest.getContent());

        questionResponse.id(uuid).status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> userDetails1(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException, JSONException {

        String accessToken = authorization.split("Bearer ")[1];
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
    public ResponseEntity<QuestionEditResponse> userDetails1(@PathVariable("questionId") String questionId,final QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization) throws AuthenticationFailedException, JSONException, InvalidQuestionException {

        String accessToken = authorization.split("Bearer ")[1];
        String uuid=questionBusinessService.updateQuestion(questionId,questionRequest.getContent(),accessToken);

        QuestionEditResponse questionEditResponse=new QuestionEditResponse().id(uuid).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }
}
