package br.com.m3tech.AppGas.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payments {
	private String pending;
	private List<Method> methods;
}
