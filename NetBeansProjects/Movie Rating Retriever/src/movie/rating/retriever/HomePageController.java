/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movie.rating.retriever;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author sandukuttan
 */
public class HomePageController implements Initializable {

    @FXML
    private Button browse;
    
    @FXML
    private TextField directory;
    
    @FXML
    private TableView table;
    
    @FXML
    protected void browseFolders(){
        FileChooser fc=new FileChooser();
        File selectedFile  = fc.showOpenDialog(null);
        fc.setTitle("Choose Movie Directory");
        fc.setInitialDirectory(new File("/home/sandukuttan/anaconda"));
        if(selectedFile!=null){
            String dir=selectedFile.getPath();
            directory.setText(dir);
        }
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
