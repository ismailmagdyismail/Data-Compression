package View;

import Controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GUI extends JFrame {
    private JPanel MainPanel;
    private JTextField writeFilePath;
    private JButton writeFilePathBrowseBtn;
    private JComboBox algorithmSelector;
    private JButton compressBtn;
    private JTextField readFilePath;
    private JButton readFilePathBrowseBtn;
    private JButton decompressBtn;
    private JTextField outputField;
    private final Controller controller;

    public GUI(){
        this.controller = new Controller(this);
        setContentPane(MainPanel);
        setTitle("Compressor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(680, 480);
        setLocationRelativeTo(null);
        setVisible(true);
        initializeAlgorithmSelector(algorithmSelector);
        writeFilePathBrowseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseBtnAction(writeFilePath);
            }
        });
        readFilePathBrowseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseBtnAction(readFilePath);
            }
        });
        compressBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkNull()){
                    compressBtnWork();
                }
            }
        });
        decompressBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkNull()){
                    decompressBtnWork();
                }
            }
        });
    }

    public JComboBox getAlgorithmSelector() {
        return algorithmSelector;
    }

    public void browseBtnAction(JTextField textField){
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            textField.setText(selectedFile.getAbsolutePath());
        }
    }

    public boolean checkNull(){
        if(algorithmSelector.getSelectedIndex() == -1 || writeFilePath.getText() == null || readFilePath.getText() == null){
            viewMessageBox("ERROR please ensure that you filled all fields\nwith a valid data");
            return false;
        }
        return true;
    }

    public void initializeAlgorithmSelector(JComboBox comboBox){
        String[] algorithmList = controller.getAlgorithmsList();
        for(String algo : algorithmList){
            comboBox.addItem(algo);
        }
    }

    public void compressBtnWork(){
        try {
            controller.setCompressionAlgorithm(algorithmSelector.getSelectedIndex());
            outputField.setText(controller.compress());
        } catch (RuntimeException e){
            viewMessageBox(e.getMessage());
        }
        viewMessageBox("compressed successfully");
    }

    public void decompressBtnWork(){
        try {
            controller.setCompressionAlgorithm(algorithmSelector.getSelectedIndex());
            outputField.setText(controller.decompress());
        } catch (RuntimeException e){
            viewMessageBox(e.getMessage());
        }
        viewMessageBox("decompressed successfully");
    }
    public String getReadFilePath(){
        return this.readFilePath.getText();
    }
    public String getWriteFilePath(){
        return this.writeFilePath.getText();
    }
    private void viewMessageBox(String message){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, message);
    }
}
