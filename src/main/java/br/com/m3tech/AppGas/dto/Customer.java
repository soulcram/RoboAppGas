package br.com.m3tech.AppGas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	private String id;
	private String name;
	private Phone phone;
	private String email;
}
