package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main extends Application {
	
	public static Stage primaryStage; // can be used by Controller classes to create new scenes
	
	protected static SceneController sceneSwitcher;
	
    @Override
    public void start(Stage primaryStage) throws Exception{
    	sceneSwitcher = new SceneController();
        Main.primaryStage = primaryStage;
        sceneSwitcher.loadMenuScene();
    }


    public static void main(String[] args) {
        launch(args);
    }
    // Function to run our bash commands
    public static void bashCmd(String cmd){
        try {
            //String command = cmd;
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
            Process process = pb.start();

            // reading stdout and stderr
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            int exitStatus = process.waitFor();
            if (exitStatus == 0) {
                String line;
                while ((line = stdout.readLine()) != null) {
                    System.out.println(line);
                }
            } else {
                String line;
                while ((line = stderr.readLine()) != null) {
                    System.err.println(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

