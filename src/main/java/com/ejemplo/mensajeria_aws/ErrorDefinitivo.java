package com.ejemplo.mensajeria_aws;

public class ErrorDefinitivo extends RuntimeException {

	public ErrorDefinitivo(String mensaje) {
		super(mensaje);
	}
}
