package fxHt;

import java.io.PrintStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import javafx.application.Platform;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import turnaus.Fencer;
import turnaus.Match;
import turnaus.SailoException;
import turnaus.Turnaus;

/**
 * @author Sara
 * @version 3.2.2022
 *
 */
public class HtGUIController implements Initializable {
    private Turnaus turnaus;
    private Fencer miekkailijanKohdalla;
    private TextArea areaFencer = new TextArea();
    private TextField edits[];
    private int kentta = 0;
    private static Match apumatch = new Match(); 
    private static Fencer apufencer = new Fencer();

    @FXML private StringGrid <Match> matchesGrid;
    @FXML private ScrollPane panelJasen;
    @FXML private Label labelVirhe;
    @FXML private ComboBoxChooser<String> cbKentat; 
    @FXML private TextField hakuehto;
    @FXML private StringGrid<Match> tableMatches;
    @FXML private ListChooser<Fencer> chooserJasenet;
    @FXML private TextField editName; 
    @FXML private TextField editClub; 
    @FXML private TextField editCountry; 


      private String kisannimi = "Swordfish";
      @Override
      public void initialize(URL url, ResourceBundle bundle) {
         alusta();     
      }

 

      
      @FXML private void handleAddFencer() {
         // ModalController.showModal(HtGUIController.class.getResource("LisaaMiekkailijaGUIView.fxml"), "Add fencer", null, "");
          newFencer();
      }
      
      @FXML private void handleEditFencer() {
         muokkaa(kentta);
      }
      
      @FXML private void handleEditMatch() {
          Dialogs.showMessageDialog("I don't know how to edit fencer");
      }
      
      @FXML private void handleRemoveFencer() {
         // Dialogs.showMessageDialog("I don't know how to remove fencer");
          removeFencer();
      }
      
      @FXML private void handleRemoveMatch() {
        //  Dialogs.showMessageDialog("I don't know how to remove match");
          removeMatch();
      }
      
      @FXML private void handleAddMatch() {
          //ModalController.showModal(HtGUIController.class.getResource("LisaaMatsiGUIView.fxml"), "Add match", null, "");
          newMatch();
      }
      
      @FXML private void handleHakuehto() {
         hae(0);
      }
      
      /**
       * Käsitellään tallennuskäsky
       */
      @FXML private void handleTallenna() {
          save();
      }
      
      
      /**
       * Käsitellään lopetuskäsky
       */
      @FXML private void handleLopeta() {
          save();
          Platform.exit();
      }
      
      private void muokkaa(int k) {
   //      LisaaMiekkailijaGUIController.kysyJasen(null, miekkailijanKohdalla);
   //      hae(miekkailijanKohdalla.getIdNum());
          if ( miekkailijanKohdalla == null ) return; 
          try { 
              Fencer fencer; 
              fencer = LisaaMiekkailijaGUIController.kysyJasen(null, miekkailijanKohdalla.clone(), k); 
              if ( fencer == null ) return; 
              turnaus.korvaaTaiLisaa(fencer); 
              hae(fencer.getIdNum()); 
          } catch (CloneNotSupportedException e) { 
              // 
          } catch (SailoException e) { 
              Dialogs.showMessageDialog(e.getMessage()); 
          }


      }

      /**
       * Luo uuden jäsenen jota aletaan editoimaan 
       */
      protected void newFencer() {
          try {
              Fencer uusi = new Fencer();
              uusi = LisaaMiekkailijaGUIController.kysyJasen(null, uusi, 1);  
              if ( uusi == null ) return;
              uusi.rekisteroi();
              turnaus.lisaa(uusi);
              hae(uusi.getIdNum());

          } catch (SailoException e) {
              Dialogs.showMessageDialog("Problems with creating new " + e.getMessage());
              return;
          }
      }
      
      /*
       * Poistetaan listalta valittu jäsen
       */
      private void removeFencer() {
          Fencer fencer = miekkailijanKohdalla;
          if ( fencer == null ) return;
          if ( !Dialogs.showQuestionDialog("Remove", "Do you wish to remove: " + fencer.getName(), "Yes", "No") )
              return;
          turnaus.poista(fencer);
          int index = chooserJasenet.getSelectedIndex();
          hae(0);
          chooserJasenet.setSelectedIndex(index);
      }

      
      /** 
       * Tekee uuden tyhjän matsin editointia varten 
       */ 
      public void newMatch() { 
          try {
              Match uusi = new Match(miekkailijanKohdalla.getIdNum());
              uusi = LisaaMatsiGUIController.kysyOttelu(null, turnaus, miekkailijanKohdalla, uusi);
              if ( uusi == null ) return;
              uusi.rekisteroi();
              turnaus.lisaa(uusi);
              naytaFencer();

          } catch (SailoException e) {
              Dialogs.showMessageDialog("Problems with creating new " + e.getMessage());
              return;
          }
                   
      }
      
      /**
       * Poistetaan harrastustaulukosta valitulla kohdalla oleva harrastus. 
       */
      private void removeMatch() {
          int rivi = matchesGrid.getRowNr();
          if ( rivi < 0 ) return;
          Match match = matchesGrid.getObject();
          if ( match == null ) return;
          turnaus.poistaMatch(match);
          naytaMatches(miekkailijanKohdalla);
          int harrastuksia = matchesGrid.getItems().size(); 
          if ( rivi >= harrastuksia ) rivi = harrastuksia -1;
          matchesGrid.getFocusModel().focus(rivi);
          matchesGrid.getSelectionModel().select(rivi);
      }


      
      
