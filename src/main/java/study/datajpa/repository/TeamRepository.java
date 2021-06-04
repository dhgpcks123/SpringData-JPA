package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;

//@Repository //어노테이션 생략해도 됨!
//왜냐면! 스프링 데이터 JPA가 인터페이스만 보고 인식해줌
public interface TeamRepository extends JpaRepository<Team, Long> {
    //여기 <Team, Long>에 Long은 pk 데이터타입 넣어주면 됨
}
