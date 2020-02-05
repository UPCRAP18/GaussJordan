import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

public class GaussJordan {
    private JPanel JPMain;
    private JTable tbMatrixOne;
    private JTable tbMatrixTwo;
    private JTextField txtRowOne;
    private JButton btnGenMatrixOne;
    private JButton btnImpMatrixOne;
    private JTextField txtRowTwo;
    private JButton btnGenMatrixTwon;
    private JButton btnImpMatrixTwo;
    private JTextField txtColOne;
    private JTextField txtColTwo;
    private JButton btnCalculate;
    private JRadioButton rdSuma;
    private JRadioButton rdResta;
    private JRadioButton rdMult;
    private JRadioButton rdGauss;
    private JTable table1;
    private DefaultTableModel tmOne;
    public GaussJordan() {
        tmOne = new DefaultTableModel(5,5);
        tbMatrixOne.setModel(tmOne);


        btnGenMatrixOne.addActionListener(actionEvent -> {
            if (!txtColOne.getText().isEmpty() || !txtRowOne.getText().isEmpty()){
                int col = Integer.parseInt(txtColOne.getText());
                int row = Integer.parseInt(txtRowOne.getText());
                tmOne.setColumnCount(col);
                tmOne.setRowCount(row);
                tbMatrixOne.setModel(tmOne);
            }else {
                JOptionPane.showMessageDialog(this.JPMain, "No puede las dimensiones en blanco", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

        });

        btnImpMatrixOne.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(this.JPMain);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            }
        });
    }

    public static void main(String[] args) {
        JFrame frmMain = new JFrame("The Matrix");
        frmMain.setContentPane(new GaussJordan().JPMain);
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMain.setLocationByPlatform(true);
        frmMain.pack();
        frmMain.setVisible(true);
    }

}
