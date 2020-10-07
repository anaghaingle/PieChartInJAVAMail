package pkg;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.stage.Stage;
import util.EmailUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.image.WritableImage;
import javafx.scene.Group;

/**
 * @author Anagha.Ingle
 *
 */
public class EmailTest extends Application{

	public static void main(String[] args) {
//Create pie chart
		
		launch(args);
		
		System.out.println("Sending Email");
		// SMTP information Change as per requirements
		String host = "smtp.gmail.com";
		String port = "587";
		String from = "ingle.ana@gmail.com";
		String password = "hjoazrmbaccsyyph";

		//Message Information
		String sendTo = "anagha.ingle@ivlglobal.com";
		String subject = "Email with image inline";
		StringBuffer messageBody = new StringBuffer("<HTML>This is mail text Body<Br>");
		messageBody.append("This mail does contain one inline images.<br>");
		messageBody.append("<p><B>Prerequisite:</B> images should be available in .png or .jpeg format <br><hr>");
		messageBody.append("<img src=\"cid:image1\" width=\"50%\" height=\"50%\" /><br><hr>");	
		messageBody.append("</HTML>");
		
		//define image to include in mail
        Map<String, String> inlineImages = new HashMap<String, String>();
        inlineImages.put("image1", "D:/SeleniumWorkspace/JavaMailTest/testresults/image.png");
		
		try{
			EmailUtil.messageSend(host, port, from, password, subject, sendTo, messageBody.toString(), inlineImages);
			System.out.println("Email Sent");
		}
		catch(Exception e){
			System.out.println("Email could not sent");
			e.printStackTrace();
			
		}//End of try catch
		
	}//End of Main

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) throws Exception {
		
        Scene scene = new Scene(new Group());
        stage.setTitle("Test Result");
        stage.setWidth(500);
        stage.setHeight(500);
 
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Passed", 13),
                new PieChart.Data("Failed", 25),
                new PieChart.Data("Abort", 1)
        );

        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Test Result");
        chart.setClockwise(true);
        chart.setLabelLineLength(15);
        chart.setLabelsVisible(true);
        chart.setStartAngle(0);
        
        ((Group) scene.getRoot()).getChildren().add(chart);
        
        //Saving the scene as image
        WritableImage image = scene.snapshot(null);
        File file = new File("D:/SeleniumWorkspace/JavaMailTest/testresults/image.png");
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", file);
        System.out.println("Image Saved");

        stage.setScene(scene);
        stage.show();
        stage.close();
        
	}

}//end of class EmailTest
