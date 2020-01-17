package net.cartola.emissorfiscal.emissorfiscal.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *	9 de jan de 2020
 *	@author robson.costa
 */

public class NcmPage {
		
	@FindBy(id = "txtNumero")
	private WebElement txtNumero;

	@FindBy(id = "txtExcecao")
	private WebElement txtExcecao;

	@FindBy(id = "txtDescricao")
	private WebElement txtDescricao;
//
	@FindBy(id = "spanMensagemErro")
	private WebElement spanMensagemErro;
	
//	@FindBy(xpath = "/html/body/main/div/div")
//	private WebElement spanMensagemErro;

	@FindBy(id = "spanMensagemSucesso")
	private WebElement spanMensagemSucesso;

	@FindBy(id = "btnCadastrarAlterar")
	private WebElement btnCadastrarAlterar;
	
	@FindBy(xpath = "//*[@id=\"consulta-ncm-container\"]/div/div/div/div/div/div/div/table/tbody/tr/td[4]")
	private WebElement btnEditarPrimeiroRegistro;
	
	private WebDriver driver;

	public NcmPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	private void preencheOCadastroDeUmNCMSemADescricao() {
		PageUtil.goToHome(driver);
		PageUtil.goToTelaDeCadastro(driver, "Ncm");

		WebDriverWait wait = new WebDriverWait(driver, 3);
	   	ExpectedCondition<Boolean> txtNumeroIsDisplayed = displayed -> txtNumero.isDisplayed();
	   	wait.until(txtNumeroIsDisplayed);
	   	
	   	txtNumero.clear();
	   	PageUtil.preencheTxt(txtNumero, "12345678", 1000L);
		assertEquals("12345678", txtNumero.getAttribute("value"));
		
	   	ExpectedCondition<Boolean> txtExcecaoIsDisplayed = displayed -> txtExcecao.isDisplayed();
	   	wait.until(txtExcecaoIsDisplayed);
		txtExcecao.clear();
	   	PageUtil.preencheTxt(txtExcecao, "11", 100L);
	   	assertEquals("11", txtExcecao.getAttribute("value"));
	}
	
	
	public void tentaCadastrarUmNcmIncompleto() {
		preencheOCadastroDeUmNCMSemADescricao();
		
		WebDriverWait wait = new WebDriverWait(driver, 3);
		ExpectedCondition<Boolean> btnCadastrarAlterarIsDisplayed = displayed -> btnCadastrarAlterar.isDisplayed();
		wait.until(btnCadastrarAlterarIsDisplayed);
		btnCadastrarAlterar.click();
		
		Awaitility.await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(this.driver.getTitle()).isEqualTo("Cadastro de NCM");
			assertThat(this.driver.findElement(By.xpath("//*[@id=\"spanMensagemErro\"]/div[1]"))
					.getText()).contains("É obrigatória uma DESCRIÇÃO");
		});
		System.out.println("\n"+ this.getClass().getName() + " test01_TentaCadastrarUmNCMVazio, Ok");
	}
	
	public void tentaCadastrarUmNcmCorretamente() {
		preencheOCadastroDeUmNCMSemADescricao();
		PageUtil.preencheTxt(txtDescricao, "Produtos para as empresas de Auto Peças", 500L);
		assertEquals( "Produtos para as empresas de Auto Peças", txtDescricao.getAttribute("value"));
		btnCadastrarAlterar.click();
		
		Awaitility.await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(this.driver.getTitle()).isEqualTo("Cadastro de NCM");
			assertThat(this.driver.findElement(By.id("spanMensagemSucesso")).getText().contains("alterado/cadastrado"));
		});
		
		System.out.println("\n"+ this.getClass().getName() + " test02_TentaCadastrarUmNCMCorretamente, Ok");
	}
	
	public void tentaEditarUmNcm() {
		PageUtil.goToHome(this.driver);
		PageUtil.goToTelaDeConsulta(this.driver, "Ncm");
//		preencheOCadastroDeUmNCM(this.driver);
//		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
//		WebElement element = driver.findElement(By.id("spanMensagemSucesso"));
		
		System.out.println("\n"+ this.getClass().getName() + " test02_DeveFalharAoTentarCadastrarONCMAnterior, Ok");
	}
	

	// CONSULTAS 
//	@Test
//	public void test02_TentaConsultarUmNCMVazio() {
//		driver.get(PATH + "ncm/consulta");
//		
////		WebElement element = driver.findElement(By.xpath("/html/body/nav/div/div[2]/a"));
////		element.click();
////		
////		element = driver.findElement(By.xpath("/html/body/nav/div/div[2]/div/a[1]"));
////		element.click();
//
//		WebElement element = driver.findElement(By.id("btnPesquisar"));
//		element.click();
//		
//		element = driver.findElement(By.id("spanMensagemErro"));
//		
//		System.out.println("cheguei aqui manolo");
//	}

}


