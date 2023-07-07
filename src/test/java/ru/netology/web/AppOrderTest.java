package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppOrderTest {
    private WebDriver driver; //объявляем переменную экземпляра класса, неприсваивая значения

    @BeforeAll
    static void setUpAll() { // метод должен быть статичным
        WebDriverManager.chromedriver().setup(); // производим настройку веб драйвера - указываем, что используем хром драйвер и вызываем для него метод сетап
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions(); //создаем опции хрома, чтобы выполнить тонкую настройку хром драйвера
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless"); //хедлес режим для селениума включается, через опцию (хедлес режим, чтобы не открывалось окно браузера при тестах)
        driver = new ChromeDriver(options); //присваиваем ранее созданной переменной driver присваиваем значение - новый экземпляр хром драйвера с опциями
        driver.get("http://localhost:9999"); //открываем страницу перед каждым тестом
    }

    @AfterEach
    void tearDown() { //после каждого теста закрываем страницу (quit) и обнуляем хром драйвер
        driver.quit();
        driver = null;
    }

    @Test
    void shouldBeSuccessfulForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Красовский Алексей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79139999999");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        var actualText = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();// trim для обрезки пробелов вконце и перед
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", actualText);
    }

    @Test
    void shouldNotEndApplicationIfCheckboxEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Красовский Алексей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79139999999");
        driver.findElement(By.cssSelector("button.button")).click();
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid")).isDisplayed());
    }
}

