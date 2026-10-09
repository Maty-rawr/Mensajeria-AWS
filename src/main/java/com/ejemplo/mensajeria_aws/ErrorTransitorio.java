package com.ejemplo.mensajeria_aws;

public class ErrorTransitorio extends RuntimeException {

	public ErrorTransitorio(String mensaje) {
		super(mensaje);
	}
}
