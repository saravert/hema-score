package turnaus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Sara
 * @version 8.4.2022
 *
 */
public class Matches implements Iterable<Match> {

    private boolean muutettu = false;
    private String tiedostonPerusNimi = "";

    
    /** Taulukko otteluista */
    private final List<Match> alkiot = new ArrayList<Match>();

    /**
     * otteluiden alustaminen
     */
    public Matches() {
        // ei tee vielä mitään 
    }
    
    /**
     * Korvaa ottelun tietorakenteessa.  Ottaa ottelun omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva ottelu.  Jos ei löydy,
     * niin lisätään uutena otteluna.
     * @param match lisättävän ottelun viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Matches matches = new Matches();
     * Match har1 = new Match(), har2 = new Match();
     * har1.rekisteroi(); har2.rekisteroi();
     * matches.getLkm() === 0;
     * matches.korvaaTaiLisaa(har1); matches.getLkm() === 1;
     * matches.korvaaTaiLisaa(har2); matches.getLkm() === 2;
     * Match har3 = har1.clone();
     * har3.aseta(2,"kkk");
     * Iterator<Match> i2=matches.iterator();
     * i2.next() === har1;
     * matches.korvaaTaiLisaa(har3); matches.getLkm() === 2;
     * i2=matches.iterator();
     * Match h = i2.next();
     * h === har3;
     * h == har3 === true;
     * h == har1 === false;
     * </pre>

     */ 
    public void korvaaTaiLisaa(Match match) throws SailoException {
        int id = match.getIdNum();
        for (int i = 0; i < getLkm(); i++) {
            if (alkiot.get(i).getIdNum() == id) {
                alkiot.set(i, match);
                muutettu = true;
                return;
            }
        }
        lisaa(match);
    }

    
    /**
     * Lisää uuden ottelun tietorakenteeseen.  Ottaa ottelun omistukseensa.
     * @param match lisättävä ottelu.  Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Match match) {
        alkiot.add(match);
        muutettu = true;
    }
    
    /**
     * Poistaa valitun harrastuksen
     * @param match poistettava harrastus
     * @return tosi jos löytyi poistettava tietue 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Matches matches = new Matches();
     *  Match pitsi21 = new Match(); pitsi21.vastaaEsimOttelu(2);
     *  Match pitsi11 = new Match(); pitsi11.vastaaEsimOttelu(1);
     *  Match pitsi22 = new Match(); pitsi22.vastaaEsimOttelu(2); 
     *  Match pitsi12 = new Match(); pitsi12.vastaaEsimOttelu(1); 
     *  Match pitsi23 = new Match(); pitsi23.vastaaEsimOttelu(2); 
     *  matches.lisaa(pitsi21);
     *  matches.lisaa(pitsi11);
     *  matches.lisaa(pitsi22);
     *  matches.lisaa(pitsi12);
     *  matches.poista(pitsi23) === false ; matches.getLkm() === 4;
     *  matches.poista(pitsi11) === true;   matches.getLkm() === 3;
     *  List<Match> h = matches.annaMatches(1);
     *  h.size() === 1; 
     *  h.get(0) === pitsi12;
     * </pre>

     */
    public boolean poista(Match match) {
        boolean ret = alkiot.remove(match);
        if (ret) muutettu = true;
        return ret;
    }

    
    /**
     * Poistaa kaikki tietyn tietyn jäsenen harrastukset
     * @param tunnusNro viite siihen, mihin liittyvät tietueet poistetaan
     * @return montako poistettiin 
     * @example
     * <pre name="test">
     *  Matches matches = new Matches();
     *  Match pitsi21 = new Match(); pitsi21.vastaaEsimOttelu(2);
     *  Match pitsi11 = new Match(); pitsi11.vastaaEsimOttelu(1);
     *  Match pitsi22 = new Match(); pitsi22.vastaaEsimOttelu(2); 
     *  Match pitsi12 = new Match(); pitsi12.vastaaEsimOttelu(1); 
     *  Match pitsi23 = new Match(); pitsi23.vastaaEsimOttelu(2); 
     *  matches.lisaa(pitsi21);
     *  matches.lisaa(pitsi11);
     *  matches.lisaa(pitsi22);
     *  matches.lisaa(pitsi12);
     *  matches.lisaa(pitsi23);
     *  matches.removeFencersMatches(2) === 3;  matches.getLkm() === 2;
     *  matches.removeFencersMatches(3) === 0;  matches.getLkm() === 2;
     *  List<Match> h = matches.annaMatches(2);
     *  h.size() === 0; 
     *  h = matches.annaMatches(1);
     *  h.get(0) === pitsi11;
     *  h.get(1) === pitsi12;
     * </pre>

     */
    public int removeFencersMatches(int tunnusNro) {
        int n = 0;
        for (Iterator<Match> it = alkiot.iterator(); it.hasNext();) {
            Match har = it.next();
            if ( har.getIdCompetitor() == tunnusNro ) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }

    
    /**
     * Lukee miekkailijat tiedostosta.  
     * TODO Kesken.
     * @param tied tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {

            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Match match= new Match();
                match.parse(rivi); // voisi olla virhekäsittely
                lisaa(match);
            }
            muutettu = false;

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }

    }


    /**
     * Tallentaa miekkailjat tiedostoon.  
     * TODO Kesken.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void save() throws SailoException {
        //if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); //  if ... System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); //  if ... System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Match match : this) {  
                fo.println(match.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }

    
    /**
     * Palauttaa turnauksen otteluiden lukumäärän
     * @return otteluiden lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }

    /**
     * Iteraattori kaikkien matsien läpikäymiseen
     * @return matsi-iteraattori
     * 
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     *  Matches matches = new Matches();
     *  Match pitsi21 = new Match(2); matches.lisaa(pitsi21);
     *  Match pitsi11 = new Match(1); matches.lisaa(pitsi11);
     *  Match pitsi22 = new Match(2); matches.lisaa(pitsi22);
     *  Match pitsi12 = new Match(1); matches.lisaa(pitsi12);
     *  Match pitsi23 = new Match(2); matches.lisaa(pitsi23);
     * 
     *  Iterator<Match> i2=matches.iterator();
     *  i2.next() === pitsi21;
     *  i2.next() === pitsi11;
     *  i2.next() === pitsi22;
     *  i2.next() === pitsi12;
     *  i2.next() === pitsi23;
     *  i2.next() === pitsi12;  #THROWS NoSuchElementException  
     *  
     *  int n = 0;
     *  int jnrot[] = {2,1,2,1,2};
     *  
     *  for ( Match har:matches ) { 
     *    har.getIdCompetitor() === jnrot[n]; n++;  
     *  }
     *  
     *  n === 5;
     *  
     * </pre>
     */


    @Override
    public Iterator<Match> iterator() {
        return alkiot.iterator();
    }
    
    /**
     * Haetaan kaikki miekkailijan ottelut
     * @param tunnusnro miekkailijan tunnusnumero jolle otteluita haetaan
     * @return tietorakenne jossa viiteet löydetteyihin otteluihin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Matches matches = new Matches();
     *  Match fiorliecht1 = new Match(2); matches.lisaa(fiorliecht1);
     *  Match fiorliecht2 = new Match(1); matches.lisaa(fiorliecht2);
     *  Match fiorliecht3 = new Match(2); matches.lisaa(fiorliecht3);
     *  Match fiorliecht4 = new Match(1); matches.lisaa(fiorliecht4);
     *  Match fiorliecht5 = new Match(2); matches.lisaa(fiorliecht5);
     *  Match fiorliecht6 = new Match(5); matches.lisaa(fiorliecht6);
     *  
     *  List<Match> loytyneet;
     *  loytyneet = matches.annaMatches(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = matches.annaMatches(1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == fiorliecht2 === true;
     *  loytyneet.get(1) == fiorliecht4 === true;
     *  loytyneet = matches.annaMatches(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) == fiorliecht6 === true;
     * </pre> 

     */
    public List<Match> annaMatches(int tunnusnro) {
        List<Match> loydetyt = new ArrayList<Match>();
        for (Match har : alkiot)
            if (har.getIdCompetitor() == tunnusnro) loydetyt.add(har);
        return loydetyt;
    }
    
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }


    /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param tied tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }


    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }

    
    /**
     * Testiohjelma otteluille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Matches matches = new Matches();
        Match match1 = new Match();
        match1.vastaaEsimOttelu(2);
        Match match2 = new Match();
        match2.vastaaEsimOttelu(1);
        Match match3 = new Match();
        match3.vastaaEsimOttelu(2);
        Match match4 = new Match();
        match4.vastaaEsimOttelu(2);

        matches.lisaa(match1);
        matches.lisaa(match2);
        matches.lisaa(match3);
        matches.lisaa(match2);
        matches.lisaa(match4);

        System.out.println("============= Matsit testi =================");

        List<Match> matches2 = matches.annaMatches(2);

        for (Match har : matches2) {
            System.out.print(har.getIdCompetitor() + " ");
            har.tulosta(System.out);
        }

    }


}
