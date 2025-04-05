package scitech.newsservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatsResponseDto {
    private String likes;
    private Boolean likedByCurrentUser;
}
