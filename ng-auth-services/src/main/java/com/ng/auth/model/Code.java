package com.ng.auth.model;

import javax.persistence.*;

@Entity
@Table(name = "back_up_codes")
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Long id;

    @Column(name = "security_code", length = 6)
    private String securityCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Code() {
    }

    public Code(String securityCode, User user) {
        this.securityCode = securityCode;
        this.user = user;
    }

    public Code(String securityCode) {
        this.securityCode = securityCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    @Override
    public String toString() {

        return id + "     codes    " + securityCode + "    user Id" + user.getId();
    }
}
