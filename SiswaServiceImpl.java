package com.example.SPP.Service;

import com.example.SPP.Model.Siswa;
import com.example.SPP.Repository.SiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SiswaServiceImpl implements SiswaService {

    @Autowired
    private SiswaRepository siswaRepository;

    @Override
    public List<Siswa> getAll() {
        return siswaRepository.findAll();
    }

    @Override
    public List<Siswa> findAll() {
        return siswaRepository.findAll();
    }

    @Override
    public Siswa save(Siswa siswa) {
        return siswaRepository.save(siswa);
    }

    @Override
    public void saveSiswa(Siswa siswa) {
        siswaRepository.save(siswa);
    }

    @Override
    public Optional<Siswa> findByNis(String nis) {
        return siswaRepository.findByNis(nis);
    }

    @Override
    public Optional<Siswa> findById(Long id) {
        return siswaRepository.findById(id);
    }

    // This method is already implemented above as findByNis

    @Override
    public void delete(String nis) {
        // This method now delegates to deleteByNis for clarity
        deleteByNis(nis);
    }

    @Override
    public void deleteById(Long id) {
        siswaRepository.deleteById(id);
    }

    @Override
    public void deleteByNis(String nis) {
        Optional<Siswa> siswa = siswaRepository.findByNis(nis);
        siswa.ifPresent(siswaRepository::delete);
    }

    @Override
    public long countAll() {
        return siswaRepository.count();
    }

    @Override
    public Optional<Siswa> getSiswaByUsername(String username) {
        return siswaRepository.findByNis(username);
    }

    @Override
    public boolean existsByNis(String nis) {
        return siswaRepository.existsByNis(nis);
    }

    @Override
    public List<Siswa> searchByNama(String nama) {
        return siswaRepository.findByNamaContainingIgnoreCase(nama);
    }

    @Override
    public List<Siswa> filterByTahunMasuk(String tahunMasuk) {
        // TEMPORARY FIX: Filter in memory until field issue is resolved
        return siswaRepository.findAll().stream()
                .filter(siswa -> siswa.getTahunMasuk() != null && 
                               siswa.getTahunMasuk().equals(tahunMasuk))
                .collect(Collectors.toList());
    }

    @Override
    public void validateSiswa(Siswa siswa) {
        if (siswa.getNis() == null || siswa.getNis().trim().isEmpty()) {
            throw new IllegalArgumentException("NIS tidak boleh kosong");
        }
        if (siswa.getNama() == null || siswa.getNama().trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong");
        }
    }

    @Override
    public Siswa update(Siswa siswa) {
        return siswaRepository.save(siswa);
    }
}