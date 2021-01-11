package ru.yandex.incoming34;

import java.io.IOException;

public class ClientMainClass {

	public static void main(String[] args) throws IOException, InterruptedException {
		new ClientProcedure("Sergei", "Hello!", 10, 500);
		new ClientProcedure("John", "Hi!", 8, 0);

	}

}
