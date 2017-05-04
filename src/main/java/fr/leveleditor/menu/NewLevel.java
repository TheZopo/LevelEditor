package fr.leveleditor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import fr.leveleditor.LevelEditor;
import fr.leveleditor.level.Level;
import fr.leveleditor.utils.Utils;

public class NewLevel extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField name = new JTextField(15);
	private JTextField sizeX = new JTextField(3);
	private JTextField sizeY = new JTextField(3);
	
	private JButton browse = new JButton("...");
	private JButton submit = new JButton("Créer le niveau !");
	
	private File selectedFile;
	
	public NewLevel() {
	    this.setTitle("Nouveau niveau");
	    this.setSize(300, 200);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	    
	    JPanel container = new JPanel();
	    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
	    
	    JPanel namePanel = new JPanel();
	    namePanel.add(new JLabel("Nom :"));
	    namePanel.add(name);
	    
	    JPanel size = new JPanel();
	    size.add(new JLabel("Taille X :"));
	    size.add(sizeX);
	    size.add(new JLabel("Y :"));
	    size.add(sizeY);
	    
	    JPanel location = new JPanel();
	    location.add(new JLabel("Emplacement :"));
	    browse.addActionListener(this);
	    location.add(browse);
	    
	    JPanel submitPanel = new JPanel();
	    submit.addActionListener(this);
	    submitPanel.add(submit);
	    
	    container.add(namePanel);
	    container.add(size);
	    container.add(location);
	    container.add(submitPanel);
	    
	    this.add(container);
	    this.setVisible(true);
	}

	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(browse)) {
			JFileChooser chooser = new JFileChooser(new File("."));
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers YAML", "yml");
			chooser.addChoosableFileFilter(filter);
			chooser.setAcceptAllFileFilterUsed(false);
			
			chooser.showOpenDialog(null);
			selectedFile = chooser.getSelectedFile();
		}
		else if(event.getSource().equals(submit)) {
			if(name.getText() != null && sizeX.getText() != null && sizeY.getText() != null && selectedFile != null) {
				if(Utils.isNumeric(sizeX.getText()) && Utils.isNumeric(sizeY.getText())) {
					Level level = new Level(name.getText(), Integer.parseInt(sizeX.getText()), Integer.parseInt(sizeY.getText()));
					level.setPath(selectedFile.getAbsolutePath());
					level.save();
					
					LevelEditor.instance.setLevel(level);
					this.setVisible(false);					
				}
				else {
					JOptionPane.showMessageDialog(null, "Les champs taille X et taille Y attendent un nombre !", "Erreur !", JOptionPane.ERROR_MESSAGE);					
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs ou spécifier un fichier valide !", "Erreur !", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
