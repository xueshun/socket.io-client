package io.socket.xue;

import java.net.URISyntaxException;

import javax.security.auth.callback.Callback;

import org.json.JSONObject;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import com.google.gson.Gson;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.entity.EventLisenter;
import io.socket.entity.UserInfo;
import okhttp3.OkHttpClient;

public class Demo {
	
	public static void main(String[] args) throws URISyntaxException {
		IO.Options options = new IO.Options();
        options.forceNew = true;
        
        Config config = new Config();
        config.useSingleServer().setAddress("127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);

        final OkHttpClient client = new OkHttpClient();
        options.webSocketFactory = client;
        options.callFactory = client;
        final Socket socket = IO.socket("http://localhost:" + 9092+ options);
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("connect");
                UserInfo userInfo = new UserInfo();
                userInfo.setDeviceToken("PC11");
                userInfo.setDeviceType("PC");
                userInfo.setNickName("网三");
                userInfo.setSID("1");
                userInfo.setUserName("Guess");
                Gson gson = new Gson();
                String json = gson.toJson(userInfo);
                Ack ack = new Ack() {
					
					@Override
					public void call(Object... args) {
						for (Object object : args) {
							System.out.println(object.toString());
						}
					}
				};
                socket.emit(EventLisenter.LOGIN, json,ack);
                //socket.emit(EventLisenter.LOGIN, json);
                //socket.close();
            }
        });
        
       /* socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
        	
			@Override
			public void call(Object... args) {
				System.out.println("connect");
				
			}
		});*/
        /*socket.on(EventLisenter.LOGIN, new Emitter.Listener(){

			@Override
			public void call(Object... args) {
				  System.out.println("connect");
				  socket.close();
			}
        });*/
        socket.open();
	}
}
