package com.example.SPP.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pembayaran")
public class Pembayaran {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nis")
    private String nis;
    
    @Column(name = "nama_siswa")
    private String namaSiswa;
    
    @Column(name = "tahun_masuk")
    private String tahunMasuk;
    
    @Column(name = "nominal")
    private Integer nominal;
    
    @Column(name = "metode_bayar")
    private String metodeBayar;
    
    @Column(name = "status_bayar")
    private String statusBayar;
    
    @Column(name = "tanggal_bayar")
    private LocalDate tanggalBayar;
    
    @Column(name = "bulan_bayar")
    private String bulanBayar;

    // Default constructor
    public Pembayaran() {}

    // Constructor with parameters
    public Pembayaran(String nis, String namaSiswa, String tahunMasuk, 
                     Integer nominal, String metodeBayar, String statusBayar, 
                     LocalDate tanggalBayar, String bulanBayar) {
        this.nis = nis;
        this.namaSiswa = namaSiswa;
        this.tahunMasuk = tahunMasuk;
        this.nominal = nominal;
        this.metodeBayar = metodeBayar;
        this.statusBayar = statusBayar;
        this.tanggalBayar = tanggalBayar;
        this.bulanBayar = bulanBayar;
    }

    // Getter methods
    public Long getId() {
        return id;
    }

    public String getNis() {
        return nis;
    }

    public String getNamaSiswa() {
        return namaSiswa;
    }

    public String getTahunMasuk() {
        return tahunMasuk;
    }

    public Integer getNominal() {
        return nominal;
    }

    public String getMetodeBayar() {
        return metodeBayar;
    }

    public String getStatusBayar() {
        return statusBayar;
    }

    public LocalDate getTanggalBayar() {
        return tanggalBayar;
    }

    public String getBulanBayar() {
        return bulanBayar;
    }

    // Setter methods
    public void setId(Long id) {
        this.id = id;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public void setNamaSiswa(String namaSiswa) {
        this.namaSiswa = namaSiswa;
    }

    public void setTahunMasuk(String tahunMasuk) {
        this.tahunMasuk = tahunMasuk;
    }

    public void setNominal(Integer nominal) {
        this.nominal = nominal;
    }

    public void setMetodeBayar(String metodeBayar) {
        this.metodeBayar = metodeBayar;
    }

    public void setStatusBayar(String statusBayar) {
        this.statusBayar = statusBayar;
    }

    public void setTanggalBayar(LocalDate tanggalBayar) {
        this.tanggalBayar = tanggalBayar;
    }

    public void setBulanBayar(String bulanBayar) {
        this.bulanBayar = bulanBayar;
    }

    @Override
    public String toString() {
        return "Pembayaran{" +
                "id=" + id +
                ", nis='" + nis + '\'' +
                ", namaSiswa='" + namaSiswa + '\'' +
                ", tahunMasuk=" + tahunMasuk +
                ", nominal=" + nominal +
                ", metodeBayar='" + metodeBayar + '\'' +
                ", statusBayar='" + statusBayar + '\'' +
                ", tanggalBayar=" + tanggalBayar +
                ", bulanBayar='" + bulanBayar + '\'' +
                '}';
    }
}