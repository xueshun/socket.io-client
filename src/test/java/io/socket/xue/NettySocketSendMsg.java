package io.socket.xue;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import com.google.gson.Gson;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.entity.EventLisenter;
import io.socket.entity.Message;
import io.socket.entity.MessageTypeEnum;
import io.socket.entity.UserInfo;
import okhttp3.OkHttpClient;

public class NettySocketSendMsg {
	public static void main(String[] args) throws URISyntaxException {
		IO.Options options = new IO.Options();
		options.forceNew = true;
		
		final OkHttpClient client = new OkHttpClient();
		options.webSocketFactory = client;
		options.callFactory = client;
		
		final Socket socket = IO.socket("http://localhost:"+9092,options);
		 socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				System.out.println("connect....");
				
				HashSet<String> receiverIDs = new HashSet<String>();
				receiverIDs.add("11");
				
				Message msg = new Message();
				msg.setSID(UUID.randomUUID().toString());
				msg.setSenderID("1");
				msg.setSenderDeviceToken("PC11");
				msg.setReceiverIDs(receiverIDs);
				msg.setTitle("Demo");
				msg.setBody("测试用例");
				msg.setTime(new Date().getTime());
				msg.setMessageType(MessageTypeEnum.TEXT);
				msg.setIsAlert(true);
				Gson gson = new Gson();
				String json = gson.toJson(msg);
				
                socket.emit(EventLisenter.NEWS, json);
			}
		 });
		 
		 socket.open();
	}
}
