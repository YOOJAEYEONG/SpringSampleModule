package com.pdf.pdfbox;

import com.openhtmltopdf.css.parser.property.PageSize;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Controller
public class PdfController {

	@RequestMapping("/")
	public void generatePdf(HttpServletRequest request, HttpServletResponse response) throws Exception {


		try (
			OutputStream os = response.getOutputStream();
			InputStream inputStream = getClass().getResourceAsStream("/static/html/template/pdf/pdfSample.html")
		) {
			String html = new String(Objects.requireNonNull(inputStream).readAllBytes(), StandardCharsets.UTF_8);

			PdfRendererBuilder builder = new PdfRendererBuilder();

			// ttf , otf 만 가능
			builder.useFont(() -> getClass().getResourceAsStream("/static/font/유앤피플 고딕 UNI.ttf"), "유앤피플 고딕");
			builder.useFont(() -> getClass().getResourceAsStream("/static/font/NanumGothic/NanumGothic-Regular.ttf"), "NanumGothic");
			builder.useFont(() -> getClass().getResourceAsStream("/static/font/Noto_Sans_KR/static/NotoSansKR-Regular.ttf"), "NotoSansKR");
			builder.useFont(() -> getClass().getResourceAsStream("/static/font/Noto_Sans_KR/static/NotoSansKR-Bold.ttf"), "NotoSansKR-Bold");

			float a4Width = PageSize.A4.getPageWidth().getFloatValue((short) 0);
			float a4Height = PageSize.A4.getPageHeight().getFloatValue((short) 0);

			builder.useDefaultPageSize(a4Width,a4Height, BaseRendererBuilder.PageSizeUnits.MM);
			builder.withHtmlContent(html, "");
			builder.toStream(os);
			builder.run();
		} catch (RuntimeException e) {
			log.error(e.getMessage(),e);
		}

	}

	@GetMapping("/pdf/pdfSample")
	String sampleHtmlView() {
		return "/html/template/pdf/pdfSample.html";
	}

}