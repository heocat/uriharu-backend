package hh.com.uriharu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hh.com.uriharu.model.DiaryEntity;

@Repository
public interface DiaryRepository extends JpaRepository<DiaryEntity,Long>{
    @Query("select d from DiaryEntity d where d.writer = ?1")
    List<DiaryEntity> findByWriter(String writer);

    @Query("select d from DiaryEntity d where d.yyyymmdd = ?1")
    List<DiaryEntity> findByDate(String yyyymmdd);
}

