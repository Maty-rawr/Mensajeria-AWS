package com.ejemplo.mensajeria_aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.awspring.cloud.sqs.operations.SqsTemplate;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class OrdenController {

	private static final Logger log = LoggerFactory.getLogger(OrdenController.class);

	private final SqsTemplate sqsTemplate;
	private final ProcesadorOrden procesador;
	private final MensajeriaProperties props;

	public OrdenController(SqsTemplate sqsTemplate, ProcesadorOrden procesador, MensajeriaProperties props) {
		this.sqsTemplate = sqsTemplate;
		this.procesador = procesador;
		this.props = props;
	}

	@PostMapping(path = "/ordenes", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String crear(@RequestBody Orden orden) {
		log.info("Orden {} guardada", orden.id());
		sqsTemplate.send(props.colaOrdenes(), orden);
		return "Orden " + orden.id() + " recibida";
	}

	@PostMapping(path = "/ordenes/sync", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String crearSync(@RequestBody Orden orden) throws InterruptedException {
		log.info("Orden {} guardada", orden.id());
		procesador.procesar(orden);
		return "Orden " + orden.id() + " procesada";
	}
}
