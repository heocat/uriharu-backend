package hh.com.uriharu.persistence;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hh.com.uriharu.model.ReplyEntity;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity,Long> {
    @Query("select r from ReplyEntity r where r.diary.dno = ?1")
    List<ReplyEntity> findReplyListByDno(Long dno);

    @Transactional
    @Modifying
    @Query("delete from ReplyEntity r where r.diary.dno =?1")
    void deleteByDno(Long dno);
}
