package Linker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("review")
public class ReviewController {
	@RequestMapping("{reviewStr}")
	String review(@PathVariable String reviewStr) {
		return "review/template";
	}
}
