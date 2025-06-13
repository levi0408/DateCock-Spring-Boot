package com.example.DateCock.Controller;

import com.example.DateCock.DTO.BusinessDTO;
import com.example.DateCock.DTO.BusinessmemberDTO;
import com.example.DateCock.Entity.BusinessEntity;
import com.example.DateCock.Service.BusinessService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class BusinessController {
    @Autowired
    BusinessService businessService;
    String path="C:\\MBC12AI\\Spring Boot\\DateCock\\src\\main\\resources\\static\\image";
    @GetMapping(value = "/businessinput")
    public String businessinput(){

        return"business/businessinput";
    }

    @PostMapping(value = "/businesssave")
    public String businesssave(BusinessDTO businessDTO, BusinessmemberDTO businessmemberDTO, MultipartHttpServletRequest mul, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        String businessnumber = (String) session.getAttribute("businessnumberA");
        businessDTO.setBusinessnumber(businessnumber);
        MultipartFile file=mul.getFile("image");
        String image=file.getOriginalFilename();
        UUID uuid=UUID.randomUUID();
        String image1 = uuid.toString() + "-" + image;
        file.transferTo(new File(path+"\\"+image1));
        businessDTO.setImage1(image1);
        BusinessEntity businessEntity=businessDTO.entity();
        businessService.insertbusiness(businessEntity);
        return "redirect:/main";

    }
    @GetMapping(value = "/businessout")
    public String businessout(Model model, HttpServletRequest request, BusinessDTO businessDTO){
        HttpSession session = request.getSession();
        String businessnumber = (String) session.getAttribute("businessnumberA");
        businessDTO.setBusinessnumber(businessnumber);
        BusinessEntity dto=businessService.allout(businessnumber);
        model.addAttribute("dto",dto);
        return "business/businessout";
    }
    @GetMapping(value = "/businessupdate")
    public String businessupdate(@RequestParam String businessnumber, Model model){
        BusinessEntity dto=businessService.updatego(businessnumber);
        model.addAttribute("dto",dto);
        return "business/businessupdate";
    }
    @PostMapping(value = "/businessupdatego")
    public String businessupdatego(BusinessDTO businessDTO, BusinessmemberDTO businessmemberDTO, MultipartHttpServletRequest mul, HttpServletRequest request) throws IOException {
        String deleteimage=businessDTO.getImage1();
        MultipartFile mf=mul.getFile("image");

        if(mf.isEmpty()){

            BusinessEntity businessEntity = businessDTO.entity();
            businessService.updateall(businessEntity);
        }
        else {
            File file=new File(path+"\\"+deleteimage);
            file.delete();

            String image=mf.getOriginalFilename();
            UUID uuid=UUID.randomUUID();
            String newimage = uuid.toString() + "-" + image;

            String newimagename = mf.getOriginalFilename();
            businessDTO.setImage1(image);//db에 저장
            mf.transferTo(new File(path+"\\"+newimagename));
            BusinessEntity businessEntity = businessDTO.entity();
            businessService.updateall(businessEntity);

        }
        return "redirect:/businessout";
    }
    @PostMapping("/alldelete")
    @ResponseBody
    public String deleteBusiness(@RequestParam String businessnumber) {
        businessService.deleteall(businessnumber);
        return "redirect:/main";
    }
}
