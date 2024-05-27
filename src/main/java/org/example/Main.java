package org.example;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final String TRENDYOL_DISCOUNTED_PRICE_XPATH = "//*[@id=\"search-app\"]/div/div[1]/div[2]/div[4]/div[1]/div/div[1]/div[1]/a/div[2]/div[4]/div/div[2]/div[2]";
    private static final String HEPSIBURADA_PRICE_XPATH = "//*[@id=\"offering-price\"]/span[1]";
    private static final String APPLE_PRICE_XPATH = "//*[@id=\":re:_label\"]/span/span[2]/span/span";

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver-mac-arm64/chromedriver");

        // URL of the Trendyol website
        String trendyolUrl = "https://www.trendyol.com";

        // Create WebDriver instance
        WebDriver driver = new ChromeDriver();

        List<Double> prices = new ArrayList<>();

        try {

            //Mazimize current window
            driver.manage().window().maximize();

            driver.get(trendyolUrl);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            Thread.sleep(3000);

            closePopup(driver);

            WebElement searchInput = driver.findElement(By.cssSelector("input[data-testid='suggestion']"));

            Thread.sleep(2000);
            searchInput.clear();
            searchInput.sendKeys("iphone 15 pro max beyaz");

            searchInput.sendKeys(Keys.ENTER);

            System.out.println("Search initiated for 'iphone 15 pro max beyaz'.");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".overlay")));

            Actions actions = new Actions(driver);
            actions.moveByOffset(100, 100).click().perform();

            Thread.sleep(2000);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TRENDYOL_DISCOUNTED_PRICE_XPATH)));

            WebElement discountedPrice = driver.findElement(By.xpath(TRENDYOL_DISCOUNTED_PRICE_XPATH));
            String discountedPriceText = discountedPrice.getText();
            double trendyolPrice = extractPrice(discountedPriceText);
            prices.add(trendyolPrice);
            System.out.println("Trendyol Price: " + trendyolPrice);

            Thread.sleep(3000);
            // Now, navigate to Hepsiburada
            driver.get("https://www.hepsiburada.com/iphone-15-pro-max-512-gb-pm-HBC00004Z7930");

            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(HEPSIBURADA_PRICE_XPATH)));

            WebElement hepsiburadaPriceElement = driver.findElement(By.xpath(HEPSIBURADA_PRICE_XPATH));
            String hepsiburadaPriceText = hepsiburadaPriceElement.getText();
            double hepsiburadaPrice = extractPrice(hepsiburadaPriceText);
            prices.add(hepsiburadaPrice);
            System.out.println("Hepsiburada Price: " + hepsiburadaPrice);

            // Now, navigate to Apple's website
            driver.get("https://www.apple.com/tr/shop/buy-iphone/iphone-15-pro/6.7-in%C3%A7-ekran-512gb-beyaz-titanyum");

            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(APPLE_PRICE_XPATH)));

            WebElement applePriceElement = driver.findElement(By.xpath(APPLE_PRICE_XPATH));
            String applePriceText = applePriceElement.getText();
            double applePrice = extractPrice(applePriceText);
            prices.add(applePrice);
            System.out.println("Apple Price: " + applePrice);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();

            // Write prices to Excel
            writeToExcel(prices);
        }
    }

    private static void writeToExcel(List<Double> prices) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Prices");

            // Write headers
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Website");
            headerRow.createCell(1).setCellValue("Product");
            headerRow.createCell(2).setCellValue("Price");
            headerRow.createCell(3).setCellValue("Cheapest");
            headerRow.createCell(4).setCellValue("Average");
            headerRow.createCell(5).setCellValue("Most Expensive");

            // Write prices
            int rowNumber = 1;
            double cheapest = Double.MAX_VALUE;
            double sum = 0;
            double mostExpensive = Double.MIN_VALUE;
            for (int i = 0; i < prices.size(); i++) {
                Double price = prices.get(i);
                Row row = sheet.createRow(rowNumber++);
                row.createCell(0).setCellValue(getWebsiteName(i));
                row.createCell(1).setCellValue("iPhone 15 Pro Max 512 GB Beyaz Titanyum");
                row.createCell(2).setCellValue(price);

                // Update cheapest price
                if (price < cheapest) {
                    cheapest = price;
                }

                // Update sum for average calculation
                sum += price;

                // Update most expensive price
                if (price > mostExpensive) {
                    mostExpensive = price;
                }
            }

            // Calculate average
            double average = sum / prices.size();

            // Write cheapest, average, and most expensive
            Row summaryRow = sheet.createRow(rowNumber);
            summaryRow.createCell(3).setCellValue(cheapest);
            summaryRow.createCell(4).setCellValue(average);
            summaryRow.createCell(5).setCellValue(mostExpensive);

            // Write to file
            try (FileOutputStream fos = new FileOutputStream("prices.xlsx")) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getWebsiteName(int websiteIndex) {
        switch (websiteIndex) {
            case 0:
                return "Trendyol";
            case 1:
                return "Hepsiburada";
            case 2:
                return "Apple";
            default:
                return "";
        }
    }

    private static double extractPrice(String priceText) {
        // Extract price as a double from the text
        return Double.parseDouble(priceText.replaceAll("[^0-9,]", "").replace(",", "."));
    }

    private static void closePopup(WebDriver driver) {
        try {
            WebElement popupCloseButton = driver.findElement(By.className("modal-close"));
            if (popupCloseButton.isDisplayed()) {
                popupCloseButton.click();
                System.out.println("Closed the initial popup.");
            }
        } catch (Exception e) {
            System.out.println("No initial popup to close.");
        }
    }
}
