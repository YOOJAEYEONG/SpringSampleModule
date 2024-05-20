package com.pdf.spire;

import com.spire.pdf.htmlconverter.LoadHtmlType;
import com.spire.pdf.htmlconverter.qt.HtmlConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;


@Controller
@RequiredArgsConstructor
public class SpireController {

	private static final Logger log = LoggerFactory.getLogger(SpireController.class);

	private final ResourceLoader resourceLoader;


	/**
	 * 공급자적합성확인신고서 양식 PDF 변환
	 */
	@RequestMapping("/spire/pdf/html_test")
	public void spirePDF(HttpServletResponse response) throws IOException {
		String fileName = "html_test.pdf";

		String resourcePath = Paths.get("C:", "Users","liuje","IdeaProjects","SpringSampleApp","spire","src","main","resources","spire","testResources") + File.separator;
		log.info("resourcePath:{}", resourcePath);

		//html 파일 가져오기
		String htmlString = fileAsString(resourcePath + "html_test2.html");

		//add css
		String cssString = fileAsString(resourcePath + "html_test.css");
		htmlString = htmlString.replace("<link rel=\"stylesheet\" type=\"text/css\" href=\"html_test.css\"/>", "<style>" + cssString + "</style>");

		//문서내 이미지 리소스
		Resource resource = resourceLoader.getResource("classpath:spire/testResources/stamp.png");
		String base64Img = encodeImageToBase64(resource.getFile());
		htmlString = htmlString.replaceAll("<img src=\"stamp.png\" width=\"80\" height=\"80\"/>","<img src=\""+ base64Img + "\" width=\"80\" height=\"80\"/>");

		//Set the plugin path
		String pluginPath = "C:\\Users\\liuje\\IdeaProjects\\SpringSampleApp\\spire\\src\\main\\resources\\spire\\plugins\\plugins-windows-x64";
		HtmlConverter.setPluginPath(pluginPath);

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		HtmlConverter.convert(htmlString, response.getOutputStream(), LoadHtmlType.Source_Code);

		//library error
//		HtmlConverter.convert(htmlString, fileName, false, 1000000, new Size(2480.0F, 3508.0F) , new PdfMargins(5.0F, 5.0F), LoadHtmlType.Source_Code);
//		HtmlConverter.convert(htmlString, fileName, false, 1000000, new Size(612.0F, 792.0F) , new PdfMargins(5.0F, 5.0F), LoadHtmlType.Source_Code);

	}

	/**
	 * 이 소스에서는 웹 폰트를 적용하는 방법이 추가 되었습니다
	 * simple.html 참고
	 */
	@RequestMapping("/spire/pdf/simple")
	public void spirePDF2(HttpServletResponse response, HttpServletRequest request) throws IOException {
		String htmlString = "";
		String fileName = "simple.pdf";

		String resourcePath = Paths.get("C:", "Users","liuje","IdeaProjects","SpringSampleApp","spire","src","main","resources","spire","testResources") + File.separator;
		log.info("resourcePath:{}", resourcePath);

		//html 파일 가져오기
		htmlString = fileAsString(resourcePath + "simple.html");

		//add css
		String cssString = fileAsString(resourcePath + "simple.css");
		htmlString = htmlString.replace("<link rel=\"stylesheet\" type=\"text/css\" href=\"simple.css\"/>", "<style>" + cssString + "</style>");

		//문서내 이미지 리소스
		Resource resource = resourceLoader.getResource("classpath:spire/testResources/stamp.png");
		String base64Img = encodeImageToBase64(resource.getFile());
		htmlString = htmlString.replaceAll("<img src=\"stamp.png\" width=\"80\" height=\"80\"/>","<img src=\""+ base64Img + "\" width=\"80\" height=\"80\"/>");

		//Set the plugin path
		String pluginPath = "C:\\Users\\liuje\\IdeaProjects\\SpringSampleApp\\spire\\src\\main\\resources\\spire\\plugins\\plugins-windows-x64";
		HtmlConverter.setPluginPath(pluginPath);

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		HtmlConverter.convert(htmlString, response.getOutputStream(), LoadHtmlType.Source_Code);
		//library error
//		HtmlConverter.convert(htmlString, fileName, false, 1000000, new Size(2480.0F, 3508.0F) , new PdfMargins(5.0F, 5.0F), LoadHtmlType.Source_Code);
//		HtmlConverter.convert(htmlString, fileName, false, 30000, new Size(612.0F, 792.0F) , new PdfMargins(90.0F, 72.0F), LoadHtmlType.Source_Code);
//		HtmlConverter.convert(htmlString, fileName, true, 300000, new Size(612.0F, 792.0F) , new PdfMargins(5.0F, 5.0F), LoadHtmlType.Source_Code);
	}


	private static String encodeImageToBase64(File file) throws IOException {
		// 파일을 바이트 배열로 읽기
		FileInputStream fileInputStream = new FileInputStream(file);
		byte[] imageBytes = new byte[(int) file.length()];
		fileInputStream.read(imageBytes);

		// 바이트 배열을 Base64 문자열로 인코딩
		String base64Image = Base64.getEncoder().encodeToString(imageBytes);

		// 파일 스트림 닫기
		fileInputStream.close();

		// 이미지를 data URI 형식으로 반환
		return "data:image/png;base64," + base64Image;
	}

	//Convert a HTML file to string
	private static String fileAsString(String filePath) throws IOException {
		File file = new File(filePath);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		StringBuilder stringBuilder = new StringBuilder();
		String temp = "";
		while ((temp = bufferedReader.readLine()) != null) {
			stringBuilder.append(temp).append("\n");
		}
		bufferedReader.close();
		return stringBuilder.toString();
	}
}