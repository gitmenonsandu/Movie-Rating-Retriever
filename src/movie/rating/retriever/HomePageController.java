/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movie.rating.retriever;


import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import org.omg.CORBA.NameValuePair;



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
    protected void browseFolders() throws IOException{
        DirectoryChooser fc=new DirectoryChooser();
        File selectedDirectory=fc.showDialog(null);
        if(selectedDirectory!=null){
            directory.setText(selectedDirectory.getPath());
            
            movieData.clear();
            File[] allFiles=selectedDirectory.listFiles();
            movieNames=new ArrayList<>();
            movieSize=new ArrayList<>();
            movieDirectory=new ArrayList<>();
            movieRating=new ArrayList<>();
            
            getAllMovieNames(allFiles,movieNames,movieSize,movieDirectory);
            for(int i=0;i<movieNames.size();++i){
                ObservableList row=FXCollections.observableArrayList();
                Integer slNo=i+1;
                
                row.addAll(slNo.toString(),movieNames.get(i),movieSize.get(i).toString(),"-","",movieDirectory.get(i));
                
                movieData.add(row);
            }
            table.setItems(movieData);
            t.start();
        }
    }
    @FXML
    protected void handleGetRating(){
        
    }
    
    @FXML
    protected void handleSearch(){
        String searchString=searchBar.getText();
        if(searchString==null)
            table.setItems(movieData);
        else{
            ObservableList<ObservableList> searchData=FXCollections.observableArrayList();
            for(ObservableList row : movieData){
                if(row.get(1).toString().toLowerCase().contains(searchString.toLowerCase()))
                    searchData.add(row);
            }
            table.setItems(searchData);
            
        }
    }
    
    ArrayList<String> movieNames;
    ArrayList<String> movieRating;
    ArrayList<Double> movieSize;
    ArrayList<String> movieDirectory;
    ObservableList<ObservableList> movieData;
    JSONObject obj;
    String omdbUrl="http://www.omdbapi.com/?t=&y=&plot=short&r=json";

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
        movieRating=new ArrayList<>();
        movieSize=new ArrayList<>();
        movieDirectory=new ArrayList<>();
        String colName=null;
        
        
        for(int i=0;i<6;++i){
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
                case 4:
                    colName="Play";
                    break;
                case 5:
                    colName="Directory";
                    break;
            }
            TableColumn col=new TableColumn(colName);
            if(i==1)
                col.setMinWidth(370);
            else if(i==5)
                col.setVisible(false);
            col.setEditable(true);
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {                                                                                              
                    return new SimpleStringProperty(param.getValue().get(j).toString());                        
                }                    
            });
            
            if(i==4)
            col.setCellFactory(new Callback<TableColumn<ObservableList,String>,TableCell<ObservableList,String>>(){
                           @Override
                           public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {
                               final TableCell<ObservableList,String> cell=new TableCell<>();
                               cell.setId(cell.getStyle());
                               cell.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, new EventHandler<javafx.scene.input.MouseEvent>(){
                                   
                                   @Override
                                   public void handle(javafx.scene.input.MouseEvent event) {
                                      if(event.getClickCount()==2){
                                        String fileDir=cell.getTableView().getItems().get(cell.getIndex()).get(5).toString();
                                        
                                        System.out.println(fileDir);
                                        
                                        
                                      }
                                    }
                                   
                               });
                               
                               cell.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, new EventHandler<javafx.scene.input.MouseEvent>(){
                                   @Override
                                   public void handle(javafx.scene.input.MouseEvent event) {
                                       try{
                                       if(!cell.getTableView().getItems().get(cell.getIndex()).isEmpty())
                                       cell.setStyle("-fx-background-color:orange");
                                       cell.setText("Play |>");
                                       }
                                       catch(Exception e){
                                           System.out.println(e);
                                       }
                                    }
                                   
                               });
                               cell.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_EXITED, new EventHandler<javafx.scene.input.MouseEvent>(){
                                   @Override
                                   public void handle(javafx.scene.input.MouseEvent event) {
                                       cell.setStyle(cell.getId());
                                       cell.setText("");
                                   }
                                   
                               });
                               return cell;
                           }
                           
                           
                       });

            
            table.getColumns().add(col);
            
        }
    }
    boolean isDigit(int i,String movieName){
        if(movieName.charAt(i)>=48 && movieName.charAt(i)<=(48+9))
            return true;
        return false;
    }
    JSONResponse j=new JSONResponse();
            List<NameValuePair>list=new ArrayList<>();
            Thread t=new Thread(new Runnable() {
                @Override
                public void run() {
                    ObservableList<ObservableList> movieDataWithRating=FXCollections.observableArrayList();
                    movieDataWithRating=movieData;
                    String movie,ratingUrl;
                    for(ObservableList row : movieData){
                        ObservableList newRow=row;
                        movie=row.get(1).toString().toLowerCase();
                        Integer start=0,end=0;
                        movie=movie.substring(0, movie.length()-3);
                        movie=movie.replace(' ','.');
                        
                        for(int i=0;i<movie.length();++i){
                            end++;
                            if(movie.charAt(i)=='-')
                                break;
                            if(i+4<movie.length())
                                if(isDigit(i+1,movie) && isDigit(i+2,movie) && isDigit(i+3,movie) && isDigit(i+4,movie))
                                    break;
                            
                        }
                        movie=movie.substring(start, end-1);
                        
                        ratingUrl=omdbUrl;
                        ratingUrl=ratingUrl.replace("?t=", "?t="+movie);
                        
                        
                        try {
                            obj = j.makeHttpRequest(ratingUrl, "GET", null);
                            newRow.set(3, obj.get("imdbRating"));
                            movieDataWithRating.set(movieData.indexOf(row), newRow);
                        } catch (Exception ex) {
                            System.out.println(ratingUrl);
                            movieDataWithRating.set(movieData.indexOf(row), row);
                            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        table.setItems(movieDataWithRating);
                    }
                    table.setItems(movieDataWithRating);
                    
                }
                
            });
    private void getAllMovieNames(File[] allFiles,ArrayList<String> movieNames,ArrayList<Double> movieSize,ArrayList<String> movieDirectory) {
        String movie=null;
        Double size=null;

        for(int i=0;i<allFiles.length;++i){
            if(allFiles[i].isDirectory())
                getAllMovieNames(allFiles[i].listFiles(), movieNames,movieSize,movieDirectory);
            else{
                movie=allFiles[i].getName();
                size=allFiles[i].length()/Math.pow(2, 20);
                if(movie.endsWith(".mp4") || movie.endsWith(".avi") || movie.endsWith(".mkv") || movie.endsWith(".flv"))
                    if(!movie.toLowerCase().contains("sample") && !(size<100)){
                        
                        movieNames.add(movie);
                        movieSize.add(size);
                        movieDirectory.add(allFiles[i].getPath());
                    }
            }
        }
        
    }
    
}
