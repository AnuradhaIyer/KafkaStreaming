package edu.sjsu.SeekersKafkaServices.kafka;

public interface IKafkaConstantsSeekers {
    public static String KAFKA_BROKERS = "13.57.252.148:9092";
        //"localhost:9092";
        //"13.56.250.104:9092";
    public static Integer MESSAGE_COUNT=1000;
    public static String CLIENT_ID="seekersClient";
    public static String TOPIC_NAME="test";
    public static String GROUP_ID_CONFIG="seekersGroup";
    public static Integer MAX_NO_MESSAGE_FOUND_COUNT=100;
    public static String OFFSET_RESET_LATEST="latest";
    public static String OFFSET_RESET_EARLIER="earliest";
    public static Integer MAX_POLL_RECORDS=1;
}
