
package com.example.SPP.Controller;

import com.example.SPP.Model.User;
import com.example.SPP.Service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class AuthController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/")
    public String home() {
        return "tampilan_awal";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        User user = userService.authenticate(username, password);
        if (user != null) {
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            if ("admin".equals(user.getRole())) {
                return "redirect:/admin/dashboard";
            } else if ("siswa".equals(user.getRole())) {
                model.addAttribute("siswa", user); // 👉 kirim ke Thymeleaf
                model.addAttribute("statusBayar", "Belum Lunas"); // misalnya
                return "/siswa_dashboard";
            }
        }
        return "redirect:/?error";
    }
    


    @GetMapping("/admin_dashboard")
    public String admin_dashboard(HttpSession session) {
        session.invalidate();
        return "admin_dashboard";
    }

    @GetMapping("/admin_data-siswa")
    public String admin_data_siswa(HttpSession session) {
        session.invalidate();
        return "admin_data-siswa";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "tampilan_awal";
    }

}
