package com.upgrad.quora.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
@NamedQueries(
        {
                @NamedQuery(name = "userByUuid", query = "select u from UserEntity u where u.uuid = :uuid"),
                @NamedQuery(name = "userByEmail", query = "select u from UserEntity u where u.email =:email"),
                @NamedQuery(name = "userByUserName", query = "select u from UserEntity u where u.userName =:userName"),


        }
)


public class UserEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 64)
    @NotNull
    private String uuid;

    @Column(name = "FIRSTNAME")
    @NotNull
    @Size(max = 30)
    private String firstName;

    @Column(name = "LASTNAME")
    @NotNull
    @Size(max = 30)
    private String lastName;

    @Column(name = "USERNAME")
    @NotNull
    @Size(max = 30)
    private String userName;

    @Column(name = "EMAIL")
    @NotNull
    @Size(max = 50)
    private String email;

    //@ToStringExclude
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "SALT")
    @NotNull
    @Size(max = 200)
    //@ToStringExclude
    private String salt;

    @Column(name = "COUNTRY")
    @NotNull
    @Size(max = 30)
    private String country;

     @Column(name = "ABOUTME")
    @NotNull
    @Size(max = 50)
    private String aboutMe;

    @Column(name = "DOB")
    @Size(max = 50)
    private String dob;

    @Column(name = "ROLE")
    @Size(max = 30)
    private String role;

    @Column(name = "CONTACTNUMBER")
    @Size(max = 30)
    private String contactNumber;

    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "user",fetch = FetchType.LAZY)
    private List<QuestionEntity> questionEntityList;


    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getSalt() { return salt; }

    public void setSalt(String salt) { this.salt = salt; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public String getAboutMe() { return aboutMe; }

    public void setAboutMe(String aboutMe) { this.aboutMe = aboutMe; }

    public String getDob() { return dob; }

    public void setDob(String dob) { this.dob = dob; }

    public String getRole() { return role;  }

    public void setRole(String role) { this.role = role; }

    public String getContactNumber() { return contactNumber; }

    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public List<QuestionEntity> getQuestionEntityList() {return questionEntityList; }

    public void setQuestionEntityList(List<QuestionEntity> questionEntityList) {this.questionEntityList = questionEntityList; }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }


}