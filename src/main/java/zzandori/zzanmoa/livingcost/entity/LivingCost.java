package zzandori.zzanmoa.livingcost.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.sql.Timestamp;
import lombok.Builder;

@Entity
public class LivingCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "views")
    private Integer views;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Builder
    public LivingCost(String title, String content, Integer views,
        Timestamp createdAt) {
        this.title = title;
        this.content = content;
        this.views = views;
        this.createdAt = createdAt;
    }
}
