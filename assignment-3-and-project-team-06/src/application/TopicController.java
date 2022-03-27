package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;



public class TopicController implements Initializable {

	private ObservableList<String> topicList = FXCollections.observableArrayList
			("Colours","Days","Days (Loan)","Months","Months (Loan)",
					"Babies","Weather", "Compass Directions", "Feelings", "Work Environment", 
					"Engineering", "Software", "Uni Life");

	protected HashMap<String,String> topicMap = new HashMap<>();

	@FXML
	protected ComboBox<String> topicBox;

	@FXML 
	private Label topicLabel;
	
	private Stage stage;
	private Scene scene;
	private Parent root;

	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// following code is run when the topic page is loaded
		populateTopicMap();
		topicBox.getItems().addAll(topicList.sorted());
	}
	

	private void populateTopicMap() {
		// Function to format the name of topics to pick appropriate word file
		for (String topic : this.topicList.sorted()) {
			topicMap.put(topic, topic.replace("(", "").replace(")", "").replace(" ", "_").toLowerCase()+".txt");
		}
	}


	public List<String> getWordsFromJAR(String topic) throws IOException {
		// Function to get 5 random words from a given word list
		InputStream is = getClass().getResourceAsStream("wordLists/"+topicMap.get(topic));
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);
		ArrayList<String> listOfWords = new ArrayList<>();
		String word = reader.readLine();

		while (word != null) {
			listOfWords.add(word.trim());
			word = reader.readLine();
		}
		reader.close();
		isr.close();
		is.close();
		Collections.shuffle(listOfWords);
		return listOfWords.subList(0, 5);
	}
	
	@FXML
	public void loadQuiz(ActionEvent e) throws Exception {
		// load new quiz scene
		if (!topicBox.getSelectionModel().isEmpty()) { // makes sure a topic is selected
			String topic = topicBox.getSelectionModel().getSelectedItem();
			List<String> wordsList = getWordsFromJAR(topic);
			System.out.println(wordsList);
			Main.sceneSwitcher.loadQuizScene(wordsList);
		}
	}

	
}
