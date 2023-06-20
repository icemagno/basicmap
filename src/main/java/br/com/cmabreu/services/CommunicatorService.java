package br.com.cmabreu.services;

import java.util.Calendar;

import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Service
@EnableScheduling
public class CommunicatorService {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;	
	
	@EventListener
	public void onDisconnectEvent(SessionDisconnectEvent event) {
		onDisconnect(event);
	}	 
	
	@EventListener
	public void onSubscribeEvent(SessionSubscribeEvent event) {
		onSubscribe(event);
	}
	
    @Scheduled(fixedDelay = 2000)
    private void ping() {
    	JSONObject data = new JSONObject();
    	data.put("pingData", "Connection Communication Test");
    	try {
    		broadcastData( "ping", data );
    	} catch (Exception e) {
			// TODO: handle exception
		}
    }	
	
	public void broadcastData( String channel, JSONObject data ) throws Exception {
		data.put("timestamp", Calendar.getInstance().getTimeInMillis() );
		
		// To Rabbit-MQ
		rabbitTemplate.convertAndSend( channel, data.toString() );
		
    	// To websocket
    	messagingTemplate.convertAndSend("/" + channel, data.toString() );
	}

	// ---------------------------------------------------------------------------------
	// WEBSOCKET
	// ---------------------------------------------------------------------------------
	
	public void onDisconnect(SessionDisconnectEvent event) {
		String sessionId = new JSONObject( event.getMessage().getHeaders() ).getString("simpSessionId");
		System.out.println( "[" + sessionId +  "]  Recebido pelo CommController: DISCONNECT EVENT" );
	}

	public void onSubscribe(SessionSubscribeEvent event) {
		String sessionId = new JSONObject( event.getMessage().getHeaders() ).getString("simpSessionId");
		System.out.println( "[" + sessionId +  "]  Recebido pelo CommController: CONNECT EVENT" );
	}

	public void incommingFromWs(String message, MessageHeaders messageHeaders) {
		String sessionId = new JSONObject( messageHeaders ).getString("simpSessionId");
		System.out.println( "[" + sessionId +  "]  Recebido pelo CommController (WebSocket) " );
		System.out.println( message );
	}	
	
	// ---------------------------------------------------------------------------------
	// RABBIT MQ
	// ---------------------------------------------------------------------------------

	@RabbitListener( queues = {"main_channel"} )
    public void receive(@Payload String payload) {
    	try {
    		System.out.println( "[ DADOS ]  Recebido pelo CommController (Rabbit Channel) " );
    		JSONObject inputProtocol = new JSONObject( payload );    
   			System.out.println( inputProtocol.toString(5) );
    	} catch ( Exception e ) {
    		e.printStackTrace();
    	}
    }	

    @RabbitListener( queues = {"ping"} )
    public void receivePing(@Payload String payload) {
    	try {
    		System.out.println( "[ PING ]  Recebido pelo CommController (Rabbit Channel) " );
    	} catch ( Exception e ) {
    		e.printStackTrace();
    	}
    }
    
}
