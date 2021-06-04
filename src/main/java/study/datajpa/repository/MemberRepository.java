package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //여기 <Member, Long>에 Long은 pk 데이터타입 넣어주면 됨
}
