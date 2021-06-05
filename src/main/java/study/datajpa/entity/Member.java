package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


//    protected Member(){} //JPA가 프록시 쓰는데 거기서 protected까지 열려있어야 동작이 가능함.

    public Member(String username){
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username= username;
        this.age = age;
        if(team != null){
            changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age= age;
    }

    public void changeUsername(String username){
        this.username = username;
    }

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
        /*
        this는 현재 객체를 말합니다. 지금 Member 객체 안에서 동작하기 때문에 Member 객체의 인스턴스를 뜻합니다.
        team.getMembers() -> Team에 있는 Members 컬렉션을 조회합니다.
        .add(this) -> Team에 있는 Members 컬렉션에 Member 객체의 인스턴스를 저장합니다.
        어.. Member객체의 team을 바꿨는데 왜 team이 갖고 있는 MemberList에 현재 Member객체를 더하는거지?
         */
    }
}
