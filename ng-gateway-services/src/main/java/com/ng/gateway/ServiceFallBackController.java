package com.ng.gateway;

import org.springframework.web.bind.annotation.*;

@RestController
public class ServiceFallBackController {

    @RequestMapping(value = "/masterService", method = RequestMethod.POST,produces = "application/json")
    public String customerService() {
        return "master-service - JWT validity cannot be asserted and should not be trusted !!";
    }

    @RequestMapping("/communityService")
    public String productService() {
        return "Community Service is down...";
    }

    @RequestMapping("/psaUserMenuService")
    public String psaUserMenuService() {
        return "User menu -service is down...";
    }


    @RequestMapping(value = "/ngAuthService", method = RequestMethod.POST,produces = "application/json")
    public  String authService() {  return "Calling -  Auth Service is down...";   }

    @RequestMapping("/fileService")
    public String fileService() {   return "file-service is down...";  }

    @RequestMapping("/lostService")
    public String lostService() {
        return "lost-article-service is down...";
    }
    @RequestMapping("/digiService")
    public String digiService() {
        return "Digi Locker Service is down...";
    }

}
