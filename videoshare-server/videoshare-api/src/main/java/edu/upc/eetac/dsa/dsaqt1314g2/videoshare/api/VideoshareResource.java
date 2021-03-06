package edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.model.Categoria;
import edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.model.Puntuaciones;
import edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.model.Review;
import edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.model.User;
import edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.model.Videos;
import edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api.model.VideosCollection;

@Path("/videoshare")
public class VideoshareResource {

	// variables globales:
	@Context
	private Application app;

	SecurityContext security;

	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	// para obtener la coleción de videos GET (1)

	@GET
	@Produces(Mediatype.VIDEOSHARE_API_VIDEOS_COLLECTION)
	public VideosCollection getVideos() {
		VideosCollection videos = new VideosCollection();

		// hacemos la conexión a la base de datos
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException(
					"Could not connect to the databaseeeeeeeeeeeeee",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildQueryGetVideosCollection();
			stmt = conn.prepareStatement(sql);
			// obtenemos la respuesta
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Videos video = new Videos();
				video.setVideoid(rs.getString("videoid"));
				video.setNombre_video(rs.getString("nombre_video"));
				video.setUsername(rs.getString("username"));
				video.setFecha(rs.getDate("fecha"));
				video.setFilename(rs.getString("videoid") + ".webm");
				video.setUrl(app.getProperties().get("videoBaseURL")
						+ video.getFilename());
				String videoid = video.getVideoid();
				try {
					System.out.println("ahora coge las reviews");
					String sqlr = "select*from review where videoid = ?";

					stmt = conn.prepareStatement(sqlr);
					stmt.setString(1, videoid);
					ResultSet rs2 = stmt.executeQuery();

					rs2 = stmt.executeQuery();
					while (rs2.next()) {
						Review review = new Review();
						review.setVideoid(rs2.getInt("videoid"));
						review.setReviewtext(rs2.getString("reviewtext"));
						review.setFecha_hora(rs2.getDate("fecha_hora"));
						review.setReviewid(rs2.getInt("reviewid"));
						review.setUsername(rs2.getString("username"));

						video.addReview(review);
					}

					System.out.println("ahora coge las categorias");

					String sqlc = "select * from categorias where videoid=?";
					stmt.close();
					stmt = conn.prepareStatement(sqlc);
					stmt.setString(1, videoid);
					rs2 = stmt.executeQuery();
					if (rs2.next()) {
						Categoria cat = new Categoria();
						cat.setTagid(rs2.getString("tagid"));
						cat.setCategoria(rs2.getString("categoria"));

						video.addCategoria(cat);
					} else {
						System.out.println("Aqui es que no hay categorias");
					}
					System.out.println("ahora coge las puntuaciones");
					String sqlp = "select * from puntuaciones where videoid=?";
					stmt.close();
					stmt = conn.prepareStatement(sqlp);
					stmt.setString(1, videoid);
					rs2 = stmt.executeQuery();
					int i = 0;
					int puntuacion = 0;
					while (rs2.next()) {
						Puntuaciones punt = new Puntuaciones();
						punt.setPuntuacionid(rs2.getInt("puntuacionid"));
						punt.setPuntuacion(rs2.getInt("puntuacion"));
						i++;
						video.addPuntuacion(punt);
						puntuacion = puntuacion + punt.getPuntuacion();
					}
					if (puntuacion != 0) {

						video.setPuntuacion((int) (puntuacion / i));
					}
				} catch (SQLException e) {
					throw new ServerErrorException(e.getMessage(),
							Response.Status.INTERNAL_SERVER_ERROR);
				} finally {
					System.out.println("ahora añade el video");
					videos.addVideos(video);
				}
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new NotFoundException();
			}
		}

