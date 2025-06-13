package com.example.DateCock.Repository;

import com.example.DateCock.Entity.BusinessEntity;
import com.example.DateCock.Entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    // Spring Data JPA가 메서드 이름을 분석하여 자동으로 다음 쿼리를 생성합니다:
    // SELECT * FROM member_entity WHERE name = ? AND email = ?
    Optional<MemberEntity> findByNameAndEmail(String name, String email);

    // id와 email로 회원의 존재 여부를 확인하는 메서드 (ORA-00933 오류 해결)
    // Spring Data JPA의 existsBy... 대신 직접 JPQL (Java Persistence Query Language) 쿼리 작성
    // 이 쿼리는 'id'와 'email'이 일치하는 MemberEntity의 개수가 0보다 큰지 확인합니다.
    @Query("SELECT COUNT(m) > 0 FROM MemberEntity m WHERE m.id = :id AND m.email = :email")
    boolean existsByIdAndEmail(@Param("id") String id, @Param("email") String email);

    @Query(value = "SELECT NAME FROM DATECOCKMEMBER WHERE DATECOCKMEMBER.id =:userid",nativeQuery = true)
    String userName(@Param("userid") String userid);

    @Query(value = "SELECT PHONE FROM DATECOCKMEMBER WHERE DATECOCKMEMBER.id =:userid",nativeQuery = true)
    String userPhone(@Param("userid") String userid);


}
