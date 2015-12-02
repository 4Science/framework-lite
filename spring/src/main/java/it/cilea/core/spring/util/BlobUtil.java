package it.cilea.core.spring.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BlobUtil implements ApplicationContextAware {

	private static ApplicationContext staticApplicationContext;

	public static void insertBlobIntoServletOutputStream(String id, boolean fileNameInContentDispositionHeader,
			Class parentClazz, HttpServletResponse response) throws Exception {

		String schema = getSchema(parentClazz);
		DataSource ds = getDatasource();

		Boolean autoCommit = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = ds.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			String query = "SELECT FILENAME, MIME_TYPE, BLOB FROM " + schema + ".BLOB WHERE ID=?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				String fileName = rs.getString(1);
				String mimeType = rs.getString(2);
				if (mimeType != null)
					response.setContentType(mimeType);
				if (fileNameInContentDispositionHeader)
					response.setHeader("Content-disposition", "attachment;filename=\"" + fileName + "\"");
				ServletOutputStream out = response.getOutputStream();
				byte[] buffer = rs.getBytes(3);
				if (buffer == null || buffer.length == 0) {
					String emptyFile = "EMPTY FILE";
					buffer = emptyFile.getBytes();
				}
				response.setContentLength(buffer.length);
				out.write(buffer);
				out.flush();
				out.close();
				return;
			} else
				throw new IllegalArgumentException("Blob with given id (" + id + ") does not exist.");

		} finally {
			try {
				if (autoCommit != null)
					conn.setAutoCommit(autoCommit);
			} catch (Exception e) {
			}
			;
			try {
				rs.close();
			} catch (Exception e) {
			}
			;
			try {
				stmt.close();
			} catch (Exception e) {
			}
			;
			try {
				conn.close();
			} catch (Exception e) {
			}
			;
		}
	}

	public static void insertBlobIntoServletOutputStream(String id, Class parentClazz, HttpServletResponse response)
			throws Exception {
		insertBlobIntoServletOutputStream(id, true, parentClazz, response);
	}

	public static String saveBlob(byte[] content, String filename, Class parentClazz) throws Exception {
		String schema = getSchema(parentClazz);

		DataSource ds = getDatasource();
		UUID uuid = UUID.randomUUID();

		Connection conn = null;
		Boolean autoCommit = null;
		PreparedStatement stmt = null;

		try {
			conn = ds.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			String query = "INSERT INTO " + schema + ".BLOB (ID, FILENAME, MIME_TYPE, BLOB) VALUES(?,?,?,?)";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, uuid.toString());
			stmt.setString(2, filename);

			// mime type extraction
			String mimeType = null;

			if (filename != null) {
				try {
					// originale
					MagicMatch match = Magic.getMagicMatch(content);
					mimeType = match.getMimeType();

					/*
					 * MagicMatch match = Magic.getMagicMatch(content); mimeType
					 * = match.getMimeType(); MagicDetector magic = new
					 * MagicDetector(type, pattern);
					 * 
					 * 
					 * TikaConfig config = TikaConfig.getDefaultConfig();
					 * Detector detector = config.getDetector();
					 * 
					 * ByteArrayInputStream bis = new
					 * ByteArrayInputStream(content);
					 * 
					 * TikaInputStream stream = TikaInputStream.get(bis);
					 * 
					 * Metadata metadata = new Metadata();
					 * metadata.add(Metadata.RESOURCE_NAME_KEY,
					 * filenameWithExtension); MediaType mediaType =
					 * detector.detect(stream, metadata);
					 */


				} catch (Exception e) {/* empty */
				}
			}

			stmt.setString(3, mimeType);

			InputStream inputStream = new ByteArrayInputStream(content);
			stmt.setBinaryStream(4, inputStream, (int) content.length);
			stmt.executeUpdate();
			conn.commit();
			return uuid.toString();
		} finally {
			try {
				if (autoCommit != null)
					conn.setAutoCommit(autoCommit);
			} catch (Exception e) {
			}
			;
			try {
				stmt.close();
			} catch (Exception e) {
			}
			;
			try {
				conn.close();
			} catch (Exception e) {
			}
			;
		}
	}

	public static String saveBlob(byte[] content, Class parentClazz) throws Exception {
		return saveBlob(content, null, parentClazz);
	}

	public static void deleteBlob(String id, Class parentClazz) throws Exception {
		String schema = getSchema(parentClazz);
		DataSource ds = getDatasource();

		Connection conn = null;
		Boolean autoCommit = null;
		PreparedStatement stmt = null;

		try {
			conn = ds.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			String query = "DELETE FROM " + schema + ".BLOB WHERE ID=?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			stmt.executeUpdate();
			conn.commit();
		} finally {
			try {
				if (autoCommit != null)
					conn.setAutoCommit(autoCommit);
			} catch (Exception e) {
			}
			;
			try {
				stmt.close();
			} catch (Exception e) {
			}
			;
			try {
				conn.close();
			} catch (Exception e) {
			}
			;
		}
	}

	public static byte[] getScaledImageByteArray(InputStream is, Integer newWidth) throws Exception {
		Image image = ImageIO.read(is);
		int width = ((BufferedImage) image).getWidth();
		int height = ((BufferedImage) image).getHeight();
		if (newWidth >= width)
			newWidth = width;
		Double ratio = (double) newWidth / width;
		Double newHeight = Math.ceil(ratio * height);
		image = image.getScaledInstance(newWidth, newHeight.intValue(), Image.SCALE_AREA_AVERAGING);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		BufferedImage bImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D bImageGraphics = bImage.createGraphics();		
		bImageGraphics.drawImage(image, null, null);
		RenderedImage rImage = (RenderedImage) bImage;
		
		ImageIO.write(rImage, "png", baos);
		return baos.toByteArray();
	}

	public static DataSource getDatasource() {
		return (DataSource) staticApplicationContext.getBean("dataSource");
	}

	public static String getSchema(Class parentClazz) {

		SessionFactory sessionFactory = (SessionFactory) staticApplicationContext.getBean("sessionFactory");
		ClassMetadata metadata = sessionFactory.getClassMetadata(parentClazz);

		if (metadata == null || !(metadata instanceof AbstractEntityPersister)) {
			throw new IllegalArgumentException("The passed model class is not correctly mapped in hibernate");
		} else {
			AbstractEntityPersister persister = (AbstractEntityPersister) metadata;
			String tableName = persister.getTableName();
			return StringUtils.substringBefore(tableName, ".");
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		staticApplicationContext = applicationContext;
	}

	public static String getBlobAsString(String id) throws Exception {

		DataSource ds = getDatasource();

		Boolean autoCommit = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String s = null;
			conn = ds.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			String query = "SELECT FILENAME, MIME_TYPE, BLOB FROM BLOB WHERE ID=?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {

				byte[] buffer = rs.getBytes(3);
				if (buffer == null || buffer.length == 0) {
				} else {
					s = new String(buffer, Charset.forName("utf-8"));
				}
				return s;
			} else
				throw new IllegalArgumentException("Blob with given id (" + id + ") does not exist.");

		} finally {
			try {
				if (autoCommit != null)
					conn.setAutoCommit(autoCommit);
			} catch (Exception e) {
			}
			;
			try {
				rs.close();
			} catch (Exception e) {
			}
			;
			try {
				stmt.close();
			} catch (Exception e) {
			}
			;
			try {
				conn.close();
			} catch (Exception e) {
			}
			;
		}
	}

	public static Blob getBlobAsBlob(String id) throws Exception {

		DataSource ds = getDatasource();

		Boolean autoCommit = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			Blob s = null;
			conn = ds.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			String query = "SELECT FILENAME, MIME_TYPE, BLOB FROM BLOB WHERE ID=?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {

				Blob buffer = rs.getBlob(3);
				if (buffer == null) {
				} else {
					s = buffer;
				}
				return s;
			} else
				throw new IllegalArgumentException("Blob with given id (" + id + ") does not exist.");

		} finally {
			try {
				if (autoCommit != null)
					conn.setAutoCommit(autoCommit);
			} catch (Exception e) {
			}
			;
			try {
				rs.close();
			} catch (Exception e) {
			}
			;
			try {
				stmt.close();
			} catch (Exception e) {
			}
			;
			try {
				conn.close();
			} catch (Exception e) {
			}
			;
		}
	}

	// public static InputStream getBlobAsInputStream(String id) throws
	// Exception {
	//
	// String blobString = getBlobAsString(id);
	// return IOUtils.toInputStream(blobString, "ISO-8859-1");
	//
	// }

	public static String getMimeTypeFromBlob(String id) throws Exception {

		DataSource ds = getDatasource();

		Boolean autoCommit = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String s = null;
			conn = ds.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			String query = "SELECT FILENAME, MIME_TYPE, BLOB FROM BLOB WHERE ID=?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {

				String buffer = rs.getString(2);
				if (buffer == null) {
				} else {
					s = buffer;
				}
				return s;
			} else
				throw new IllegalArgumentException("Blob with given id (" + id + ") does not exist.");

		} finally {
			try {
				if (autoCommit != null)
					conn.setAutoCommit(autoCommit);
			} catch (Exception e) {
			}
			;
			try {
				rs.close();
			} catch (Exception e) {
			}
			;
			try {
				stmt.close();
			} catch (Exception e) {
			}
			;
			try {
				conn.close();
			} catch (Exception e) {
			}
			;
		}
	}

}
