package com.tt.ts.rest.quotation.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tt.nc.common.util.TTLog;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.rest.common.util.ErrorCodeConstant;
import com.tt.ts.rest.quotation.manager.QuotationManager;
import com.tt.ts.rest.quotation.model.QuotationCommonBean;
import com.tt.ts.rest.quotation.model.QuotationModel;
import com.tt.ts.rest.quotation.model.QuotationProductsModel;

@Service
@Component(value = "QuotationService")
public class QuotationService {

	@Autowired
	private QuotationManager quotationManager;

	private static Font TIME_ROMAN = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
	private static Font TIME_ROMAN_SMALL = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
	private static Font TIME_ROMAN_VSMALL = new Font(Font.TIMES_ROMAN, 8);
	private static Font TIME_ROMAN_VSMALL_BOLD = new Font(Font.TIMES_ROMAN, 8, Font.BOLD);

	public ResultBean saveQuotationData(QuotationModel quotationModel) {
		ResultBean resultBean = new ResultBean();
		try {

			quotationModel.setCreationTime(new Date());
			quotationModel.setLastModTime(new Date());

			quotationModel = quotationManager.saveUpdateQuotation(quotationModel);
			resultBean.setResultObject(quotationModel);
			resultBean.setIserror(false);
		} catch (Exception ex1) {
			resultBean.setIserror(true);
			String errorCode = ErrorCodeConstant.DATA_NOT_SAVED_QUOTE;
			String errorMessage = ResourceBundle.getBundle("ApplicationResources",
					LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(0, errorCode, ex1);
		}
		return resultBean;
	}

	public ResultBean fetchQuotationByQuoteNo(Integer quotationId) {
		ResultBean resultBean = new ResultBean();
		try {
			List<QuotationModel> quotationModel = quotationManager
					.fetchQuotationByQuoteNo(quotationId);
			resultBean.setResultList(quotationModel);
			resultBean.setIserror(false);
		} catch (Exception e) {
			resultBean.setIserror(true);
			String errorCode = ErrorCodeConstant.DATA_NOT_FOUND_QUOTE;
			String errorMessage = ResourceBundle.getBundle("ApplicationResources",
					LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(0, errorCode, e);
		}
		return resultBean;
	}

	public ResultBean fetchQuotationByUserId(Integer userId) {
		ResultBean resultBean = new ResultBean();
		try {
			List<QuotationModel> quotationModel = quotationManager.fetchQuoteByUserId(userId);
			resultBean.setResultList(quotationModel);
			resultBean.setIserror(false);
		} catch (Exception e) {
			resultBean.setIserror(true);
			String errorCode = ErrorCodeConstant.DATA_NOT_FOUND_QUOTE;
			String errorMessage = ResourceBundle.getBundle("ApplicationResources",
					LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(0, errorCode, e);
		}
		return resultBean;
	}

	public ResultBean fetchQuoteByUserIdAndDateRange(Integer userId, Date startDate, Date endDate) {
		ResultBean resultBean = new ResultBean();
		try {
			List<QuotationModel> quotationModel = quotationManager.fetchQuoteByUserIdAndDateRange(
					userId, startDate, endDate);
			resultBean.setResultList(quotationModel);
			resultBean.setIserror(false);
		} catch (Exception e) {
			resultBean.setIserror(true);
			String errorCode = ErrorCodeConstant.DATA_NOT_FOUND_QUOTE;
			String errorMessage = ResourceBundle.getBundle("ApplicationResources",
					LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(0, errorCode, e);
		}
		return resultBean;
	}

	public ResultBean downloadQuotePdf(String file, QuotationModel quotationModel,
			HttpServletResponse response) {
		ResultBean resultBean = new ResultBean();
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();

			addMetaData(document);
			addTitlePage(document);
			addTitleClientPage(document);
			createTableClient(document, quotationModel);

			if (quotationModel.gettTQuotationProducts() != null
					&& quotationModel.gettTQuotationProducts().size() > 0) {
				for (QuotationProductsModel ttQuoteProducts : quotationModel
						.gettTQuotationProducts()) {
					if ("flight".equalsIgnoreCase(ttQuoteProducts.getProductName())) {
						addTitleFlightPage(document);
						// createTableFlight(document, ttQuoteProducts);
					} else if ("hotel".equalsIgnoreCase(ttQuoteProducts.getProductName())) {
						addTitleHotelPage(document);
						// createTableHotel(document, quotationModel,
						// ttQuoteProducts);
					}
				}
			}
			document.close();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos = convertPDFToByteArrayOutputStream(file);
			OutputStream os = response.getOutputStream();
			baos.writeTo(os);
			os.flush();
		} catch (Exception e) {
			resultBean.setIserror(true);
			String errorCode = ErrorCodeConstant.DATA_NOT_DOWNLOAD_QUOTE;
			String errorMessage = ResourceBundle.getBundle("ApplicationResources",
					LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(0, errorCode, e);
		}
		return resultBean;
	}

	public ResultBean sentMailQuotation(QuotationModel quotationModel) {
		ResultBean resultBean = new ResultBean();
		try {

		} catch (Exception e) {
			resultBean.setIserror(true);
			String errorCode = ErrorCodeConstant.DATA_NOT_SENT_QUOTE;
			String errorMessage = ResourceBundle.getBundle("ApplicationResources",
					LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(0, errorCode, e);
		}
		return resultBean;
	}

	private static void addMetaData(Document document) {
		document.addTitle("Quotation PDF");
		document.addSubject("Quotation PDF");
	}

	private static void addTitleClientPage(Document document) throws DocumentException {

		Paragraph preface = new Paragraph();
		creteEmptyLine(preface, 1);
		preface.add(new Paragraph("Agent and Client Details", TIME_ROMAN_SMALL));

		creteEmptyLine(preface, 1);
		document.add(preface);
	}

	private static void addTitleFlightPage(Document document) throws DocumentException {

		Paragraph preface = new Paragraph();
		creteEmptyLine(preface, 1);
		preface.add(new Paragraph("Flight Details", TIME_ROMAN_SMALL));

		creteEmptyLine(preface, 1);
		document.add(preface);
	}

	private static void addTitleHotelPage(Document document) throws DocumentException {

		Paragraph preface = new Paragraph();
		creteEmptyLine(preface, 1);
		preface.add(new Paragraph("Hotel Details", TIME_ROMAN_SMALL));

		creteEmptyLine(preface, 1);
		document.add(preface);
	}

	private static void addTitlePage(Document document) throws DocumentException {

		Paragraph preface = new Paragraph();
		creteEmptyLine(preface, 1);
		preface.add(new Paragraph("Quotation Details", TIME_ROMAN));
		creteEmptyLine(preface, 1);
		document.add(preface);
	}

	private static void creteEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private static void createTableClient(Document document, QuotationModel tTQuotation)
			throws DocumentException {

		PdfPTable table = new PdfPTable(2);

		PdfPCell c1 = new PdfPCell(new Phrase("Agent Name : HardCoded Agent Name",
				TIME_ROMAN_VSMALL));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Client Name : HardCoded Client Name", TIME_ROMAN_VSMALL));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Agency Details : Agency one", TIME_ROMAN_VSMALL));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Client Details : Client one", TIME_ROMAN_VSMALL));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		document.add(table);
	}

	private ByteArrayOutputStream convertPDFToByteArrayOutputStream(String fileName) {

		InputStream inputStream = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {

			inputStream = new FileInputStream(fileName);
			byte[] buffer = new byte[1024];
			baos = new ByteArrayOutputStream();

			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}

		} catch (FileNotFoundException e) {
			TTLog.printStackTrace(0, e);
		} catch (IOException e) {
			TTLog.printStackTrace(0, e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					TTLog.printStackTrace(0, e);
				}
			}
		}
		return baos;
	}
	
	public ResultBean processQuotationModel(QuotationCommonBean quotationCommonBean) {
		ResultBean resultBean = new ResultBean();
		try {
			resultBean.setIserror(false);
		} catch (Exception ex1) {
			resultBean.setIserror(true);
			String errorCode = ErrorCodeConstant.DATA_NOT_FOUND_QUOTE;
			String errorMessage = ResourceBundle.getBundle("ApplicationResources",
					LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(0, errorCode, ex1);
		}
		return resultBean;
	}
}
