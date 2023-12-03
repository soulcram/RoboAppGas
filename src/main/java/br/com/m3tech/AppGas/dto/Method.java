package br.com.m3tech.AppGas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Method {
	private String value;
	private String methodInfo;
	private String changeFor;
}
