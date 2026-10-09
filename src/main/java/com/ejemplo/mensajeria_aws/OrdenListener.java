package com.ejemplo.mensajeria_aws;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;

@Component
public class OrdenListener {

	private static final Logger log = LoggerFactory.getLogger(OrdenListener.class);

	private final ProcesadorOrden procesador;
	private final SqsTemplate sqsTemplate;
	private final MensajeriaProperties props;
	private final Set<String> procesadas = ConcurrentHashMap.newKeySet();

	public OrdenListener(ProcesadorOrden procesador, SqsTemplate sqsTemplate, MensajeriaProperties props) {
		this.procesador = procesador;
		this.sqsTemplate = sqsTemplate;
		this.props = props;
	}

	@SqsListener(id = "procesador-ordenes", value = "${mensajeria.cola-ordenes}")
	public void escuchar(Orden orden) throws InterruptedException {
		log.info("Recibida orden {} de {}", orden.id(), orden.cliente());

		if (procesadas.contains(orden.id())) {
			log.warn("Orden {} duplicada, se ignora", orden.id());
			return;
		}
		try {
			procesador.procesar(orden);
			procesadas.add(orden.id());
		} catch (ErrorDefinitivo e) {
			log.error("Orden {} descartada: {}", orden.id(), e.getMessage());
			sqsTemplate.send(props.dlqOrdenes(), orden);
			procesadas.add(orden.id());
		}
	}
}
