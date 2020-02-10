import com.google.common.io.Files;
import sun.rmi.transport.ObjectTable;

import javax.print.DocFlavor;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.*;
import java.util.List;

public class GaussJordan {
    private JPanel JPMain;
    private JTable tbMatrixOne, tbMatrixTwo, tbMatrixResult;
    private JTextField txtRowOne, txtRowTwo, txtColOne, txtColTwo, txtMatrixOne, txtMatrixTwo;
    private JButton btnGenMatrixOne, btnImpMatrixOne, btnGenMatrixTwo, btnImpMatrixTwo, btnCalculate;
    private JRadioButton rdSuma, rdResta, rdMult, rdGauss;
    private DefaultTableModel tmOne, tmTwo, tmResult;
    private int colsOne = 0, rowsOne = 0, colsTwo = 0, rowsTwo = 0;
    private float[][] matrix_result;

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
                String fileExt = Files.getFileExtension(selectedFile.getAbsolutePath());
                if(fileExt.equals("txt")){
                    parseDocument(selectedFile, tmOne, tbMatrixOne);
                }else{
                    JOptionPane.showMessageDialog(this.JPMain,
                            "Archivo invalido.\nPor favor seleccione un archivo de texto",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnImpMatrixTwo.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(this.JPMain);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String fileExt = Files.getFileExtension(selectedFile.getAbsolutePath());
                if(fileExt.equals("txt")){
                    parseDocument(selectedFile, tmTwo, tbMatrixTwo);
                }else{
                    JOptionPane.showMessageDialog(this.JPMain,
                            "Archivo invalido.\nPor favor seleccione un archivo de texto",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        new DropTarget(txtMatrixOne, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {

            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {

            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {

            }

            @Override
            public void dragExit(DropTargetEvent dte) {

            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    // Accept the drop first, important!
                    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    List data = (List) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    File fileDragged = (File) data.get(0);
                    txtMatrixOne.setText(fileDragged.getName());
                    parseDocument(fileDragged, tmOne, tbMatrixOne);


                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });

        new DropTarget(txtMatrixTwo, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {

            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {

            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {

            }

            @Override
            public void dragExit(DropTargetEvent dte) {

            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    // Accept the drop first, important!
                    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    List data = (List) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    File fileDragged = (File) data.get(0);
                    txtMatrixTwo.setText(fileDragged.getName());
                    parseDocument(fileDragged, tmTwo, tbMatrixTwo);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });

        btnCalculate.addActionListener(e -> {
                    colsOne = tbMatrixOne.getColumnCount();
                    rowsOne = tbMatrixOne.getRowCount();
                    colsTwo = tbMatrixTwo.getColumnCount();
                    rowsTwo = tbMatrixTwo.getRowCount();
                    if (tbMatrixOne.isEditing()) {
                        tbMatrixOne.getCellEditor().stopCellEditing();
                    }
                    if (tbMatrixTwo.isEditing()) {
                        tbMatrixTwo.getCellEditor().stopCellEditing();
                    }

                    if (rdSuma.isSelected()) {
                        performSum();
                    } else if (rdResta.isSelected()) {
                        performRest();
                    } else if (rdMult.isSelected()) {
                        performMulti();
                    } else if (rdGauss.isSelected()) {
                        performGaussJordan();
                    }
                });
    }

    private void parseDocument(File file, DefaultTableModel tm, JTable tb){
        try {
            String data = new String(java.nio.file.Files.readAllBytes(file.toPath()));

            String[] str_filas = data.split(";");
            int filas = str_filas.length;

            int cols = 0;

            for (String str_fila : str_filas) {
                int temp = str_fila.split(" ").length;
                if (temp > cols) {
                    cols = temp;
                }
            }

            tm.setRowCount(filas);
            tm.setColumnCount(cols);

            int i = 0, j = 0;
            for (String str_linea: str_filas) {
                for (String val: str_linea.split(" ")) {
                    tm.setValueAt(val, i,j);
                    j++;
                }
                j = 0;
                i++;
            }

            tb.setModel(tm);

        }catch (IOException ex){
            System.out.println(ex);
        }

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
            for (int i = 0; i < rowsOne; i++) {
                for (int j = 0; j < colsTwo; j++) {
                    double val = 0;
                    for (int k = 0; k < colsOne; k++) {
                        double val_1 = tmOne.getValueAt(j,k) != null ? Double.parseDouble(String.valueOf(tmOne.getValueAt(i,k))) : 0;
                        double val_2 = tmTwo.getValueAt(k,j) != null ? Double.parseDouble(String.valueOf(tmTwo.getValueAt(k,j))) : 0;
                        val += val_1*val_2;
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

    private void performGaussJordan(){
        int filas = tbMatrixOne.getRowCount();
        int cols = tbMatrixOne.getColumnCount();
        tmResult.setRowCount(filas);
        tmResult.setColumnCount(cols);
        matrix_result = new float[filas][cols];
        //Volcado a arreglo
        setValuesMatrix(filas, cols);
        int n = filas;
        int flag = 0;

        flag = solveMatrix(n);

        if (flag == 1){
            flag = verifyConsist(n, flag);
        }

        makeOnes(filas, cols);;

        if (flag == 2)
            JOptionPane.showMessageDialog(this.JPMain, "Existen soluciones infinitas", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
        else if (flag == 3)
            JOptionPane.showMessageDialog(this.JPMain, "No existen soluciones", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
        else {
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < cols; j++) {
                    tmResult.setValueAt(this.matrix_result[i][j], i,j);
                }
            }
        }

        tbMatrixResult.setModel(tmResult);

    }

    private void makeOnes(int f, int c){
        for (int i = 0; i < f; i++){
            float diver = this.matrix_result[i][i];
            for (int j = 0; j < c; j ++){
                float res = this.matrix_result[i][j] / diver;
                String res_format = String.format("%.2g", res);
                this.matrix_result[i][j] = Float.parseFloat(res_format);
            }
        }

    }

    private int verifyConsist(int n, int flag){
        int i,j;
        float sum;
        // flag == 2 Soluciones Infinitas
        // flag == 3 Sin solucion
        flag = 3;
        for (i = 0; i < n; i++) {
            sum = 0;
            for (j = 0; j < n; j++)
                sum = sum + this.matrix_result[i][j];
            if (sum == this.matrix_result[i][j])
                flag = 2;
        }
        return flag;
    }

    private int solveMatrix(int n){
        int c, flag = 0;
        //n --> Numero de filas
        for (int i = 0; i < n; i++) {
            if (this.matrix_result[i][i] == 0){
                c = 1;
                while (this.matrix_result[i + c][i] == 0 && (i + c) < n)
                    c++;
                if ((i + c) == n) {
                    flag = 1;
                    break;
                }
                for (int j = i, k = 0; k <= n; k++) {
                    float temp = matrix_result[j][k];
                    matrix_result[j][k] = matrix_result[j+c][k];
                    matrix_result[j+c][k] = temp;
                }
            }

            for (int j = 0; j < n; j++) {
                if (i != j) {
                    float p = this.matrix_result[j][i] / this.matrix_result[i][i];
                    for (int k = 0; k <= n; k++)
                        this.matrix_result[j][k] = this.matrix_result[j][k] - (this.matrix_result[i][k]) * p;
                }
            }
        }
        return flag;
    }

    private void setValuesMatrix(int filas, int cols){
        for (int i = 0; i < filas; i++){
            for (int j = 0; j < cols; j++){
                float value = this.tmOne.getValueAt(i,j) != null ? Float.parseFloat(this.tmOne.getValueAt(i,j).toString()) : 0;
                this.matrix_result[i][j] = value;
            }
        }
    }

    private void renderMatrix(int f, int c){
        for (int i = 0; i < f; i++){
            for (int j = 0; j < c; j++){
                System.out.print(this.matrix_result[i][j] + " ");
            }
            System.out.println("");
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