		return videos;
	}

	// (2) Obtener un video a partir de su identificador videoid
	@GET
	@Path("/{videoid}")
	@Produces(Mediatype.VIDEOSHARE_API_VIDEOS)
	public Videos getVideoid(@PathParam("videoid") String videoid) {

		// llamaremos a la método que nos permite obtener un video a partir de
		// su
		// video id además de la categoría que está asociado, comentarios, y
		// puntuación

		Videos video = getVideoFromDatabase(videoid);
		return video;

	}

	public static String md5(String clear) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] b = md.digest(clear.getBytes());
		int size = b.length;
		StringBuffer h = new StringBuffer(size);
		// algoritmo y arreglo md5
		for (int i = 0; i < size; i++) {
			int u = b[i] & 255;
			if (u < 16) {
				h.append("0" + Integer.toHexString(u));
			} else {
				h.append(Integer.toHexString(u));
			}
		}
		// clave encriptada
		return h.toString();
	}

	// crear un nuevo usuario.
	@POST
	@Path("/users")
	@Consumes(Mediatype.VIDEOSHARE_API_USERS)
	@Produces(Mediatype.VIDEOSHARE_API_USERS)
	public User creatUser(User usuario) throws Exception {

		System.out.println("Entra en método crear usuario");
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildCreateUser();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, usuario.getUsername());
			stmt.setString(2, usuario.getUserpass());
			stmt.setString(3, usuario.getName());
			stmt.setString(4, usuario.getEmail());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				throw new ForbiddenException("You have got registered");
			} else {
				// Something has failed...
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ForbiddenException("Este usuario ya existe!");
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}
		return usuario;
	}

	private String buildCreateUser() {
		return "insert into users (username, userpass, name,email) values (?, MD5(?),   ? ,?)";
	}

	// (6)PUT de un video. Sólo lo puede modificar el usuario que ha subido el
	// video
	@PUT
	@Path("/{videoid}")
	@Consumes(Mediatype.VIDEOSHARE_API_VIDEOS)
	@Produces(Mediatype.VIDEOSHARE_API_VIDEOS)
	public Videos updateBook(@PathParam("videoid") String videoid, Videos video) {

		// ¡¡¡¡¡¡¡ falta añadir que compruebe que el usuario que edita sea el
		// que lo haya creado
		// Alicia solo edita lo de Alicia
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			// llamamos a la función para la query y la hacemos la database
			String sql = buildUpdateVideo();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, video.getNombre_video());
			stmt.setString(2, videoid);

			stmt.executeUpdate(); // para añadir la ficha del libro con los
									// datos a la BBDD
			// si ha ido bien la inserción
			video = getVideoFromDatabase(videoid);

			// ResultSet rs = stmt.getGeneratedKeys();
			// if (rs.next()) {
			// // devuelve el video editado
			//
			// } else {
			// throw new NotFoundException("Could not update the video info");
			// }

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}

		return video;

	}

	// (7)DELETE, eliminar un video a partir de su libroid. Eliminar solo el
	// usuario que lo ha creado. Exige estar registrado.

	// cuando se elimine el video se eliminará todo lo que esté asociado a él,
	// es decir, se eliminarán las categorías, los comentarios, las
	// puntuaciones.
	/*
	 * @POST
	 * 
	 * @Consumes(Mediatype.VIDEOSHARE_API_VIDEOS)
	 * 
	 * @Produces(Mediatype.VIDEOSHARE_API_VIDEOS) public VideosCollection
	 * creatvideo(Videos video) { // Comprobamos que el usuario que vaya a crear
	 * la ficha de libro sea // ADMIN VideosCollection videos = new
	 * VideosCollection();
	 * 
	 * 
	 * if (!security.isUserInRole("registered")) { throw new
	 * ForbiddenException("You have not get registered"); }
	 * 
	 * 
	 * Connection conn = null; try { conn = ds.getConnection(); } catch
	 * (SQLException e) { throw new
	 * ServerErrorException("Could not connect to the database",
	 * Response.Status.SERVICE_UNAVAILABLE); }
	 * 
	 * PreparedStatement stmt = null; try { String sql =
	 * buildQueryInsertVideo(); stmt = conn.prepareStatement(sql,
	 * Statement.RETURN_GENERATED_KEYS);
	 * 
	 * stmt.setString(1, video.getNombre_video()); stmt.setString(2,
	 * video.getUsername()); // stmt.setDate(3, video.getFecha());
	 * 
	 * stmt.executeUpdate();
	 * 
	 * // si ha ido bien la inserciÃ³n ResultSet rs = stmt.getGeneratedKeys();
	 * if (rs.next()) { videos = getVideos(); } else { // Something has
	 * failed... // throw new NotFoundException();
	 * System.out.println("no encuentra el video"); } } catch (SQLException e) {
	 * e.printStackTrace(); } finally { try { if (stmt != null) stmt.close();
	 * conn.close(); } catch (SQLException e) { throw new
	 * ServerErrorException(e.getMessage(),
	 * Response.Status.INTERNAL_SERVER_ERROR); } }
	 * 
	 * return videos; }
	 * 
	 * private String buildQueryInsertVideo() { return
	 * "insert into videos (videoid, nombre_video, username,  fecha) value (null, ?, ?, now())"
	 * ; }
	 */
	@DELETE
	@Path("/{videoid}/{username}")
	public void deleteVideo(@PathParam("videoid") String videoid,
			@PathParam("username") String username) {
		// String username = usuario.getUsername();
		// ¡¡¡¡ falta añadir que el usuario que vaya a eliminar sea el que ha
		// subido el video !!!!

		try {
			validateUserActions(username, videoid);
			// Todo OK. Continuamos:
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException(
						"Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}

			PreparedStatement stmt = null;
			try {
				// llamamos a la función para la query y la hacemos la database
				String sql = buildDeleteVideo();
				stmt = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, videoid);

				int rows = stmt.executeUpdate();

				if (rows == 0) {
					throw new NotFoundException("There's no video with videod="
							+ videoid);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					conn.close();
				} catch (SQLException e) {
					throw new ServerErrorException(e.getMessage(),
							Response.Status.INTERNAL_SERVER_ERROR);
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	// Validar que el usuario que modifica/elimina es el que ha subido el vídeo
	private void validateUserActions(String currentUsername, String videoid)
			throws Exception {
		System.out.println("Entra en método validateUserActions");

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildValidateUser();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, videoid);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				String username_video = rs.getString("username");

				// ahora comprobamos que el usuario que intenta modificar sea el
				// que ha subido el vídeo
				if (currentUsername.equals(username_video)) {
					System.out
							.println("El usuario que trata de modificar/eliminar y el que ha subido el vídeo es el mismo. Todo OK.");

				} else {
					throw new ForbiddenException(
							"No tienes permiso para modificar o eliminar este vídeo.");
				}
			} else {
				throw new Exception("No existe vídeo con ese videoid.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}

	}

	private String buildValidateUser() {
		return "select username from videos where videoid=?";
	}

	// (8) Hacer publicación de un comentario de un video
	@POST
	@Path("/{videoid}/reviews")
	@Consumes(Mediatype.VIDEOSHARE_API_REVIEWS)
	@Produces(Mediatype.VIDEOSHARE_API_REVIEWS)
	public Videos creatReview(@PathParam("videoid") String videoid,
			Review review) {
		// Comprobamos que el usuario que vaya a crear la ficha de libro sea
		// ADMIN
		Videos video = null;
		/*
		 * if (!security.isUserInRole("registered")) { throw new
		 * ForbiddenException("You have not registered"); }
		 */

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildCreateReview();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, videoid);
			stmt.setString(2, review.getUsername());
			stmt.setString(4, review.getReviewtext());
			stmt.setDate(3, review.getFecha_hora());
			stmt.executeUpdate();

			// si ha ido bien la inserción
			ResultSet rs = stmt.getGeneratedKeys();
			video = getVideoFromDatabase(videoid);
			if (rs.next()) {

			} else {
				// Something has failed...
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}

		return video;
	}

	// (10) DELETE - eliminar un comentario con reviewid de un video que videoid
	@DELETE
	@Path("/{videoid}/reviews/{reviewid}/{username}")
	public void deleteReview(@PathParam("videoid") String videoid,
			@PathParam("reviewid") String reviewid,
			@PathParam("username") String username) {

		// nos aseguramos que el usuario esté registrado
		/*
		 * if (!security.isUserInRole("registered")) { throw new
		 * ForbiddenException("You have not registered"); }
		 */

		// ahora el usuario, que el usuario que vaya a eliminar el comentario
		// del video
		// sea el que ha creado dicho comentario
		// validateUser(reviewid);
		try {
			validateUser(username, reviewid);
			// Todo OK. Continuamos:
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException(
						"Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}

			PreparedStatement stmt = null;
			try {
				// llamamos a la función para la query y la hacemos la database
				String sql = buildDeleteReview();
				stmt = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);

				stmt.setString(1, reviewid);

				int rows = stmt.executeUpdate();

				if (rows == 0) {
					throw new NotFoundException(
							"There's no review with review=" + reviewid
									+ "with the video with videoid=" + videoid);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					conn.close();
				} catch (SQLException e) {
					throw new ServerErrorException(e.getMessage(),
							Response.Status.INTERNAL_SERVER_ERROR);
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// ******************* Métodos adicionales / QUERIES *********************

	// (1)GET colección de libros
	private String buildQueryGetVideosCollection() {
		return "select * from videos";
	}

	// (2)GET de un libro con identificador bookid
	private String buildQueryGetVideoByVideoid() {
		return "select * from videos where videoid=?";
	}

	// 5.3. Método para obtener libro con bookid
	private Videos getVideoFromDatabase(String videoid) {
		Connection conn = null;
		// VideosCollection videos = new VideosCollection();
		Videos video = new Videos();
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildQueryGetVideoByVideoid();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, videoid);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {

				video.setVideoid(rs.getString("videoid"));
				video.setNombre_video(rs.getString("nombre_video"));
				video.setUsername(rs.getString("username"));
				video.setFecha(rs.getDate("fecha"));
				video.setFilename(rs.getString("videoid") + ".webm");
				video.setUrl(app.getProperties().get("videoBaseURL")
						+ video.getFilename());
			} else {

			}

			String sqlr = "select*from review where videoid = ?";
			stmt.close();
			stmt = conn.prepareStatement(sqlr);
			stmt.setString(1, videoid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Review review = new Review();
				review.setVideoid(rs.getInt("videoid"));
				review.setReviewtext(rs.getString("reviewtext"));
				review.setFecha_hora(rs.getDate("fecha_hora"));
				review.setReviewid(rs.getInt("reviewid"));
				review.setUsername(rs.getString("username"));

				video.addReview(review);
			}

			String sqlc = "select*from categorias where videoid=?";
			stmt.close();
			stmt = conn.prepareStatement(sqlc);
			stmt.setString(1, videoid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Categoria cat = new Categoria();
				cat.setTagid(rs.getString("tagid"));
				cat.setCategoria(rs.getString("categoria"));

				video.addCategoria(cat);
				// } else {
				// throw new NotFoundException();
			}

			String sqlp = "select*from puntuaciones where videoid=?";
			stmt.close();
			stmt = conn.prepareStatement(sqlp);
			stmt.setString(1, videoid);
			rs = stmt.executeQuery();
			int i = 0;
			int puntuacion = 0;
			while (rs.next()) {
				Puntuaciones punt = new Puntuaciones();
				punt.setPuntuacionid(rs.getInt("puntuacionid"));
				punt.setPuntuacion(rs.getInt("puntuacion"));

				video.addPuntuacion(punt);
				i++;
				puntuacion = puntuacion + rs.getInt("puntuacion");
			}
			if (puntuacion != 0) {

				video.setPuntuacion((int) (puntuacion / i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// Haya ido bien o haya ido mal cierra las conexiones
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}
		return video;
	}

	// (6)PUT hacer una actualización de la ficha del libro:
	private String buildUpdateVideo() {
		return "update videos set nombre_video = ? where videoid = ? ";
	}

	// (7) DELETE - eliminar un video a partir de videoid
	private String buildDeleteVideo() {
		return "delete from videos where videoid=?";
	}

	// (8) POST Crear una reseña de un libro con bookid
	private String buildCreateReview() {
		return "insert into review (videoid, username, fecha_hora, reviewtext) value (?, ?, ?, ?)";
	}

	// 8.1. Obtener review a partir del reviewid
	private Review getReviewFromDatbase(String reviewid) {
		Connection conn = null;

		Review review = new Review();
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildQueryGetReviewByReviewid();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(reviewid));

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				review.setVideoid(rs.getInt("videoid"));
				review.setUsername(rs.getString("username"));
				review.setReviewid(rs.getInt("reviewid"));
				review.setFecha_hora(rs.getDate("fecha_hora"));
				// FALTA AÑADIR PARA EL TEXTO DE LA RESEÑA
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// Haya ido bien o haya ido mal cierra las conexiones
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}
		return review;

	}

	// 8.2. Hacer query
	private String buildQueryGetReviewByReviewid() {
		return "select*from review where reviewid=?";
	}

	private String getReviewsFromDatabaseByVideoid(String videoid) {
		return "select username from review where review.videoid = " + videoid;
	}

	// (9) Actulizar reseña
	// 9.1. Validación del usuario (alicia sólo editar/eliminar reseña de
	// Alicia)
	private void validateUser(String Username, String reviewid) {
		// si el usuario que consulta la reseña no es el que la ha creado,
		// ForbiddenException
		Review currentReview = getReviewFromDatbase(reviewid);
		if (Username.equals(currentReview.getUsername())) {
			System.out
					.println("El usuario que trata de modificar/eliminar y el que ha subido el vídeo es el mismo. Todo OK.");

		} else {
			throw new ForbiddenException(
					"No tienes permiso para modificar o eliminar este comentario.");
		}
	}

	private int validateUserPunt(String videoid, String usuario)
			throws SQLException {
		// si el usuario que consulta la reseña no es el que la ha creado,
		// ForbiddenException
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		String sql = "select * from puntuaciones where videoid = ? and username = ? ";
		stmt = conn.prepareStatement(sql);
		stmt.setString(1, videoid);
		stmt.setString(2, usuario);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {

			return 0;
		}

		else {
			return 1;
		}
	}

	// (10) Eliminar una reseña de un libro
	// 10.1. Query a la base de datos:
	private String buildDeleteReview() {
		return "delete from review where reviewid=?";
	}

	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	@POST
	@Path("/upload/{username}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Videos uploadVideo(@FormDataParam("title") String title,
			@FormDataParam("video") InputStream video,
			@FormDataParam("video") FormDataContentDisposition fileDisposition,
			@FormDataParam("categoria") String categoria,
			@PathParam("username") String username) {

		String donde = fileDisposition.getFileName();
		System.out.println(donde);
		Videos video1 = null;
		String filename;
		int videoid;
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			String sql = "insert into videos (nombre_video, username, fecha) values (?,?, now())";
			String sql2 = "insert into categorias(videoid, categoria) values(?,?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt2 = conn.prepareStatement(sql2);
			stmt.setString(1, title);

			stmt.setString(2, username);
			System.out.println("Query:" + stmt);
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				videoid = rs.getInt(1);
				System.out.println(videoid);
				filename = Integer.toString(videoid) + ".webm";
				String file = app.getProperties().get("uploadFolder")
						+ filename;
				FileCopy(video, file);
				video1 = getVideoFromDatabase(Integer.toString(videoid));
				stmt2.setInt(1, videoid);
				stmt2.setString(2, categoria);
				System.out.println("Query:" + stmt2);
				stmt2.executeUpdate();
			} else {

				// Something has failed...
			}

			// se utiliza el método sting para pasarle el stingid
			// para crear el sting -> JSON
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}

		}

		// ImageData imageData = new ImageData();
		// imageData.setFilename(uuid.toString() + ".png");
		// imageData.setTitle(title);
		//
		// imageData.setImageURL(app.getProperties().get("imgBaseURL")
		// + imageData.getFilename());
		//
		return video1;

	}

	// private void writeVideo(InputStream video, String videoid) {
	// String filename = videoid + ".webm";
	// try {
	// DataInputStream dis = new DataInputStream(video);
	// String file = app.getProperties().get("uploadFolder") + filename;
	// FileOutputStream out = new FileOutputStream(file);
	// DataOutputStream output = new DataOutputStream(out);
	// String entrada = "";
	// while ((entrada = dis.readLine()) != null) {
	// entrada = entrada + dis.readChar();
	// }
	// output.writeChars(entrada);
	// DataOutputStream out = new DataOutputStream( file );
	//
	// BufferedReader inputStream = null;
	// PrintWriter outputStream = new PrintWriter(new
	// FileWriter(app.getProperties().get("uploadFolder") + filename));;
	// Scanner s = null;
	// inputStream = new BufferedReader(new FileReader());
	// s = new Scanner (inputStream);

	// while (s.hasNext()) {
	// v = v+s.next();
	// }

	// file.read(
	// file,
	// "webm",
	// new File(app.getProperties().get("uploadFolder") + filename));
	// } catch (IOException e) {
	// throw new InternalServerErrorException(
	// "Something has been wrong with the file.");
	// }
	//
	// }

	public void FileCopy(InputStream in, String destinationFile) {
		// System.out.println("Desde: " + sourceFile);
		System.out.println("Hacia: " + destinationFile);

		try {
			// File inFile = new File(sourceFile);
			File outFile = new File(destinationFile);

			// InputStream in = new FileInputStream(inFile);
			FileOutputStream out = new FileOutputStream(outFile);

			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}

			in.close();
			out.close();
		} catch (IOException e) {
			System.err.println("Hubo un error de entrada/salida!!!");
		}
	}

	// Buscar un video por su titulo
	@GET
	@Path("/search")
	@Produces(Mediatype.VIDEOSHARE_API_VIDEOS_COLLECTION)
	public VideosCollection searchvideo(@QueryParam("titulo") String titulo) {
		System.out.println("Entramos en el método");
		VideosCollection videos = new VideosCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException(
					"Could not connect to the databaseeeeeeeeeeeeee",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		try {
			String sql = buildQueryGetVideoBytitulo();
			stmt = conn.prepareStatement(sql);
			System.out.println(titulo);
			stmt.setString(1, '%' + titulo + '%');
			System.out.println("Query:" + stmt);
			ResultSet rs = stmt.executeQuery();

			// obtenemos los videoid de los videos que tienen asignada esa
			// categoria
			if (rs.next()) {
				// Videos video = new Videos();
				// video.setVideoid(rs.getString("videoid"));
				System.out.println("AAAAAAAAAAAAAAAAA");
				Videos video2 = getVideoFromDatabase(rs.getString("videoid"));
				System.out.println(video2.getUsername());
				videos.addVideos(video2);
				while (rs.next()) {
					// Videos video = new Videos();
					// video.setVideoid(rs.getString("videoid"));
					System.out.println("AAAAAAAAAAAAAAAAA");
					video2 = getVideoFromDatabase(rs.getString("videoid"));
					System.out.println(video2.getUsername());
					videos.addVideos(video2);
				}
			} else {

				System.out.println("No hay un video con ese nombre");
				throw new ServerErrorException(
						"No hay un video con ese nombre",
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// Haya ido bien o haya ido mal cierra las conexiones
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}

		return videos;

	}

	// método para buscar y obtener libro a partir de la categoria:

	private String buildQueryGetVideoBytitulo() {
		String sql = "select * from videos where nombre_video like ?";
		System.out.println("Query:" + sql);
		return sql;
	}

	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	// método para aumentar la puntuación de un video
	@POST
	@Path("/{videoid}/puntuacion")
	@Consumes(Mediatype.VIDEOSHARE_API_PUNTUACION)
	// @Produces(Mediatype.VIDEOSHARE_API_VIDEOS)
	public Videos insterPuntuacion(@PathParam("videoid") String videoid,
			Puntuaciones Puntos) throws SQLException {

		Videos videop = new Videos();
		String username = Puntos.getUsername();
		Puntuaciones puntuacion = Puntos;

		if (validateUserPunt(videoid, username) == 0) {
			throw new ServerErrorException(
					"You have already qualified this video!",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		// el primer es obtener los puntos que ya tiene el videoid en la tabla
		// (si se da el caso de que el video ya ha sido puntuado, cualquier otro
		// caso 0)
		// else {
		int punt = puntuacion.getPuntuacion();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildQueryInsertPuntuacion();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, puntuacion.getVideoid());
			stmt.setString(2, puntuacion.getUsername());
			stmt.setInt(3, punt);

			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				// throw new ForbiddenException("You have got registered");
				videop = getVideoFromDatabase(videoid);
			} else {
				// Something has failed...
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}

		return videop;
		// }
	}

	private String buildQueryInsertPuntuacion() {
		return "insert into puntuaciones (videoid, username, puntuacion) value (?,?,?) ";
	}

	private int getPuntuacionFromVideoid(String videoid) {

		int puntos = 0;

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = "select puntuacion from puntuaciones where videoid="
					+ videoid;
			stmt = conn.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				puntos = rs.getInt("puntuacion");
				System.out.println("Se han obtenido los puntos del video: "
						+ puntos);
			} else {
				// Something has failed...
				// throw new NotFoundException();
				System.out
						.println("No se ha encontrado la puntuación en la tabla");
				puntos = 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}

		// sea cual sea el resultado devuelve puntos (el valor de la tabla o 0)
		return puntos;
	}

	// * * * *
	// para ordenar por categoria/puntuación/
	@GET
	@Path("/bypuntuacion")
	@Produces(Mediatype.VIDEOSHARE_API_VIDEOS_COLLECTION)
	public VideosCollection getVideosOrderByPuntuacion() {
		VideosCollection videos = getVideos();
		/*
		 * Videos video = new Videos(); // hacemos la conexión a la base de
		 * datos Connection conn = null; try { conn = ds.getConnection(); }
		 * catch (SQLException e) { throw new ServerErrorException(
		 * "Could not connect to the databaseeeeeeeeeeeeee",
		 * Response.Status.SERVICE_UNAVAILABLE); }
		 * 
		 * PreparedStatement stmt = null; try { String sql =
		 * buildQueryGetVideosOrderByPuntuacion(); stmt =
		 * conn.prepareStatement(sql); // obtenemos la respuesta ResultSet rs =
		 * stmt.executeQuery();
		 * 
		 * while (rs.next()) { try {
		 * 
		 * String videoid = rs.getString("videoid");
		 * 
		 * // ahora obtenemos la info del video con videoid: video =
		 * getVideoFromDatabase(videoid);
		 * 
		 * } catch (SQLException e) { throw new
		 * ServerErrorException(e.getMessage(),
		 * Response.Status.INTERNAL_SERVER_ERROR); } finally {
		 * System.out.println("ahora añade el video"); videos.addVideos(video);
		 * } } } catch (SQLException e) { throw new
		 * ServerErrorException(e.getMessage(),
		 * Response.Status.INTERNAL_SERVER_ERROR); } finally { try { if (stmt !=
		 * null) stmt.close(); conn.close(); } catch (SQLException e) { throw
		 * new NotFoundException(); } }
		 */
		videos.ordenar();
		return videos;
	}

	// (1)GET colección de libros
	/*
	 * private String buildQueryGetVideosOrderByPuntuacion() { return
	 * "select * from puntuaciones order by puntuacion"; }
	 */

	// * * *

	@GET
	@Path("/bycategoria")
	@Produces(Mediatype.VIDEOSHARE_API_VIDEOS_COLLECTION)
	public VideosCollection getVideosOrderByCategoria() {
		VideosCollection videos = new VideosCollection();
		Videos video = new Videos();
		// hacemos la conexión a la base de datos
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException(
					"Could not connect to the databaseeeeeeeeeeeeee",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildQueryGetVideosOrderByCategoria();
			stmt = conn.prepareStatement(sql);
			// obtenemos la respuesta
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				try {

					String videoid = rs.getString("videoid");

					// ahora obtenemos la info del video con videoid:
					video = getVideoFromDatabase(videoid);

				} catch (SQLException e) {
					throw new ServerErrorException(e.getMessage(),
							Response.Status.INTERNAL_SERVER_ERROR);
				} finally {
					System.out.println("ahora añade el video");
					videos.addVideos(video);
				}
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new NotFoundException();
			}
		}

		return videos;
	}

	// (1)GET colección de libros
	private String buildQueryGetVideosOrderByCategoria() {
		return "select * from categorias order by categoria";
	}

	// * * *
	// Ordernar por fecha
	@GET
	@Path("/byfecha")
	@Produces(Mediatype.VIDEOSHARE_API_VIDEOS_COLLECTION)
	public VideosCollection getVideosOrderByDate() {
		VideosCollection videos = new VideosCollection();
		Videos video = new Videos();
		// hacemos la conexión a la base de datos
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException(
					"Could not connect to the databaseeeeeeeeeeeeee",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildQueryGetVideosOrderByCategoria();
			stmt = conn.prepareStatement(sql);
			// obtenemos la respuesta
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				try {

					String videoid = rs.getString("videoid");

					// ahora obtenemos la info del video con videoid:
					video = getVideoFromDatabase(videoid);

				} catch (SQLException e) {
					throw new ServerErrorException(e.getMessage(),
							Response.Status.INTERNAL_SERVER_ERROR);
				} finally {
					System.out.println("ahora añade el video");
					videos.addVideos(video);
				}
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new NotFoundException();
			}
		}

		return videos;
	}

	// (1)GET colección de libros
	private String buildQueryGetVideosOrderByDate() {
		return "select * from videos order by fecha";
	}

	// * * *
	// Ordernar por fecha
	@GET
	@Path("/byusername")
	@Produces(Mediatype.VIDEOSHARE_API_VIDEOS_COLLECTION)
	public VideosCollection getVideosOrderByUsername() {
		VideosCollection videos = new VideosCollection();
		Videos video = new Videos();
		// hacemos la conexión a la base de datos
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException(
					"Could not connect to the databaseeeeeeeeeeeeee",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildQueryGetVideosOrderUsername();
			stmt = conn.prepareStatement(sql);
			// obtenemos la respuesta
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				try {

					String videoid = rs.getString("videoid");

					// ahora obtenemos la info del video con videoid:
					video = getVideoFromDatabase(videoid);

				} catch (SQLException e) {
					throw new ServerErrorException(e.getMessage(),
							Response.Status.INTERNAL_SERVER_ERROR);
				} finally {
					System.out.println("ahora añade el video");
					videos.addVideos(video);
				}
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new NotFoundException();
			}
		}

		return videos;
	}

	// (1)GET colección de libros
	private String buildQueryGetVideosOrderUsername() {
		return "select * from videos order by username";
	}

	// / * * * * obtener por categoria

	@GET
	@Path("/searchc")
	@Produces(Mediatype.VIDEOSHARE_API_VIDEOS_COLLECTION)
	public VideosCollection getVideosByCategoria(
			@QueryParam("categoria") String categoria) {
		VideosCollection videos = new VideosCollection();
		Videos video = new Videos();
		// hacemos la conexión a la base de datos
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException(
					"Could not connect to the databaseeeeeeeeeeeeee",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildQueryGetVideosByCategoria();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, categoria);
			// obtenemos la respuesta
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				try {
					System.out
							.println("hemos obtenido video a partir de la categoría");
					String videoid = rs.getString("videoid");

					// ahora obtenemos la info del video con videoid:
					video = getVideoFromDatabase(videoid);
					System.out.println("se ha añadido a la colección");

				} catch (SQLException e) {
					throw new ServerErrorException(e.getMessage(),
							Response.Status.INTERNAL_SERVER_ERROR);
				} finally {
					System.out.println("ahora añade el video");
					videos.addVideos(video);
				}
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new NotFoundException();
			}
		}

		return videos;
	}

	// (1)GET colección de libros
	private String buildQueryGetVideosByCategoria() {
		return "select * from categorias where categoria = ?";
	}

	// * * * * * ** * * *
	// login del usuario
	// crear un nuevo usuario.
	@POST
	@Path("/login")
	@Consumes(Mediatype.VIDEOSHARE_API_USERS)
	@Produces(Mediatype.VIDEOSHARE_API_USERS)
	public User loginUser(User usuario) throws Exception {

		System.out.println("Entra en método login usuario");
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		String userlogname = usuario.getUsername();
		String userlogpass = md5(usuario.getUserpass());

		PreparedStatement stmt = null;
		try {
			String sql = buildLoginUser();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, usuario.getUsername());
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				// String username = rs.getString("username");
				String userpass = rs.getString("userpass");

				if (userlogpass.equals(userpass)) {
					System.out.println("Usuario y password son correctos.");
				} else {
					System.out.println("Usuario y/o password son incorrectos.");
					throw new Exception("Erro de login.");
				}
			} else {
				throw new Exception(
						"Ningún usuario con ese username y password.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}
		return usuario;
	}

	private String buildLoginUser() {
		return "select * from users where username=?";
	}

}
