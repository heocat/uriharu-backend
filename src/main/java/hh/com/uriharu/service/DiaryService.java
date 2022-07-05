package hh.com.uriharu.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import hh.com.uriharu.model.DiaryEntity;
import hh.com.uriharu.persistence.DiaryRepository;
import hh.com.uriharu.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DiaryService {

    @Autowired
    private DiaryRepository repository;

    @Autowired UserRepository userRepository;


    public List<DiaryEntity> create(final DiaryEntity entity) {
        //Validations
        validate(entity);
        repository.save(entity);
        log.info("Entity Id : {} is saved",entity.getDno());
        return repository.findByWriter(entity.getWriter());
    }

    //리팩토링한 메서드
        private void validate(final DiaryEntity entity){
           
            if (entity == null) {
                log.warn("Entity cannot be null");
                throw new RuntimeException("Entity cannot be null");
            }
    
            if (entity.getWriter()==null) {
                log.warn("unknown user");
                throw new RuntimeException("unknown user");
            }
        }

        //내 아이디로 조회
        public List<DiaryEntity> retrieve(final String userId) {
            return repository.findByWriter(userId);
        }

        //일기 번호로 조회
        public DiaryEntity retrieveByDno(final Long dno) {
            return repository.findById(dno).get();
        }
        //일기 날짜 조회
        public List<DiaryEntity> retrieveByDate(final String yyyymmdd) {
            return repository.findByDate(yyyymmdd);
        }

        //수정
        public Long update(final DiaryEntity entity){
            validate(entity);
            Optional<DiaryEntity> opEnti = repository.findById(entity.getDno());

            if(opEnti.isPresent()){
                DiaryEntity enti = opEnti.get();
                enti.setTitle(entity.getTitle());
                enti.setContents(entity.getContents());
                enti.setModdate(entity.getModdate());

                repository.save(enti);
               return enti.getDno();
            }
            return entity.getDno();
            
        }

        //삭제
        @Modifying
        public void deleteByDto(DiaryEntity entity) {
            repository.delete(entity);
        }

        //id 일치하는 계정 닉네임 찾기
        public String nicknameById(String id) {
            return userRepository.findNicknameById(id);
        }
    
}
