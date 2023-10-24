package Linker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("review")
public class ReviewController {
	@PostMapping("writeReg")
	Object write(String ir1) {
		System.out.println(ir1);
		return "/review/write";
	}
	
	@RequestMapping("{reviewStr}")
	String review(@PathVariable String reviewStr) {
		return "review/template";
	}
}
