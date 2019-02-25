package controllers;

import annotations.Controller;
import annotations.RequestMapping;

@Controller
public class HelloController {
    @RequestMapping(method = "GET", uri = "/")
    public String say() {
        return "Bonjour";
    }

    @RequestMapping(method = "GET", uri = "/hello")
    public String sayHello() {
        return "Hello World !";
    }

    @RequestMapping(method = "GET", uri = "/bye")
    public String sayGoodBye(){
        return "Goodbye !";
    }

    @RequestMapping(method = "GET", uri = "/boum")
    public String explode(){
        throw new RuntimeException("Explosion !");
    }
}
