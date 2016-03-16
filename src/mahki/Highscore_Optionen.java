/*
 * OptionenPopup für Mahki GUI
 */

package mahki;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Michael Holst
 */
public class Highscore_Optionen extends JFrame{
    private final GUI gui;
    private JLabel LabelHoehe;
    private JLabel LabelBreite;
    private JLabel LabelFarbe;
    private JSpinner SpinnerHoehe;
    private JSpinner SpinnerBreite;
    private JSpinner SpinnerFarbe;
    private JButton BtnOk;
    private JButton BtnCancel;
    private Highscore_Optionen frame;
    private JCheckBox CheckEndless;
    
    Highscore_Optionen(final GUI gui){
        
        super("Highscoreoptionen");
        this.setSize(300, 300);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.gui = gui;
        this.frame = this;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setAlwaysOnTop(true);
       
        this.setLayout(null);
        
        LabelHoehe = new JLabel("Höhe");
        LabelHoehe.setBounds(75, 50, 40, 25);
        add(LabelHoehe);
        
        SpinnerHoehe = new JSpinner();
        SpinnerHoehe.setBounds(116, 50, 100, 25);
        SpinnerHoehe.setModel(new SpinnerNumberModel(10, 10, 40, 1));
        SpinnerHoehe.setValue(new Integer(gui.gamefield_y));
        add(SpinnerHoehe);
        
        LabelBreite = new JLabel("Breite");
        LabelBreite.setBounds(75, 75, 40, 25);
        
        add(LabelBreite);
        
        SpinnerBreite = new JSpinner();
        SpinnerBreite.setBounds(116,75, 100, 25);
        SpinnerBreite.setModel(new SpinnerNumberModel(10, 10, 40, 1));
        SpinnerBreite.setValue(new Integer(gui.gamefield_x));
        add(SpinnerBreite);
        
        LabelFarbe = new JLabel("Farben");
        LabelFarbe.setBounds(75, 100, 40, 25);
        add(LabelFarbe);
        
        SpinnerFarbe = new JSpinner();
        SpinnerFarbe.setBounds(116,100, 100, 25);
        SpinnerFarbe.setModel(new SpinnerNumberModel(2, 2, 5, 1));
        SpinnerFarbe.setValue(new Integer(gui.colors));
        add(SpinnerFarbe);
        
        BtnOk = new JButton("Ok");
        BtnOk.setBounds(45,125, 100, 25);
        add(BtnOk);
        
        BtnOk.addActionListener(new java.awt.event.ActionListener() {
            private HighScore hs;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(hs!= null)
                    hs.dispose();
                hs = new HighScore((int)SpinnerFarbe.getValue(), (int)SpinnerBreite.getValue(), (int)SpinnerHoehe.getValue(), CheckEndless.isSelected());
                
                hs.refresh();
                hs.setVisible(true);
            }
        });
        
        BtnCancel = new JButton("Abbrechen");
        BtnCancel.setBounds(145,125, 100, 25);
        add(BtnCancel);
        
        BtnCancel.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frame.dispose();
            }
        });   
        
        CheckEndless = new JCheckBox("Endlosmodus");
        CheckEndless.setBounds(80, 150, 300, 25);
        CheckEndless.setSelected(gui.isEndlessMode);
        add(CheckEndless);
        
    }    
}
