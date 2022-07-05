package hh.com.uriharu.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hh.com.uriharu.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String>{
    UserEntity findByEmail(String email);
    Boolean existsByEmail(String email);
    UserEntity findByEmailAndPassword(String email, String password);

    @Query("select nickname from UserEntity u where u.id =:id")
    String findNicknameById (String id);
    
}
