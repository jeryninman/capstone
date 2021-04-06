/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capstone;

import Model.ARFF;
import Model.Analytics;
import Model.DataPoint;
import Model.Filter;
import Model.Forecast;
import Model.User;
import Model.ZipLoanChart;
import Model.lineChartData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jeryn
 */
public class DashboardController implements Initializable {
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        hide();
        userLabel.setText(currentUser.getUsername());
        
        ObservableList<String> type = FXCollections.observableArrayList("Business Type", "Corporation", "Limited  Liability Company(LLC)","Subchapter S Corporation", "Non-Profit Organization", "Partnership");
        ObservableList<String> ranges = FXCollections.observableArrayList("Loan Range", "a $5-10 million", "b $2-5 million", "c $1-2 million", "\"d $350,000-1 million\"", "\"e $150,000-350,000\"");
        
        
        //Run data
        runData(data, "src/capstone/archive.csv");
        

        //initialize Forecast   
        
        String fileName = "src/capstone/originalData.arff";
        try{
            ARFF.generateARFF(data, fileName);
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            forecast.setInstances(forecast.getDataSet(fileName));
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        //initialize Import
        boxType.setItems(type);
        boxType.setValue("Business Type");
        boxLoanRange.setItems(ranges);
        boxLoanRange.setValue("Loan Range");
        
        //initialize Query
        dataTableView.setItems(data);
        
        //initialize Filter

        boxTypeFilter.setItems(type);
        boxTypeFilter.setValue("Business Type");
        boxRangeFilter.setItems(ranges);
        boxRangeFilter.setValue("Loan Range");
        
        //initialize Analytics
        analytics.generateTotalsData(data);
        analytics.generatePieData(data);
        analytics.generateLineChartSeries(data);
        hideAnalytics();
        boxPie.setItems(analytics.getZips());
        boxPie.getSelectionModel().selectFirst();
    }    
    
    // variable for data
    private ObservableList<DataPoint> data = FXCollections.observableArrayList();
    private static String log = "Log: \n";
    private User currentUser;
    
    DashboardController(User user){
        this.currentUser = user;
    }
    
    public User getUser(){
        return currentUser;
    }
    
    public void setUser(User user){
        this.currentUser = user;
    }
    public String getLog(){
        return log;
    }
    public void setLog(String log){
        this.log = log;
    }
    
    public ObservableList<DataPoint> getData(){
        return data;
    }
    
    private Forecast forecast = new Forecast();
    
    private  Analytics analytics = new Analytics();
    
    // Menu Bar controls
    
    @FXML
    private AnchorPane homePane;
    
    @FXML
    private AnchorPane queryPane;
    
    @FXML
    private AnchorPane analyticsPane;
    
    @FXML
    private AnchorPane forecastingPane;
    
    @FXML
    private AnchorPane appMetricsPane;
    
    @FXML
    private AnchorPane importPane;
    
    @FXML
    private AnchorPane filterPane;
    
    @FXML
    private Label userLabel; 
    
    // Query Controls
    
    @FXML
    private TableView<DataPoint> dataTableView;
    
    @FXML
    private TableColumn colRange;
    
    @FXML
    private TableColumn colName;
    
    @FXML
    private TableColumn colAddress;
    
    @FXML
    private TableColumn colCity;
    
    @FXML
    private TableColumn colZip;
    
    @FXML 
    private TableColumn colNAICSCode;
    
    @FXML
    private TableColumn colType;
    
    @FXML
    private TableColumn colJobs;
    
    @FXML
    private TableColumn colDate;
    
    
    // Filter Controls
    
    @FXML
    private CheckBox chkRange;
    
    @FXML
    private CheckBox chkName;
    
    @FXML
    private CheckBox chkAddress;
    
    @FXML
    private CheckBox chkCity;
    
    @FXML
    private CheckBox chkZip;
    
    @FXML
    private CheckBox chkType;
    
    @FXML
    private CheckBox chkJobs;
    
