package br.com.m3tech.AppGas.controller;

import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.m3tech.AppGas.model.Task;
import br.com.m3tech.AppGas.service.TaskService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class TaskController {

	@Autowired
	private TaskService taskService;
	
	private PrintService[] ps;

	@GetMapping("/config")
	public String home(Model model) {
		
		List<String> boxImpressoras = new ArrayList<>();
		
		DocFlavor df = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		ps = PrintServiceLookup.lookupPrintServices(df, null);
		for (PrintService p : ps) {
			boxImpressoras.add(p.getName());
		}
		
        Task task = taskService.buscarPrimeiro();
        model.addAttribute("task", task);
        model.addAttribute("opcoesImpressora", boxImpressoras);
        return "config";
	}

	@PostMapping("/config")
	public void create(Task modelo) {

		log.info(modelo.toString());

		Task task = taskService.buscarPrimeiro();

		if (!StringUtils.isBlank(modelo.getClientId())) {
			task.setClientId(modelo.getClientId());
		}
		if (!StringUtils.isBlank(modelo.getClientSecret())) {
			task.setClientSecret(modelo.getClientSecret());
		}
		if (!StringUtils.isBlank(modelo.getUrl())) {
			task.setUrl(modelo.getUrl());
		}
		if (!StringUtils.isBlank(modelo.getImpressora())) {
			task.setImpressora(modelo.getImpressora());
		}

		taskService.salvarTask(task);
	}

}
