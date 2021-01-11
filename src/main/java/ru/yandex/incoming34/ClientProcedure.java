package ru.yandex.incoming34;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class ClientProcedure implements Runnable, ChannelWriter {
	Thread t;

	public ClientProcedure(String name, String message, int loopCount, long sleepTime) {
		super();
		this.name = name;
		this.message = message;
		this.loopCount = loopCount;
		this.sleepTime = sleepTime;
		t = new Thread(this);
		System.out.println("Thead: " + t);
		t.start();
	}

	private String name;
	private String message;
	private int loopCount;
	private long sleepTime;
	private int port = 8080;
	private final int CAPACITY = 1024;
	private ByteBuffer byteBuffer = ByteBuffer.allocate(CAPACITY);
	private String hostName = "localhost";
	SocketChannel socketChannel;

	@Override
	public void run() {
		try {
			t.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connectToServer();
		for (int i = 0; i < loopCount; i++) {
			ByteBuffer buffer = ByteBuffer.wrap((name + " " + message).trim().getBytes());
			doWrite(buffer, socketChannel);
			buffer.flip();

		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		System.out.println("Data sent at " + LocalDateTime.now());
	}

	private void connectToServer() {
		try {
			socketChannel = SocketChannel.open();
			if (socketChannel == null) {
				System.out.println("There is no SocketChannel");
			} else if (!socketChannel.isConnected()) {

				System.out.println("Connecting to server...");
				InetSocketAddress address = new InetSocketAddress(hostName, port);
				socketChannel = SocketChannel.open(address);
				socketChannel.configureBlocking(false);
				// socketChannel.connect(address);
				System.out.println("Connection established: " + socketChannel);
			} else {
				System.out.println("Connection established: " + socketChannel);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
