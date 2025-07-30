package com.example.SPP.Repository;

import com.example.SPP.Model.Pembayaran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PembayaranRepository extends JpaRepository<Pembayaran, Long> {
    
    // Mencari pembayaran berdasarkan NIS
    List<Pembayaran> findByNis(String nis);
    
    // Mencari pembayaran berdasarkan nama siswa
    List<Pembayaran> findByNamaSiswa(String namaSiswa);
    
    // Mencari pembayaran berdasarkan status bayar
    List<Pembayaran> findByStatusBayar(String statusBayar);
    
    // Mencari pembayaran berdasarkan bulan bayar
    List<Pembayaran> findByBulanBayar(String bulanBayar);
    
    // Mencari pembayaran berdasarkan tahun masuk
    List<Pembayaran> findByTahunMasuk(String tahunMasuk);
    
    // Mencari pembayaran berdasarkan rentang tanggal
    List<Pembayaran> findByTanggalBayarBetween(LocalDate startDate, LocalDate endDate);
    
    // Mencari pembayaran berdasarkan NIS dan bulan bayar
    List<Pembayaran> findByNisAndBulanBayar(String nis, String bulanBayar);
    
    // Mencari pembayaran berdasarkan NIS dan status bayar
    List<Pembayaran> findByNisAndStatusBayar(String nis, String statusBayar);
    
    // Query kustom untuk mencari total pembayaran berdasarkan status
    @Query("SELECT SUM(p.nominal) FROM Pembayaran p WHERE p.statusBayar = :status")
    Long getTotalByStatus(@Param("status") String status);
    
    // Query kustom untuk mencari pembayaran berdasarkan NIS dan tahun
    @Query("SELECT p FROM Pembayaran p WHERE p.nis = :nis AND YEAR(p.tanggalBayar) = :tahun")
    List<Pembayaran> findByNisAndTahun(@Param("nis") String nis, @Param("tahun") int tahun);
    
    // Menghitung jumlah pembayaran berdasarkan status
    long countByStatusBayar(String statusBayar);
}