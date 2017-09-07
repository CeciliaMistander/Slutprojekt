package com.example.demo.Controller;

import com.example.demo.Domain.Errand;
import com.example.demo.Interfaces.ErrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Calendar;

@Controller
public class ErrandController {

    @Autowired
    private ErrandRepository errandRepository;


    @ResponseBody

    @GetMapping ("")
    public ModelAndView Start () {
        return new ModelAndView("Index");
    }

    @GetMapping("/login")
    public ModelAndView Index() {
        return new ModelAndView("Index");
    }

    @PostMapping("/login")
    public String verifyUser(HttpSession session, @RequestParam String username, @RequestParam String password) {
        System.out.println("Hej");
        if (errandRepository.verifyUser(username,password)) {
            session.setAttribute("user",username);
            return "redirect:/errands";
        }
        return "redirect:/login";
    }

    @GetMapping("/errands")
    public ModelAndView Errands(HttpSession session) {
        if(session.getAttribute("user")!=null) {
            return new ModelAndView("Errands")
                    .addObject("errands", errandRepository.getErrands());
        }
        else {
            return new ModelAndView ("Index");
        }
    }

    @PostMapping("/delete/{errandId}")
    public String RefreshErrands(@PathVariable int errandId) {
        errandRepository.deleteErrand(errandId);
        return "redirect:/errands";
    }

    @PostMapping("/help/{errandId}")
    public String RefreshStatus(@PathVariable int errandId) {
        errandRepository.chooseErrand(errandId);
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
    public String errand (@RequestParam String name, @RequestParam String topic, @RequestParam String errand)
    {

        errandRepository.addErrand(name, topic, errand);
        return "redirect:/errands";
    }

    @GetMapping("/newuser")
    public ModelAndView newUser() {
        return new ModelAndView("NewUser");
    }

    @PostMapping("/newuser")
    public String addUser(@RequestParam String name, @RequestParam String username, @RequestParam String password) {
        errandRepository.addUser(name, username, password);
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
