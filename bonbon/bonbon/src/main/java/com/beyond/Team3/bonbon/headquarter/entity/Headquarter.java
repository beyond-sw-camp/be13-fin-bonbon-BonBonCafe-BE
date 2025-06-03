package com.beyond.Team3.bonbon.headquarter.entity;



import com.beyond.Team3.bonbon.common.base.EntityDate;
import com.beyond.Team3.bonbon.headquarter.dto.HeadquarterRequestDto;
import com.beyond.Team3.bonbon.headquaterStock.entity.HeadquarterStock;
import com.beyond.Team3.bonbon.notice.entity.Notice;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Entity
@DynamicUpdate // 업데이터 할 때 변경된 필드만
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "headquarter")
public class Headquarter extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "headquarter_id")
    private Long headquarterId;

    @Column(name = "name")
    private String name;

    @Column(name = "headquarter_tel")
    private String headquarterTel;

    @Column(name = "road_address")
    private String roadAddress;

    @Column(name = "detail_address")
    private String detailAddress;

    @OneToMany(mappedBy = "headquarter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HeadquarterStock> stocks = new ArrayList<>();

    @OneToMany(mappedBy = "headquarterId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notice> notices = new ArrayList<>();

    public void updateHeadquarter(HeadquarterRequestDto dto) {
        this.name = dto.getName();
        this.headquarterTel = dto.getHeadquarterTel();
        this.roadAddress = dto.getRoadAddress();
        this.detailAddress = dto.getDetailAddress();
    }

    public static Headquarter createHeadquarter(HeadquarterRequestDto dto) {
        return Headquarter.builder()
                .name(dto.getName())
                .headquarterTel(dto.getHeadquarterTel())
                .roadAddress(dto.getRoadAddress())
                .detailAddress(dto.getDetailAddress())
                .build();
    }
//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User userId;
}
