package br.com.m3tech.AppGas.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImprimirUtils {

	public static void imprimir(String text, String nomeImpressora) {
		try {
			log.info("Imprimindo na impressora: {}", nomeImpressora);
			DocFlavor df = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
			PrintService[] ps = PrintServiceLookup.lookupPrintServices(df, null);
			
			PrintService impressora = null;
			
			for (PrintService p : ps) {
				if(nomeImpressora.equalsIgnoreCase(p.getName())) {
					impressora = p;
				}
			}
			
			if(impressora == null) {
				impressora = ps[0];
			}
			
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
