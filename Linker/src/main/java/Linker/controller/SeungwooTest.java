package Linker.controller;

import java.sql.Timestamp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import Linker.model.Member;

@Controller
@RequestMapping("seungTest")
public class SeungwooTest {
	
	@RequestMapping("calender")
	public String calender() {
		
		return "seungTest/calender";
	}
	
}
