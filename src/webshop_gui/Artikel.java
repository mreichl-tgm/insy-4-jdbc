package webshop_gui;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Artikel {
        private final SimpleStringProperty abez;
        private final SimpleFloatProperty preis;
        private final SimpleIntegerProperty vstueckz;
        private final SimpleStringProperty ainfo;
        private final SimpleIntegerProperty anr;

        
        Artikel(int anr, String abez, String ainfo, Float preis, int vstueckz) {
        	this.anr = new SimpleIntegerProperty(anr);
        	this.abez = new SimpleStringProperty(abez);
            this.ainfo = new SimpleStringProperty(ainfo);
            this.preis = new SimpleFloatProperty(preis);
            this.vstueckz = new SimpleIntegerProperty(vstueckz);
        }
 
        public String getAbez() {
            return abez.get();
        }
 
        public void setAbez(String abez) {
            this.abez.set(abez);
        }
 
        public String getAinfo() {
            return ainfo.get();
        }
 
        public void setAinfo(String ainfo) {
        	this.ainfo.set(ainfo);
        }
 
        public Float getPreis() {
            return preis.get();
        }
 
        public void setPreis(float preis) {
            this.preis.set(preis);
        }
        
        public int getVstueckz() {
            return vstueckz.get();
        }
 
        public void setVstueckz(int vstueckz) {
            this.vstueckz.set(vstueckz);
        }

        public int getAnr() {
            return anr.get();
        }
 
        public void setAnr(int anr) {
            this.anr.set(anr);
        }
        
        
 
} 