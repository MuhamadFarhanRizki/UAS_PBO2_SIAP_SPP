package com.example.SPP.Repository;

import com.example.SPP.Model.Siswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SiswaRepository extends JpaRepository<Siswa, Long> {
    
    // Only include methods that definitely work
    Optional<Siswa> findByNis(String nis);
    
    boolean existsByNis(String nis);
    
    List<Siswa> findByNamaContainingIgnoreCase(String nama);
    
    void deleteByNis(String nis);
    
    // Remove problematic method for now:
    // List<Siswa> findByTahunMasuk(String tahunMasuk);
}