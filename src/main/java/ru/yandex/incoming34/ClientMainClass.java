package ru.yandex.incoming34;

import java.io.IOException;

public class ClientMainClass {

	public static void main(String[] args) throws IOException, InterruptedException {
		new ClientProcedure("Hello!", 10, 500);
		//clientProcedure.start();
		//clientProcedure.begin("Hello!");
		new ClientProcedure("Hi!", 8, 300);
		new ClientProcedure ("Shalom!", 9, 100);
		//clientProcedure2.start();
		//clientProcedure2.begin("Hi!!!");
		

	}

}
