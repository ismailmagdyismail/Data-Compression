package CompressView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class CompressionView extends JFrame {
    private JPanel MainPanel;
    private JTextField writeFilePath;
    private JButton writeFilePathBrowseBtn;
    private JComboBox algorithmSelector;
    private JButton compressBtn;
    private JTextField readFilePath;
    private JButton readFilePathBrowseBtn;
    private JButton decompressBtn;

    public CompressionView(){
        setContentPane(MainPanel);
        setTitle("Compressor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(680, 480);
        setLocationRelativeTo(null);
        setVisible(true);
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
                    compressBtn();
                }
            }
        });
        decompressBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkNull()){
                    decompressBtn();
                }
            }
        });
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
        if(algorithmSelector.getSelectedIndex() == -1 || writeFilePath.getText() == null || readFilePath.getText() != null){
            JFrame frame = new JFrame("Error");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JOptionPane.showMessageDialog(frame, "ERROR please ensure that you filled all fields\nwith a valid data");
            return false;
        }
        return true;
    }
    public void compressBtn(){
        //TODO implement compress button function
    }

    public void decompressBtn(){
        //TODO implement decompress button function
    }
    public void setCompressAction(){

    }
}
