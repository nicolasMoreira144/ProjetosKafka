package servico;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import model.Venda;
import serializer.VendaSerializer;

public class GeradorVendas {

	private static Random rand = new Random();
	private static long operacao = 0;
	private static BigDecimal valorIngresso = BigDecimal.valueOf(500);

	public static void main(String[] args) throws InterruptedException {
		
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VendaSerializer.class.getName());

		try (KafkaProducer<String, Venda> producer = new KafkaProducer<>(properties)) {
			
			while(true) {
				Venda venda = geraVenda();
				ProducerRecord<String, Venda> record = new ProducerRecord<>("venda-ingressos", venda);
				producer.send(record);
				Thread.sleep(250);
			}
		}

	}

	private static Venda geraVenda() {
		long cliente = rand.nextLong();
		int qtdIngressos = rand.nextInt(10);
		return new Venda(operacao++, cliente, qtdIngressos, valorIngresso.multiply(BigDecimal.valueOf(qtdIngressos)));
	}

}
