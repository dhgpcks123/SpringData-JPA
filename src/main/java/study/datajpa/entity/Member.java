package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String username;

    protected Member(){} //JPA가 프록시 쓰는데 거기서 protected까지 열려있어야 동작이 가능함.

    public Member(String username){
        this.username = username;
    }

    public void changeUsername(String username){
        this.username = username;
    }
}
