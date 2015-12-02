package it.cilea.core.spring.controller;

import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class LogController extends Spring3CoreController {

	public abstract String getFullyQualifiedLogPath();

	public void handleShowLogs(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			OutputStream outputstream = response.getOutputStream();
			FileInputStream inputream = new FileInputStream(getFullyQualifiedLogPath());
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputream.read(buffer)) != -1) {
				outputstream.write(buffer, 0, bytesRead);
			}
			inputream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
