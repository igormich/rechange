import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import base.Achivements;
import base.DataHolder;
import base.Scene;
import day1.Scene1;

public class Rechange {

	public static void main(String[] args) throws Exception {
		
		File save = new File("game.sav");
		JFrame frame= new JFrame("Меню");
		frame.setLayout(new GridLayout(7, 1));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JButton newGame = new JButton("Новая игра");
		frame.add(newGame);
		newGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				Scene1 scene = new Scene1();
				try {
					scene.load();		
					scene.init();
					scene.display();
					scene.run();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame, "Что то пошло не так, пните автора", "Ошибка", JOptionPane.ERROR_MESSAGE);
				}
				
				frame.dispose();
			}
		});
		JButton oldGame = new JButton("Продолжить");
		oldGame.setEnabled(save.exists());
		frame.add(oldGame);
		oldGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(save)));
					Scene scene = (Scene) ois.readObject();
					DataHolder.data =  (Map<String, Object>) ois.readObject();
					scene.display();
					scene.run();
					ois.close();
					frame.dispose();
				}  catch (Exception e) {
					JOptionPane.showMessageDialog(frame, "Что то пошло не так, попробуйте начать новую игру", "Ошибка", JOptionPane.ERROR_MESSAGE);
					save.delete();
					oldGame.setEnabled(false);
				}
			}
		});
		JButton achivement = new JButton("Достижения");
		frame.add(achivement);
		achivement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ends = Achivements.loadAchivements().stream().collect(Collectors.joining("<br>"));
				JOptionPane.showMessageDialog(frame, "<html>Открытые достижения:<br>"+ends, 
						"Достижения", JOptionPane.INFORMATION_MESSAGE);
				
			}
		});
		JButton endings = new JButton("Концовки");
		frame.add(endings);
		endings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ends = Achivements.loadEndings().stream().collect(Collectors.joining("<br>"));
				JOptionPane.showMessageDialog(frame, "<html>Открытые концовки:<br>"+ends, 
						"Концовки", JOptionPane.INFORMATION_MESSAGE);
				
			}
		});
		JButton autors = new JButton("Авторы");
		frame.add(autors);
		autors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "<html>Михайлов Игорь - код, рисование<br>Анастасия  Бадьина - рисование, тестирование", 
						"Авторы", JOptionPane.INFORMATION_MESSAGE);
				
			}
		});
		JButton help = new JButton("Помощь");
		frame.add(help);
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "<html>Ходить - WASD<br>Использовать - E", 
						"Помощь", JOptionPane.INFORMATION_MESSAGE);
				
			}
		});
		JButton exit = new JButton("Выход");
		frame.add(exit);
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.dispose();
			}
		});
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
