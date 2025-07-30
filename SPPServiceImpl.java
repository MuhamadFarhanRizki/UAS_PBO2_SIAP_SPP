package com.example.SPP.Service;

import com.example.SPP.Model.SPP;
import com.example.SPP.Repository.SPPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SPPServiceImpl implements SPPService {
    
    @Autowired
    private SPPRepository sppRepository;
    
    @Override
    public List<SPP> getAll() {
        return sppRepository.findAll();
    }
    
    @Override
    public void save(SPP spp) {
        sppRepository.save(spp);
    }
    
    @Override
    public Optional<SPP> findById(Long id) {
        return sppRepository.findById(id);
    }
    
    @Override
    public void delete(Long id) {
        sppRepository.deleteById(id);
    }
    
    @Override
    public Optional<SPP> getNominalByTahun(String tahun) {
        return sppRepository.findByTahun(tahun);
    }
    
    @Override
    public Optional<SPP> findByTahun(String tahun) {
        return sppRepository.findByTahun(tahun);
    }
}