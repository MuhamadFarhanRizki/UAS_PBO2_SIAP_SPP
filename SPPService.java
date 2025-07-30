package com.example.SPP.Service;

import com.example.SPP.Model.SPP;
import java.util.List;
import java.util.Optional;

public interface SPPService {
    List<SPP> getAll();
    void save(SPP spp);
    Optional<SPP> findById(Long id);
    void delete(Long id);
    Optional<SPP> getNominalByTahun(String tahun);
    Optional<SPP> findByTahun(String tahun);
}