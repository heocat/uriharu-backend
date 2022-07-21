package hh.com.uriharu.model;



import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
//오브젝트 생성을 위한 디자인 패턴 중 하나
//builder 클래스를 개발하지 않고도 builder패턴 이용가능
@Builder
//매개변수가 없는 생성자를 구현해줌
@NoArgsConstructor
//클래스의 모든 맴버변수를 매개변수로 받는 생성자를 구현
@AllArgsConstructor
//클래스 멤버 변수의 getter,setter 메서드를 구현해준다.
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "ReplyEntity")
@Entity
@EntityListeners(value = {AuditingEntityListener.class})
@ToString(exclude = {"diary"})
public class ReplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;    
    private String writer;
    private String nickname;
    private String contents;

    @CreatedDate
    @Column(name="regdate", updatable=false)
    private LocalDateTime regdate;

    @LastModifiedDate
    @Column(name="moddate")
    private LocalDateTime moddate;

    @ManyToOne(fetch = FetchType.LAZY)
    private DiaryEntity diary;
}
