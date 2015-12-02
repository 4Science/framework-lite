package it.cilea.core.spring.controller;

import it.cilea.core.spring.util.BlobUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SimpleBlobController extends Spring3CoreController {

	@RequestMapping("/blob/get.fragment")
	public void blobGet(@RequestParam String blobId, @RequestParam Class clazz, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BlobUtil.insertBlobIntoServletOutputStream(blobId, clazz, response);
	}

}
