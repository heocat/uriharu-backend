package hh.com.uriharu.dto;

import java.time.LocalDateTime;

import hh.com.uriharu.model.DiaryEntity;
import hh.com.uriharu.model.ReplyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReplyDTO {
    private Long rno;
    private Long dno;
    private String writer;
    private String nickname;
    private String contents;
    private LocalDateTime regdate;
    private LocalDateTime moddate;


    public ReplyDTO(final ReplyEntity entity){
        this.rno = entity.getRno();
        this.nickname = entity.getNickname();
        this.writer = entity.getWriter();
        this.contents = entity.getContents();
        this.regdate = entity.getRegdate();
        this.moddate = entity.getModdate();
    }

    public static ReplyEntity toEntity(final ReplyDTO dto) {
        return ReplyEntity.builder()
        .rno(dto.getRno())
        .contents((dto.getContents()))
        .diary(DiaryEntity.builder().dno(dto.getDno()).build())
        .build();
    }
}
