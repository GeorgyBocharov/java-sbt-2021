package ru.sbt.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.sbt.clients.AbstractClient;
import ru.sbt.clients.HoldingClient;
import ru.sbt.clients.IndividualClient;
import ru.sbt.service.error.ClientParsingException;
import ru.sbt.service.impl.ClientParsingServiceV1;
import ru.sbt.service.impl.ClientParsingServiceV2;
import utils.JsonTestFilesLoader;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static utils.JsonTestFilesLoader.HOLDING_CLIENT_FILE;
import static utils.JsonTestFilesLoader.INDIVIDUAL_CLIENT_FILE;

public class ClientParsingServiceV1Test {


    @ParameterizedTest
    @MethodSource("provideTestDataAndClientServiceImpls")
    public void parseClientFromJson(String fileName,
                                    AbstractClient expectedClient,
                                    ClientParsingService clientParsingService) throws IOException {

        String jsonString = JsonTestFilesLoader.loadJsonFile(fileName);

        Optional<AbstractClient> clientOptional = clientParsingService
                .parseClientFromJson(jsonString);

        Assertions.assertTrue(clientOptional.isPresent());
        Assertions.assertEquals(expectedClient, clientOptional.get());
    }

    @ParameterizedTest
    @MethodSource("provideClientServiceImpls")
    public void parseNonClientJsonTest(ClientParsingService clientParsingService) {
        String nonClientJson = "{\"name\": \"Audi\", \"model\": \"a4\"}";
        Assertions.assertThrows(
                ClientParsingException.class,
                () -> clientParsingService.parseClientFromJson(nonClientJson)
        );
    }

    private static Stream<Arguments> provideClientServiceImpls() {
        return Stream.of(
                Arguments.of(new ClientParsingServiceV1()),
                Arguments.of(new ClientParsingServiceV2())
        );
    }

    private static Stream<Arguments> provideTestDataAndClientServiceImpls() {
        IndividualClient individualClient = new IndividualClient( "George", "12345325632");

        HoldingClient holdingClient = new HoldingClient(
                "Samokat",
                "Sber",
                10,
                List.of("LitRes", "PIK Group", "Sber Mobile", "2 Gis")
        );


        return Stream.of(
                Arguments.of(
                        INDIVIDUAL_CLIENT_FILE,
                        individualClient,
                        new ClientParsingServiceV1()
                ),
                Arguments.of(
                        INDIVIDUAL_CLIENT_FILE,
                        individualClient,
                        new ClientParsingServiceV2()
                ),
                Arguments.of(
                        HOLDING_CLIENT_FILE,
                        holdingClient,
                        new ClientParsingServiceV1()
                ),
                Arguments.of(
                        HOLDING_CLIENT_FILE,
                        holdingClient,
                        new ClientParsingServiceV2()
                )
        );
    }
}
