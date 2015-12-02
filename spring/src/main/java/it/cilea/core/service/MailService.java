package it.cilea.core.service;

import it.cilea.core.CoreConstant;
import it.cilea.core.model.Attach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.AbstractResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailService {
	private static final Logger log = LoggerFactory.getLogger(MailService.class);
	@Autowired
	private JavaMailSenderImpl mailSender;

	public void send(String from, String[] to, String[] cc, String[] bcc, String subject, String text,
			Set<Attach> attachSet, Boolean html) {

		log.info("\n\nmailSender: getHost: " + mailSender.getHost() + " getPassword: " + mailSender.getPassword()
				+ " " + " getPort: " + mailSender.getPort() + " " + " getUsername: " + mailSender.getUsername());

		log.info("\nfrom: " + from + " to: " + to + " sub" + subject + "\n\ntext: " + text);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;

		try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			String[] toCopy = new String[to.length];
			for (int i = 0; i < toCopy.length; i++)
				toCopy[i] = checkMailLimitata(to[i]);
			helper.setTo(toCopy);
			if (cc != null) {
				for (int i = 0; i < cc.length; i++)
					cc[i] = checkMailLimitata(cc[i]);
				helper.setCc(cc);
			}
			if (bcc != null)
				helper.setBcc(bcc);

			helper.setText(text, html);
			helper.setSubject(subject);
			if (attachSet != null)
				for (Attach singolo : attachSet) {
					InputStream attach = new ByteArrayInputStream(singolo.getFile());
					StreamAttachmentDataSource datasource = new StreamAttachmentDataSource(attach, singolo.getName(),
							singolo.getMediaType());
					helper.addAttachment(singolo.getName(), datasource);

				}
			mailSender.send(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}

	}

	public void send(String from, String[] to, String[] cc, String[] bcc, String subject, String text) {

		Set<Attach> attachSet = new HashSet<Attach>();
		if (attachSet.isEmpty())
			attachSet = null;
		this.send(from, to, cc, bcc, subject, text, attachSet, true);
	}

	public void send(String from, String[] to, String subject, String text) {

		this.send(from, to, null, null, subject, text);
	}

	public void send(String from, String[] to, String[] cc, String subject, String text) {

		this.send(from, to, cc, null, subject, text);
	}
	
	

	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	// classe di supporto

	public class StreamAttachmentDataSource extends AbstractResource {
		private ByteArrayOutputStream outputStream;
		private String name;
		private String contentType;

		public StreamAttachmentDataSource(InputStream inputStream, String name, String contentType) {
			this.outputStream = new ByteArrayOutputStream();
			this.name = name;
			this.contentType = contentType;

			int read;
			byte[] buffer = new byte[256];
			try {
				while ((read = inputStream.read(buffer)) != -1) {
					getOutputStream().write(buffer, 0, read);
				}
			} catch (IOException e) {
			}
		}

		public String getDescription() {
			return "Stream resource used for attachments";
		}

		public InputStream getInputStream() throws IOException {
			return new ByteArrayInputStream(this.outputStream.toByteArray());
		}

		public String getContentType() {
			return contentType;
		}

		public String getName() {
			return name;
		}

		public ByteArrayOutputStream getOutputStream() {
			return outputStream;
		}

	}

	public String checkMailLimitata(String mailTo) {
		if (CoreConstant.LIMITA_MAIL)
			return CoreConstant.INDIRIZZO_MAIL_LIMITATA;
		return mailTo;

	}

}
