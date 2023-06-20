package br.com.cmabreu.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class AeronaveTestService {

	private double lat = -22.9469;
	private double lon = -43.1510;
	
	@Autowired private CommunicatorService comm;
	
	@Scheduled(fixedDelay = 100 )
	private void test() {
		
		JSONObject obj = new JSONObject();
		obj.put("lat", lat);
		obj.put("lon", lon);
		
		try {
			comm.broadcastData( "main_channel", obj );
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		lon = lon + 0.0001; // 111 metros
		if( lon > 180 ) lon = -180;
		
	}
	
}
