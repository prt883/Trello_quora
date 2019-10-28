package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionBusinessService {
    @Autowired
    UserDao userDao;

    @Autowired
    QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public String postQuestion(String accessToken,String content) throws AuthenticationFailedException {
        UserAuthTokenEntity userAuthTokenEntity=userDao.getUserAuthToken(accessToken);
        if(userAuthTokenEntity==null){
            throw new AuthenticationFailedException("ATHR-001","User has not signed in");
        }
        if(userAuthTokenEntity.getLogoutAt()!=null){
            throw new AuthenticationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        }

        final QuestionEntity questionEntity=new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(content);
        questionEntity.setUser(userAuthTokenEntity.getUser());
        final ZonedDateTime now = ZonedDateTime.now();
        questionEntity.setDate(now);

        String uuid=questionDao.createQuestion(questionEntity).getUuid();
        return uuid;
    }
public List<QuestionEntity> getAllQuestions(String accessToken) throws AuthenticationFailedException {
    UserAuthTokenEntity userAuthTokenEntity=userDao.getUserAuthToken(accessToken);
    if(userAuthTokenEntity==null){
        throw new AuthenticationFailedException("ATHR-001","User has not signed in");
    }
    if(userAuthTokenEntity.getLogoutAt()!=null){
        throw new AuthenticationFailedException("ATHR-002","User is signed out.Sign in first to get all questions");
    }
    return questionDao.getAllQuestions();
}

}


