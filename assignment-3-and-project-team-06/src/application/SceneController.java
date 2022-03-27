package application;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {

	Stage stage;
	Scene scene;
	
	private String css = getClass().getResource("application.css").toExternalForm();

	@FXML
	public void loadTopicScene(ActionEvent event) throws Exception {
		// load topic selection screen
		Parent root = FXMLLoader.load(getClass().getResource("/Topics.fxml"));
		stage = Main.primaryStage;
		scene = new Scene(root);
		scene.getStylesheets().add(css);
		stage.setScene(scene);
		stage.show();
	}

	protected void loadTopicScene() throws IOException {
		// load topic selection screen
		Parent root = FXMLLoader.load(getClass().getResource("/Topics.fxml"));
		stage = Main.primaryStage;
		scene = new Scene(root);
		scene.getStylesheets().add(css);
		stage.setScene(scene);
		stage.show();
	}

	protected void loadMenuScene() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
		Parent root = loader.load();
		scene = new Scene(root);
		scene.getStylesheets().add(css);
		stage = Main.primaryStage;
		stage.setTitle("Kemu Kupu - Spelling"); // set title for gui window
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

	protected void loadQuizScene(List<String> wordsList) throws IOException, InterruptedException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Quiz.fxml"));
		Parent root = loader.load();
		QuizController quizController = loader.getController();
		quizController.setWords(wordsList);
		quizController.wordNumLabel.setText("Spell word 1 out of 5");
		
		// Call festival to read out first word of quiz. 
		Festival.ttsMaori(1,wordsList.get(0), true);
		stage = Main.primaryStage;
		scene = new Scene(root);
		scene.getStylesheets().add(css);
		stage.setScene(scene);
		stage.show();
	}
	
	protected void loadRewardScene(double score) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reward.fxml"));
		Parent root = loader.load();
		RewardController controller = loader.getController();
		controller.setScore(score);
		stage = Main.primaryStage;
		scene = new Scene(root);
		scene.getStylesheets().add(css);
		stage.setScene(scene);
	}



}