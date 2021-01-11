package ru.yandex.incoming34;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerProcedure implements ChannelWriter {
	private final int PORT = 8080;
	private final int CAPACITY = 1024;
	private final String ADDRESS = "localhost";
	InetSocketAddress listenAddress;
	ArrayList<SelectionKey> listOfKeys;
	Selector selector;
	private Map<SocketChannel, StringBuilder> session = new HashMap<>();
	private boolean cont = true;
	private static int qtyMessage = 0;

	public void begin() {
		try {
			selector = Selector.open();
			listenAddress = new InetSocketAddress(ADDRESS, PORT);
			ServerSocketChannel serverSocketChannel = initServerChannel();
			selector.selectedKeys().clear();
			proceed(serverSocketChannel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void proceed(ServerSocketChannel serverSocketChannel) {
		System.out.println("IN PROCEED");
		while (cont) {
			try {
				selector.select();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
			while (keys.hasNext()) {
				SelectionKey key = keys.next();
				keys.remove();
				System.out.println("Working with" + key.hashCode());
				if (!key.isValid()) {
					continue;
				} else {
					// keys.remove();
				}

				if (key.isAcceptable()) {
					System.out.println("Key " + key.hashCode() + " is acceptable");

					doAccept(key);
				}
				// key.interestOps(0);
				if (key.isReadable()) {
					System.out.println("Key " + key.hashCode() + " is readable");
					doRead(key);
				}

				if (cont == false) {
					break;
				}

				try {
					selector.selectNow();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					key.channel().close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	private void doRead(SelectionKey key) {
		// cont = false;
		System.out.println("IN DOREAD");
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer byteBuffer = ByteBuffer.allocate(CAPACITY);
		try {
			int numRead = socketChannel.read(byteBuffer);
			if (numRead != -1) {
				byte[] data = new byte[numRead];
				System.arraycopy(byteBuffer.array(), 0, data, 0, numRead);
				System.out.println("GOTTEN " + ++qtyMessage + ": " + new String(data));
				// key.interestOps(0);
			} else {
				session.remove(socketChannel);
				socketChannel.close();

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doEcho(SelectionKey key) {
		ByteBuffer buffer = ByteBuffer.wrap(session.get(key.channel()).toString().trim().getBytes());
		doWrite(buffer, (SocketChannel) key.channel());

	}

	private void doAccept(SelectionKey oneKey) {

		try {
			// InetSocketAddress listenAddress = new InetSocketAddress(ADDRESS, PORT);
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) oneKey.channel();
			// SelectableChannel serverSocketChannel = oneKey.channel();
			serverSocketChannel = ServerSocketChannel.open();
			if ((!serverSocketChannel.socket().isBound())) {
				serverSocketChannel.socket().bind(listenAddress);
			}
			System.out.println("IN DOACCEPT ssch is bound: " + serverSocketChannel.socket().isBound());
			SocketChannel channel = serverSocketChannel.accept();
			channel.configureBlocking(false);
			Socket socket = channel.socket();
			// int interestOps = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
			SocketAddress socketAddress = socket.getRemoteSocketAddress();
			System.out.println("Connected to address: " + socketAddress);
			channel.register(selector, SelectionKey.OP_READ);
			System.out.println("Registered channel: " + channel.supportedOptions());
			System.out.println("Total in select: " + selector.select());
			System.out.println("In session: " + session.size());

		} catch (IOException e) {
			System.out.println(e);
			return;
			// e.printStackTrace();
		}

	}

	private ServerSocketChannel initServerChannel() {
		System.out.println("IN INITCHANNEL");
		ServerSocketChannel serverSocketChannel = null;
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().setReuseAddress(true);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println(
					"Server started: " + serverSocketChannel + "BOUND: " + serverSocketChannel.socket().isBound());
			//

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serverSocketChannel;

	}

}
