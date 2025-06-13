package com.example.DateCock.Repository;




import com.example.DateCock.Entity.BusinessmemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessmemberRepository extends JpaRepository<BusinessmemberEntity,String>{




    BusinessmemberEntity findByBusinessnumber(String businessnumber);

    @Query(value = "select count(*) from datecockbusinessmember where businessnumber =:businessnumber",nativeQuery = true)
    int checkbusinessnumber(@Param("businessnumber") String businessnumber);

    @Query("SELECT BusinessmemberEntity FROM BusinessmemberEntity BusinessmemberEntity WHERE BusinessmemberEntity.businessname = :businessname AND BusinessmemberEntity.email = :email")
    BusinessmemberEntity findMemberId(@Param("businessname") String businessname, @Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(businessmember) > 0 THEN true ELSE false END FROM BusinessmemberEntity businessmember WHERE businessmember.businessnumber = :businessnumber")
    boolean MatchBusinessNumber(@Param("businessnumber") String businessnumber);

    @Query("SELECT CASE WHEN COUNT(businessmember) > 0 THEN true ELSE false END FROM BusinessmemberEntity businessmember WHERE businessmember.businessnumber = :businessnumber AND businessmember.email = :email")
    boolean MatchBusinessnumberEmail(@Param("businessnumber") String businessnumber, @Param("email") String email);
}
