package com.example.parkinglot;

import com.example.parkinglot.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ParkingLotApplicationTests {

	@LocalServerPort
	private int port;

	private static ObjectMapper mapper;

	private static String baseUrl = "http://localhost:";

	private static RestTemplate restTemplate;

	@BeforeAll
	public static void init() {
		restTemplate = new RestTemplate();
		mapper = new ObjectMapper();
	}

	@Test
	public void testParkingLotSuccessFlow() throws IOException {
		//Adding a parking lot
		String url = baseUrl + port + "/v1/parking-lot";
		File file = new File(
				ParkingLotApplicationTests.class.getClassLoader().getResource("parkingLots.json").getFile()
		);
		ResponseEntity responseEntity = restTemplate.postForEntity(url, mapper.readValue(file, ParkingLotRequest.class),ParkingLot.class);
		ParkingLot response = (ParkingLot) responseEntity.getBody();
		assertNotNull(response);
		assertAll(
				() -> assertTrue(response.getSlotsCount()==12),
				() -> assertTrue(response.getParkingFloors().size()==3),
				() -> assertTrue(response.getParkingFloors().stream().findFirst().get().isAvailable())
				);

		//Getting a slot for Car with slot type "small"

		url = baseUrl +port + "/v1/parking/0";
		file = new File(
				this.getClass().getClassLoader().getResource("freeSlotRequest.json").getFile()
		);
		responseEntity = restTemplate.postForEntity(url, mapper.readValue(file, Vehicle.class), ParkedResponse.class);
		ParkedResponse parkedResponse = (ParkedResponse) responseEntity.getBody();
		assertNotNull(response);
		assertAll(
				() -> assertTrue(parkedResponse.getCarNumber()==82),
				() -> assertTrue(parkedResponse.getTicketNumber()==1),
				() -> assertTrue(parkedResponse.getParkingSlotType().equalsIgnoreCase("SMALL")),
				() -> assertTrue(parkedResponse.getSlot().startsWith("Floor"))
		);

		//Free a slot from Car with slot type "small"

		url = baseUrl +port + "/v1/parking/1";
		responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
		UnParkedResponse unParkedResponse = mapper.readValue(responseEntity.getBody().toString(), UnParkedResponse.class);
		assertNotNull(response);
		assertAll(
				() -> assertTrue(unParkedResponse.getCarNumber()==82),
				() -> assertTrue(unParkedResponse.getParkingSlotType().equalsIgnoreCase("SMALL")),
				() -> assertTrue(unParkedResponse.getSlot().startsWith("Floor"))
		);
	}




}
