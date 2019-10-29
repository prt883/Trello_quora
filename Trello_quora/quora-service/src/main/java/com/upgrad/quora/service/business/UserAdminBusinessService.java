package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAdminBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteUser(final String uuid, final String accessToken) throws AuthenticationFailedException, UserNotFoundException {


        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        if(userAuthTokenEntity==null){
            throw new AuthenticationFailedException("ATHR-001","User has not signed in");
        }
        if(userAuthTokenEntity.getLogoutAt()!=null){
            throw new AuthenticationFailedException("ATHR-002","User is signed out");
        }
        if(!userAuthTokenEntity.getUuid().equals(uuid)){
            throw new UserNotFoundException("USR-001","User with entered uuid to be deleted does not exist");
        }
        if(!userAuthTokenEntity.getUser().getRole().equals("admin")){
            throw new AuthenticationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin");
        }
         userDao.deleteUserByUuid(uuid);
        return uuid;
    }

    public UserEntity getUser(String uuid,String accessToken) throws AuthenticationFailedException, UserNotFoundException {

        UserAuthTokenEntity userAuthTokenEntity=userDao.getUserAuthToken(accessToken);
        if(userAuthTokenEntity==null){
            throw new AuthenticationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthTokenEntity.getLogoutAt()!=null){
            throw new AuthenticationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }

        if(!userAuthTokenEntity.getUuid().equals(uuid)){
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
        }
        return userAuthTokenEntity.getUser();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity createUser(final UserEntity userEntity) throws SignUpRestrictedException {

        if(userDao.getUserByUsername(userEntity.getUserName())!=null){
            throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");
        }
        if(userDao.getUserByEmail(userEntity.getEmail())!=null){
            throw new SignUpRestrictedException("SGR-002","This user has already been registered, try with any other emailId");
        }

        String password = userEntity.getPassword();
        if (password == null) {
            userEntity.setPassword("quora@123");
        }
        String[] encryptedText = cryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);
        UserEntity createdUser= userDao.createUser(userEntity);
        return createdUser;
    }
}
