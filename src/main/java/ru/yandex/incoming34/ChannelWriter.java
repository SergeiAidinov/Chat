package ru.yandex.incoming34;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public interface ChannelWriter {

	default void doWrite(ByteBuffer byteBuffer, SocketChannel socketChannel) {
		if (byteBuffer == null || socketChannel == null) {
			throw new IllegalArgumentException("Required buffer and channel");
		}

		while (byteBuffer.hasRemaining()) {
			try {
				socketChannel.write(byteBuffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
