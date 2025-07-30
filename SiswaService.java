package com.example.SPP.Service;

import com.example.SPP.Model.Siswa;
import java.util.List;
import java.util.Optional;

public interface SiswaService {
    List<Siswa> getAll();
    List<Siswa> findAll();
    Siswa save(Siswa siswa);
    void saveSiswa(Siswa siswa);
    
    // IMPORTANT: These method signatures must match what your controller expects
    Optional<Siswa> findByNis(String nis);           // Find by NIS (String)
    Optional<Siswa> findById(Long id);               // Find by ID (Long) - Used in controller
    
    void delete(String nis);                         // Delete by NIS (String)
    void deleteById(Long id);                        // Delete by ID (Long) - Used in controller  
    void deleteByNis(String nis);                    // Explicit delete by NIS
    
    long countAll();
    Optional<Siswa> getSiswaByUsername(String username);
    boolean existsByNis(String nis);
    List<Siswa> searchByNama(String nama);
    List<Siswa> filterByTahunMasuk(String tahunMasuk);
    void validateSiswa(Siswa siswa);
    Siswa update(Siswa siswa);
}