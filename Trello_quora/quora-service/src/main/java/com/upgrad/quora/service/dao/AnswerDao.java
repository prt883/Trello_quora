package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class AnswerDao {


    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }


    public AnswerEntity getAnswerByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("answerByUuid", AnswerEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void updateAnswer(final AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);
    }


    public String deleteAnswerByUuid(final String uuid) {

           Query query= entityManager.createQuery("delete from AnswerEntity a where a.uuid = :uuid");
           query.setParameter("uuid",uuid);
           query.executeUpdate();
           return uuid;
    }

}
