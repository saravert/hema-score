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
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

/**
 * @author Sara
 * @version 6.4.2022
 *
 */
public class Fencers implements Iterable<Fencer> {
    
    private static final int MAX_FENCERS   = 10;
    private boolean muutettu = false;
    private int lkm = 0;
    private String kokoNimi = "";
    private String tiedostonPerusNimi = "miekkailijat";
    private Fencer alkiot[] = new Fencer[MAX_FENCERS];

    /**
     * Oletusmuodostaja
     */
    public Fencers() {
        // Attribuuttien oma alustus riittää
    }

    
    /**
     * Lisää uuden jäsenen tietorakenteeseen.  Ottaa jäsenen omistukseensa.
     * @param fencer lisätäävän jäsenen viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     */
    public void lisaa(Fencer fencer) throws SailoException {
        if (lkm >= alkiot.length) throw new SailoException("Liikaa alkioita");
        alkiot[lkm] = fencer;
        lkm++;
        muutettu = true;
    }
    
    /**
     * Palauttaa viitteen i:teen jäseneen.
     * @param id k
     * @return viite jäseneen, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    public Fencer anna(int id) {
       for(int i = 0; i < lkm; i++) {
           if(alkiot [i] == null)
               continue;
           if(alkiot[i].getIdNum() == id)
               return alkiot[i];
       }
        return null;
    }
    
    /**
     * Korvaa jäsenen tietorakenteessa.  Ottaa jäsenen omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva jäsen.  Jos ei löydy,
     * niin lisätään uutena jäsenenä.
     * @param fencer lisätäävän jäsenen viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä<pre name="test">
     */
    public void korvaaTaiLisaa(Fencer fencer) throws SailoException {
        int id = fencer.getIdNum();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getIdNum() == id ) {
                alkiot[i] = fencer;
                muutettu = true;
                return;
            }
        }
        lisaa(fencer);
    }

    
    /**
     * Palauttaa kerhon jäsenten lukumäärän
     * @return jäsenten lukumäärä
     */
    public int getLkm() {
        return lkm;
    }
    
    /**
     * Tallentaa miekkailijat tiedostoon.  Kesken.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void save() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); // if .. System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Fencer fencer : alkiot) {  
                if(fencer == null)
                    continue;
                fo.println(fencer.toString());
            }
            //} catch ( IOException e ) { // ei heitä poikkeusta
            //  throw new SailoException("Tallettamisessa ongelmia: " + e.getMessage());
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;

    }
    
    
    /**
     * Lukee jäsenistön tiedostosta. 
     * @param tied tiedoston perusnimi
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String tied) throws SailoException{
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
 //          kokoNimi = fi.readLine();
 //           if ( kokoNimi == null ) throw new SailoException("Kerhon nimi puuttuu");
 //           String rivi = fi.readLine();
  //          if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");
            // int maxKoko = Mjonot.erotaInt(rivi,10); // tehdään jotakin
            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';') continue;
                Fencer fencer = new Fencer();
                fencer.parse(rivi); // voisi olla virhekäsittely
                lisaa(fencer);
            }
            muutettu = false;
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }

    }
    
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }

    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }
    
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }

    /**
     * Palauttaa Turnauksen koko nimen
     * @return Turnauksen koko nimi merkkijononna
     */
    public String getKokoNimi() {
        return kokoNimi;
    }

    /**
     * Luokka jäsenten iteroimiseksi.
     */
    public class FencersIterator implements Iterator<Fencer> {
        private int kohdalla = 0;
        
        /**
         * Onko olemassa vielä seuraavaa jäsentä
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä jäseniä
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava jäsen
         * @return seuraava jäsen
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Fencer next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }
    
    /**
     * Palautetaan iteraattori jäsenistään.
     * @return jäsen iteraattori
     */
   @Override
    public Iterator<Fencer> iterator() {
        return new FencersIterator();
    }
    
    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien jäsenten viitteet 
     * @param hakuehto hakuehto 
     * @param k etsittävän kentän indeksi  
     * @return tietorakenteen löytyneistä jäsenistä
     */ 
    public Collection<Fencer> etsi(String hakuehto, int k) { 
        String ehto = "*"; 
        if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto; 
        int hk = k; 
        if ( hk < 0 ) hk = 1;

        Collection<Fencer> loytyneet = new ArrayList<Fencer>(); 
        for (int i = 0; i < getLkm(); i++) {  
            if (WildChars.onkoSamat(alkiot[i].anna(hk), ehto)) loytyneet.add(alkiot[i]);   

        } 
        return loytyneet; 
    }

    /** 
     * Poistaa jäsenen jolla on valittu tunnusnumero  
     * @param id poistettavan jäsenen tunnusnumero 
     * @return 1 jos poistettiin, 0 jos ei löydy 
     *  @example 
     */ 
    public int poista(int id) { 
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    } 
    
    /** 
     * Etsii jäsenen id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return jäsen jolla etsittävä id tai null 
     */ 
    public Fencer annaId(int id) { 
        for (Fencer fencer : this) { 
            if (id == fencer.getIdNum()) return fencer; 
        } 
        return null; 
    } 


    /** 
     * Etsii jäsenen id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return löytyneen jäsenen indeksi tai -1 jos ei löydy 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Fencers jasenet = new Fencers(); 
     * Fencer aku1 = new Fencer(), aku2 = new Fencer(), aku3 = new Fencer(); 
     * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
     * int id1 = aku1.getIdNum(); 
     * jasenet.lisaa(aku1); jasenet.lisaa(aku2); jasenet.lisaa(aku3); 
     * jasenet.etsiId(id1+1) === 1; 
     * jasenet.etsiId(id1+2) === 2; 
     * </pre>

     */ 
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getIdNum()) return i; 
        return -1; 
    }



    /**
     * Testiohjelma miekkailijoille
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Fencers fencers = new Fencers();

        Fencer miekkailija1 = new Fencer(), miekkailija2 = new Fencer();
        miekkailija1.rekisteroi();
        miekkailija1.vastaaDeiLiberi();
        miekkailija2.rekisteroi();
        miekkailija2.vastaaDeiLiberi();

        try {
            fencers.lisaa(miekkailija1);
            fencers.lisaa(miekkailija2);

            System.out.println("============= Miekkailijat testi =================");

            int i = 0;
            for (Fencer fencer: fencers) { 
                System.out.println("Jäsen nro: " + i++);
                fencer.tulosta(System.out);

                fencer.tulosta(System.out);
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }

 

}
