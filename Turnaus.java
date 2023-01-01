package turnaus;

import java.io.File;
import java.util.Collection;
import java.util.List;


/**
 * @author Sara
 * @version 6.4.2022
 *
  * Testien alustus
 * @example
 * <pre name="testJAVA">
 * #import turnaus.SailoException;
 *  private Turnaus turnaus;
 *  private Fencer fiore1;
 *  private Fencer fiore2;
 *  private int fid1;
 *  private int fid2;
 *  private Match match21;
 *  private Match match11;
 *  private Match match22; 
 *  private Match match12; 
 *  private Match match23;
 *  
 *  public void alustaTurnaus() {
 *    turnaus = new Turnaus();
 *    fiore1 = new Fencer(); fiore1.vastaaDeiLiberi(); fiore1.rekisteroi();
 *    fiore2 = new Fencer(); fiore2.vastaaDeiLiberi(); fiore2.rekisteroi();
 *    fid1 = fiore1.getIdNum();
 *    fid2 = fiore2.getIdNum();
 *    match21 = new Match(fid2); match21.vastaaEsimOttelu(fid2);
 *    match11 = new Match(fid1); match11.vastaaEsimOttelu(fid1);
 *    match22 = new Match(fid2); match22.vastaaEsimOttelu(fid2); 
 *    match12 = new Match(fid1); match12.vastaaEsimOttelu(fid1); 
 *    match23 = new Match(fid2); match23.vastaaEsimOttelu(fid2);
 *    try {
 *    turnaus.lisaa(fiore1);
 *    turnaus.lisaa(fiore2);
 *    turnaus.lisaa(match21);
 *    turnaus.lisaa(match11);
 *    turnaus.lisaa(match22);
 *    turnaus.lisaa(match12);
 *    turnaus.lisaa(match23);
 *    } catch ( Exception e) {
 *       System.err.println(e.getMessage());
 *    }
 *  }
 * </pre>
*/
public class Turnaus {
    
    private Fencers fencers = new Fencers();
    private Matches matches = new Matches(); 
    
    /**
     * Lisää turnaukseen uuden miekkailijan
     * @param fencer lisättävä miekkailija
     * @throws SailoException jos lisäystä ei voida tehdä
     * @example
     * <pre name="test">
     * #THROWS SailoException  
     *  alustaTurnaus();
     *  turnaus.etsi("*",0).size() === 2;
     *  turnaus.lisaa(fiore1);
     *  turnaus.etsi("*",0).size() === 3;

     */
    public void lisaa(Fencer fencer) throws SailoException {
        fencers.lisaa(fencer);
    }
    
    /** 
     * Korvaa jäsenen tietorakenteessa.  Ottaa jäsenen omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva jäsen.  Jos ei löydy, 
     * niin lisätään uutena jäsenenä. 
     * @param fencer lisätäävän jäsenen viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo täynnä 
     * @example
     * <pre name="test">
     * #THROWS SailoException  
     *  alustaTurnaus();
     *  turnaus.etsi("*",0).size() === 2;
     *  turnaus.korvaaTaiLisaa(fiore1);
     *  turnaus.etsi("*",0).size() === 2;
     * </pre>
     */ 
    public void korvaaTaiLisaa(Fencer fencer) throws SailoException { 
        fencers.korvaaTaiLisaa(fencer); 
    }
    
    /** 
     * Korvaa harrastuksen tietorakenteessa.  Ottaa harrastuksen omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva harrastus.  Jos ei löydy, 
     * niin lisätään uutena harrastuksena. 
     * @param match lisärtävän harrastuksen viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo täynnä 
     */ 
    public void korvaaTaiLisaa(Match match) throws SailoException { 
        matches.korvaaTaiLisaa(match); 
    } 


    
    /**
     * Listään uusi harrastus kerhoon
     * @param match lisättävä harrastus 
     * @throws SailoException jos tulee ongelmia
     */
    public void lisaa(Match match)throws SailoException {
        matches.lisaa(match);
    }


