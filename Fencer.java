package turnaus;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Sara
 * @version 6.4.2022
 *
 */
public class Fencer implements Cloneable{
    
    private int idNum;
    private static int seuraavaNro  = 1;
    private String name             ="";
    private String club             ="";
    private String country          ="";

    
    
    /**
     * Antee miekkailijalle seuraavan rekisterinumeron
     * @return tunnistenumeron
     * @example
     * <pre name="test">
     *   Fencer fiore1 = new Fencer();
     *   fiore1.getIdNum() === 0;
     *   fiore1.rekisteroi();
     *   Fencer fiore2 = new Fencer();
     *   fiore2.rekisteroi();
     *   int n1 = fiore1.getIdNum();
     *   int n2 = fiore2.getIdNum();
     *   n1 === n2-1;
     * </pre>

     */
    public int rekisteroi() {
        idNum = seuraavaNro;
        seuraavaNro++;
        return idNum;
    }
    
    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot miekkailijalle. 
     */
    public void vastaaDeiLiberi() {
        name = "Dei Liberi Fiore "+ rand(100, 999);
        club = "Premariacco";
        country = "Italy";
        
    }
    
    /**
     * Arpoo satunnaisen luvun annetulta väliltä
     * @param alaraja random numeron alaraja
     * @param ylaraja random numeron yläraja
     * @return randomluvun
     */
    public static int rand(int alaraja, int ylaraja) {
        double n = (ylaraja-alaraja)*Math.random() + alaraja;
        return (int)Math.round(n);

    }

    /**
     * Palauttaa miekkailijan tunnusnumeron.
     * @return miekkailijan tunnusnumero
     */
    public int getIdNum() {
        return idNum;
    }
    
    /**
     * @return miekkailijan nimi
     * @example
     * <pre name="test">
     *   Fencer miekkailija1 = new Fencer();
     *   miekkailija1.vastaaDeiLiberi();
     *   miekkailija1.getName() =R= "Dei Liberi Fiore .*";
     * </pre>

     */
    public String getName() {
        return name;
    }
    
    /**
     * @return miekkailijan edustaman seuran
     */
    public String getClub() {
        return club;
    }
    
    /**
     * @return miekkailijan edustaman maan
     */
    public String getCountry() {
        return country;
    }
    
    /**
     * Tulostetaan miekkailijan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Tulostetaan miekkailijan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", idNum, 3) + "  " + name);
        out.println(club);
        out.println(country);
    }
    
    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setIdNum(int nr) {
        idNum = nr;
        if (idNum >= seuraavaNro) seuraavaNro = idNum + 1;
    }
    
    /**
     * Palauttaa miekkailijan tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return miekkailija tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Fencer fencer = new Fencer();
     *   fencer.parse("   3  |  Ankka Aku   | Premaccio");
     *   fencer.toString().startsWith("3|Ankka Aku|Premaccio|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     * </pre>

     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) {
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|";
        }
        return sb.toString();

    }
    
    /**
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     */
    public String getKysymys(int k) {
        switch (k) {
            case 0:
                return "idNum";
            case 1:
                return "name";
            case 2:
                return "club";
            case 3:
                return "country";
            default:
                return "???";
        }
    }
    
    /**
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     */
    public String anna(int k) {
        switch (k) {
            case 0:
                return "" + idNum;
            case 1:
                return "" + name;
            case 2:
                return "" + club;
            case 3:
                return "" + country;
            default:
                return "???";
        }
    }

    /**
     * @return matsin kenttien lukumäärä
     */
    public int getKenttia() {
        return 4;
    }
    
    /**
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     */
    public int ekaKentta() {
        return 1;
    }

    
    /**
     * Selvitää miekkailijan tiedot | erotellusta merkkijonosta
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva idNum.
     * @param rivi josta jäsenen tiedot otetaan
     *  @example
     * <pre name="test">
     *   Fencer fencer = new Fencer();
     *   fencer.parse("   3  |  Ankka Aku   | Premaccio");
     *   fencer.getIdNum() === 3;
     *   fencer.toString().startsWith("3|Ankka Aku|Premaccio|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     *
     *   fencer.rekisteroi();
     *   int n = fencer.getIdNum();
     *   fencer.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero
     *   fencer.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
     *   fencer.getIdNum() === n+20+1;
     *     
     * </pre>

     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setIdNum(Mjonot.erota(sb, '|', getIdNum()));
        name = Mjonot.erota(sb, '|', name);
        club = Mjonot.erota(sb, '|', club);
        country = Mjonot.erota(sb, '|', country);
    }
    
    /**
     * Tehdään identtinen klooni jäsenestä
     * @return Object kloonattu jäsen
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Fencer fencer = new Fencer();
     *   fencer.parse("   3  |  Ankka Aku   | Premaccio");
     *   Fencer kopio = fencer.clone();
     *   kopio.toString() === fencer.toString();
     *   fencer.parse("   4  |  Ankka Tupu   | Fior Di Battalgia");
     *   kopio.toString().equals(fencer.toString()) === false;
     * </pre>
     */
    @Override
    public Fencer clone() throws CloneNotSupportedException {
        Fencer uusi;
        uusi = (Fencer) super.clone();
        return uusi;
    }

    
    @Override
    public boolean equals(Object fencer) {
        if ( fencer == null ) return false;
        return this.toString().equals(fencer.toString());
    }


    @Override
    public int hashCode() {
        return idNum;
    }


    
    /**
     * Testiohjelma jäsenelle.
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Fencer miekkailja1 = new Fencer(), miekkailija2 = new Fencer();
        miekkailja1.rekisteroi();
        miekkailija2.rekisteroi();
        miekkailja1.tulosta(System.out);
        miekkailja1.vastaaDeiLiberi();
        miekkailja1.tulosta(System.out);

        miekkailija2.vastaaDeiLiberi();
        miekkailija2.tulosta(System.out);

        miekkailija2.vastaaDeiLiberi();
        miekkailija2.tulosta(System.out);
    }

    /**
     * @param text x
     * @return x
     */
    public String setName(String text) {
       this.name = text;
       return null;
        
    }

    /**
     * @param text x
     * @return x
     */
    public String setClub(String text) {
        this.club = text;
        return null;
    }

    /**
     * @param text x
     * @return x
     */
    public String setCountry(String text) {
        this.country = text;
        return null;
        
    }



    

}
