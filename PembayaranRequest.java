package com.example.SPP.Dto;

public class PembayaranRequest {
    private String nis;
    private String namaSiswa;
    private String tahunMasuk;
    private Integer nominal;
    private String metodeBayar;
    private String bulanBayar;

    // Default constructor
    public PembayaranRequest() {}

    // Constructor with parameters
    public PembayaranRequest(String nis, String namaSiswa, String tahunMasuk, 
                           Integer nominal, String metodeBayar, String bulanBayar) {
        this.nis = nis;
        this.namaSiswa = namaSiswa;
        this.tahunMasuk = tahunMasuk;
        this.nominal = nominal;
        this.metodeBayar = metodeBayar;
        this.bulanBayar = bulanBayar;
    }

    // Getter methods
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

    public String getBulanBayar() {
        return bulanBayar;
    }

    // Setter methods
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

    public void setBulanBayar(String bulanBayar) {
        this.bulanBayar = bulanBayar;
    }

    @Override
    public String toString() {
        return "PembayaranRequest{" +
                "nis='" + nis + '\'' +
                ", namaSiswa='" + namaSiswa + '\'' +
                ", tahunMasuk='" + tahunMasuk + '\'' +
                ", nominal=" + nominal +
                ", metodeBayar='" + metodeBayar + '\'' +
                ", bulanBayar='" + bulanBayar + '\'' +
                '}';
    }
}