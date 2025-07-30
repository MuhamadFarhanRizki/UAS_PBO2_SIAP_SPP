
package com.example.SPP.Service;

import com.example.SPP.Dto.PembayaranRequest;
import com.example.SPP.Model.Pembayaran;
import com.example.SPP.Repository.PembayaranRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PembayaranService {

    @Autowired
    private PembayaranRepository pembayaranRepository;

    public List<Pembayaran> getAllPembayaran() {
        return pembayaranRepository.findAll();
    }

    public Optional<Pembayaran> findById(Long id) {
        return pembayaranRepository.findById(id);
    }

    public List<Pembayaran> findByNis(String nis) {
        return pembayaranRepository.findByNis(nis);
    }

    // PERBAIKAN: Method save yang mendukung kedua tipe Date (java.util.Date dan
    // LocalDate)
    public void save(Pembayaran pembayaran) {
        try {
            // Set tanggal bayar jika belum ada - support untuk Date dan LocalDate
            if (pembayaran.getTanggalBayar() == null) {
                // Jika model menggunakan java.util.Date
                if (isUsingUtilDate(pembayaran)) {
                    // Gunakan reflection atau casting yang aman
                    setPembayaranDateUtil(pembayaran, new Date());
                } else {
                    // Jika model menggunakan LocalDate
                    setPembayaranDateLocal(pembayaran, LocalDate.now());
                }
            }

            System.out.println("Menyimpan pembayaran untuk NIS: " + pembayaran.getNis());
            System.out.println("Bulan: " + pembayaran.getBulanBayar());
            System.out.println("Status: " + pembayaran.getStatusBayar());

            pembayaranRepository.save(pembayaran);
            System.out.println("Pembayaran berhasil disimpan!");

        } catch (Exception e) {
            System.out.println("Error saat menyimpan pembayaran: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Helper method untuk menentukan tipe Date yang digunakan
    private boolean isUsingUtilDate(Pembayaran pembayaran) {
        try {
            // Coba set dengan Date - jika berhasil berarti menggunakan util.Date
            return pembayaran.getClass().getMethod("setTanggalBayar", Date.class) != null;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    // Helper method untuk set Date menggunakan java.util.Date
    private void setPembayaranDateUtil(Pembayaran pembayaran, Date date) {
        try {
            pembayaran.getClass().getMethod("setTanggalBayar", Date.class).invoke(pembayaran, date);
        } catch (Exception e) {
            System.out.println("Gagal set tanggal dengan util.Date: " + e.getMessage());
        }
    }

    // Helper method untuk set Date menggunakan LocalDate
    private void setPembayaranDateLocal(Pembayaran pembayaran, LocalDate date) {
        try {
            pembayaran.getClass().getMethod("setTanggalBayar", LocalDate.class).invoke(pembayaran, date);
        } catch (Exception e) {
            System.out.println("Gagal set tanggal dengan LocalDate: " + e.getMessage());
        }
    }

    // TAMBAHAN: Method untuk mengecek apakah pembayaran sudah ada
    public boolean isPembayaranExists(String nis, String bulanBayar) {
        try {
            List<Pembayaran> existing = pembayaranRepository.findByNisAndBulanBayar(nis, bulanBayar);
            boolean exists = existing != null && !existing.isEmpty();
            System.out.println("Cek pembayaran exists untuk NIS: " + nis + ", Bulan: " + bulanBayar + " = " + exists);
            return exists;
        } catch (Exception e) {
            System.out.println("Error saat cek pembayaran exists: " + e.getMessage());
            return false;
        }
    }

    // PERBAIKAN: Method delete dengan error handling
    public void delete(Long id) {
        try {
            if (pembayaranRepository.existsById(id)) {
                pembayaranRepository.deleteById(id);
                System.out.println("Pembayaran dengan ID " + id + " berhasil dihapus");
            } else {
                System.out.println("Pembayaran dengan ID " + id + " tidak ditemukan");
            }
        } catch (Exception e) {
            System.out.println("Error saat menghapus pembayaran: " + e.getMessage());
            throw e;
        }
    }

    public long countAll() {
        try {
            return pembayaranRepository.count();
        } catch (Exception e) {
            System.out.println("Error saat count pembayaran: " + e.getMessage());
            return 0;
        }
    }

    // PERBAIKAN: Method updateStatus dengan validasi
    public void updateStatus(Long id, String status) {
        try {
            Optional<Pembayaran> optional = pembayaranRepository.findById(id);
            if (optional.isPresent()) {
                Pembayaran pembayaran = optional.get();

                // Validasi status
                if (status == null || status.trim().isEmpty()) {
                    throw new IllegalArgumentException("Status tidak boleh kosong");
                }

                String oldStatus = pembayaran.getStatusBayar();
                pembayaran.setStatusBayar(status);

                pembayaranRepository.save(pembayaran);
                System.out.println(
                        "Status pembayaran ID " + id + " berubah dari '" + oldStatus + "' ke '" + status + "'");
            } else {
                System.out.println("Pembayaran dengan ID " + id + " tidak ditemukan");
                throw new RuntimeException("Pembayaran tidak ditemukan");
            }
        } catch (Exception e) {
            System.out.println("Error saat update status: " + e.getMessage());
            throw e;
        }
    }

    // Method yang sudah ada - tetap pertahankan untuk kompatibilitas
    public void simpanPembayaran(PembayaranRequest request) {
        try {
            Pembayaran pembayaran = new Pembayaran();
            pembayaran.setNis(request.getNis());
            pembayaran.setNamaSiswa(request.getNamaSiswa());
            pembayaran.setTahunMasuk(request.getTahunMasuk());
            pembayaran.setNominal(request.getNominal());
            pembayaran.setMetodeBayar(request.getMetodeBayar());
            pembayaran.setStatusBayar("Belum Lunas");
            pembayaran.setBulanBayar(request.getBulanBayar());

            // Set tanggal dengan handling yang fleksibel
            if (isUsingUtilDate(pembayaran)) {
                setPembayaranDateUtil(pembayaran, new Date());
            } else {
                setPembayaranDateLocal(pembayaran, LocalDate.now());
            }

            pembayaranRepository.save(pembayaran);
            System.out.println("Pembayaran dari PembayaranRequest berhasil disimpan");
        } catch (Exception e) {
            System.out.println("Error saat simpan pembayaran dari request: " + e.getMessage());
            throw e;
        }
    }

    // Hapus pembayaran berdasarkan id
    public void deleteById(Long id) {
        pembayaranRepository.deleteById(id);
    }

    public void bayarSPP(PembayaranRequest request) {
        try {
            Pembayaran pembayaran = new Pembayaran();
            pembayaran.setNis(request.getNis());
            pembayaran.setNamaSiswa(request.getNamaSiswa());
            pembayaran.setTahunMasuk(request.getTahunMasuk());
            pembayaran.setNominal(request.getNominal());
            pembayaran.setMetodeBayar(request.getMetodeBayar());
            pembayaran.setStatusBayar("Lunas");
            pembayaran.setBulanBayar(request.getBulanBayar());

            // Set tanggal dengan handling yang fleksibel
            if (isUsingUtilDate(pembayaran)) {
                setPembayaranDateUtil(pembayaran, new Date());
            } else {
                setPembayaranDateLocal(pembayaran, LocalDate.now());
            }

            pembayaranRepository.save(pembayaran);
            System.out.println("Pembayaran SPP berhasil disimpan dengan status Lunas");
        } catch (Exception e) {
            System.out.println("Error saat bayar SPP: " + e.getMessage());
            throw e;
        }
    }

    public List<Pembayaran> filterByTanggal(LocalDate start, LocalDate end) {
        try {
            return pembayaranRepository.findByTanggalBayarBetween(start, end);
        } catch (Exception e) {
            System.out.println("Error saat filter by tanggal: " + e.getMessage());
            return List.of(); // Return empty list
        }
    }

    // Method tambahan untuk mendukung controller
    public List<Pembayaran> findByStatusBayar(String statusBayar) {
        try {
            return pembayaranRepository.findByStatusBayar(statusBayar);
        } catch (Exception e) {
            System.out.println("Error saat find by status: " + e.getMessage());
            return List.of();
        }
    }

    public List<Pembayaran> findByBulanBayar(String bulanBayar) {
        try {
            return pembayaranRepository.findByBulanBayar(bulanBayar);
        } catch (Exception e) {
            System.out.println("Error saat find by bulan: " + e.getMessage());
            return List.of();
        }
    }

    public List<Pembayaran> findByTahunMasuk(String tahunMasuk) {
        try {
            return pembayaranRepository.findByTahunMasuk(tahunMasuk);
        } catch (Exception e) {
            System.out.println("Error saat find by tahun masuk: " + e.getMessage());
            return List.of();
        }
    }

    public List<Pembayaran> findByNisAndBulanBayar(String nis, String bulanBayar) {
        try {
            return pembayaranRepository.findByNisAndBulanBayar(nis, bulanBayar);
        } catch (Exception e) {
            System.out.println("Error saat find by NIS and bulan: " + e.getMessage());
            return List.of();
        }
    }

    public Long getTotalByStatus(String status) {
        try {
            Long total = pembayaranRepository.getTotalByStatus(status);
            return total != null ? total : 0L;
        } catch (Exception e) {
            System.out.println("Error saat get total by status: " + e.getMessage());
            return 0L;
        }
    }

    public long countByStatusBayar(String statusBayar) {
        try {
            return pembayaranRepository.countByStatusBayar(statusBayar);
        } catch (Exception e) {
            System.out.println("Error saat count by status: " + e.getMessage());
            return 0;
        }
    }

    // TAMBAHAN: Method utility untuk debugging
    public void debugPembayaran(Pembayaran pembayaran) {
        System.out.println("=== DEBUG PEMBAYARAN ===");
        System.out.println("NIS: " + pembayaran.getNis());
        System.out.println("Nama Siswa: " + pembayaran.getNamaSiswa());
        System.out.println("Tahun Masuk: " + pembayaran.getTahunMasuk());
        System.out.println("Nominal: " + pembayaran.getNominal());
        System.out.println("Metode Bayar: " + pembayaran.getMetodeBayar());
        System.out.println("Bulan Bayar: " + pembayaran.getBulanBayar());
        System.out.println("Status Bayar: " + pembayaran.getStatusBayar());
        System.out.println("Tanggal Bayar: " + pembayaran.getTanggalBayar());
        System.out.println("========================");
    }

    // TAMBAHAN: Method untuk validasi data pembayaran
    public boolean validatePembayaran(Pembayaran pembayaran) {
        if (pembayaran == null) {
            System.out.println("Validation failed: Pembayaran object is null");
            return false;
        }

        if (pembayaran.getNis() == null || pembayaran.getNis().trim().isEmpty()) {
            System.out.println("Validation failed: NIS is empty");
            return false;
        }

        if (pembayaran.getBulanBayar() == null || pembayaran.getBulanBayar().trim().isEmpty()) {
            System.out.println("Validation failed: Bulan bayar is empty");
            return false;
        }

        if (pembayaran.getMetodeBayar() == null || pembayaran.getMetodeBayar().trim().isEmpty()) {
            System.out.println("Validation failed: Metode bayar is empty");
            return false;
        }

        System.out.println("Validation passed for pembayaran");
        return true;
    }
}