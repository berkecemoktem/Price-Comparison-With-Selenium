<img width="110" alt="220px-Selenium_Logo" src="https://github.com/berkecemoktem/Price-Comparison-With-Selenium/assets/75270752/5a7c10c2-23c3-41a2-8400-bf50dc54234c"> 

# Price Scraper   

## Overview
This project uses Selenium WebDriver to scrape prices of the "iPhone 15 Pro Max 512 GB Beyaz Titanyum" from three different websites: Trendyol, Hepsiburada, and Apple. The scraped prices are then written to an Excel file for easy comparison.

## Prerequisites
To run this project, you need to have the following technologies installed:

**Java**: Version 8 or higher.

**Maven**: For managing project dependencies.

**ChromeDriver**: To control the Chrome browser.

**Selenium WebDriver**: For browser automation.

**Apache POI**: For writing data to an Excel file.

## What is Selenium?
Selenium is a powerful tool primarily used for end-to-end (E2E) UI testing of web applications. It allows you to interact with web elements and automate browser actions, making it ideal for testing user interfaces. Additionally, Selenium is often used for web scraping to extract data from websites.

## How to Run?
**Clone the repo**:
```git clone https://github.com/yourusername/price-scraper.git```

**Install dependencies**:
```mvn clean install```

**Run the app in your IDE**

**Check the output**:
The prices will be printed in the console.
An Excel file named prices.xlsx will be generated in the project root directory, containing the scraped prices and a summary with the cheapest, average, and most expensive prices.

## Code Structure
***Main.java***: Contains the main logic for navigating to the websites, scraping prices, and writing them to an Excel file.

***XPath Constants***: XPath addresses are defined at the top of the file for better readability and maintainability.

***Excel Writing***: The prices are written to an Excel file using Apache POI, with columns for Website, Product, Price, Cheapest, Average, and Most Expensive.

## Important Notes
1. Ensure you have the correct path to the chromedriver executable set in your system. (Your driver MUST be compatible with your browser version also!)
2. Ensure that you have the necessary libraries installed for Selenium (In this project, I've added them as JARs)
3. You may need to update the XPath addresses if the website structure changes. (Websites change everyday, you need to find better and more unique XPATHs)
