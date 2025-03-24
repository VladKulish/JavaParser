package com.project.parsing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.parsing.Parser;
import com.project.parsing.db.WriteToDB;
import com.project.parsing.excel.WriteToExcel;
import com.project.parsing.model.Unit;
import com.project.parsing.service.ApplicationService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class AppController {
    public static final String BaseURL = "https://www.work.ua/jobs-it-";
    public static String ProductQuery = "";
    public static String FinalURL=BaseURL + ProductQuery.replace(" ","");
    public static int JobCounter = 0;
    public static List<Unit> Result;
    public static Parser parser = new Parser();
    public static WriteToDB DBWriter = new WriteToDB();
    public static WriteToExcel ExcelWriter = new WriteToExcel();
    public static ApplicationService service = new ApplicationService(new RestTemplate(), new ObjectMapper());
    public Text dollarToGrn;
    public Text EuroToGrn;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button PrevBtn;

    @FXML
    private Button SrchBtn;

    @FXML
    private Button WriteToExcelBtn;

    @FXML
    private Button WriteToDBBtn;

    @FXML
    private TextArea aboutJobTextField;

    @FXML
    private TextArea jobNameTextField;

    @FXML
    private Button nextBtn;

    @FXML
    private TextArea salaryTextField;

    @FXML
    private TextField srchField;

    @FXML
    void initialize() {
        SrchBtn.setOnAction(actionEvent -> { // Search for jobs
            if(!srchField.getText().isEmpty()){
                ProductQuery = srchField.getText().replace(" ","+");
                FinalURL = BaseURL + ProductQuery.replace(" ","");
                Result = parser.ParseURLs(FinalURL);
                Platform.runLater(()->{
                    jobNameTextField.appendText(Result.get(JobCounter).getJobName());
                    aboutJobTextField.appendText(Result.get(JobCounter).getAboutJob());
                    salaryTextField.appendText(Result.get(JobCounter).getJobSalaryRange());
                });
            }
        });
        nextBtn.setOnAction(actionEvent -> { // Next job
            if(JobCounter+1 <= Result.size()-1){
                JobCounter++;
                jobNameTextField.clear();
                aboutJobTextField.clear();
                salaryTextField.clear();
                jobNameTextField.appendText(Result.get(JobCounter).getJobName());
                aboutJobTextField.appendText(Result.get(JobCounter).getAboutJob());
                salaryTextField.appendText(Result.get(JobCounter).getJobSalaryRange());
            }else{
                System.out.println("Out of range");
            }
        });
        PrevBtn.setOnAction(actionEvent -> { // Previous job
            if(JobCounter-1 >= 0){
                JobCounter--;
                jobNameTextField.clear();
                aboutJobTextField.clear();
                salaryTextField.clear();
                jobNameTextField.appendText(Result.get(JobCounter).getJobName());
                aboutJobTextField.appendText(Result.get(JobCounter).getAboutJob());
                salaryTextField.appendText(Result.get(JobCounter).getJobSalaryRange());
            }else{
                System.out.println("Out of range");
            }
        });
        WriteToExcelBtn.setOnAction(actionEvent -> { // Create or replace excel file with data
            if(!Result.isEmpty()){
                ExcelWriter.toExcel(Result);
            }else{
                System.out.println("Empty array.");
            }
        });
        WriteToDBBtn.setOnAction(actionEvent -> { // Add jobs to database
            if(!Result.isEmpty()){
                DBWriter.saveUnitsToDatabase(Result);
            }else{
                System.out.println("Empty array.");
            }
        });
        loadExchangeRate();
    }
    public void loadExchangeRate() { // Get exchange rate from PrivatBank api
        new Thread(()->{
            List<String> exchRate = new ArrayList<>();
            try {
                exchRate=service.fetchDataFromURL("https://api.privatbank.ua/p24api/pubinfo?exchange&coursid=5");
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            List<String> finalExchRate = exchRate;
            Platform.runLater(()->{
                EuroToGrn.setText(finalExchRate.get(0));
                dollarToGrn.setText(finalExchRate.get(1));
            });
        }).start();
    }
}

