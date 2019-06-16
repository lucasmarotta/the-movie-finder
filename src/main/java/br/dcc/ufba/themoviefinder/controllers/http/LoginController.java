package br.dcc.ufba.themoviefinder.controllers.http;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController 
{
	@GetMapping({"/", "/login"})
	public ModelAndView login()
	{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("index");
		mv.addObject("content", "login");
		return mv;
	}
}
