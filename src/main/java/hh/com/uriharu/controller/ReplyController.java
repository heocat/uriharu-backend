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

import hh.com.uriharu.dto.ReplyDTO;
import hh.com.uriharu.dto.ResponseDTO;
import hh.com.uriharu.model.ReplyEntity;
import hh.com.uriharu.service.ReplyService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("reply")
public class ReplyController {
    @Autowired
    private ReplyService service;
    

    @GetMapping("all/{dno}")
    public ResponseEntity<?> getAllReply(@AuthenticationPrincipal String userId, @PathVariable Long dno) {
        log.warn("userdno",userId, dno);
        if (userId == null) {
            log.warn("user is null!");
            throw new RuntimeException("user is null! please login!");
        }else{
                List<ReplyEntity> entities = service.readAll(dno);
                List<ReplyDTO> dtos = entities.stream().map(ReplyDTO :: new).collect(Collectors.toList());
                ResponseDTO<ReplyDTO> response = ResponseDTO.<ReplyDTO>builder().data(dtos).build();
                log.warn("response:"+response);
                return ResponseEntity.ok().body(response);
            }
           
        }
    
    @PostMapping("add")
    public ResponseEntity<?> addReply(@AuthenticationPrincipal String userId,@RequestBody ReplyDTO dto){
        try {
            ReplyEntity entity = ReplyDTO.toEntity(dto);
            entity.setRno(null);
            entity.setWriter(userId);
            entity.setNickname(service.nicknameById(userId));
            
            List<ReplyEntity> entities = service.create(entity);
            List<ReplyDTO> dtos = entities.stream().map(ReplyDTO :: new).collect(Collectors.toList());
            ResponseDTO<ReplyDTO> response = ResponseDTO.<ReplyDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ReplyDTO> response = ResponseDTO.<ReplyDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("modify")
    public ResponseEntity<?> modifyReply(@AuthenticationPrincipal String userId,@RequestBody ReplyDTO dto) {
        ReplyEntity entity = ReplyDTO.toEntity(dto);
        entity.setWriter(userId);
        entity.setNickname(service.nicknameById(userId));

        Long dno = service.update(userId,entity);
        log.warn("dno:"+dno);
        
        List<ReplyEntity> entities = service.readAll(dno);
        log.warn("entities:"+entities);
        
            List<ReplyDTO> dtos = entities.stream().map(ReplyDTO :: new).collect(Collectors.toList());
            ResponseDTO<ReplyDTO> response = ResponseDTO.<ReplyDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("remove")  
    private ResponseEntity<?> removeReply(@AuthenticationPrincipal String userId,@RequestBody ReplyDTO dto) {
        try {
            ReplyEntity entity = ReplyDTO.toEntity(dto);
            Long dno = entity.getDiary().getDno();
            service.deleteByDto(entity);

            List<ReplyEntity> entities = service.readAll(dno);
            log.warn("entities:"+entities);
            
            List<ReplyDTO> dtos = entities.stream().map(ReplyDTO :: new).collect(Collectors.toList());
            ResponseDTO<ReplyDTO> response = ResponseDTO.<ReplyDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
            
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ReplyDTO> response = ResponseDTO.<ReplyDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }


}
