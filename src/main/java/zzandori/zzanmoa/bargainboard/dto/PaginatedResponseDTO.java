package zzandori.zzanmoa.bargainboard.dto;

import java.util.List;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Getter
@ToString
public class PaginatedResponseDTO<T> {
    private int recentNewsCount;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private List<T> content;

    public PaginatedResponseDTO(Page<T> pageData, int recentNewsCount) {
        this.recentNewsCount = recentNewsCount;
        this.currentPage = pageData.getNumber();
        this.totalPages = pageData.getTotalPages();
        this.totalElements = pageData.getTotalElements();
        this.content = pageData.getContent();
    }
}
