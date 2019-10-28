package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class QuestionDao {


    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestions() {
        try {
            return entityManager.createNamedQuery("allQuestions", QuestionEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public QuestionEntity getQuestionByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("quesByUuid", QuestionEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUserByUsername(final String userName) {
        try {
            return entityManager.createNamedQuery("userByUserName", UserEntity.class).setParameter("userName", userName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }


    public void updateQuestion(final QuestionEntity questionEntity) {
        entityManager.merge(questionEntity);
    }

    public UserAuthTokenEntity getUserAuthToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class).setParameter("accessToken", accessToken).getSingleResult();

        } catch (NoResultException nre) {

            return null;
        }

    }

    public String deleteUserByUuid(final String uuid) {

           Query query= entityManager.createQuery("delete from UserEntity ut where ut.uuid = :uuid");
           query.setParameter("uuid",uuid);
           query.executeUpdate();
            return uuid;
    }

}
