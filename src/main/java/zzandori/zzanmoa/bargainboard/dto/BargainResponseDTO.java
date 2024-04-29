package zzandori.zzanmoa.bargainboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class BargainResponseDTO {

    private Integer id;
    private Integer eventId;
    private Integer districtId;
    private String title;
    private String content;
    @JsonFormat
    private LocalDate createdAt;
}