      /**
       * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
       * yksi iso tekstikenttä, johon voidaan tulostaa miekkailijan tiedot.
       * Alustetaan myös miekkailijalistan kuuntelija 
       */
      protected void alusta() {
          chooserJasenet.clear();
          chooserJasenet.addSelectionListener(e -> naytaFencer());
          
          cbKentat.clear(); 
          for (int k = apufencer.ekaKentta(); k < apufencer.getKenttia(); k++) 
              cbKentat.add(apufencer.getKysymys(k), null); 
          cbKentat.getSelectionModel().select(0); 

          edits = new TextField[]{editName, editClub, editCountry}; 
          
          // alustetaan harrastustaulukon otsikot 
          int eka = apumatch.ekaKentta(); 
          int lkm = apumatch.getKenttia(); 
          String[] headings = new String[lkm-eka]; 
          for (int i=0, k=eka; k<lkm; i++, k++) headings[i] = apumatch.getKysymys(k); 
          matchesGrid.initTable(headings); 
          matchesGrid.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
          matchesGrid.setEditable(false); 
          matchesGrid.setPlaceholder(new Label("No matches yet")); 
           
          // Tämä on vielä huono, ei automaattisesti muutu jos kenttiä muutetaan. 
          matchesGrid.setColumnSortOrderNumber(1); 
          matchesGrid.setColumnSortOrderNumber(2); 
          matchesGrid.setColumnWidth(1, 60); 

      }
      

      
      /**
     * @param turnaus valittu turnaus
     */
    public void setTurnaus(Turnaus turnaus) {
          this.turnaus = turnaus;
          naytaFencer();
      }
    
    /**
     * Näyttää listasta valitun miekkailijan tiedot, tilapäisesti yhteen isoon edit-kenttään
     */
    protected void naytaFencer() {
        miekkailijanKohdalla = chooserJasenet.getSelectedObject();

        if (miekkailijanKohdalla == null) return;
        
        LisaaMiekkailijaGUIController.naytaFencer(edits, miekkailijanKohdalla);
        naytaMatches(miekkailijanKohdalla);
    }


    
    private void naytaMatches(Fencer fencer) {
        if ( fencer == null ) return;
        
        matchesGrid.clear();
        try {
            for(Match match: turnaus.annaMatches(miekkailijanKohdalla)){
                Fencer vastustaja = turnaus.annaFencer(match.getOpponent());
               String [] rivi = {vastustaja != null? vastustaja.getName(): "tuntematon", match.getCompetitorPoints(), match.getOpponentPoints()};
                matchesGrid.add(match, rivi);
            }
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ottelun hakemisessa ongelmia! " );
            e.printStackTrace();
        }
    }

      
      /**
       * Hakee miekkalijan tiedot listaan
       * @param jnr miekkalijan numero, joka aktivoidaan haun jälkeen
       */
    protected void hae(int jnr) {
        int jnro = jnr; // jnro jäsenen numero, joka aktivoidaan haun jälkeen 
        if ( jnro <= 0 ) { 
            Fencer kohdalla = miekkailijanKohdalla; 
            if ( kohdalla != null ) jnro = kohdalla.getIdNum(); 
        }
        
        int k = cbKentat.getSelectionModel().getSelectedIndex() + apufencer.ekaKentta(); 

          String ehto = hakuehto.getText(); 
          if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; 
          chooserJasenet.clear();

          int index = 0;
          Collection<Fencer> fencers;
          fencers = turnaus.etsi(ehto, k);
          int i = 0;
          for (Fencer fencer:fencers) {
              if (fencer == null)
                  continue;
              if (fencer.getIdNum() == jnro) index = i;
              chooserJasenet.add(fencer.getName(), fencer);
              i++;
          }
          chooserJasenet.setSelectedIndex(index); // tästä tulee muutosviesti joka näyttää jäsenen
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
       * Tietojen tallennus
       * @return null jos onnistuu, muuten virhe tekstinä
       */
      private String save() {
          try {
              turnaus.save();
              return null;
          } catch (SailoException ex) {
              Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
              return ex.getMessage();
          }

      }
      
      /**
       * Tarkistetaan onko tallennus tehty
       * @return true jos saa sulkaa sovelluksen, false jos ei
       */
      public boolean voikoSulkea() {
          save();
          return true;
      }




    /**
     * @return x
     
    public boolean avaa() {
        String uusinimi = AloitusGUIController.kysyNimi(null, kisannimi);
        if (uusinimi == null) return true;
        lueTiedosto(uusinimi);
        return true;
    }
    */
    /**
     * @param nimi tiedoston nimi
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    protected String lueTiedosto(String nimi) {
        kisannimi = nimi;
        setTitle("Kisa - " + kisannimi);
        try {
            turnaus.lueTiedostosta(nimi);
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }

    }
    
    private void setTitle(String title) {
       // ModalController.getStage(hakuehto).setTitle(title);
    }
    
    /**
     * Tulostaa miekkalijan tiedot
     * @param os tietovirta johon tulostetaan
     * @param fencer tulostettava miekkailija
     */
    public void tulosta(PrintStream os, final Fencer fencer) {
        os.println("----------------------------------------------");
        fencer.tulosta(os);
        os.println("----------------------------------------------");
        try {
            List<Match> matches = turnaus.annaMatches(fencer);
            for (Match match:matches) 
                match.tulosta(os);     
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Matsien hakemisessa ongelmia! " + ex.getMessage());
        }  
 
    }
    
    /**
     * Tulostaa listassa olevat miekkailijat tekstialueeseen
     * @param text alue johon tulostetaan
     */
    public void tulostaValitut(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Tulostetaan kaikki jäsenet");
            Collection<Fencer> fencers = turnaus.etsi("", -1); 
            for (Fencer fencer:fencers) { 

                tulosta(os, fencer);
                os.println("\n\n");
            }
        }
    }



}