    @FXML
    private CheckBox chkDate;
    
    @FXML
    private TextField txtNameFilter;
    
    @FXML
    private TextField txtAddressFilter;
    
    @FXML
    private TextField txtCityFilter;
    
    @FXML
    private TextField txtZipFilter;
    
    @FXML
    private TextField txtJobsFilter;
    
    @FXML
    private TextField txtDateFilter;
    
    @FXML
    private ChoiceBox boxRangeFilter;
    
    @FXML
    private ChoiceBox boxTypeFilter;
    
            
    // Analytics Controls
    
    @FXML 
    private ChoiceBox boxPie;
    
    @FXML
    private PieChart chartPie;
    
    @FXML
    private BarChart<String, Number> chartTotals;
    
    @FXML
    private CategoryAxis xAxis;
    
    @FXML
    private LineChart<Number, Number> chartLine;
    
    @FXML
    private CheckBox chkA;
    
    @FXML
    private CheckBox chkB;
    
    @FXML
    private CheckBox chkC;
    
    @FXML
    private CheckBox chkD;
    
    @FXML
    private CheckBox chkE;
    
    @FXML
    private Pane paneSeries;
    
    // Forecasting Controls
    
    @FXML
    private TextField txtForecastJobs;
    
    @FXML
    private TextField txtForecastZip;
    
    @FXML
    private TextArea txtareaForecast;
    
   
    // Import Data Controls
    
    @FXML 
    private TextField  txtBusiness;
    
    @FXML 
    private TextField  txtAddress;
    
    @FXML 
    private TextField  txtCity;
    
    @FXML 
    private TextField  txtZip;
    
    @FXML 
    private TextField  txtNAICS;
    
    @FXML 
    private TextField  txtJobs;
    
    @FXML 
    private TextField  txtDate;
    
    @FXML 
    private ChoiceBox boxType;
    
    @FXML 
    private ChoiceBox boxLoanRange;
    
    //App Metrics controls
    @FXML
    private TextArea txtAreaLog;
    
    // Menu Bar Methods
    
