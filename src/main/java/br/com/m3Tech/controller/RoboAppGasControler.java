package br.com.m3Tech.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

import javax.print.PrintService;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import br.com.m3Tech.utils.LabelUtils;
import br.com.m3Tech.utils.TextFieldUtils;

public class RoboAppGasControler {

	String info = "";

	private JTextField url;
	private JTextField urlOrders;
	private JTextField email;
	private JTextField senha;
//	private JComboBox<String> boxImpressoras = new JComboBox<String>();

	private JTextArea mensagens;

	protected String actionSelecionada = "Click";

	private PrintService[] ps;

	public void executar() {

		url = TextFieldUtils.getTextField("app.deliveryvip.com.br", 400, 10, 30);
		urlOrders = TextFieldUtils.getTextField("company_acf70b5b-19c2-4624-8ebf-6d890acf1b11", 400, 10, 70);
		email = TextFieldUtils.getTextField("marymara96@hotmail.com", 10, 110);
		senha = TextFieldUtils.getTextField("piraju3258", 10, 150);
		

		mensagens = new JTextArea();
		mensagens.setSize(500, 300);

		JScrollPane scroll = new JScrollPane(mensagens, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(10, 300, 500, 300);
		scroll.setMinimumSize(new Dimension(160, 200));
		scroll.setPreferredSize(new Dimension(160, 200));

		JButton botaoLigarDesligar = new JButton("Ligar/Desligar");
		botaoLigarDesligar.setBounds(220, 110, 200, 20);
		botaoLigarDesligar.addActionListener(ligarDesligar());

		JButton clear = new JButton("Clear");
		clear.setBounds(520, 300, 200, 20);
		clear.addActionListener(clear());

//		try {
//
//			boxImpressoras.removeAllItems();
//			DocFlavor df = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
//			ps = PrintServiceLookup.lookupPrintServices(df, null);
//			for (PrintService p : ps) {
//				boxImpressoras.addItem(p.getName());
//			}
//		} catch (Exception ex) {
//
//			System.err.println(ex.getMessage() + "  Local:  " + ex.getLocalizedMessage());
//
//		}
//
//		boxImpressoras.setBounds(10, 200, 200, 20);

//		String[] actions = { "Preencher Campo", "Click"};
//		JComboBox<String> comboBox = new JComboBox<String>(actions);
//		comboBox.addActionListener(selecionarAction());
//		comboBox.setBounds(10,200,200,20);

		JFrame f = new JFrame("Robô Aceitar Pedidos");

		f.setLayout(null);

		f.setSize(800, 800);

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

		f.add(LabelUtils.getLabel("Url do site", 10, 10));
		f.add(url);
		f.add(LabelUtils.getLabel("Url dos Pedidos", 10, 50));
		f.add(urlOrders);
		f.add(LabelUtils.getLabel("Email", 10, 90));
		f.add(email);
		f.add(LabelUtils.getLabel("Senha", 10, 130));
		f.add(senha);
//		f.add(LabelUtils.getLabel("Impressora", 10, 170));
//		f.add(boxImpressoras);

		f.add(botaoLigarDesligar);
		f.add(clear);
		f.add(scroll);
	}

//	private ActionListener selecionarAction() {
//		{
//			return new ActionListener() {
//
//				public void actionPerformed(ActionEvent e) {
//
//					JComboBox cb = (JComboBox) e.getSource();
//					actionSelecionada = (String) cb.getSelectedItem();
//
//				}
//			};
//
//		}
//	}

	private ActionListener ligarDesligar() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Controlador.ligado = !Controlador.ligado;

				if (Controlador.ligado) {
					info += "Robô do AppGas ligado!\n";
					try {
						iniciarRobo();
					} catch (InterruptedException e1) {
						info += e1.getMessage() + "\n";
						Controlador.ligado = false;
					}
				} else {
					info += "Robô do AppGas desligado!\n";
				}

				atualizarMensagens();
			}
		};

	}

	private void iniciarRobo() throws InterruptedException {

		final String pocOrders = "https://" + url.getText() + "/panel/"+ urlOrders.getText() + "/orders";
		
		

		Thread thread = new Thread() {
			public void run() {
				System.out.println("Thread Running");
				System.out.println("Versão 2023-05-25");
				String absolutePath = new File(".").getAbsolutePath();
				File file = new File(absolutePath.substring(0, absolutePath.length() - 1) + "chromedriver.exe");
				System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
				WebDriver driver = new ChromeDriver();
				Actions action = new Actions(driver);

				driver.get("https://" + url.getText() + "/");
				try {
					sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				driver.findElement(By.xpath("//input[@id='user_email']")).sendKeys(email.getText());
				driver.findElement(By.xpath("//input[@id='user_password']")).sendKeys(senha.getText());
				
				driver.findElement(By.xpath("//input[@id='user_password']")).submit();
				
				try {
					sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				driver.get(pocOrders);

				while (Controlador.ligado) {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					try {
			            
						
						WebElement orderList = driver.findElement(By.id("order_list"));
						
						List<WebElement> AllTurboFrame = orderList.findElements(By.xpath("//turbo-frame[starts-with(@id, 'card_order_')]"));
			            int RowCount = AllTurboFrame.size();
			            for (int i = 0; i < RowCount; i++)
			            {
			            // Check the check boxes based on index
			               WebElement itemDaLista = AllTurboFrame.get(i);
			               

			               WebElement spanElement = itemDaLista.findElement(By.xpath(".//span[contains(@class, 'group-[.preparing]')]"));
			               
//			               if(spanElement != null ) {
//			            	   System.out.println(spanElement.getText());
//			               }

			               if(spanElement != null && "Novo".equalsIgnoreCase(spanElement.getText())) {
			            	   
			            	   itemDaLista.click();
			            	   
			            	   WebElement turboFrameSelected = driver.findElement(By.xpath("//turbo-frame[starts-with(@id, 'selected_order_')]"));
			            	   WebElement buttonElement = turboFrameSelected.findElement(By.xpath(".//button[@value='accept']"));
			            	   
			            	   if(buttonElement != null) {
			            		   buttonElement.click();
			            	   }
			            	   
			            	   

			            	  
			            	   
			               }
			               
			               

			            }
						
//						orderList.click();
						
//						System.out.println();

//						sleep(1000);
//						String text = driver.findElement(By.id("new-orders")).getText();
//
//						String[] split = text.split("\n");

//						for (String s : split) {
//							if (s.contains("Nº")) {
//
//								System.out.println("newOrders: " + text);
//
//								String[] split2 = s.split(" ");
//
//								String idpedido = "link-go-to-new-order-" + split2[1];
//
//								String pedido = split2[1];
////								String produto = "";
////								String userAddress = "";
////								String freight = "";
////								String totalDiscount = "";
////								String changeValue = "";
////								String total = "";
////								String paymentLabel = "";
////								String cliente = "";
//
////								try {
////									cliente = split[1];
////								} catch (Exception e) {
////									System.err.println("Erro: " + e);
////								}
//
//								System.out.println("Pedido: " + pedido);
//
//								String order = "https://" + url.getText() + "/order/" + pedido;
//								
//								System.out.println("Link Pedido: " + order);
//								
//								driver.findElement(By.id(idpedido)).click();

//								String app = driver.findElement(By.id("app")).getText();
//
//								print("App", app);
								
//								boolean aceitou = false;
								
//								String checkboxAtivado = "";
//								
//								try {
//									checkboxAtivado = driver.findElement(By.id("print-order-checkbox")).getText();
//								}catch(Exception e) {
//									
//								}
//								
//								
//								System.out.println("checkBox - " + checkboxAtivado);
//								
//								int i = 0;
//								
//								while (!aceitou && i < 10) {
//									try {
//										driver.findElement(By.id("acceptOrderBtn")).click();
//										print("Clicou no botão aceitar", "");
//										aceitou = true;
//									} catch (Exception e) {
//										sleep(1000);
//										print("Falhou ao clicar no botão aceitar", "");
//										aceitou = false;
//										i++;
//									}
//								}
//								
//								System.out.println("Pedido: " + split2[1] + " aceito.");
//
//								
//								boolean imprimiu = false;
//								
//								driver.get(order);
//
//								i = 0;
//								while (!imprimiu && i < 10) {
//									try {
//										driver.findElement(By.id("print-button")).click();
//										print("Clicou no botão imprimir", "");
//										imprimiu = true;
//									} catch (Exception e) {
//										sleep(1000);
//										print("Falhou ao clicar no botão imprimir", "");
//										imprimiu = false;
//										i++;
//									}
//								}
//								
//								
//								if(imprimiu) {
//									System.out.println("Entrou na tela imprimir");
//									Set<String> windowHandles = driver.getWindowHandles();
//									if (!windowHandles.isEmpty()) {
//									    driver.switchTo().window((String) windowHandles.toArray()[windowHandles.size() - 1]);
//									}
//									System.out.println("Enviando botão enter");
//									action.sendKeys(Keys.ENTER);
//									System.out.println("Enviou botão enter");
//								}
//								//Now work with the dialog as with an ordinary page:  
//								driver.findElement(By.className("action-button")).click();
//									
//								try {
//									produto = driver.findElement(By.id("product-info-container")).getText();
//									print("Produto", produto);
//								} catch (Exception e) {
//									System.err.println("Erro: " + e);
//								}
//
//								try {
//									userAddress = driver.findElement(By.id("user-address")).getText();
//									print("userAddress", userAddress);
//								} catch (Exception e) {
//									System.err.println("Erro: " + e);
//								}
//								try {
//									totalDiscount = driver.findElement(By.id("total-discount")).getText();
//									print("totalDiscount", totalDiscount);
//								} catch (Exception e) {
//									System.err.println("Erro: " + e);
//								}
//								try {
//									changeValue = driver.findElement(By.id("change-value")).getText();
//									print("changeValue", changeValue);
//								} catch (Exception e) {
//									System.err.println("Erro: " + e);
//								}
//								try {
//									freight = driver.findElement(By.id("freight")).getText();
//									print("freight", freight);
//								} catch (Exception e) {
//									System.err.println("Erro: " + e);
//								}
//								try {
//									total = driver.findElement(By.id("total")).getText();
//									print("total", total);
//								} catch (Exception e) {
//									System.err.println("Erro: " + e);
//								}
//								try {
//									paymentLabel = driver.findElement(By.id("payment-label")).getText();
//									print("paymentLabel", paymentLabel);
//								} catch (Exception e) {
//									System.err.println("Erro: " + e);
//								}

//								String finalDelinha = "\r\n";
//
//								String textImpressao = "     ZE DELIVERY "+ finalDelinha ;
//										textImpressao += "Pedido: " + pedido + finalDelinha;
//										textImpressao += LocalDateTime.now() + finalDelinha ;
//										textImpressao += "Pagamento: " + paymentLabel + finalDelinha;
//										textImpressao += "------------------------------------------" + finalDelinha ;
//										textImpressao += userAddress + finalDelinha;
//										textImpressao += "Cliente: " + cliente + finalDelinha;
//										textImpressao += "------------------------------------------"  + finalDelinha;
//										textImpressao += "Produtos:" + finalDelinha;
//										textImpressao += produto + finalDelinha ;
//										textImpressao += "Frete: " + freight + finalDelinha ;
//										textImpressao += "Desconto: ";
//										textImpressao += totalDiscount != null ? totalDiscount + finalDelinha : "R$0.00" + finalDelinha; 
//										textImpressao += "Total a pagar: " + total + finalDelinha;
//										if(!"".equals(changeValue)) {
//											textImpressao += "Troco para: " + changeValue + finalDelinha ;
//										}
//										textImpressao += finalDelinha;
//										textImpressao += finalDelinha;
//										textImpressao += finalDelinha;
//								ImprimirUtils.imprimir(textImpressao, ps[boxImpressoras.getSelectedIndex()]);
//
//								print("TextImpressão", textImpressao);

//								sleep(1000);
//								driver.get(pocOrders);
//								sleep(1000);
//								driver.findElement(By.id("close-alert-modal")).click();
//
//								
//								info += "Pedido: " + split2[1] + " aceito.\n";
//							}
//						}

					} catch (Exception ex) {
						System.out.println(ex.getMessage());
//						int i = 0;
//						System.err.println(ex.getMessage());
//						driver.get(pocOrders);
//						try {
//							sleep(1000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						boolean clicouNoModal = false;
//						while(!clicouNoModal && i < 10) {
//							try {
//								driver.findElement(By.id("close-alert-modal")).click();
//								clicouNoModal = true;
//							}catch(Exception e) {
//								print("Falhou ao clicar no botão Modal", "");
//								clicouNoModal = false;
//								i++;
//							}
//						}
//						;
					}
				}

				driver.quit();
			}
		};

		thread.start();

	}

	private void print(String layout, String text) {
		if (text != null) {
			System.out.println(layout + " \r\n" + text);
		}
	}

	private String semAcento(String text) {
		String nfdNormalizedString = Normalizer.normalize(text, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	private ActionListener clear() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				limparInfo();
				atualizarMensagens();
			}
		};

	}

	private void atualizarMensagens() {
		mensagens.setText(info);
	}

	private void limparInfo() {
		info = "";
	}

}
