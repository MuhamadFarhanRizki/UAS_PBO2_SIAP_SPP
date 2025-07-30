package com.example.SPP.Controller;
import com.example.SPP.Model.Siswa;
import com.example.SPP.Dto.PembayaranRequest;
import com.example.SPP.Model.Pembayaran;
import com.example.SPP.Service.SiswaService;
import com.example.SPP.Service.PembayaranService;
import com.example.SPP.Service.SPPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class PembayaranController {
    
    @Autowired
    private SiswaService siswaService;
    
    @Autowired
    private PembayaranService pembayaranService;
    
    @Autowired
    private SPPService sppService;
    
    // ✅ PERBAIKAN 1: Handle Optional<Siswa> dengan benar
    @GetMapping("/form")
    public String showFormPembayaran(Model model, Principal principal) {
        String username = principal.getName();
        
        // ✅ Ambil data siswa dari Optional
        Optional<Siswa> siswaOptional = siswaService.getSiswaByUsername(username);
        
        if (siswaOptional.isPresent()) {
            Siswa siswa = siswaOptional.get();
            model.addAttribute("siswa", siswa);
            
            // ✅ Ambil nominal dari data siswa yang sudah ada atau dari SPP
            Integer nominal = siswa.getNominal();
            if (nominal == null || nominal == 0) {
                // Jika nominal siswa null, ambil dari tabel SPP
                nominal = sppService.findByTahun(siswa.getTahunMasuk())
                    .map(spp -> spp.getNominal())
                    .orElse(0);
            }
            model.addAttribute("nominal", nominal);
        } else {
            // ✅ Handle jika siswa tidak ditemukan
            model.addAttribute("error", "Data siswa tidak ditemukan");
            return "error"; // atau redirect ke halaman error
        }
        
        return "bayar-spp";
    }
    
    // ✅ Simpan pembayaran oleh siswa
    @PostMapping("/bayar")
    public String bayarSpp(@ModelAttribute PembayaranRequest request) {
        pembayaranService.bayarSPP(request);
        return "redirect:/riwayat";
    }
    
    // ✅ PERBAIKAN 2: Handle Optional<Siswa> untuk riwayat pembayaran
    @GetMapping("/riwayat")
    public String historyPembayaranSiswa(Model model, Principal principal) {
        String username = principal.getName();
        Optional<Siswa> siswaOptional = siswaService.getSiswaByUsername(username);
        if (siswaOptional.isPresent()) {
            Siswa siswa = siswaOptional.get();
            List<Pembayaran> history = pembayaranService.findByNis(siswa.getNis());
            model.addAttribute("listPembayaran", history);
            model.addAttribute("siswa", siswa);
        } else {
            model.addAttribute("error", "Data siswa tidak ditemukan");
            model.addAttribute("listPembayaran", List.of());
        }
        return "riwayat-spp";
    }
    
    // ✅ ALTERNATIF: Method helper untuk mendapatkan siswa dengan error handling
    private Siswa getSiswaByUsernameOrThrow(String username) {
        return siswaService.getSiswaByUsername(username)
            .orElseThrow(() -> new RuntimeException("Siswa dengan username " + username + " tidak ditemukan"));
    }
    
    // ✅ Contoh penggunaan method helper (opsional)
    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        try {
            String username = principal.getName();
            Siswa siswa = getSiswaByUsernameOrThrow(username);
            model.addAttribute("siswa", siswa);
            return "profile";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}