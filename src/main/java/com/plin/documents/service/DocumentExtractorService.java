package com.plin.documents.service;

import com.plin.documents.entity.Document;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v142.page.Page;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class DocumentExtractorService {
    public Document savePageAsPdf(String url){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        ChromeDriver driver = new ChromeDriver(options);
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        driver.get(url);
        String title = driver.getTitle() + ".pdf";
        byte[] content = printToPdf(devTools);
        driver.quit();
        return new Document(null, title, content, null, null);
    }

    private byte[] printToPdf(DevTools devTools){
        Page.PrintToPDFResponse pdfResponse = devTools.send(Page.printToPDF(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        ));
        return Base64.getDecoder().decode(pdfResponse.getData());
    }
}
