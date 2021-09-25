package ru.sbt;


import ru.sbt.clients.AbstractClient;
import ru.sbt.service.ClientParsingService;
import ru.sbt.service.impl.ClientParsingServiceV1;
import ru.sbt.utils.JsonFilesLoader;

import java.util.Arrays;


public class Main {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Error! Program args must consist of a single String - path to json file with client. " +
                    "But actually it is " + Arrays.toString(args));
            return;
        }

        String jsonString = JsonFilesLoader.loadJsonFile(args[0]).orElseThrow();

//        ClientParsingService clientParsingService = new ClientParsingServiceV2();
        ClientParsingService clientParsingService = new ClientParsingServiceV1();

        AbstractClient client = clientParsingService.parseClientFromJson(jsonString).orElse(null);
        System.out.println(client);

    }
}
