package com.example.demo.Controller;

import com.example.demo.Domain.Errand;
import com.example.demo.Domain.User;
import com.example.demo.Interfaces.ErrandRepository;
import com.example.demo.Interfaces.JdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@Controller
public class ErrandController {

    @Autowired
    private ErrandRepository errandRepository;


    @ResponseBody

    @GetMapping ("")
    public ModelAndView start () {
        return new ModelAndView("Index");
    }

    @GetMapping("/login")
    public ModelAndView index() {
        return new ModelAndView("Index");
    }

    @PostMapping("/login")
    public String verifyUser(HttpSession session, @RequestParam String username, @RequestParam String password) {
        User user = errandRepository.verifyUser(username, password);

            if (user != null) {
                session.setAttribute("user", user);
                return "redirect:/errands";
            }

        return "redirect:/login";
    }

    @GetMapping("/errands")
    public ModelAndView errands(HttpSession session) {
        if(session.getAttribute("user")!=null) {
            return new ModelAndView("Errands")
                    .addObject("errands", errandRepository.getErrands());
        }
        else {
            return new ModelAndView ("Index");
        }
    }

    @GetMapping("/errands/{errandId}")
    public ModelAndView errand (HttpSession session, @PathVariable long errandId) {
        if (session.getAttribute("user") !=null) {
            return new ModelAndView("Errand")
                    .addObject("errand", errandRepository.getErrand(errandId));
        }
        else{
            return new ModelAndView("Index");
        }
    }

    @GetMapping("/filed")
    public ModelAndView filedErrands (HttpSession session) {
        if (session.getAttribute("user") !=null) {
            return new ModelAndView("FiledErrands")
                    .addObject("filedErrands", errandRepository.getFiledErrands());
        }
        else {
            return new ModelAndView("Index");
        }
    }

    @PostMapping("/delete/{errandId}")
    public String refreshErrands(@PathVariable int errandId) {
        errandRepository.deleteErrand(errandId);
        return "redirect:/errands";
    }

    @PostMapping("/help/{errandId}")
    public String refreshStatus(@PathVariable int errandId, HttpSession session) {
        User user = (User)session.getAttribute("user");
        errandRepository.chooseErrand(errandId, user.name);
        return "redirect:/errands";
    }

    @PostMapping("/file/{errandId}")
    public String refreshWhenFiled(@PathVariable int errandId) {
        errandRepository.fileErrand(errandId);
        return "redirect:/errands";
    }

    @PostMapping("/reactivate/{errandId}")
    public String reactivation (@PathVariable int errandId) {
        errandRepository.reactivateErrand(errandId);
        return "redirect:/errands";
    }

    @GetMapping("/submit")
    public ModelAndView Submissions(HttpSession session) {
        if(session.getAttribute("user") !=null) {
            return new ModelAndView("SubmitErrand");
        }
        else {
            return new ModelAndView ("Index");
        }
    }

    @PostMapping("/submit")
    public String errand (HttpSession session, @RequestParam String topic, @RequestParam String errand) {
        User user = (User)session.getAttribute("user");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String creation = dtf.format(now);
        errandRepository.addErrand(user.name, topic, errand, creation);
        return "redirect:/errands";
    }

    @GetMapping("/newuser")
    public ModelAndView newUser() {
        return new ModelAndView("NewUser");
    }

    @PostMapping("/newuser")
    public String addUser(@RequestParam String name, @RequestParam String username, @RequestParam String password, @RequestParam String admin) {
        errandRepository.addUser(name, username, password, admin);
        return "redirect:";
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session, HttpServletResponse res) {
        session.invalidate();
        Cookie cookie = new Cookie("jsessionid", "");
        cookie.setMaxAge(0);
        res.addCookie(cookie);
        return new ModelAndView("Index");
    }
}
