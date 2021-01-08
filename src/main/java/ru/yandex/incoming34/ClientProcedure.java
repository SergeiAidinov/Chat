package ru.yandex.incoming34;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ClientProcedure implements Runnable {
	Thread t;
	
	public ClientProcedure(String message, int loopCount, long sleepTime) {
		super();
		this.message = message;
		this.loopCount = loopCount;
		this.sleepTime = sleepTime;
		t = new Thread(this);
		System.out.println("Thead: " + t);
		t.start();
	}

	private String message;
	private int loopCount;
	private long sleepTime;
	private int port = 8080;
	private final int CAPACITY = 1024;
	private ByteBuffer byteBuffer = ByteBuffer.allocate(CAPACITY);
	private String hostName = "localhost";

	public void run ()  {
try {
		for (int i = 0; i < loopCount; i++) {
			Socket socket = new Socket("localhost", port);
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			Writer writer = new OutputStreamWriter(os, "US-ASCII");
			PrintWriter out = new PrintWriter(writer, true);
			out.println(message);
			BufferedReader in = new BufferedReader(new InputStreamReader(is, "US-ASCII"));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(i + ": " + line);
			}
			socket.close();
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
} catch (IOException e){
	
}
	} 

	private void connectToServer() {
		try {
			SocketChannel socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			InetSocketAddress address = new InetSocketAddress(hostName, port);
			socketChannel.connect(address);
			// while (!socketChannel.finishConnect()) {
			System.out.println("Connecting to server...");
			// }
			byteBuffer.flip();
			byteBuffer.clear();
			byteBuffer.put((byte) 0);
			Selector selector = Selector.open();
			socketChannel.register(selector, SelectionKey.OP_READ);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

}
