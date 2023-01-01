package fxHt;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import turnaus.Fencer;
import turnaus.Match;
import turnaus.Turnaus;

/**
 * @author Sara
 * @version 2.3.2022
 *
 */
public class LisaaMatsiGUIController implements ModalControllerInterface<Match>, Initializable {
    
    @FXML private TextField editCompetitorPoints;
    @FXML private TextField editOpponentPoints;
    @FXML private TextField competitor;
    @FXML private Label labelVirhe;
    @FXML private ComboBoxChooser <Fencer> cbOpponent;
    private boolean tallenna = false;
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        //      
    }
    
    private void alusta() {
       editCompetitorPoints.setText("0"); 
       editOpponentPoints.setText("0");
       cbOpponent.clear();
       Collection<Fencer> fencers = turnaus.etsi("", 0);
       competitor.setText(miekkailijanKohdalla.getName());
       fencers.remove(miekkailijanKohdalla);
       for(Fencer fencer : fencers) {
           cbOpponent.add(fencer.getName(), fencer);
       }
    }

    /**
     * @param modalitystage mille ollaan modaalisia
     * @param turnaus x
     * @param miekkailijanKohdalla x
     * @param matchKohdalla x
     * @return x
     */
    public static Match kysyOttelu(Stage modalitystage, Turnaus turnaus, Fencer miekkailijanKohdalla, Match matchKohdalla ){
       return ModalController.<Match, LisaaMatsiGUIController>showModal(LisaaMatsiGUIController.class.getResource("LisaaMatsiGUIView.fxml"), 
               "Muokkaa ottelua", 
               modalitystage, matchKohdalla,  
               ctrl-> {ctrl.setTurnaus(turnaus); ctrl.setFencer(miekkailijanKohdalla);});
    }
    
    
    @Override
    public void handleShown() {
        alusta();
    }
    
    @FXML private void handleOK() {
        if ( miekkailijanKohdalla != null && miekkailijanKohdalla.getName().trim().equals("") ) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }
        matchKohdalla.aseta(2, "" + cbOpponent.getSelectedObject().getIdNum());
        matchKohdalla.aseta(3, "" + editCompetitorPoints.getText());
        matchKohdalla.aseta(4, "" + editOpponentPoints.getText());
        tallenna = true;
        ModalController.closeStage(labelVirhe);
    }
    
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }
    
    @FXML private void handleCancel() {
        miekkailijanKohdalla = null;
        ModalController.closeStage(labelVirhe);

    }
    

    @Override
    public Match getResult() {
        if(tallenna  == false)
            return null;
        return matchKohdalla;
    }

    @Override
    public void setDefault(Match oletus) {
        matchKohdalla = oletus;    
    }
    
    /**
     * @param miekkailijanKohdalla x
     */
    public void setFencer(Fencer miekkailijanKohdalla) {
        this.miekkailijanKohdalla = miekkailijanKohdalla;
    }
    
    /**
     * @param turnaus x
     */
    public void setTurnaus (Turnaus turnaus) {
        this.turnaus = turnaus;
    }
    
    private Turnaus turnaus;
    private Fencer miekkailijanKohdalla;
    private Match matchKohdalla;
    
}