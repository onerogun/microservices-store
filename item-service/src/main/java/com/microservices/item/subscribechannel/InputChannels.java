package com.microservices.item.subscribechannel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface InputChannels {

        String INPUT_FILE_CREATED = "input_file_created";
        String INPUT_FILE_DELETED = "input_file_deleted";

        @Input("input_file_created")
        SubscribableChannel inputFileCreated();

        @Input("input_file_deleted")
        SubscribableChannel inputFileDeleted();

}
