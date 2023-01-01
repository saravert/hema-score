package fxHt;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import turnaus.Fencer;

/**
 * @author Sara
 * @version 2.3.2022
 *
 */
public class LisaaMiekkailijaGUIController implements ModalControllerInterface<Fencer>, Initializable{

    @FXML private TextField editName;
    @FXML private TextField editClub;
    @FXML private TextField editCountry;   
    @FXML private Label labelVirhe;
    
    private TextField edits[];

    /**
     * @param url x
     * @param bundle x
     */ 
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();  
    }
    
    @FXML private void handleOK() {
        if ( miekkailijanKohdalla != null && miekkailijanKohdalla.getName().trim().equals("") ) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }

        ModalController.closeStage(labelVirhe);
    }

    
    @FXML private void handleCancel() {
        miekkailijanKohdalla = null;
        ModalController.closeStage(labelVirhe);

    }

// ========================================================    
    private Fencer miekkailijanKohdalla;
    private int kentta = 0;
    

    /**
     * Tyhjentään tekstikentät 
     * @param edits x
     */
    public static void tyhjenna(TextField[] edits) {
        for (TextField edit : edits)
            edit.setText("");

    }


    /**
     * Tekee tarvittavat muut alustukset.
     */
    protected void alusta() {
        edits = new TextField[]{editName, editClub, editCountry};
        int i = 0;
        for (TextField edit : edits) {
            final int k = ++i;
            edit.setOnKeyReleased( e -> kasitteleMuutosMiekkailijaan(k, (TextField)(e.getSource())));
        }

    }
    
    
    @Override
    public void setDefault(Fencer oletus) {
        miekkailijanKohdalla = oletus;
        naytaFencer(edits, miekkailijanKohdalla);
    }
    
    private void setKentta(int kentta) {
        this.kentta = kentta;
    }


    
    @Override
    public Fencer getResult() {
        return miekkailijanKohdalla;
    }
    
    
    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        editName.requestFocus();
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

    
    /**
     * Käsitellään miekkailijaan tullut muutos
     * @param edit muuttunut kenttä
     */
    private void kasitteleMuutosMiekkailijaan(int k, TextField edit) {
        if (miekkailijanKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        switch (k) {
           case 1 : virhe = miekkailijanKohdalla.setName(s); break;
           case 2 : virhe = miekkailijanKohdalla.setClub(s); break;
           case 3 : virhe = miekkailijanKohdalla.setCountry(s); break;
           default:
        }
        if (virhe == null) {
            Dialogs.setToolTipText(edit,"");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit,virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }

    
    /**
     * Näytetään miekkailijan tiedot TextField komponentteihin
     * @param edits editoitava
     * @param fencer näytettävä miekkailija
     */
    public static void naytaFencer(TextField[] edits, Fencer fencer) {
        if (fencer == null) return;
        edits[0].setText(fencer.getName());
        edits[1].setText(fencer.getClub());
        edits[2].setText(fencer.getCountry());

    }
    
    
    /**
     * Luodaan miekkailijan kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * TODO: korjattava toimimaan
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @param kentta x
     * @return null jos painetaan Cancel, muuten täytetty tietue
     */
    public static Fencer kysyJasen(Stage modalityStage, Fencer oletus, int kentta) {
        return ModalController.<Fencer, LisaaMiekkailijaGUIController>showModal(
                LisaaMiekkailijaGUIController.class.getResource("LisaaMiekkailijaGUIView.fxml"),
                    "Miekkailija",
                    modalityStage, oletus, 
                    ctrl -> ctrl.setKentta(kentta) 
                );
    }

    
      // TODO
}