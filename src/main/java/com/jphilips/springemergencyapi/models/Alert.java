package com.jphilips.springemergencyapi.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long alert_id;
    private String username;
    private Double longitude;
    private Double latutide;
    private String town;

    private LocalDateTime date_created;
    private LocalDateTime date_updated;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Status status;
}
