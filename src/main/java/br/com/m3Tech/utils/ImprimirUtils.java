package br.com.m3Tech.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

public class ImprimirUtils {

	public static void imprimir(String text, PrintService impressora) {
		try {
			InputStream stream = new ByteArrayInputStream((text + (char) 27 + (char)109).getBytes());

			DocPrintJob dpj = impressora.createPrintJob();


			PrintRequestAttributeSet printerAtributes = new HashPrintRequestAttributeSet();
	        printerAtributes.add(new JobName("Impressao Robo do AppGas", null));
	        printerAtributes.add(OrientationRequested.PORTRAIT);
	        printerAtributes.add(MediaSizeName.ISO_A4);

			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

			Doc documentoTexto = new SimpleDoc(stream, flavor, null);

			dpj.print(documentoTexto, printerAtributes);
		} catch (PrintException ex) {
			System.err.println(ex.getMessage() + "  Local:  " + ex.getLocalizedMessage());
		}
	}
}