    /**
     * Poistaa jäsenistöstä ja harrasteista ne joilla on nro. Kesken.
     * @param fencer x
     * @return montako jäsentä poistettiin
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaTurnaus();
     *   turnaus.etsi("*",0).size() === 2;
     *   turnaus.annaMatches(fiore1).size() === 2;
     *   turnaus.poista(fiore1) === 1;
     *   turnaus.etsi("*",0).size() === 1;
     *   turnaus.annaMatches(fiore1).size() === 0;
     *   turnaus.annaMatches(fiore2).size() === 3;
     * </pre>

     */
    public int poista(Fencer fencer) {
        if ( fencer == null ) return 0;
        int ret = fencers.poista(fencer.getIdNum()); 
        matches.removeFencersMatches(fencer.getIdNum()); 
        return ret; 

    }

    /** 
     * Poistaa tämän harrastuksen 
     * @param match poistettava harrastus 
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaTurnaus();
     *   turnaus.annaMatches(fiore1).size() === 2;
     *   turnaus.poistaMatch(match11);
     *   turnaus.annaMatches(fiore1).size() === 1;

     */ 
    public void poistaMatch(Match match) { 
        matches.poista(match); 
    } 

    
    /**
     * Palauttaa i:n miekkailijan
     * @param i monesko miekkailija palautetaan
     * @return viite i:teen miekkailijaan
     * @throws IndexOutOfBoundsException jos i väärin
     * merkattu malliharkassa poistettavaksi
     */
    public Fencer annaFencer(int i) throws IndexOutOfBoundsException {
        return fencers.anna(i);
    }
    
    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien jäsenten viitteet 
     * @param hakuehto hakuehto  
     * @param k etsittävän kentän indeksi  
     * @return tietorakenteen löytyneistä jäsenistä 
     */ 
    public Collection<Fencer> etsi(String hakuehto, int k) { 
        return fencers.etsi(hakuehto, k); 
    } 
    

    
    /**
     * Palautaa turnauksen osallistujamäärän
     * @return osallistujamäärä
     */
    public int getFencers() {
        return fencers.getLkm();
    }
    
    /**
     * Haetaan kaikki miekkailijan ottelut
     * @param fencer miekkailija jolla haetaan otteluita
     * @throws SailoException jos tulee ongelmia
     * @return tietorakenne jossa viiteet löydetteyihin harrastuksiin
     *  @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.*;
     * 
     *  alustaTurnaus();
     *  Fencer fiore3 = new Fencer();
     *  fiore3.rekisteroi();
     *  turnaus.lisaa(fiore3);
     *  
     *  List<Match> loytyneet;
     *  loytyneet = turnaus.annaMatches(fiore3);
     *  loytyneet.size() === 0; 
     *  loytyneet = turnaus.annaMatches(fiore1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == match11 === true;
     *  loytyneet.get(1) == match12 === true;
     *  loytyneet = turnaus.annaMatches(fiore2);
     *  loytyneet.size() === 3; 
     *  loytyneet.get(0) == match21 === true;
     * </pre> 
 
     */
    public List<Match> annaMatches(Fencer fencer) throws SailoException {
        return matches.annaMatches(fencer.getIdNum());
    }

    
    /**
     * Tallettaa turnauksen tiedot tiedostoon
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void save() throws SailoException {
        String virhe = "";
        try {
            fencers.save();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }

        try {
            matches.save();
        } catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);

    }
    
    /**
     * Lukee kerhon tiedot tiedostosta
     * @param nimi jota käyteään lukemisessa
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        fencers = new Fencers(); // jos luetaan olemassa olevaan niin helpoin tyhjentää näin
        matches = new Matches();

        setTiedosto(nimi);
        fencers.lueTiedostosta();
        matches.lueTiedostosta();

    }
    
    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        fencers.setTiedostonPerusNimi(hakemistonNimi + "miekkailijat");
        matches.setTiedostonPerusNimi(hakemistonNimi + "matsit");
    }

    
    /**
     * Testiohjelma kerhosta
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Turnaus turnaus = new Turnaus();

        try {
            // kerho.lueTiedostosta("kelmit");

            Fencer miekkailija1 = new Fencer(), miekkailija2 = new Fencer();
            miekkailija1.rekisteroi();
            miekkailija1.vastaaDeiLiberi();
            miekkailija2.rekisteroi();
            miekkailija2.vastaaDeiLiberi();

            turnaus.lisaa(miekkailija1);
            turnaus.lisaa(miekkailija2);

            System.out.println("============= Turnauksen testi =================");

            for (int i = 0; i < turnaus.getFencers(); i++) {
                Fencer fencer = turnaus.annaFencer(i);
                System.out.println("Miekkailija paikassa: " + i);
                fencer.tulosta(System.out);
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
