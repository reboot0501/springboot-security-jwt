package com.cos.jwt.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class User {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String roles; // USER,ADMIN

    public List<String> getRoleList() {
        //
        if(this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        // null 회피
        return new ArrayList<>();
    }
}
