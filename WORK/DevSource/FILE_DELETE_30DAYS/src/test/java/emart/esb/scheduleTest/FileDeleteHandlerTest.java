package emart.esb.scheduleTest;

import emart.esb.schedule.FileDeleteHandler;

public class FileDeleteHandlerTest {
	public static void main(String[] args) throws Exception {
		FileDeleteHandler h = new FileDeleteHandler();
		h.onStart();
	}
}
