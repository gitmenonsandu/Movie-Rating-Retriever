/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movie.rating.retriever;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author sandukuttan
 */
public class HomePageController implements Initializable {

    @FXML
    private Button browse;
    
    @FXML
    private Button getRating;
    
    @FXML
    private TextField directory;
    
    @FXML
    private TableView table;
    
    @FXML
    private TextField searchBar;
    
    @FXML
    protected void browseFolders(){
        DirectoryChooser fc=new DirectoryChooser();
        Window ownerWindow = null;
        File selectedDirectory=fc.showDialog(ownerWindow);
        if(selectedDirectory!=null){
            directory.setText(selectedDirectory.getPath());
        }
        
        File[] allFiles=selectedDirectory.listFiles();
        movieNames=new ArrayList<>();
        movieSize=new ArrayList<>();
        getAllMovieNames(allFiles,movieNames,movieSize);
        for(int i=0;i<movieNames.size();++i){
            ObservableList row=FXCollections.observableArrayList();
            Integer slNo=i+1;
            row.addAll(slNo.toString(),movieNames.get(i),movieSize.get(i).toString(),"-");
            movieData.add(row);
        }
        table.setItems(movieData);
        
    }
    @FXML
    protected void handleGetRating(){
        
    }
    ArrayList<String> movieNames;
    ArrayList<Double> movieSize;
    ObservableList<ObservableList> movieData;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        movieData=FXCollections.observableArrayList();
        movieNames=new ArrayList<>();
        movieSize=new ArrayList<>();
        String colName=null;
        for(int i=0;i<4;++i){
            final int j=i;
            switch (i) {
                case 0:
                    colName="Sl.No";
                    break;
                case 1:
                    colName="Movie Name";
                    break;
                case 2:
                    colName="Size";
                    break;
                case 3:
                    colName="Rating";
                    break;
            }
            TableColumn col=new TableColumn(colName);
            if(i==1)
                col.setMinWidth(400);
            col.setEditable(true);
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {                                                                                              
                    return new SimpleStringProperty(param.getValue().get(j).toString());                        
                }                    
            });
            table.getColumns().add(col);
            
        }
    }    

    private void getAllMovieNames(File[] allFiles,ArrayList<String> movieNames,ArrayList<Double> movieSize) {
        System.out.println(allFiles.length);
        String movie=null;
        Double size=null;
        for(int i=0;i<allFiles.length;++i){
            if(allFiles[i].isDirectory())
                getAllMovieNames(allFiles[i].listFiles(), movieNames,movieSize);
            else{
                movie=allFiles[i].getName();
                size=allFiles[i].length()/Math.pow(2, 20);
                if(movie.endsWith(".mp4") || movie.endsWith(".avi") || movie.endsWith(".mkv") || movie.endsWith(".flv"))
                    if(!movie.toLowerCase().contains("sample") && !(size<100)){
                        movieNames.add(movie);
                        movieSize.add(size);
                    }
            }
        }
        
    }
    
}
