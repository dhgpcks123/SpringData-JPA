package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    List<Member> findTop3HelloBy();
    /*
    count...By
    exists...By
    delete ... By
    findDestinct, findMemberDistinctBy
    findFirstTop10 등등
     */

    @Query(name = "Member.findByUsername") //NamedQuery.. 굳이 복잡하게 실무에서 써야할 필요가 없는 듯?
    List<Member> NQfindByUsername(@Param("username") String username);
//    JPQL에 :username 명확히 작성했을 때 @Param어노테이션 필요하다.
    //그냥 여기에 JPQL칠 수 있는데 굳이 쪼개서 NamedQuery를 엔티티클래스에 쓸 필요가 없잖아.

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
    //@Query장점! 오타쳤다? 애플리케이션 로딩시점에 오류 발생함! 오 굳 NamedQuery장점 그냥 들고 있으면서
    //훨씬 간단하네. @Query 이름 없는 NamedQuery라고 생각하면 되겠다
    // --> 동적 쿼리는 ? QueryDSL을 써야한다.

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); //컬렉션
    Member findMemberByUsername(String username); //단건
    Optional<Member> findOptionalByUsername(String username); //단건 Optional
    //반환 타입을 유연하게 쓸 수 있도록 스프링 데이터 JPA가 지원해준다
}