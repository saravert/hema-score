package turnaus;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

import static turnaus.Fencer.rand;

/**
 * @author Sara
 * @version 8.4.2022
 *
 */
public class Match implements Cloneable{
    private int idNum;
    private int idCompetitor;
    private int idOpponent;
  //  private String competitor;
   // private String opponent;
    private int competitorPoints;
    private int opponentPoints;
    
    private static int seuraavaNro = 1;
    
    /**
     * 
     */
    public Match() {
        //
    }
    
    /**
     * @param idCompetitor x
     */
    public Match(int idCompetitor) {
        this.idCompetitor = idCompetitor;
    }
    
    /**
     * @return vastustajan nimen
     */
    public int getOpponent() {
        return idOpponent;
    }
    
    /**
     * @return matsin kenttien lukumäärä
     */
    public int getKenttia() {
        return 5;
    }
    
    /**
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     */
    public int ekaKentta() {
        return 2;
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
                return "idCompetitor";
            case 2:
                return "Opponent";
            case 3:
                return "Competitor Points";
            case 4:
                return "Opponent Points";
            default:
                return "???";
        }
    }
    
    /**
     * @param k Minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     *  @example
     * <pre name="test">
     *   Match har = new Match();
     *   har.parse("   2   |  1  |   2  | 15 | 9 ");
     *   har.anna(0) === "2";   
     *   har.anna(1) === "1";   
     *   har.anna(2) === "2";   
     *   har.anna(3) === "15";   
     *   har.anna(4) === "9";   
     * </pre>

     */
    public String anna(int k) {
        switch (k) {
            case 0:
                return "" + idNum;
            case 1:
                return "" + idCompetitor;
            case 2:
                return "" + idOpponent;
            case 3:
                return "" + competitorPoints;
            case 4:
                return "" + opponentPoints;
            default:
                return "???";
        }
    }

    /**
     * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k minkä kentän sisältö asetetaan
     * @param s asetettava sisältö merkkijonona
     * @return null jos ok, muuten virheteksti
     * @example
     * <pre name="test">
     *   Match har = new Match();
     *   har.aseta(3,"kissa") === "competitorPoints: Ei kokonaisluku";
     *   har.aseta(3,"1940")  === null;
     *   har.aseta(4,"kissa") === "opponentPoints: Ei kokonaisluku";
     *   har.aseta(4,"20")    === null;
     * </pre>

     */
    public String aseta(int k, String s) {
        String st = s.trim();
        StringBuffer sb = new StringBuffer(st);
        switch (k) {
            case 0:
                setIdNum(Mjonot.erota(sb, '$', getIdNum()));
                return null;
            case 1:
                idCompetitor = Mjonot.erota(sb, '$', idCompetitor);
                return null;
            case 2:
                idOpponent = Mjonot.erota(sb, '$', idOpponent);
                return null;
            case 3:
                try {
                    competitorPoints = Mjonot.erotaEx(sb, '§', competitorPoints);
                } catch (NumberFormatException ex) {
                    return "competitorPoints: Ei kokonaisluku";
                }
                return null;

            case 4:
                try {
                    opponentPoints = Mjonot.erotaEx(sb, '§', opponentPoints);
                } catch (NumberFormatException ex) {
                    return "opponentPoints: Ei kokonaisluku";
                }
                return null;

            default:
                return "Väärä kentän indeksi";
        }
    }


    
    /**
     * @return miekkailijan pisteet
     */
    public String getCompetitorPoints() {
       String point = String.valueOf(competitorPoints);
       return point;
    }
    
    /**
     * @return vastustajan pisteet
     */
    public String getOpponentPoints() {
        String point = String.valueOf(opponentPoints);
        return point;
    }
    
    /**
     * Palautetaan mille miekkailijalle ottelu kuuluu
     * @return miekkailijan id
     */
    public int getIdCompetitor() {
        return idCompetitor;
    }
    
    /**
     * Palautetaan ottelun oma id
     * @return ottelun id
     */
    public int getIdNum() {
        return idNum;
    }
    
    /**
     * Tehdään identtinen klooni ottelusta
     * @return Object kloonattu ottelu
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Match har = new Match();
     *   har.parse("   2   |  1  |   2  | 10 | 5 ");
     *   Match kopio = har.clone();
     *   kopio.toString() === har.toString();
     *   har.parse("   1   |  2  |   1  | 8 | 9 ");
     *   kopio.toString().equals(har.toString()) === false;
     * </pre>

     */
    @Override
    public Match clone() throws CloneNotSupportedException { 
        return (Match)super.clone();
    }

    
    /**
     * Antaa ottelulle seuraavan tunnusnumeron
     * @return ottelun uusi tunnus_nro
     * @example
     * <pre name="test">
     *   Match match1 = new Match();
     *   match1.getIdNum() === 0;
     *   match1.rekisteroi();
     *   Match match2 = new Match();
     *   match2.rekisteroi();
     *   int n1 = match1.getIdNum();
     *   int n2 = match2.getIdNum();
     *   n1 === n2-1;
     * </pre>

     */
    public int rekisteroi() {
        idNum = seuraavaNro;
        seuraavaNro++;
        return idNum;
    }
    
    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot ottelulle.
     * Pisteet arvotaan, jotta kahdella ottelulla ei olisi
     * samoja tietoja.
     * @param nro viite miekkailijaan, jonka ottelusta on kyse
     */
    public void vastaaEsimOttelu(int nro) {
        idCompetitor = nro;
        idOpponent = getOpponent();
       // competitor = "Dei Liberi Fiore";
       // opponent = "Liechtenauer Johannes";
        competitorPoints = rand(0, 20);
        opponentPoints = rand(0, 20);
    }


    
    /**
     * Tulostetaan ottelun tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(idCompetitor + " " + idOpponent + " " + competitorPoints + " " + opponentPoints);
    }


    /**
     * Tulostetaan miekkailijan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Testiohjelma Otteluille.
     * @param args ei käytössä
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setIdNum(int nr) {
        idNum = nr;
        if ( idNum >= seuraavaNro ) seuraavaNro = idNum + 1;
    }
    
    /**
     * Palauttaa ottelun tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return otelu tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Match match = new Match();
     *   match.parse("   2   |  1  |   3  | 10 | 20 ");
     *   match.toString()    === "2|1|3|10|20";
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
     * Selvitää ottelun tiedot | erotellusta merkkijonosta.
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusnro.
     * @param rivi josta ottelun tiedot otetaan
     * @example
     * <pre name="test">
     *   Match match = new Match();
     *   match.parse("   2   |  1  |   2  | 10 | 5 ");
     *   match.getIdCompetitor() === 1;
     *   match.toString()    === "2|1|2|10|5";
     *   
     *   match.rekisteroi();
     *   int n = match.getIdNum();
     *   match.parse(""+(n+20));
     *   match.rekisteroi();
     *   match.getIdNum() === n+20+1;
     *   match.toString()     === "" + (n+20+1) + "|1|2|10|5";
     * </pre>

     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));

    }
    
    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) return false;
        return this.toString().equals(obj.toString());
    }
    

    @Override
    public int hashCode() {
        return idNum;
    }


    
    /**
     * Testiohjelma Ottelulle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Match match = new Match();
        match.vastaaEsimOttelu(2);
        match.tulosta(System.out);
    }

    
}
