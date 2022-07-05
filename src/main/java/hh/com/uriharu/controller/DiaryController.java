package hh.com.uriharu.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hh.com.uriharu.dto.DiaryDTO;
import hh.com.uriharu.dto.ResponseDTO;
import hh.com.uriharu.model.DiaryEntity;
import hh.com.uriharu.service.DiaryService;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequestMapping("diary")
public class DiaryController {
    @Autowired
    private DiaryService service;

    

    @PostMapping("create")
    public ResponseEntity<?> createHaru(@AuthenticationPrincipal String userId,@RequestBody DiaryDTO dto){
        try {
            DiaryEntity entity = DiaryDTO.toEntity(dto);
            entity.setDno(null);
            entity.setWriter(userId);
            entity.setNickname(service.nicknameById(userId));
            
            List<DiaryEntity> entities = service.create(entity);
            List<DiaryDTO> dtos = entities.stream().map(DiaryDTO :: new).collect(Collectors.toList());
            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("myread")
    public ResponseEntity<?> retieveMyHaru(@AuthenticationPrincipal String userId){

        List<DiaryEntity> entites = service.retrieve(userId);
        List<DiaryDTO> dtos = entites.stream().map(DiaryDTO::new).collect(Collectors.toList());
        ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("read/{dno}")
    public ResponseEntity<?> retieveHaruByDno(@AuthenticationPrincipal String userId, @PathVariable Long dno){

        DiaryEntity entity = service.retrieveByDno(dno);
        log.warn("entity : {}  is ", entity);
        DiaryDTO dto = DiaryDTO.builder()
        .dno(entity.getDno())
        .nickname(entity.getNickname())
        .writer(entity.getWriter())
        .title(entity.getTitle())
        .contents(entity.getContents())
        .regdate(entity.getRegdate())
        .moddate(entity.getModdate())
        .build();
        
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("modify")
    public ResponseEntity<?> updateHaru(@AuthenticationPrincipal String userId, @RequestBody DiaryDTO dto) {

        DiaryEntity entity = DiaryDTO.toEntity(dto);
        String date = dto.getYyyymmdd();
        entity.setWriter(userId);

        Long dno = service.update(entity);
        retieveHaruByDno(userId,dno);
        
        return retieveHaruByDate(userId, date);
    }

    @GetMapping("dateread/{yyyymmdd}")
    public ResponseEntity<?> retieveHaruByDate(@AuthenticationPrincipal String userId, @PathVariable String yyyymmdd){

        List<DiaryEntity> entites = service.retrieveByDate(yyyymmdd);
        List<DiaryDTO> dtos = entites.stream().map(DiaryDTO::new).collect(Collectors.toList());
        ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("remove")  
    private ResponseEntity<?> removeHaruByDno(@AuthenticationPrincipal String userId,@RequestBody DiaryDTO dto) {
        try {
            DiaryEntity entity = DiaryDTO.toEntity(dto);
            String date = dto.getYyyymmdd();

                service.deleteByDto(entity);
            
            return retieveHaruByDate(userId , date);
            
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
