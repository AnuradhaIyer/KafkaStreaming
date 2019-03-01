package edu.sjsu.SeekersKafkaServices.kafka;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static edu.sjsu.SeekersKafkaServices.kafka.IKafkaConstantsSeekers.GROUP_ID_CONFIG;
import static edu.sjsu.SeekersKafkaServices.kafka.IKafkaConstantsSeekers.TOPIC_NAME;

public class KafkaProducerSeekers {
    static long x =0;
    static Properties propsProducer = new Properties();
    static Producer<Long, String> prod = null;
    static Properties propsConsumer = new Properties();
    static Consumer<Long, String> consumer = null;

    public static Producer<Long, String> createProducer() {
        if(prod == null) {
            propsProducer.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstantsSeekers.KAFKA_BROKERS);
            propsProducer.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstantsSeekers.CLIENT_ID);
            propsProducer.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
            propsProducer.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            prod = new KafkaProducer<>(propsProducer);
        }
        return prod;
    }

    private static Consumer<Long, String> createConsumer() {
        if(consumer == null) {
            propsConsumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstantsSeekers.KAFKA_BROKERS);
            propsConsumer.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);
            propsConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
            propsConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            consumer = new KafkaConsumer<>(propsConsumer);
            consumer.subscribe(Collections.singletonList(IKafkaConstantsSeekers.TOPIC_NAME));
        }
        return consumer;
    }



    public static void runProducer(String msg) {
        Producer<Long, String> producer = KafkaProducerSeekers.createProducer();

            ProducerRecord<Long, String> record = new ProducerRecord<>(IKafkaConstantsSeekers.TOPIC_NAME, msg);
            try {
                RecordMetadata metadata = producer.send(record).get();
                System.out.println("Record sent with key " + " to partition " + metadata.partition()
                        + " with offset " + metadata.offset());
            }
            catch (ExecutionException e) {
                System.out.println("Error in sending record");
                System.out.println(e);
            }
            catch (InterruptedException e) {
                System.out.println("Error in sending record");
                System.out.println(e);
            }

    }

    public static String runConsumer(){

        String result = "";
        final Consumer<Long, String> consumer = createConsumer();
        final int giveUp = 1;   int noRecordsCount = 0;
        while (true) {
            final ConsumerRecords<Long, String> consumerRecords =
                consumer.poll(500);
            if (consumerRecords.count()==0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }
            for (ConsumerRecord<Long, String> record : consumerRecords) {
                System.out.printf("Consumer Record:(%d, %s, %d, %d)\n", record.key(), record.value(),
                    record.partition(), record.offset());
                result += record.value() + "\n";
            }
            consumer.commitAsync();
        }
        result.substring(0,result.length());

        System.out.println("consumer closing ");
        return result;
    }

//    public static void runProducer(String msg) throws Exception{
//
//
//
//
//
//        // create instance for properties to access producer configs
//        Properties props = new Properties();
//
//        //Assign localhost id
//        props.put("bootstrap.servers", IKafkaConstantsSeekers.KAFKA_BROKERS);
//
//        //Set acknowledgements for producer requests.
//        props.put("acks", "all");
//
//        //If the request fails, the producer can automatically retry,
//        props.put("retries", 0);
//
//        //Specify buffer size in config
//        props.put("batch.size", 16384);
//
//        //Reduce the no of requests less than 0
//        props.put("linger.ms", 1);
//
//        //The buffer.memory controls the total amount of memory available to the producer for buffering.
//        props.put("buffer.memory", 33554432);
//
//        props.put("key.serializer", StringSerializer.class.getName());
//
//        props.put("value.serializer", StringSerializer.class.getName());
//
//        Producer<String, String> producer = new KafkaProducer
//            <>(props);
//
//        for(int i = 0; i < 10; i++)
//            producer.send(new ProducerRecord<>(TOPIC_NAME,
//                Integer.toString(i), Integer.toString(i)));
//        System.out.println("Message sent successfully");
//        producer.close();
//    }
}
