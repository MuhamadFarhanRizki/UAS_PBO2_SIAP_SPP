package com.example.SPP.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "spp")
public class SPP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tahun;
    private Integer nominal;

    public SPP() {}

    public SPP(Long id, String tahun, Integer nominal) {
        this.id = id;
        this.tahun = tahun;
        this.nominal = nominal;
    }

    // Getter dan Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public Integer getNominal() {
        return nominal;
    }

    public void setNominal(Integer nominal) {
        this.nominal = nominal;
    }
}
