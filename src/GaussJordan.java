import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GaussJordan {
    private JPanel JPMain;
    private JTable tbMatrixOne, tbMatrixTwo, tbMatrixResult;
    private JTextField txtRowOne, txtRowTwo, txtColOne, txtColTwo, txtMatrixOne, txtMatrixTwo;
    private JButton btnGenMatrixOne, btnImpMatrixOne, btnGenMatrixTwo, btnImpMatrixTwo, btnCalculate;
    private JRadioButton rdSuma, rdResta, rdMult, rdGauss;
    private DefaultTableModel tmOne, tmTwo, tmResult;
    private int colsOne = 0, rowsOne = 0, colsTwo = 0, rowsTwo = 0;

    public GaussJordan() {

        tmOne = new DefaultTableModel(5,5);
        tmTwo = new DefaultTableModel(5,5);
        tmResult = new DefaultTableModel(5,5){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbMatrixOne.setModel(tmOne);
        tbMatrixTwo.setModel(tmTwo);
        tbMatrixResult.setModel(tmResult);
        tbMatrixOne.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tbMatrixTwo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tbMatrixResult.setBorder(BorderFactory.createLineBorder(Color.BLACK));

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

        btnGenMatrixTwo.addActionListener(actionEvent -> {
            if (!txtColTwo.getText().isEmpty() || !txtRowTwo.getText().isEmpty()){
                int col = Integer.parseInt(txtColTwo.getText());
                int row = Integer.parseInt(txtRowTwo.getText());
                tmTwo.setColumnCount(col);
                tmTwo.setRowCount(row);
                tbMatrixTwo.setModel(tmTwo);
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
                //TODO Parse the file
                //System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            }
        });
        btnCalculate.addActionListener(e -> {
            colsOne = tbMatrixOne.getColumnCount();
            rowsOne = tbMatrixOne.getRowCount();
            colsTwo = tbMatrixTwo.getColumnCount();
            rowsTwo = tbMatrixTwo.getRowCount();
            if(tbMatrixOne.isEditing()){
                tbMatrixOne.getCellEditor().stopCellEditing();
            }
            if(tbMatrixTwo.isEditing()){
                tbMatrixTwo.getCellEditor().stopCellEditing();
            }

            if (rdSuma.isSelected()){
                performSum();
            }else if (rdResta.isSelected()){
                performRest();
            }else if (rdMult.isSelected()){
                performMulti();
            }
        });
    }

    private void performSum(){
        if(colsOne == colsTwo && rowsOne == rowsTwo){
            //tmResult = new DefaultTableModel(rowsOne,colsOne);
            tmResult.setColumnCount(colsOne);
            tmResult.setRowCount(rowsOne);
            for (int i = 0; i < rowsOne; i++){
                for (int j = 0; j < colsOne; j++) {
                    double val_1 = tmOne.getValueAt(i,j) != null ? Double.parseDouble(String.valueOf(tmOne.getValueAt(i,j))) : 0;
                    double val_2 = tmTwo.getValueAt(i,j) != null ? Double.parseDouble(String.valueOf(tmTwo.getValueAt(i,j))) : 0;
                    double temp = val_1 + val_2;
                    tmResult.setValueAt(temp, i, j);
                }
            }
            tbMatrixResult.setModel(tmResult);
        }else{
            JOptionPane.showMessageDialog(this.JPMain, "Las matrices deben de tener la misma dimension", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void performRest(){
        if(colsOne == colsTwo && rowsOne == rowsTwo){
            //tmResult = new DefaultTableModel(rowsOne,colsOne);
            tmResult.setColumnCount(colsOne);
            tmResult.setRowCount(rowsOne);
            for (int i = 0; i < rowsOne; i++){
                for (int j = 0; j < colsOne; j++) {
                    double val_1 = tmOne.getValueAt(i,j) != null ? Double.parseDouble(String.valueOf(tmOne.getValueAt(i,j))) : 0;
                    double val_2 = tmTwo.getValueAt(i,j) != null ? Double.parseDouble(String.valueOf(tmTwo.getValueAt(i,j))) : 0;
                    double temp = val_1 - val_2;
                    tmResult.setValueAt(temp, i, j);
                }
            }
            tbMatrixResult.setModel(tmResult);
        }else{
            JOptionPane.showMessageDialog(this.JPMain, "Las matrices deben de tener la misma dimension", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void performMulti(){
        if(colsOne == rowsTwo){
            tmResult.setRowCount(rowsOne);
            tmResult.setColumnCount(colsTwo);
            //double[][] result = new double[rowsOne][colsTwo];
            for (int i = 0; i < rowsOne; i++) {
                for (int j = 0; j < colsTwo; j++) {
                    double val = 0;
                    for (int k = 0; k < colsOne; k++) {
                        double val_1 = tmOne.getValueAt(j,k) != null ? Double.parseDouble(String.valueOf(tmOne.getValueAt(i,k))) : 0;
                        double val_2 = tmTwo.getValueAt(k,j) != null ? Double.parseDouble(String.valueOf(tmTwo.getValueAt(k,j))) : 0;
                        val += val_1*val_2;
                        //result[i][j] += val_1*val_2;
                    }
                    tmResult.setValueAt(val,i,j);
                }
            }
            tbMatrixResult.setModel(tmResult);
        }else {
            JOptionPane.showMessageDialog(this.JPMain,
                    "El numero de columnas de la primer matriz debe de coincidir con el numero de filas de la segunda matriz", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        }
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
