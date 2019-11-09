package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AnswerBusinessService {
    @Autowired
    UserDao userDao;

    @Autowired
    AnswerDao answerDao;
    @Autowired
    QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public String postAnswer(String accessToken,String questionId,String ans) throws InvalidQuestionException, AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity=userDao.getUserAuthToken(accessToken);
        if(userAuthTokenEntity==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthTokenEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post an answer");
        }
        if(userAuthTokenEntity.getExpiresAt().isBefore(ZonedDateTime.now())){
            throw new AuthorizationFailedException("ATHR-004","Token Expired. Sign in again");
        }

        QuestionEntity questionEntity=questionDao.getQuestionByUuid(questionId);
        if(questionEntity==null){
            throw new InvalidQuestionException("QUES-001","The question entered is invalid");
        }

        final AnswerEntity answerEntity=new AnswerEntity();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setAns(ans);
        answerEntity.setQuestionEntity(questionEntity);
        answerEntity.setUser(userAuthTokenEntity.getUser());
        final ZonedDateTime now = ZonedDateTime.now();
        answerEntity.setDate(now);
        return answerDao.createAnswer(answerEntity).getUuid();


    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String updateAnswer(String answerId,String ans,String accessToken) throws AnswerNotFoundException, AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity=userDao.getUserAuthToken(accessToken);

        if(userAuthTokenEntity==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthTokenEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to edit an answer");
        }
        if(userAuthTokenEntity.getExpiresAt().isBefore(ZonedDateTime.now())){
            throw new AuthorizationFailedException("ATHR-004","Token Expired. Sign in again");
        }
        AnswerEntity answerEntity=answerDao.getAnswerByUuid(answerId);
        if(answerEntity==null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
        if(userAuthTokenEntity.getUser().getId()==answerEntity.getUser().getId()){
            answerEntity.setAns(ans);
            final ZonedDateTime now = ZonedDateTime.now();
            answerEntity.setDate(now);
            answerDao.updateAnswer(answerEntity);

            return answerId;
        }
        throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");
    }




    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteAnswer(String answerId,String accessToken) throws AnswerNotFoundException, AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity=userDao.getUserAuthToken(accessToken);

        if(userAuthTokenEntity==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthTokenEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to delete an answer");
        }
        if(userAuthTokenEntity.getExpiresAt().isBefore(ZonedDateTime.now())){
            throw new AuthorizationFailedException("ATHR-004","Token Expired. Sign in again");
        }
        AnswerEntity answerEntity=answerDao.getAnswerByUuid(answerId);
        if(answerEntity==null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
        if(userAuthTokenEntity.getUser().getId()==answerEntity.getUser().getId() || userAuthTokenEntity.getUser().getRole().equals("admin")){
            return answerDao.deleteAnswerByUuid(answerId);

        }
        throw new AuthorizationFailedException("ATHR-003","Only the answer owner or admin can delete the answer");
    }

    public List<AnswerEntity> getAnswersByQuestionId(String questionId, String accessToken) throws InvalidQuestionException, AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity=userDao.getUserAuthToken(accessToken);

        if(userAuthTokenEntity==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthTokenEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get the answers");
        }
        if(userAuthTokenEntity.getExpiresAt().isBefore(ZonedDateTime.now())){
            throw new AuthorizationFailedException("ATHR-004","Token Expired. Sign in again");
        }

        QuestionEntity questionEntity=questionDao.getQuestionByUuid(questionId);
        if(questionEntity==null){
            throw new InvalidQuestionException("QUES-001","The question with entered uuid whose details are to be seen does not exist");
        }
        return questionEntity.getAnswerEntityList();

    }

}


