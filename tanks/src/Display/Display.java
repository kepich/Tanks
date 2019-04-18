package Display;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import javax.swing.JFrame;

import IO.input;

public abstract class Display {
	private static boolean 			isCreated = false;
	private static JFrame 			window;				// Окно
	private static Canvas 			content;			// Канвас
	private static BufferedImage 	buffer;				// Буфферизация
	private static int[] 			bufferData;
	private static Graphics 		bufferGraphics;
	private static int 				clearColor;
	private static BufferStrategy 	bufferStrategy;		// Сглаживание
	
	public static void create(int width, int height, String title, int _clearColor, int numBuffers) {	// Создание окна
		if(isCreated) return;
		
		window = new JFrame(title);
		content = new Canvas();
		Dimension size = new Dimension(width, height);			// Размер канваса
		content.setPreferredSize(size);							// Устанока размеров
		window.setResizable(false);
		window.getContentPane().add(content);					// Запихивание конваса в окно
		window.pack();											// Авторазмер окна по размеру канваса
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// Выключение по крестику
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		bufferData = ((DataBufferInt)buffer.getRaster().getDataBuffer()).getData();
		bufferGraphics = buffer.getGraphics();
		clearColor = _clearColor;
		
		content.createBufferStrategy(numBuffers);
		bufferStrategy = content.getBufferStrategy();
		( (Graphics2D) bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		isCreated = true;
	}
	
	public static void clear() {								// Очистка экрана
		Arrays.fill(bufferData, clearColor);
	}
	
	public static void swapBuffers() {							// Смена буфферов
		Graphics g = bufferStrategy.getDrawGraphics();
		g.drawImage(buffer, 0, 0, null);
		bufferStrategy.show();
	}
	
	public static Graphics2D getGraphics() {
		return (Graphics2D) bufferGraphics;
	}
	
	public static void destroy() {
		if(!isCreated)
			return;
		window.dispose();
		isCreated = false;
	}
	
	public static void setTitle(String title) {					// Установка заголовка
		window.setTitle(title);
	}
	
	public static void addInputListener(input InputListener) {
		window.add(InputListener);
	}
}
