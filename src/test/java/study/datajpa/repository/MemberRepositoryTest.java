package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Rollback(value = true)
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("회원가입 테스트")
    public void testMember(){
        System.out.println("memberRepository = " + memberRepository.getClass());
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    @DisplayName("JpaRepository상속 동작")
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

//        findMember1.setUsername("member!!!!!!");


        //단건 조회 검증
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        //카운트 조회 검증
        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);

    }
    @Test
    public void findByUsernameAndAgeGreaterThan(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findHelloBy(){
        List<Member> helloBy = memberRepository.findHelloBy();
    }
    @Test
    public void findTop3HelloBy(){
        List<Member> helloBy = memberRepository.findTop3HelloBy();
    }

    @Test
    public void namedQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.NQfindByUsername("AAA");

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo(m1.getUsername());
    }

    @Test
    public void testQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);

        Assertions.assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    @DisplayName("DTO 조회하기")
    public void findMemberDto(){
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void findByNames(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }
    @Test
    public void returnType(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA");
        Member aaa1 = memberRepository.findMemberByUsername("AAA");
        Optional<Member> aaa2 = memberRepository.findOptionalByUsername("AAA");
        System.out.println("aaa = " + aaa);
        System.out.println("aaa1 = " + aaa1);
        System.out.println("aaa2 = " + aaa2);

        //만약 컬렉션 조회했을 때 없으면 emptyCollection을 반환한다.
        List<Member> result = memberRepository.findListByUsername("asdfgh");
        System.out.println(">>>>>>result = " + result);
        //빈 컬렉션을 반환해준다.
        //if(result != null) 이거 절대 하지 마세요. 그냥 빈 거 반환해줘.
        //스프링 데이터 JPA의 경우 그냥 try catch해줌
        //단건인 경우는 결과가가 null 나옴
        Member resultOne = memberRepository.findMemberByUsername("asdf");
        System.out.println(">>>>>>resultOne = " + resultOne);

        //이거 자바8나오고 나서 논쟁 없어짐ㅋ
        //Optional쓰면 되니까. 근데... 회사에선 안 씀 ㅠㅠ

        //한 건 조회하는건데!!!! 결과가 2개 이상이면 어떻게 되냐!? --> 예외터짐!
        //IncorrectResultSizeException
    }

    @Test
    public void paging(){
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        //페이지 0부터 시작

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        //totalCount쿼리까지 알아서 날려줌 오잉!
        //엔티티는 외부에 노출하면 안 됨★★★★★
        //DTO에 담아서 보내. 그래서 Entity를 안 쓰고 다른 거에 담아서 넘기는구나.
        //왜냐하면 밖에서 Entity바꾸면?? 그럼 DB는!?
        // DTO로 쉽게 바꾸는 법
        Page<MemberDto> map = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));
        //이거 map... 함수형에서 썻던 거ㄱ ㅏㅌ네

//        Page<Member> page = memberRepository.findTop3ByAge(age, pageRequest);
        // 위에서 3건만

        //then
        List<Member> content = page.getContent(); //내용
        long totalElements = page.getTotalElements(); //총페이지

        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);

        Assertions.assertThat(page.getContent().size()).isEqualTo(3);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);//페이지 번호 가져옴!
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void slicePaging(){
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        //페이지 0부터 시작

        //when
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);
        //totalCount쿼리까지 알아서 날려줌 오잉!

        //then
        List<Member> content = page.getContent(); //내용

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        Assertions.assertThat(page.getContent().size()).isEqualTo(3);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);//페이지 번호 가져옴!
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 33));
        memberRepository.save(new Member("member5", 52));
        //when

//        entityManager.flush();
//        entityManager.clear();

        //이거 안하고 Repository.bulkAgePlus에서
        //@Modifying(clearAutomatically = true)로 써도 됨.

        int resultCount = memberRepository.bulkAgePlus(20);

        //결과는!?

        //then
        Assertions.assertThat(resultCount).isEqualTo(3);
        //bulk연산은 영속성 컨텍스트 손 안 대고 그냥 DB에 바로 update쿼리를 날림.

        List<Member> all = memberRepository.findAll();
        System.out.println("all = " + all);
        //봐바 업데이트 안 됐지? 영속성 컨텍스트에 있는데 업데이트 쿼리는 db에 날렸거든!
        //벌크형쿼리 쓰기 전에 영속성컨텍스트에꺼 다 날려얗해!
    }

    @Test
    public void findMemberLazy(){
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 15, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();
        
        //when
        List<Member> all = memberRepository.findAll();
        for (Member member : all) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
            // team에서 데이터 조회하는데 쿼리 두 번 날림...
            // member가 10000개면? 쿼리를 10000개 날림.
            // 그냥 한방에 가져오면 1번만 조인해서 가져오면 되는데...
            // 이게 바로 N+1 문제라고 합니다. 쿼리 1번 날렸는데 N개 만큼 쿼리 날려!
            // 네트웤을 N번 왔다갔다;;;
        }
        //then
    }
    @Test
    public void findMemberfetchJoin() {
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 15, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();

        //when
        List<Member> all = memberRepository.findMemberFetchJoin();
        for (Member member : all) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
            // MemberRepository확인
            //한방에 끌고 아ㅗ서 조회해줌. N+1문제 해결
        }
        //then
    }
}
