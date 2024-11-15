// (Mantle)
package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demo.model.SaInfo;
import com.example.demo.model.SaInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import java.util.List;

@Controller
public class SaInfoController {

    @Autowired
    private SaInfoRepository saInfoRepository;

    // sa資料總覽 (http://localhost:8080/saInfo)
    @GetMapping("/saInfo")
    public String showSaInfo(Model model) {
        List<SaInfo> saList = saInfoRepository.findAll();
        model.addAttribute("saList", saList);
        return "saInfo/saInfo"; 
    }

    // 回傳 JSON 格式的資料 (http://localhost:8080/saInfo/json)
    @GetMapping("/saInfo/json")
    @ResponseBody
    public List<SaInfo> getSaInfoJson() {
        return saInfoRepository.findAll();
    }
}