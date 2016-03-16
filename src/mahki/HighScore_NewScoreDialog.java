/*
 * Dialog zur Erzeugung eines neuen Highscoreintrags in Mahki
 */

package mahki;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.beans.*;
import java.awt.event.*;
import java.util.regex.Pattern;
import javax.swing.JDialog;
/**
 *
 * @author Dennis Walz und Michael Holst
 */
public class HighScore_NewScoreDialog extends JDialog implements ActionListener, PropertyChangeListener{

    private final String OKSTRING = "Eintragen";
    private final String CANCELSTRING = "Abbrechen";
    private final String ONLYLETTERSALLOWEDSTRING = ">>>> Nur Zahlen und Buchstaben erlaubt!";
    private final String TOOMANYLETTERSSTRING = ">>>> Nur 20 Zeichen erlaubt!";
    private final int score;    /*  Punkte des Spiels */
    private String typedText;   /*  Usereingabe */
    private JTextField textField;   /*  Textfeld für Namenseingaben   */
    private HighScore hs;   /*    Zum Dialog gehörige Highscore   */
    private JOptionPane optionPane; /*  OptionPane des Dialogs  */     
    private String gratulationstring;   /* Meldung an den Spieler   */

    /**
     * Dialog zur Erzeugung eines neuen Highscoreintrags in Mahki
     * @param parent Highscore in die Eintragung erfolgen soll
     * @param score Punkte des Spiels
     */
    public HighScore_NewScoreDialog(HighScore parent, int score) {
        
        /*  Initialisierung des Dialogs */
        super(parent, true);             
        this.setTitle("HighScore"); 
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); 
        this.setLocationRelativeTo(null);
        this.setSize(500, 180);        
        this.score = score;
        
        /*  Erzeugen des Textfelds für den Namen    */
        this.textField = new JTextField(10);
        this.hs = parent;        
        this.gratulationstring = "Gratulation!\nDu hast mit " + score + " Punkten einen Platz in der HighScore erreicht.\nTrage jetzt deinen Namen ein";   
       
        /*  Erzeugen des optionPane für Eintragung bzw. Abbruch der Eintragung  */
        Object[] options = {OKSTRING, CANCELSTRING};
        Object[] array = {gratulationstring, textField};         
        this.optionPane = new JOptionPane(array,JOptionPane.QUESTION_MESSAGE,JOptionPane.YES_NO_OPTION, null, options, options[0]); 
        this.setContentPane(optionPane); 
        
        /*  Gleichbehandlung des Fensterschließens wie einen Abbruch des Dialogs    */
        this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {               
                    optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
            }
        });
        
        this.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
                textField.requestFocusInWindow();
            }
        });
        
        textField.addActionListener(this);
        optionPane.addPropertyChangeListener(this);
        this.setVisible(true);
    }
 
    /**
     * Behandlung Drücken der Entertaste wie OK Klicken
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("ACTION");
        optionPane.setValue(OKSTRING);
    }
 
    /**
     * Behandlung Auswahl Eintragung in die Highscore durch Benutzer
     * @param e PropertyChangeEvent Benutzereingabe
     */ 
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
 
        if (isVisible()
         && (e.getSource() == optionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();
 
            if (value == JOptionPane.UNINITIALIZED_VALUE) 
                return;
            
            if (OKSTRING.equals(value)) {
                    typedText = textField.getText();
                if ((Pattern.compile("^[0-9a-zA-ZäüöÄÖÜß ]+$")).matcher(typedText).find() && typedText.length() <= 20) {
                    
                    hs.addScore(typedText, score);
                    hs.saveScores();
                    hs.refresh();
                    this.dispose();
                    
                } else {
                   
                    if(typedText.length() > 20){
                         Object[] array = {gratulationstring,TOOMANYLETTERSSTRING, textField};
                         optionPane.setMessage(array);
                    } 
                    else{
                        Object[] array = {gratulationstring,ONLYLETTERSALLOWEDSTRING, textField};
                        optionPane.setMessage(array);
                    }                    
                    textField.selectAll();
                    optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
                    typedText = null;
                    textField.requestFocusInWindow();
                }
            } else {       
               hs.refresh();
               this.dispose();
            }
        }
    }
}
