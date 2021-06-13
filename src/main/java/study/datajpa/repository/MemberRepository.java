package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.Entity;
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

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    //카운트 쿼리 join 따로. left join. 이렇게 안 하면 count 개 많이 함.
    //countQuery따로 사용
    Page<Member> findByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m")
    Page<Member> findPageableByAge(int age, Pageable pageable);
    //sorting도 마찬가지로 너무 복잡해지면 안 풀릴 수 있음..
    //그럴 때 그냥 쿼리 날려주는 게 좋 음. 근데 그렇게 복잡한 거 쓸 일이 있나.

    Slice<Member> findSliceByAge(int age, Pageable pageable);

    //Page안하고 싶고 그냥 데이터 열개만 리스트로 꺼내와라!
    // List<Member> page = memberRepository.findByAge(age, pageRequest);
    // 그러면 3개 나오고 끝!

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age +5 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team") //JPQL, fetchjoin
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();
    //새로 정의해서 쓰기 귀찮잖아 이 때 쓰는 게 EntityGraph...
    //근데 문제가 뭐냐? 같은 findAll()쓰는데 여기는 fetchJoin하고 싶고, 또 다른 곳은 안 하고 싶어.
    //그럴 땐 그냥 JPQL쓰는 게 나은 것 같은데?

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();
    //JPQL 쿼리 짯는데!!!! fetch조인 하고 싶어. 이렇게 섞어서도 쓸 수 있음.

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);
    // 회원조회할 때 웬만하면 가지고 오겠다... 그러면 이렇게 쓰는 걸로...
    // EntityGraph는 JPA에서 제공해줌.

}
