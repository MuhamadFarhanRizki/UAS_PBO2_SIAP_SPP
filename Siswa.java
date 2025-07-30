package com.example.SPP.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "siswa")
public class Siswa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nis", unique = true, nullable = false)
    private String nis;
    
    @Column(name = "nama", nullable = false)
    private String nama;
    
    @Column(name = "jenis_kelamin")
    private String jenisKelamin;
    
    @Column(name = "tanggal_lahir")
    private String tanggalLahir;
    
    // This field must exist and match exactly
    @Column(name = "tahun_masuk")
    private String tahunMasuk;
    
    @Column(name = "nominal")
    private Integer nominal;
    
    // Relationship with SPP
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spp_id")
    private SPP spp;
    
    // Default constructor
    public Siswa() {}
    
    // Constructor with parameters
    public Siswa(String nis, String nama, String jenisKelamin, String tanggalLahir, String tahunMasuk) {
        this.nis = nis;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.tanggalLahir = tanggalLahir;
        this.tahunMasuk = tahunMasuk;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNis() {
        return nis;
    }
    
    public void setNis(String nis) {
        this.nis = nis;
    }
    
    public String getNama() {
        return nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public String getJenisKelamin() {
        return jenisKelamin;
    }
    
    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }
    
    public String getTanggalLahir() {
        return tanggalLahir;
    }
    
    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }
    
    // IMPORTANT: This getter/setter must exist
    public String getTahunMasuk() {
        return tahunMasuk;
    }
    
    public void setTahunMasuk(String tahunMasuk) {
        this.tahunMasuk = tahunMasuk;
    }
    
    public Integer getNominal() {
        return nominal;
    }
    
    public void setNominal(Integer nominal) {
        this.nominal = nominal;
    }
    
    public SPP getSpp() {
        return spp;
    }
    
    public void setSpp(SPP spp) {
        this.spp = spp;
    }
    
    @Override
    public String toString() {
        return "Siswa{" +
                "id=" + id +
                ", nis='" + nis + '\'' +
                ", nama='" + nama + '\'' +
                ", jenisKelamin='" + jenisKelamin + '\'' +
                ", tanggalLahir='" + tanggalLahir + '\'' +
                ", tahunMasuk='" + tahunMasuk + '\'' +
                ", nominal=" + nominal +
                '}';
    }
}