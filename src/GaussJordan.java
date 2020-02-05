import com.sun.glass.ui.Screen;

import javax.swing.*;
import java.awt.*;

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

    GaussJordan(){
        //JSPOne.setSize(new Dimension(0,340));
        //JSPTwo.setSize(new Dimension(0,340));
        tbMatrixOne.setSize(0,120);
        tbMatrixTwo.setPreferredSize(new Dimension(0,120));
    }

    public static void main(String[] args) {
        JFrame frmMain = new JFrame("The Matrix");
        frmMain.setContentPane(new GaussJordan().JPMain);
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frmMain.setLocationRelativeTo(null);
        frmMain.setLocationByPlatform(true);
        frmMain.pack();
        frmMain.setVisible(true);
    }

}
