package application;

/**Class that controls quiz functionality.
 * Includes:
 * Marking
 * Help
 * Add macron
 * Repeat word
 * **/

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class QuizController implements Initializable {

    @FXML
    TextField textField;
    @FXML
    Button enterButton;
    @FXML
    Button speakButton;

    @FXML
    Button dontKnowButton;

    @FXML
    Label wordNumLabel;
    
    @FXML
    Button macronButton;
    
    @FXML
    private Slider ttsSlider;

    @FXML
    Button macronHelp;

    @FXML
    Tooltip toolTip;
    
    @FXML
    Label hintLabel;
    
    @FXML
    Label currentScore;

    private Stage stage;
    private Scene scene;
    private Parent root;

    List<String> wordsToTest;
    int currentIndex = 0;
    int attempts = 1;
    double score = 0;

    AtomicInteger caretPos = new AtomicInteger();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showImage();
    }
    public void showImage(){
        Image image = new Image(getClass().getResourceAsStream("macronHelp.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);
        toolTip.setGraphic(imageView);
        toolTip.setStyle("-fx-background-color: transparent");
        macronHelp.setTooltip(toolTip);
    }
    // function that finds the position of the users cursor/caret to use when adding a macron
    @FXML
    public void readCaretPos() {
        textField.caretPositionProperty().addListener((obs, oldVal, newVal) -> {
            if (textField.isFocused()) {
                caretPos.set(newVal.intValue());
            }
        });
    }

    // function allowing user to press enter to submit their answer	
    @FXML
    public void handleEnterKeyPressed(KeyEvent event) throws IOException, InterruptedException {
        // get the position of the caret/cursor every time a key is pressed
	readCaretPos();
	
	//if the user hit enter then 
        if (event.getCode() == KeyCode.ENTER) {
            if (!textField.getText().isBlank()) {
                textField.setPromptText("Spell Word Here...");
                submitAnswer();
            } else {
                textField.setPromptText("CANNOT BE EMPTY");
            }
        }
    }

    // function allowing user to click enter button to submit answer
    @FXML
    public void handleEnterButtonClicked(ActionEvent event) throws IOException, InterruptedException {
        if (!textField.getText().isBlank()) {
            textField.setPromptText("Spell Word Here...");
            submitAnswer();
        } else {
            textField.setPromptText("CANNOT BE EMPTY");
        }
    }

    // checks if users answer is correct and updates score
    @FXML
    public void submitAnswer() throws IOException, InterruptedException {
	// if user gets word right first try
        if(textField.getText().equalsIgnoreCase(wordsToTest.get(currentIndex))){ // case for correct
            if (attempts == 1) {
            	score +=1;
            	updateScore();	
            } else {
            	score +=0.5;
            	updateScore();
            }
     
            Festival.ttsEnglish(1, "correct");
            goNextWord();
            attempts=1;
	// if user gets word wrong first try
        } else if(attempts == 1){
            attempts = 2;
            Festival.ttsEnglish(1, "incorrect");
            Festival.ttsMaori(getSliderValue(),wordsToTest.get(currentIndex), false);
            showHint();
            textField.clear();
	// if user gets both attempts wrong
        } else if(attempts ==2){
            Festival.ttsEnglish(1,"Incorrect. better luck next time");
            goNextWord();
            attempts =1;
        }
    }
	// function to show user a hint after getting the first attempt incorrect
	public void showHint() {
		String hint = Character.toString(wordsToTest.get(currentIndex).charAt(1));
		hintLabel.setText("* Hint, second letter of answer is: " + hint + " *");
		hintLabel.setVisible(true);
	}
	
	// function that adds a macron to a vowel the user has the caret/cursor on
	@FXML
	public void addMacron(ActionEvent event) {
		if (!textField.getText().isBlank()) {
			StringBuilder sb = new StringBuilder(textField.getText());			
			int caretIndex = caretPos.intValue()-1;
			if (isValidForMacron(sb, caretIndex)){ 
				Character c = macronised(sb.charAt(caretIndex));
				sb.replace(caretIndex, caretIndex+1, c.toString());
				textField.setText(sb.toString());
			}
		}
	}
    
    // function providing functionality for the skip button
    @FXML
    public void dontKnowButtonPressed(ActionEvent event) throws IOException, InterruptedException {
        if (currentIndex != 4) {
            Festival.ttsEnglish(1, "Thats alright. you'll get it next time");
        }
        goNextWord();
    }

    // function that moves from the current word to the next word
    public void goNextWord() throws IOException, InterruptedException {
    	hintLabel.setVisible(false);
        currentIndex += 1;
        textField.clear();
        if(currentIndex<5) {
            Festival.ttsEnglish(1,"Now spell");
            Festival.ttsMaori(getSliderValue(), wordsToTest.get(currentIndex), false);
            wordNumLabel.setText("Spell word " + (currentIndex+1) + " out of 5");
            attempts = 1;
        } else{
           Main.sceneSwitcher.loadRewardScene(this.score);
        }
    }
    
    // function to return the appropriate macronised character
    private Character macronised(Character c) {
		Character ch = Character.toLowerCase(c);

		switch (ch) {
		case 'a':
			if (Character.isUpperCase(c)) {
				return '\u0100';
			} else {
				return '\u0101';
			}
		case 'e':
			if (Character.isUpperCase(c)) {
				return '\u0112';
			} else {
				return '\u0113';
			}
		case 'i':
			if (Character.isUpperCase(c)) {
				return '\u012A';
			} else {
				return '\u012B';
			}	
		case 'o':
			if (Character.isUpperCase(c)) {
				return '\u014C';
			} else {
				return '\u014D';
			}
		case 'u':
			if (Character.isUpperCase(c)) {
				return '\u016A';
			} else {
				return '\u016B';
			}


		}
		return null;
	}

    // function checking whether character can be macronised
    private boolean isValidForMacron(StringBuilder sb, int caretIndex) {
		char c = Character.toLowerCase(sb.charAt(caretIndex));

		Boolean isVowel = (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u');

		if (caretIndex+1 < sb.length() && Character.isLetter(sb.charAt(caretIndex+1))) {
			return isVowel;
		} else if (caretIndex == sb.length()-1) {
			return isVowel;
		}

		return false;
	}

    // function providing functionality for the repeat word button
    @FXML
    public void repeatWord(ActionEvent event) throws IOException, InterruptedException{
        Festival.ttsMaori(getSliderValue(), wordsToTest.get(currentIndex), false);
    }

    // setter function allowing the tested words to be passed into the controller class
    public void setWords(List<String> wordList) throws IOException, InterruptedException {
        this.wordsToTest = wordList;
    }

    // getter function allowing the slider value to be used for festival
    @FXML
    private double getSliderValue(){
    	return Math.round((2-ttsSlider.getValue())*100.0)/100.0;
    }
    
    // function to update the users score
    public void updateScore() {
		currentScore.setText("SCORE = " + this.score);
    }


}
