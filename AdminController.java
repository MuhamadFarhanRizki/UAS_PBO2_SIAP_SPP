package com.example.SPP.Controller;

import com.example.SPP.Model.Pembayaran;
import com.example.SPP.Model.SPP;
import com.example.SPP.Model.User;
import com.example.SPP.Model.Siswa;
import com.example.SPP.Service.PembayaranService;
import com.example.SPP.Service.SiswaService;
import com.example.SPP.Service.UserServiceImpl;
import com.example.SPP.Service.SPPService;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SiswaService siswaService;

    @Autowired
    private PembayaranService pembayaranService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private SPPService sppService;

    // Dashboard
    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("totalSiswa", siswaService.countAll());
        model.addAttribute("totalTransaksi", pembayaranService.countAll());
        return "admin_dashboard";
    }

    // Halaman siswa
    @GetMapping("/siswa")
    public String dataSiswa(Model model) {
        try {
            model.addAttribute("siswaList", Optional.ofNullable(siswaService.getAll()).orElse(new ArrayList<>()));
            model.addAttribute("sppList", Optional.ofNullable(sppService.getAll()).orElse(new ArrayList<>()));
            model.addAttribute("siswaForm", new Siswa());
            return "admin_data-siswa";
        } catch (Exception e) {
            model.addAttribute("siswaList", new ArrayList<>());
            model.addAttribute("sppList", new ArrayList<>());
            model.addAttribute("siswaForm", new Siswa());
            model.addAttribute("error", "Terjadi kesalahan saat memuat data");
            return "admin_data-siswa";
        }
    }

    @PostMapping("/siswa/simpan")
    public String simpanSiswa(@RequestParam Map<String, String> params) {
        System.out.println("=== MULAI PROSES SIMPAN SISWA ===");

        // Log semua parameter yang diterima
        System.out.println("Parameter yang diterima:");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            System.out.println("- " + entry.getKey() + ": [" + entry.getValue() + "]");
        }

        try {
            String idStr = params.get("id");
            String nis = params.get("nis");
            String nama = params.get("nama");
            String jenisKelamin = params.get("jenisKelamin");
            String tanggalLahir = params.get("tanggalLahir");
            String sppIdStr = params.get("tahunMasuk");
            String nominalValue = params.get("nominalValue");

            System.out.println("Data yang akan diproses:");
            System.out.println("- ID: " + idStr);
            System.out.println("- NIS: " + nis);
            System.out.println("- Nama: " + nama);
            System.out.println("- Jenis Kelamin: " + jenisKelamin);
            System.out.println("- Tanggal Lahir: " + tanggalLahir);
            System.out.println("- SPP ID: " + sppIdStr);

            // Validasi input
            if (nis == null || nis.trim().isEmpty()) {
                return "redirect:/admin/siswa?error=nis_required";
            }
            if (nama == null || nama.trim().isEmpty()) {
                return "redirect:/admin/siswa?error=nama_required";
            }
            if (jenisKelamin == null || jenisKelamin.trim().isEmpty()) {
                return "redirect:/admin/siswa?error=jenis_kelamin_required";
            }
            if (tanggalLahir == null || tanggalLahir.trim().isEmpty()) {
                return "redirect:/admin/siswa?error=tanggal_lahir_required";
            }
            if (sppIdStr == null || sppIdStr.trim().isEmpty()) {
                return "redirect:/admin/siswa?error=tahun_masuk_required";
            }

            // ===== PERBAIKAN: KONSISTEN GUNAKAN LONG UNTUK ID =====
            boolean isUpdate = idStr != null && !idStr.trim().isEmpty() && !idStr.equals("0");
            Siswa siswa = null;
            String oldNis = null;

            if (isUpdate) {
                try {
                    Long id = Long.parseLong(idStr.trim());
                    System.out.println("Mencari siswa dengan ID: " + id);

                    Optional<Siswa> siswaOpt = siswaService.findById(id);
                    if (siswaOpt.isPresent()) {
                        siswa = siswaOpt.get();
                        oldNis = siswa.getNis();
                        System.out.println("✓ Siswa ditemukan untuk update. NIS lama: " + oldNis);
                    } else {
                        System.err.println("Error: Siswa dengan ID " + id + " tidak ditemukan");
                        return "redirect:/admin/siswa?error=siswa_not_found";
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error: Format ID tidak valid: " + idStr);
                    return "redirect:/admin/siswa?error=invalid_id_format";
                }
            } else {
                siswa = new Siswa();
                System.out.println("✓ Membuat objek siswa baru");
            }

            // Set data siswa
            siswa.setNis(nis.trim());
            siswa.setNama(nama.trim());
            siswa.setJenisKelamin(jenisKelamin.trim());
            siswa.setTanggalLahir(tanggalLahir.trim());

            // Proses SPP
            Long sppId;
            try {
                sppId = Long.parseLong(sppIdStr.trim());
                System.out.println("SPP ID yang akan dicari: " + sppId);
            } catch (NumberFormatException e) {
                System.err.println("Error: Format SPP ID tidak valid: " + sppIdStr);
                return "redirect:/admin/siswa?error=invalid_spp_id";
            }

            Optional<SPP> sppOptional = sppService.findById(sppId);
            if (sppOptional.isEmpty()) {
                System.err.println("Error: SPP dengan ID " + sppId + " tidak ditemukan");
                return "redirect:/admin/siswa?error=spp_not_found";
            }

            SPP spp = sppOptional.get();
            siswa.setSpp(spp);
            siswa.setTahunMasuk(String.valueOf(spp.getId()));

            // Set nominal
            if (nominalValue != null && !nominalValue.trim().isEmpty()) {
                try {
                    String cleanNominal = nominalValue.replaceAll("[^0-9]", "");
                    if (!cleanNominal.isEmpty()) {
                        siswa.setNominal(Integer.parseInt(cleanNominal));
                    } else {
                        siswa.setNominal(spp.getNominal());
                    }
                } catch (NumberFormatException e) {
                    siswa.setNominal(spp.getNominal());
                }
            } else {
                siswa.setNominal(spp.getNominal());
            }

            // Simpan data siswa
            siswa = siswaService.save(siswa);
            System.out.println("✓ Data siswa berhasil disimpan dengan ID: " + siswa.getId());

            // Proses user otomatis
            try {
                if (isUpdate && oldNis != null) {
                    updateUserForSiswa(oldNis, nis.trim(), nama.trim(), tanggalLahir.trim());
                } else {
                    createNewUserForSiswa(nis.trim(), nama.trim(), tanggalLahir.trim());
                }
            } catch (Exception userException) {
                System.err.println("Error saat membuat/update user: " + userException.getMessage());
                return "redirect:/admin/siswa?success=true&warning=user_creation_failed";
            }

            return "redirect:/admin/siswa?success=true";

        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/admin/siswa?error=unexpected_error";
        }
    }

    @GetMapping("/siswa/edit/{id}")
    public String editSiswaById(@PathVariable("id") Long id, Model model) {
        System.out.println("=== EDIT SISWA DIPANGGIL ===");
        System.out.println("ID yang diterima: [" + id + "]");

        try {
            Optional<Siswa> siswaOpt = siswaService.findById(id);

            if (siswaOpt.isPresent()) {
                Siswa siswa = siswaOpt.get();
                System.out.println("✓ Siswa ditemukan: " + siswa.getNama() + " (NIS: " + siswa.getNis() + ")");

                // Set data untuk form edit
                model.addAttribute("siswa", siswa);
                model.addAttribute("siswaForm", siswa);

                // Set data pendukung
                model.addAttribute("siswaList", siswaService.getAll());
                model.addAttribute("sppList", sppService.getAll());
                model.addAttribute("isEdit", true);

                return "admin_data-siswa";

            } else {
                System.err.println("❌ Siswa dengan ID " + id + " tidak ditemukan");

                // Debug info
                List<Siswa> allSiswa = siswaService.getAll();
                String availableIds = allSiswa.stream()
                        .map(s -> s.getId().toString())
                        .collect(Collectors.joining(","));

                return "redirect:/admin/siswa?error=siswa_not_found&searched_id=" + id +
                        "&available_ids=" + availableIds;
            }

        } catch (Exception e) {
            System.err.println("❌ Error saat mencari siswa: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/admin/siswa?error=edit_failed";
        }
    }

    @GetMapping("/siswa/delete/{id}")
    public String deleteSiswaById(@PathVariable("id") Long id) {
        System.out.println("=== DELETE SISWA DIPANGGIL ===");
        System.out.println("ID yang diterima: [" + id + "]");

        try {
            Optional<Siswa> siswaOpt = siswaService.findById(id);

            if (siswaOpt.isPresent()) {
                Siswa siswa = siswaOpt.get();
                String nis = siswa.getNis();
                String nama = siswa.getNama();

                // Hapus pembayaran terkait
                try {
                    List<Pembayaran> pembayaranTerkait = pembayaranService.findByNis(nis);
                    if (pembayaranTerkait != null && !pembayaranTerkait.isEmpty()) {
                        for (Pembayaran pembayaran : pembayaranTerkait) {
                            pembayaranService.deleteById(pembayaran.getId());
                        }
                    }
                } catch (Exception e) {
                    System.err.println("⚠️ Error menghapus pembayaran: " + e.getMessage());
                }

                // Hapus siswa
                siswaService.deleteById(id);
                System.out.println("✓ Siswa berhasil dihapus");

                // Hapus user terkait
                try {
                    Optional<User> userOpt = userService.findByUsername(nis);
                    if (userOpt.isPresent()) {
                        User user = userOpt.get();
                        if ("siswa".equals(user.getRole())) {
                            userService.deleteByUsername(user.getUsername());
                        }
                    }
                } catch (Exception e) {
                    System.err.println("⚠️ Error menghapus user: " + e.getMessage());
                }

                return "redirect:/admin/siswa?success=deleted&nama=" + nama;

            } else {
                System.err.println("❌ Siswa dengan ID " + id + " tidak ditemukan");

                List<Siswa> allSiswa = siswaService.getAll();
                String availableIds = allSiswa.stream()
                        .map(s -> s.getId().toString())
                        .collect(Collectors.joining(","));

                return "redirect:/admin/siswa?error=siswa_not_found&searched_id=" + id +
                        "&available_ids=" + availableIds;
            }

        } catch (Exception e) {
            System.err.println("❌ Error saat menghapus siswa: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/admin/siswa?error=delete_failed";
        }
    }

    // ===== HELPER METHODS =====

    private void createNewUserForSiswa(String nis, String nama, String tanggalLahir) {
        System.out.println("=== MEMBUAT USER BARU ===");

        try {
            Optional<User> existingUser = userService.findByUsername(nis);
            if (existingUser.isPresent()) {
                System.out.println("⚠️ User dengan username " + nis + " sudah ada");
                return;
            }

            User newUser = new User();
            newUser.setUsername(nis);
            newUser.setPassword(tanggalLahir.replaceAll("-", ""));
            newUser.setNama(nama);
            newUser.setRole("siswa");

            userService.saveUser(newUser);
            System.out.println("✓ User baru berhasil dibuat: " + nis);

        } catch (Exception e) {
            System.err.println("❌ Gagal membuat user: " + e.getMessage());
            throw e;
        }
    }

    private void updateUserForSiswa(String oldNis, String newNis, String nama, String tanggalLahir) {
        System.out.println("=== UPDATE USER SISWA ===");

        try {
            Optional<User> existingUser = userService.findByUsername(oldNis);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setUsername(newNis);
                user.setPassword(tanggalLahir.replaceAll("-", ""));
                user.setNama(nama);
                user.setRole("siswa");

                userService.saveUser(user);
                System.out.println("✓ User berhasil diupdate: " + newNis);
            } else {
                createNewUserForSiswa(newNis, nama, tanggalLahir);
            }
        } catch (Exception e) {
            System.err.println("❌ Gagal update user: " + e.getMessage());
            throw e;
        }
    }

    // ===== DEBUG ENDPOINTS (OPSIONAL) =====

    @GetMapping("/debug/all-siswa")
    @ResponseBody
    public String debugAllSiswa() {
        try {
            List<Siswa> allSiswa = siswaService.getAll();
            StringBuilder result = new StringBuilder("<h3>DEBUG: Semua Data Siswa</h3>");
            result.append("Total siswa: ").append(allSiswa.size()).append("<br><br>");

            for (Siswa s : allSiswa) {
                result.append("ID: ").append(s.getId())
                        .append(" | NIS: ").append(s.getNis())
                        .append(" | Nama: ").append(s.getNama())
                        .append("<br>");
            }

            return result.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // ===== PEMBAYARAN ENDPOINTS =====

    @GetMapping("/data-pembayaran")
    public String dataPembayaran(Model model) {
        try {
            model.addAttribute("siswaList", siswaService.getAll());
            model.addAttribute("listPembayaran", pembayaranService.getAllPembayaran());
            model.addAttribute("pembayaran", new Pembayaran());
            return "admin_data-pembayaran";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Terjadi kesalahan saat memuat data pembayaran");
            model.addAttribute("pembayaran", new Pembayaran());
            return "admin_data-pembayaran";
        }
    }

    @PostMapping("/pembayaran/simpan")
    public String simpanPembayaran(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam("nis") String nis,
            @RequestParam("metodeBayar") String metodeBayar,
            @RequestParam("bulanBayar") String bulanBayar,
            @RequestParam("statusBayar") String statusBayar,
            @RequestParam("tanggalBayar") String tanggalBayarStr,
            HttpSession session) {

        System.out.println("=== PEMBAYARAN ENDPOINT DIPANGGIL ===");
        System.out.println("NIS: " + nis);

        try {
            // Validasi input
            if (nis == null || nis.trim().isEmpty()) {
                return "redirect:/admin/data-pembayaran?error=nis_required";
            }

            // Cari siswa berdasarkan NIS
            Optional<Siswa> siswaOpt = siswaService.findByNis(nis.trim());
            if (siswaOpt.isEmpty()) {
                return "redirect:/admin/data-pembayaran?error=siswa_not_found";
            }

            Siswa siswa = siswaOpt.get();

            // Cek duplikasi pembayaran
            boolean pembayaranExists = false;
            if (id == null) {
                pembayaranExists = pembayaranService.isPembayaranExists(nis.trim(), bulanBayar);
            } else {
                Optional<Pembayaran> existingPembayaran = pembayaranService.findById(id);
                if (existingPembayaran.isPresent()) {
                    if (!existingPembayaran.get().getNis().equals(nis.trim())
                            || !existingPembayaran.get().getBulanBayar().equals(bulanBayar)) {
                        pembayaranExists = pembayaranService.isPembayaranExists(nis.trim(), bulanBayar);
                    }
                }
            }

            if (pembayaranExists) {
                return "redirect:/admin/data-pembayaran?error=pembayaran_exists";
            }

            Pembayaran pembayaran;
            if (id != null) {
                pembayaran = pembayaranService.findById(id).orElse(new Pembayaran());
            } else {
                pembayaran = new Pembayaran();
            }

            pembayaran.setNis(siswa.getNis());
            pembayaran.setNamaSiswa(siswa.getNama());
            pembayaran.setTahunMasuk(siswa.getTahunMasuk());
            pembayaran.setNominal(siswa.getNominal());
            pembayaran.setMetodeBayar(metodeBayar);
            pembayaran.setBulanBayar(bulanBayar);
            pembayaran.setStatusBayar(statusBayar);

            try {
                pembayaran.setTanggalBayar(LocalDate.parse(tanggalBayarStr));
            } catch (Exception ex) {
                return "redirect:/admin/data-pembayaran?error=tanggal_invalid";
            }

            pembayaranService.save(pembayaran);
            return "redirect:/admin/data-pembayaran?success=true";

        } catch (Exception e) {
            System.err.println("Error saat menyimpan pembayaran: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/admin/data-pembayaran?error=save_failed";
        }
    }

    @GetMapping("/pembayaran/edit/{id}")
    public String editPembayaranById(@PathVariable Long id, Model model) {
        Optional<Pembayaran> pembayaranOpt = pembayaranService.findById(id);
        model.addAttribute("pembayaran", pembayaranOpt.orElse(new Pembayaran()));
        model.addAttribute("siswaList", siswaService.getAll());
        model.addAttribute("listPembayaran", pembayaranService.getAllPembayaran());
        return pembayaranOpt.isPresent() ? "admin_data-pembayaran"
                : "redirect:/admin/data-pembayaran?error=pembayaran_not_found";
    }

    @GetMapping("/pembayaran/delete/{id}")
    public String deletePembayaranById(@PathVariable Long id) {
        if (pembayaranService.findById(id).isPresent()) {
            pembayaranService.deleteById(id);
            return "redirect:/admin/data-pembayaran?success=deleted";
        }
        return "redirect:/admin/data-pembayaran?error=pembayaran_not_found";
    }

    @GetMapping("/bayar")
    public String dataBayar(Model model) {
        try {
            List<Pembayaran> pembayaranList = pembayaranService.getAllPembayaran();
            model.addAttribute("pembayaranList", pembayaranList != null ? pembayaranList : new ArrayList<>());
            return "admin/bayar";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Terjadi kesalahan saat memuat data pembayaran");
            return "admin/bayar";
        }
    }

    @PostMapping("/ubah-status")
    public String ubahStatus(@RequestParam Long id, @RequestParam String status) {
        try {
            pembayaranService.updateStatus(id, status);
            return "redirect:/admin/bayar?success=status_updated";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/bayar?error=update_failed";
        }
    }

    // ===== SPP ENDPOINTS =====

    @GetMapping("/spp")
    public String dataSpp(Model model) {
        List<SPP> sppList = sppService.getAll();
        model.addAttribute("listSPP", sppList);
        model.addAttribute("sppForm", new SPP());
        return "admin_data-spp";
    }

    @PostMapping("/spp/simpan")
    public String simpanSpp(@RequestParam("tahun") String tahun,
            @RequestParam("nominal") Integer nominal,
            @RequestParam(value = "id", required = false) Long id) {
        SPP spp = new SPP();
        if (id != null && id > 0) {
            spp = sppService.findById(id).orElse(new SPP());
        }
        spp.setTahun(tahun);
        spp.setNominal(nominal);
        sppService.save(spp);
        return "redirect:/admin/spp";
    }

    @GetMapping("/spp/edit/{id}")
    public String editSpp(@PathVariable Long id, Model model) {
        model.addAttribute("sppForm", sppService.findById(id).orElse(new SPP()));
        model.addAttribute("listSPP", sppService.getAll());
        return "admin_data-spp";
    }

    @GetMapping("/spp/delete/{id}")
    public String deleteSpp(@PathVariable Long id) {
        sppService.delete(id);
        return "redirect:/admin/spp";
    }

    @GetMapping("/api/spp/nominal/{tahun}")
    @ResponseBody
    public ResponseEntity<Integer> getNominalByTahun(@PathVariable String tahun) {
        return sppService.getAll().stream()
                .filter(spp -> spp.getTahun().equals(tahun))
                .findFirst()
                .map(spp -> ResponseEntity.ok(spp.getNominal()))
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== PROFIL ENDPOINT =====

    @GetMapping("/profil")
    public String profil(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        Optional<User> user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "admin/profil";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}