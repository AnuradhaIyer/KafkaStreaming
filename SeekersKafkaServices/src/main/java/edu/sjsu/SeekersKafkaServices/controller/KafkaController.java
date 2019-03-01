package edu.sjsu.SeekersKafkaServices.controller;

import edu.sjsu.SeekersKafkaServices.model.Message;
import org.springframework.web.bind.annotation.*;

import static edu.sjsu.SeekersKafkaServices.kafka.KafkaProducerSeekers.runConsumer;
import static edu.sjsu.SeekersKafkaServices.kafka.KafkaProducerSeekers.runProducer;


@RestController
public class KafkaController {


    @RequestMapping(value = "/producer",method = RequestMethod.POST)
    @ResponseBody
    public String producer(@RequestBody Message message) {

        System.out.println("message: " + message.getMsg());
        try {
            runProducer(message.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Hi";
    }

    @RequestMapping(value = "/consumer",method = RequestMethod.GET)
    @ResponseBody
    public String consumer() {
        System.out.println("message: " + "inconsumer");
        return runConsumer();
    }
}