    public static void runData(ObservableList<DataPoint> data, String file){
        /*
        import csv, skip 1 row(header), only take columns 0(LoanSize) 1(Name) 2(Address) 3 (City) 4 (State) 5(Zip) 6(NAICS) 7(BusinessType) 12(Jobs) 13(date) 
        */
        
        //read in data
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            //skip header
            br.readLine();
            
            // initialize id 
            int id = 1;
            
            while ((line = br.readLine()) != null) {
            
                
                int i = 0;
                int startIndex = 0;
                boolean entry = false;
                String loanSize = null;
                String name = null;
                String address = null;
                String city = null;
                String zip = null;
                String naics = null;
                String type = null;
                String jobs = null;
                String date = null;
                String state = null;
                 
                for (int index = 0; index < line.length(); index++){
                    if (line.charAt(index) == '\"'){
                        entry = !entry; 
                    }
                    else if (line.charAt(index) == ',' && !entry){
                        switch(i){
                            case 0:
                                loanSize = line.substring(startIndex, index);
                            case 1:
                                name = line.substring(startIndex, index);
                            case 2:
                                address = line.substring(startIndex, index);
                            case 3:
                                city = line.substring(startIndex, index);
                            case 4: 
                                state = line.substring(startIndex, index);
                            case 5:
                                zip = line.substring(startIndex, index);
                            case 6:
                                naics = line.substring(startIndex, index);
                            case 7:
                                type = line.substring(startIndex, index);
                            case 12:
                                jobs = line.substring(startIndex, index);
                            case 13:
                                date = line.substring(startIndex, index);
                            default:
                                ;
                        }
                        i++;
                        startIndex = index + 1;
                    }
                }
                
                // Only want data within WA 
                if (state.equals("WA")){
                    DataPoint datapoint = new DataPoint(id, name, address, city, zip, naics, type, loanSize, jobs, date); 
                    data.add(datapoint);
                    id++;
                }
                
                
            }
        
        
         }catch (IOException e){
            String error = "Error loading dataset information." + e.toString();
            log(error);
                 }
        }
    
    @FXML
    private void displayHome(){
        hide();
        homePane.setVisible(true);
    }
    
    @FXML
    private void displayQuery(){
        hide();
        queryPane.setVisible(true);
    }
    
    @FXML
    private void displayFilter(){
        hide();
        filterPane.setVisible(true);
    }
    
    @FXML
    private void displayAnalytics(){
        hide();
        analyticsPane.setVisible(true);
    }
    
    @FXML
    private void displayForecasting(){
        hide();
        forecastingPane.setVisible(true);
    }
    
    @FXML
    private void displayAppMetrics(){
        hide();
        appMetricsPane.setVisible(true);
        printLog();
    }
    
    @FXML
    private void displayImport(){
        hide();
        importPane.setVisible(true);
    }
    
    private void hide(){
        homePane.setVisible(false);
        queryPane.setVisible(false);
        analyticsPane.setVisible(false);
        forecastingPane.setVisible(false);
        appMetricsPane.setVisible(false);
        importPane.setVisible(false);
        filterPane.setVisible(false);
    }
            

    private void commitChanges(ObservableList<DataPoint> data){
        
        try{
            FileWriter write = new FileWriter("src/capstone/archive.csv");
            String delimin = ",";
            String blank = "";
            String datapoint = "";
            // Header line
            write.append("LoanRange");
            write.append(delimin);
            write.append("BusinessName");
            write.append(delimin);
            write.append("Address");
            write.append(delimin);
            write.append("City");
            write.append(delimin);
            write.append("State");
            write.append(delimin);
            write.append("Zip");
            write.append(delimin);
            write.append("NAICSCode");
            write.append(delimin);
            write.append("BusinessType");
            write.append(delimin);
            write.append("RaceEthnicity");
            write.append(delimin);
            write.append("Gender");
            write.append(delimin);
            write.append("Veteran");
            write.append(delimin);
            write.append("NonProfit");
            write.append(delimin);
            write.append("JobsRetained");
            write.append(delimin);
            write.append("DateApproved");
            write.append(delimin);
            write.append("Lender");
            write.append(delimin);
            write.append("CD");
            write.append("\n");
            
            for (DataPoint entry : data){
                // reorder data point for correct order, insert blanks to keep file structure
              
                datapoint = entry.getLoanRange() + delimin + entry.getBusinessName() 
                        + delimin + entry.getAddress() + delimin + entry.getCity() 
                        + delimin + "WA" + delimin + entry.getZip() + delimin 
                        + entry.getNAICSCode() + delimin + entry.getBusinessType()
                        + delimin + blank + delimin + blank + delimin + blank + delimin 
                        + blank + delimin + entry.getJobsRetained() + delimin 
                        + entry.getDateApproved() + delimin + blank + delimin + blank + "\n";
                write.append(datapoint);
                /*
                write.append(String.join(delimin, entry.getLoanRange(), entry.getBusinessName(), 
                        entry.getAddress(), entry.getCity(), blank, entry.getZip(), 
                        entry.getNAICSCode(), entry.getBusinessType(), blank, 
                        blank, blank, blank, entry.getJobsRetained(), entry.getDateApproved(),
                        blank, blank));
                write.append("\n");
                */
            }
            
            write.flush();
            write.close();
            
            
        }catch(Exception e){
            String error = "Failed data commit. " + e.getMessage();
            log(error);
        }
        
    }
    
    @FXML 
    private void logOut() throws IOException{
    
        Alert a = new Alert(AlertType.CONFIRMATION);
        a.setTitle("Confirmation");
        a.setHeaderText("Confirm and Exit");
        a.setContentText("Confirm changes and log out?");
        Optional<ButtonType> result = a.showAndWait();
        
        if (result.get() == (ButtonType.OK)){
            commitChanges(data);
            try{
                String userChange = currentUser.getUsername() + " User has logged out.";
                log(userChange);
                FileWriter write = new FileWriter("src/capstone/log.txt");
                write.append(log);
                write.flush();
                write.close();
                
                System.exit(0);
                
            }catch (Exception e){
                log("Log out failed");
            }
        }
    }
 
    
    // Query Methods
    @FXML
    private void filter(){
        queryPane.setVisible(false);
        filterPane.setVisible(true);
    }
    
    @FXML
    private void resetFilters(){
        dataTableView.setItems(getData());
        colRange.setVisible(true);
        colName.setVisible(true);
        colAddress.setVisible(true);
        colCity.setVisible(true);
        colZip.setVisible(true);
        colNAICSCode.setVisible(true);
        colType.setVisible(true);
        colJobs.setVisible(true);
        colDate.setVisible(true);
        
    } 
    
    // Filter Methods
    
    @FXML
    private void changeTable(){
        //clear table contents, hide all columns, will turn on requested columns 
        dataTableView.setItems(null);
        colRange.setVisible(false);
        colName.setVisible(false);
        colAddress.setVisible(false);
        colCity.setVisible(false);
        colZip.setVisible(false);
        colNAICSCode.setVisible(false);
        colType.setVisible(false);
        colJobs.setVisible(false);
        colDate.setVisible(false);
    
       
        // find requested data
        ObservableList<DataPoint> filteredData = filterData(data);
        dataTableView.setItems(filteredData);
        
        
        //switch to query pane
        filterPane.setVisible(false);
        queryPane.setVisible(true);
        
    }
    
    private boolean dataExists(int id, ObservableList<DataPoint> data){
        boolean exists = false;
        
        for (DataPoint d: data){
            if (d.getBusinessID() == id){
                return true;
            }
        }
            
        return exists;
    }
    
    private ObservableList<DataPoint> filterData(ObservableList<DataPoint> data){
        
        //container for filtered data
        ObservableList<DataPoint> filteredData = FXCollections.observableArrayList();
        //filter variables
        String loanSize = null;
        String name = null;
        String address = null;
        String city = null;
        String zip = null;
        String naics = null;
        String type = null;
        String jobs = null;
        String date = null;
        Filter filter;
        // filter states
        boolean bSize, bName, bAddress, bCity, bZip, bNaics, bType, bJobs, bDate;
        
        //get filters
        if (chkRange.isSelected()){
            colRange.setVisible(true);
            // check for further filter
            if (boxRangeFilter.getSelectionModel().getSelectedItem().equals("Loan Range")){
                //no extra filter, keep everything
            }
            else {
                loanSize = boxRangeFilter.getSelectionModel().getSelectedItem().toString();
            }
        }
        if (chkName.isSelected()){
            colName.setVisible(true);
            // check for further filter
            if (txtNameFilter.getText().isEmpty()){
                //no extra filter, keep everything
            }
            else {
                name = txtNameFilter.getText();
            }
        }
        if (chkAddress.isSelected()){
            colAddress.setVisible(true);
            // check for further filter
            if (txtAddressFilter.getText().isEmpty()){
                //no extra filter, keep everything
            }
            else {
                address = txtAddressFilter.getText();
            }
        }
        if (chkCity.isSelected()){
            colCity.setVisible(true);
            // check for further filter
            if (txtCityFilter.getText().isEmpty()){
                //no extra filter, keep everything
            }
            else {
                city = txtCityFilter.getText();
            }
        }
        if (chkZip.isSelected()){
            colZip.setVisible(true);
            // check for further filter
            if (txtZipFilter.getText().isEmpty()){
                //no extra filter, keep everything
            }
            else {
                zip = txtZipFilter.getText();
            }
        }
        if (chkType.isSelected()){
            colType.setVisible(true);
            // check for further filter
            if (boxTypeFilter.getSelectionModel().getSelectedItem().equals("Business Type")){
                //no extra filter, keep everything
            }
            else {
                type = boxTypeFilter.getSelectionModel().getSelectedItem().toString();
            }
        }
        if (chkJobs.isSelected()){
            colJobs.setVisible(true);
            // check for further filter
            if (txtJobsFilter.getText().isEmpty()){
                //no extra filter, keep everything
            }
            else {
                jobs = txtJobsFilter.getText();
            }
        }
        if (chkDate.isSelected()){
            colDate.setVisible(true);
            // check for further filter
            if (txtDateFilter.getText().isEmpty()){
                //no extra filter, keep everything
            }
            else {
                date = txtDateFilter.getText();
            }
        }
        
        filter = new Filter(loanSize, name, address, city, zip, naics, type, jobs, date);
        
        for (DataPoint filtData: data){
            //reset states
            bSize = false; 
            bName = false; 
            bAddress = false; 
            bCity = false;
            bZip = false; 
            bNaics = false; 
            bType = false; 
            bJobs = false; 
            bDate = false; 
            
            if (filter.getLoanSize() != null){
                if (filtData.getLoanRange().contains(filter.getLoanSize())){
                    bSize = true;
                }
            }
            else 
                bSize = true;
            
            if (filter.getName() != null){
                if (filtData.getBusinessName().contains(filter.getName())){
                    bName = true;
                }
            }
            else
                bName = true;
            
            if (filter.getAddress() != null){
                if (filtData.getAddress().contains(filter.getAddress())){
                    bAddress = true;
                }
            }
            else
                bAddress = true;
            
            if (filter.getCity() != null){
                if (filtData.getCity().contains(filter.getCity())){
                    bCity = true;
                }
            }
            else
                bCity = true;
            
            if (filter.getZip() != null){
                if (filtData.getZip().contains(filter.getZip())){
                    bZip = true;
                }
            }
            else
                bZip = true;
            
            if (filter.getNaics() != null){
                if (filtData.getNAICSCode().contains(filter.getNaics())){
                    bNaics = true;
                }
            }
            else
                bNaics = true;
            
            if (filter.getType() != null){
                if (filtData.getBusinessType().contains(filter.getType())){
                    bType = true;
                }
            }
            else
                bType = true;
            
            if (filter.getJobs() != null){
                if (filtData.getJobsRetained().contains(filter.getJobs())){
                    bJobs = true;
                }
            }
            else
                bJobs = true;
            
            if (filter.getDate() != null){
                if (filtData.getDateApproved().contains(filter.getDate())){
                    bDate = true;
                }
            }
            else
                bDate = true;
            
            if (bSize && bName && bAddress && bCity && bZip && bNaics && bType && bJobs && bDate) {
                if (!dataExists(filtData.getBusinessID(), filteredData)){
                    filteredData.add(filtData);
                }
            }
            
        }
    
        return filteredData;
        
    }
    // Analytics Methods
    @FXML
    private void printPie(){
        hideAnalytics();
        String zip = boxPie.getSelectionModel().getSelectedItem().toString();
        ZipLoanChart pie = analytics.getPieInst(zip);
        int inst = pie.getInstances();
        
        //grab data for chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
        new PieChart.Data("$5-10 million", pie.getLoanA()),
        new PieChart.Data("$2-5 million", pie.getLoanB()),
        new PieChart.Data("$1-2 million", pie.getLoanC()),
        new PieChart.Data("$350,000-1 million", pie.getLoanD()),
        new PieChart.Data("$150,000-350,000", pie.getLoanE()));
        
        
        chartPie.setData(pieChartData);
        chartPie.setVisible(true);
        chartPie.setTitle("Zip: " + zip);
        chartPie.setLabelsVisible(false);
        chartPie.setLegendVisible(true);
        chartPie.setLegendSide(Side.BOTTOM);
        
    }
    
    @FXML
    private void printTotals(){
        hideAnalytics();
        
        chartTotals.setTitle("Loan Value Totals");
        XYChart.Series totalsSeries = new XYChart.Series();
        totalsSeries.getData().add(new XYChart.Data("$5M-10M",analytics.getTotals().getLoanA()));
        totalsSeries.getData().add(new XYChart.Data("$2M-5M",analytics.getTotals().getLoanB()));
        totalsSeries.getData().add(new XYChart.Data("$1M-2M",analytics.getTotals().getLoanC()));
        totalsSeries.getData().add(new XYChart.Data("$350K-1M",analytics.getTotals().getLoanD()));
        totalsSeries.getData().add(new XYChart.Data("$150k-350k",analytics.getTotals().getLoanE()));
        //work around for bug in dynamically created FXML bar charts
        xAxis.setCategories(FXCollections.observableArrayList("$5M-10M", "$2M-5M", "$350K-1M", "$150k-350k"));
        chartTotals.setLegendVisible(false);
        chartTotals.setVisible(true);
        chartTotals.getData().add(totalsSeries);
        
        
    }
    
    @FXML
    private void printLine(){
        hideAnalytics();
        chartLine.setTitle("Loan Values by Company Size");
        XYChart.Series lineSeriesA = new XYChart.Series();
        XYChart.Series lineSeriesB = new XYChart.Series();
        XYChart.Series lineSeriesC = new XYChart.Series();
        XYChart.Series lineSeriesD = new XYChart.Series();
        XYChart.Series lineSeriesE = new XYChart.Series();
        
        generateLineSeries(lineSeriesA, analytics.getLoanA());
        generateLineSeries(lineSeriesB, analytics.getLoanB());
        generateLineSeries(lineSeriesC, analytics.getLoanC());
        generateLineSeries(lineSeriesD, analytics.getLoanD());
        generateLineSeries(lineSeriesE, analytics.getLoanE());
        
        lineSeriesA.setName("$5M-10M");
        lineSeriesB.setName("$2M-5M");
        lineSeriesC.setName("$1M-2M");
        lineSeriesD.setName("350K-1M");
        lineSeriesE.setName("150K-350K");
        
        if (chkA.isSelected()){
            chartLine.getData().add(lineSeriesA);
        }
        if (chkB.isSelected()){
            chartLine.getData().add(lineSeriesB);
        }
        if (chkC.isSelected()){
            chartLine.getData().add(lineSeriesC);
        }
        if (chkD.isSelected()){
            chartLine.getData().add(lineSeriesD);
        }
        if (chkE.isSelected()){
            chartLine.getData().add(lineSeriesE);
        }
        paneSeries.setVisible(true);
        chartLine.setCreateSymbols(false);
        chartLine.setVisible(true);
        
    }
    
    private void generateLineSeries(XYChart.Series lineSeries, ObservableList<lineChartData> data){
        
        for (lineChartData d : data){
            lineSeries.getData().add(new XYChart.Data(d.getJobsRetained(), d.getCount()));
        }
    }
    private void hideAnalytics(){
        chartTotals.setVisible(false);
        chartTotals.getData().clear();
        chartPie.setVisible(false);
        chartTotals.getData().clear();
        chartLine.setVisible(false);
        chartLine.getData().clear();
        paneSeries.setVisible(false);
        
    }
    // Forecasting Methods
    
    @FXML
    private void generateForecasts(){
        generalForecasts(forecast);
    }
    
  
    private void generalForecasts(Forecast forecast){
        
        String fileName = "src/capstone/forecastoutput.arff";
        String processed = "";
        String zip = txtForecastZip.getText();
        Alert a = new Alert(AlertType.ERROR);
        
        int jobs = 0;
        try{
            jobs = Integer.parseInt(txtForecastJobs.getText());
            try{
                processed = Forecast.process(forecast.getInstances(), zip, jobs);
            }catch (Exception e2){
                processed = "Model does not have enough information to make prediction on"
                        + "\n Zip: " + zip  + " with company size of " + jobs + ".\n"
                        + "This combination has been logged for next model iteration. ";
                log(processed);
            }
        }catch (Exception e1){
            a.setContentText("Jobs must be a whole number.");
            a.show();
        }
       
        txtareaForecast.setText(processed);
        
    
            
    }
    
    
    // Import Methods
    @FXML
    private void addRow(){
        ObservableList<DataPoint> data = this.data;
        int newId = data.size() + 1;
        String name = txtBusiness.getText();
        String address = txtAddress.getText();
        String city = txtCity.getText();
        String zip = txtZip.getText();
        String naics = txtNAICS.getText();
        String job = txtJobs.getText();
        String date = txtDate.getText();
        String type = boxType.getSelectionModel().getSelectedItem().toString();
        String loan = boxLoanRange.getSelectionModel().getSelectedItem().toString();
        
        if (validate(name, address, city, zip, naics, job, date, type, loan)){
            try{
                DataPoint newRow = new DataPoint(newId, name, address, city, zip, naics, type, loan, job, date);
                data.add(newRow);
                txtBusiness.clear();
                txtAddress.clear();
                txtCity.clear();
                txtZip.clear();
                txtNAICS.clear();
                txtJobs.clear();
                txtDate.clear();
                boxType.setValue("Business Type");
                boxLoanRange.setValue("Loan Range");
                
            }catch(Exception e2){
                // alert for failed creation of datapoint
                String error = "Datapoint creation failed " + e2.getMessage() ;
                log(error);
            }
        }
                
        
        
    }
    
    private boolean validate(String name, String address, String city, String zip, String naics, String job, String date, String type, String loan){
        boolean valid = true;
        SimpleDateFormat sdfrmt = new SimpleDateFormat("MM/dd/yyyy");
        Alert a = new Alert(AlertType.ERROR);
        String alertContent = "";
        
        if (name.isEmpty()){
            //alert for name 
            alertContent += "Invalid Name\n";
            valid = false;
        }
        if (address.isEmpty()){
            //alert for address
            alertContent += "Invalid Address\n";
            valid = false;
        }
        if (city.isEmpty()){
            // alert for city
           alertContent += "Invalid City\n";
            valid = false;
        }
        try{
            Integer.parseInt(zip);
        }catch(NumberFormatException er){
            // alert for zip invalid input
            alertContent += "Invalid Zip, must be numeric\n";
            valid = false;
        }
        try{
            int i = Integer.parseInt(naics);
            if (i < 111110){
                alertContent += "Invalid NAICS code, must be at least 111110 \n";
                valid = false;
            }
        }catch(NumberFormatException er){
            //  alert for invalid naics input
            alertContent += "Invalid NAICS code, must be numeric";
            valid = false;
        }
        try{
            Integer.parseInt(job);
        }catch(NumberFormatException er){
            // alert for job invalid input
           alertContent += "Invalid Jobs Retained, must be numeric \n";
            valid = false;
        }
        try{
            Date formattedDate = sdfrmt.parse(date);
        }catch(ParseException e){
            //alert incorrect date format
            alertContent += "Invalid Date Format: MM/DD/YYYY \n";
            valid = false;
        }

        if (type.equals("Business Type")){
            //alert for type 
            alertContent += "Must Select Business Type\n";
            valid = false;
        }
        if (loan.equals("Loan Range")){
            // alert for range
            alertContent += "Must Select Loan Range\n";
            valid = false;
        }
        
        if (!valid){
            alertContent += "Failed input";
            a.setContentText(alertContent);
            a.show();
        }
        
        return valid;
    }
    
    @FXML
    private void reset(){
        txtBusiness.clear();
        txtAddress.clear();
        txtCity.clear();
        txtZip.clear();
        txtNAICS.clear();
        txtJobs.clear();
        txtDate.clear();
        boxType.setValue("Business Type");
        boxLoanRange.setValue("Loan Range");
    }
    
    //APP Metrics methods
    @FXML
    private void printLog(){
        txtAreaLog.setText(log);
    }
    
    public static void log(String newLog){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        log = log + "\n" + timestamp + "  " + newLog;
    }
}
