package com.plin.documents.service;

import com.plin.documents.entity.Document;
import com.plin.documents.exception.DocumentException;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Base64;

@Service
public class DocumentExtractorService {

    @Value("${selenium.grid}")
    private String seleniumGridUrl;

    public Document savePageAsPdf(String url) {
        URL seleniumUrl = buildSeleniumGridUrl();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        RemoteWebDriver driver = new RemoteWebDriver(seleniumUrl, options);
        driver.get(url);
        String title = driver.getTitle() + ".pdf";
        byte[] content = printToPdf(driver);
        driver.quit();
        return new Document(null, title, content, null, null);
    }

    private URL buildSeleniumGridUrl(){
        try {
            return URI.create(seleniumGridUrl).toURL();
        } catch (MalformedURLException e) {
            throw new DocumentException("URL mal formada para Selenium Grid", e);
        }
    }

    private byte[] printToPdf(RemoteWebDriver driver){
        Pdf pdf = driver.print(new PrintOptions());
        return Base64.getDecoder().decode(pdf.getContent());
    }
}
