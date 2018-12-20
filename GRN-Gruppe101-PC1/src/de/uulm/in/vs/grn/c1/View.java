package de.uulm.in.vs.grn.c1;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class View extends JFrame implements ActionListener {
    private JFrame jFrame;
    private JComboBox comboBox = new JComboBox();
    private JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

    private SockgramClient sc = null;

    private JLabel label;
    private JButton b1;
    private JButton b2;
    private File file;
    public View(SockgramClient sc){
        this.sc = sc;
        jFrame = new JFrame();
        jFrame.setSize(400,400);
        jFrame.setTitle("Sockagram-Client");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        BorderLayout borderLayout = new BorderLayout();
        jFrame.getContentPane().setLayout(borderLayout);
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout());
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());

        jFrame.getContentPane().setBackground(Color.LIGHT_GRAY);

        b1 = new JButton("Select File");
        b1.addActionListener(this);

        b2 = new JButton("Appy Filter");
        b2.addActionListener(this);
        southPanel.add(b2);

        label = new JLabel("Currently selected File: NONE");

        comboBox.addItem("0");
        comboBox.addItem("1");
        comboBox.addItem("2");
        comboBox.addItem("3");
        comboBox.addItem("4");
        comboBox.addItem("5");
        comboBox.addItem("6");
        comboBox.addActionListener(this);

        centerPanel.add(b1);
        centerPanel.add(label);
        northPanel.add(comboBox);


        jFrame.getContentPane().add(northPanel,BorderLayout.NORTH);
        jFrame.getContentPane().add(centerPanel,BorderLayout.CENTER);
        jFrame.getContentPane().add(southPanel,BorderLayout.SOUTH);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG, GIF and JPG images", "png", "gif","jpg");
        fileChooser.addChoosableFileFilter(filter);


        jFrame.setVisible(true);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()== b1){
            int val = fileChooser.showOpenDialog(null);
            file = fileChooser.getSelectedFile();
            label.setText("Currently selected File: " + file.getName());
        } else if(e.getSource() == comboBox){
            System.out.println("Combobox");
        }else if(e.getSource() == b2){
            sc.action();
        }


    }
}
