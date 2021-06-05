package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

//@Repository //어노테이션 생략해도 됨!
//왜냐면! 스프링 데이터 JPA가 인터페이스만 보고 인식해줌
public interface MemberRepository extends JpaRepository<Member, Long> {
    //여기 <Member, Long>에 Long은 pk 데이터타입 넣어주면 됨
    List<Member> findByUsername(String username);
    //쿼리 메서드 기능이 있다. 이거 이렇게만 써도 구현해준다!
    //쿼리 메서드!!!!
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
    //ByUsername은 =, Age한 다음에 GreaterThan age 보다 크면

    //만약 조건 더 놓고싶으면?
//    List<Member> findByUsernameAndAgeGreaterThanAndIdAndTeamAndDay(String username, int age, String id, Team team, String day);
    //점점점 더 길게 됨. 한 2개 -3개 까진 괜찮은데... 그거보다 길면 JPQL짜서 만드는 게 유용하다.

    //find...By, findHelloBy() 전체조회!
    List<Member> findHelloBy();

    List<Member> findTop3HelloBy(int no);
    /*
    count...By
    exists...By
    delete ... By
    findDestinct, findMemberDistinctBy
    findFirstTop10 등등
     */
}