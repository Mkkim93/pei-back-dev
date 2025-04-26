package kr.co.pei.pei_app.domain.entity.hospital;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.*;

@Entity
public class Hospital {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String name;
}
