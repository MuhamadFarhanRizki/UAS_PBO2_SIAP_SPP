package com.example.SPP.Controller;

import com.example.SPP.Model.Pembayaran;
import com.example.SPP.Model.Siswa;
import com.example.SPP.Service.PembayaranService;
import com.example.SPP.Service.SiswaService;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/siswa")
public class SiswaController {

    @Autowired
    private SiswaService siswaService;

    @Autowired
    private PembayaranService pembayaranService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        Optional<Siswa> siswa = siswaService.findByNis(username);
        model.addAttribute("siswa", siswa.orElse(null));
        return "siswa_dashboard";
    }

    @GetMapping("/bayar")
    public String formBayar(HttpSession session, Model model) {
        String nis = (String) session.getAttribute("username");
        Optional<Siswa> siswaOpt = siswaService.findByNis(nis);

        if (siswaOpt.isPresent()) {
            Siswa siswa = siswaOpt.get();
            model.addAttribute("siswa", siswa);

            // Untuk debugging
            System.out.println("Siswa ditemukan: " + siswa.getNama());
            System.out.println("NIS: " + siswa.getNis());
            System.out.println("Nominal: " + siswa.getNominal());
        } else {
            System.out.println("Siswa tidak ditemukan dengan NIS: " + nis);
            model.addAttribute("error", "Data siswa tidak ditemukan");
        }

        return "siswa_bayar";
    }

    @PostMapping("/bayar")
    public String prosesBayar(
            HttpSession session,
            @RequestParam("tanggal_bayar") String tanggalBayar,
            @RequestParam("metode_bayar") String metodeBayar,
            @RequestParam("bulan_bayar") String bulanBayar,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            String nis = (String) session.getAttribute("username");
            Optional<Siswa> siswaOpt = siswaService.findByNis(nis);

            if (siswaOpt.isEmpty()) {
                model.addAttribute("error", "Data siswa tidak ditemukan");
                return formBayar(session, model);
            }

            Siswa siswa = siswaOpt.get();

            // Cek apakah pembayaran untuk bulan ini sudah ada
            boolean pembayaranExists = pembayaranService.isPembayaranExists(nis, bulanBayar);
            if (pembayaranExists) {
                model.addAttribute("error", "Pembayaran untuk bulan " + bulanBayar + " sudah ada");
                model.addAttribute("siswa", siswa);
                return "siswa_bayar";
            }

            // Buat objek pembayaran baru
            Pembayaran pembayaran = new Pembayaran();
            pembayaran.setNis(siswa.getNis());
            pembayaran.setNamaSiswa(siswa.getNama());
            pembayaran.setTahunMasuk(siswa.getTahunMasuk());
            pembayaran.setNominal(siswa.getNominal());
            pembayaran.setTanggalBayar(LocalDate.parse(tanggalBayar));
            pembayaran.setMetodeBayar(metodeBayar);
            pembayaran.setBulanBayar(bulanBayar);

            // Set status berdasarkan metode pembayaran
            if ("Cash".equals(metodeBayar)) {
                pembayaran.setStatusBayar("Berhasil");
                redirectAttributes.addFlashAttribute("successMessage", "Pembayaran berhasil!");
                redirectAttributes.addFlashAttribute("paymentType", "cash");
            } else if ("Transfer".equals(metodeBayar)) {
                pembayaran.setStatusBayar("Proses");
                redirectAttributes.addFlashAttribute("successMessage", "Silahkan transfer ke nomor rekening berikut:");
                redirectAttributes.addFlashAttribute("paymentType", "transfer");
                redirectAttributes.addFlashAttribute("accountNumber", "1234211");
                redirectAttributes.addFlashAttribute("bankName", "Bank Jaga");
                redirectAttributes.addFlashAttribute("accountName", "Siap Bayar SPP");
            }

            // Simpan pembayaran
            pembayaranService.save(pembayaran);

            return "redirect:/siswa/history";

        } catch (Exception e) {
            System.err.println("Error saat memproses pembayaran: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Terjadi kesalahan saat memproses pembayaran");
            return formBayar(session, model);
        }
    }

    @GetMapping("/history")
    public String history(HttpSession session, Model model,
            @ModelAttribute("paymentType") String paymentType,
            @ModelAttribute("accountNumber") String accountNumber,
            @ModelAttribute("bankName") String bankName,
            @ModelAttribute("accountName") String accountName) {

        String nis = (String) session.getAttribute("username");

        // Get siswa data
        siswaService.findByNis(nis).ifPresent(siswa -> model.addAttribute("siswa", siswa));

        // Get payment history
        model.addAttribute("pembayaranList", pembayaranService.findByNis(nis));

        // Forward flash attributes (jika ada)
        if (paymentType != null && !paymentType.isEmpty()) {
            model.addAttribute("paymentType", paymentType);
        }
        if (accountNumber != null)
            model.addAttribute("accountNumber", accountNumber);
        if (bankName != null)
            model.addAttribute("bankName", bankName);
        if (accountName != null)
            model.addAttribute("accountName", accountName);

        return "siswa_history";
    }

    @GetMapping("/profil")
    public String profil(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        Optional<Siswa> siswa = siswaService.findByNis(username);
        model.addAttribute("user", siswa.orElse(null));
        return "siswa_profil";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}