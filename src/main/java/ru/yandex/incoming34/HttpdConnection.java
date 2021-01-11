package ru.yandex.incoming34;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class HttpdConnection {

	private final int CAPACITY = 1024;

	SocketChannel clientSocket;
	ByteBuffer byteBuffer = ByteBuffer.allocate(CAPACITY);

	public HttpdConnection(SocketChannel clientSocket) {
		this.clientSocket = clientSocket;

	}

	void read(SelectionKey selectionKey) {

	}

}
