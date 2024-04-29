package zzandori.zzanmoa.email.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zzandori.zzanmoa.bargainboard.entity.BargainBoard;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "email_send_list")
public class EmailSendList extends TimeStamp{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bargain_id")
    private BargainBoard bargain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_id")
    private Email email;
}
