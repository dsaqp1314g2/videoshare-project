package edu.upc.eetac.dsa.dsaqt1314g2.videoshare.api;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.InternalServerErrorException;

public class Prueba {
	public static void FileCopy(InputStream in, String destinationFile) {
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

	public static void main(String[] args) throws IOException {

		// try {

		// FileInputStream in = new FileInputStream(
		// "C:/Users/Mohamed/git/videoshare-project/www/video/2.webm");
		//
		// // String entrada = "";
		// DataInputStream dis = new DataInputStream(in);
		// String file =
		// "C:/Users/Mohamed/git/videoshare-project/www/video/7.webm";
		// FileOutputStream out = new FileOutputStream(file);
		// DataOutputStream output = new DataOutputStream(out);
		// String c;
		// int count;
		// int cont = 0;
		// while ((dis.readLine()) != null) {
		// c = dis.readLine();
		// output.writeChars(c);
		// System.out.println(c);
		// cont = cont +1;
		// System.out.println(cont);
		//
		// }
		//
		// dis.close();
		// output.close();
		// out.close();
		// in.close();
		// }catch (IOException e) {
		// System.err.println("Hubo un error de entrada/salida!!!");
		// }

		// output.writeChars(entrada);
		File inFile = new File(
				"C:/Users/Mohamed/git/videoshare-project/www/video/2.webm");
		InputStream in = new FileInputStream(inFile);
		FileCopy(in,
				"C:/Users/Mohamed/git/videoshare-project/www/video/72.webm");

		System.out.println("ya está");
	}

}
