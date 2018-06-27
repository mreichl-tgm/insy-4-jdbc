package webshop_gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class ArtikelAdmin extends Application {
 
    private TableView<Artikel> table = new TableView<Artikel>();
    private ObservableList<Artikel> data;
   
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
    	
    	JDBCClient db = new JDBCClient();    	
    	
    	data = FXCollections.observableArrayList();
    	data.addAll(db.getAllArticles());
    	
        Scene scene = new Scene(new Group());
        stage.setTitle("Artikel Admin");
        stage.setWidth(820);
        stage.setHeight(550);
 
        final Label label = new Label("geführte Artkel");
        label.setFont(new Font("Arial", 20));
 
        table.setEditable(true);
 
        TableColumn<Artikel, Integer> anrCol = new TableColumn<Artikel, Integer>("ANr");
        anrCol.setMinWidth(50);
        anrCol.setCellValueFactory(
                new PropertyValueFactory<Artikel, Integer>("anr"));
        
        TableColumn abezCol = new TableColumn("Bezeichnung");
        abezCol.setMinWidth(200);
        abezCol.setCellValueFactory(
                new PropertyValueFactory<Artikel, String>("abez"));

        abezCol.setCellFactory(TextFieldTableCell.forTableColumn());
        abezCol.setOnEditCommit(		
            new EventHandler<CellEditEvent<Artikel, String>>() {
                @Override
                public void handle(CellEditEvent<Artikel, String> t) {
                    Artikel a = (Artikel) t.getTableView().getItems().get(
                    		t.getTablePosition().getRow()
                    	);
                    a.setAbez(t.getNewValue());
                    db.saveArticle(a);
                }
            }
        );

        
        TableColumn ainfoCol = new TableColumn("Informationen");
        ainfoCol.setMinWidth(250);
        ainfoCol.setCellValueFactory(
                new PropertyValueFactory<Artikel, String>("ainfo"));
        ainfoCol.setCellFactory(TextFieldTableCell.forTableColumn());
        ainfoCol.setOnEditCommit(		
            new EventHandler<CellEditEvent<Artikel, String>>() {
                @Override
                public void handle(CellEditEvent<Artikel, String> t) {
                    Artikel a = (Artikel) t.getTableView().getItems().get(
                    		t.getTablePosition().getRow()
                    	);
                    a.setAinfo(t.getNewValue());
                    db.saveArticle(a);
                }
            }
        );
        
        
        TableColumn preisCol = new TableColumn("Preis");
        preisCol.setMinWidth(100);
        preisCol.setCellValueFactory(
                new PropertyValueFactory<Artikel, Float>("preis"));
        preisCol.setCellFactory(
        	    TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        preisCol.setOnEditCommit(		
                new EventHandler<CellEditEvent<Artikel, Float>>() {
                    @Override
                    public void handle(CellEditEvent<Artikel, Float> t) {
                        Artikel a = (Artikel) t.getTableView().getItems().get(
                        		t.getTablePosition().getRow()
                        	);
                        a.setPreis(t.getNewValue());
                        db.saveArticle(a);
                    }
                }
            );
       
        TableColumn vstueckzCol = new TableColumn("verfügbar");
        vstueckzCol.setMinWidth(130);
        vstueckzCol.setCellValueFactory(
                new PropertyValueFactory<Artikel, Integer>("vstueckz"));
        vstueckzCol.setCellFactory(
        		TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        vstueckzCol.setOnEditCommit(		
            new EventHandler<CellEditEvent<Artikel, Integer>>() {
                @Override
                public void handle(CellEditEvent<Artikel, Integer> t) {
                    Artikel a = (Artikel) t.getTableView().getItems().get(
                    		t.getTablePosition().getRow()
                    	);
                    a.setVstueckz(t.getNewValue());
                    db.saveArticle(a);
                }
            }
        );
 
        table.setItems(data);
        table.getColumns().addAll(anrCol, abezCol, ainfoCol, preisCol, vstueckzCol);
        table.setMinWidth(800);
        
        final TextField addAnr = new TextField();
        addAnr.setPromptText("ANr");
        addAnr.setMaxWidth(anrCol.getPrefWidth());
        final TextField addAbez = new TextField();
        addAbez.setMaxWidth(abezCol.getPrefWidth());
        addAbez.setPromptText("Artikelbezeichnung");
        final TextField addAinfo = new TextField();
        addAinfo.setMaxWidth(ainfoCol.getPrefWidth());
        addAinfo.setPromptText("Info");
        final TextField addPreis = new TextField();
        addPreis.setMaxWidth(preisCol.getPrefWidth());
        addPreis.setPromptText("Preis");
        final TextField addVstueckz = new TextField();
        addVstueckz.setMaxWidth(vstueckzCol.getPrefWidth());
        addVstueckz.setPromptText("verfügbar");
        
        
 
        final Button addButton = new Button("Speichern");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	Artikel a = new Artikel(
                        Integer.parseInt(addAnr.getText()),
                        addAbez.getText(),
                        addAinfo.getText(),
                        Float.parseFloat(addPreis.getText()),
                        Integer.parseInt(addVstueckz.getText()));
                data.add(a);
                db.addArticle(a);
                addAnr.clear();
                addAbez.clear();
                addAinfo.clear();
                addPreis.clear();
                addVstueckz.clear();
                   
            }
        });
 
        final HBox hb = new HBox();

        hb.getChildren().addAll(addAnr, addAbez, addAinfo, addPreis, addVstueckz, addButton);
        hb.setSpacing(3);
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        stage.setScene(scene);
        stage.show();
    	
         
    }
}