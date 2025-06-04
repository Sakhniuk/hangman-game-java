import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HangMan extends JFrame implements ActionListener{
        private int incorrectGuesses;

        private String[] wordChallenge;

        private final WordDB wordDB;
        private JLabel hangmanImage, categoryLabel, hiddenWordLabel, resultLabel, wordLabel;
        private JButton[] letterButtons;
        private JDialog resultDialog;

        public HangMan(){
            super("Hangman Game (Java Ed.)");
            setSize(CommonConstants.FRAME_SIZE);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(null);
            setResizable(false);
            getContentPane().setBackground(CommonConstants.BACKGROUND_COLOR);

            wordDB = new WordDB();
            letterButtons = new JButton[26];
            wordChallenge = wordDB.loadChallenge();
            createResultDialog();

            addGuiComponents();
        }

        private void addGuiComponents(){
            hangmanImage = CustomTools.loadImage(CommonConstants.IMAGE_PATH);
            hangmanImage.setBounds(0, 0, hangmanImage.getPreferredSize().width, hangmanImage.getPreferredSize().height);

            categoryLabel = new JLabel(wordChallenge[0]);
            categoryLabel.setFont(new Font("Arial", Font.BOLD, 30));
            categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
            categoryLabel.setOpaque(true);
            categoryLabel.setForeground(CommonConstants.TEXT_COLOR);
            categoryLabel.setBackground(CommonConstants.SECONDARY_COLOR);
            categoryLabel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                    BorderFactory.createEmptyBorder(5, 0, 5, 0)
            ));
            categoryLabel.setBounds(
                    0,
                    hangmanImage.getPreferredSize().height - 28,
                    CommonConstants.FRAME_SIZE.width,
                    categoryLabel.getPreferredSize().height
            );

            hiddenWordLabel = new JLabel(CustomTools.hideWords(wordChallenge[1]));
            hiddenWordLabel.setFont(new Font("Arial", Font.BOLD, 64));
            hiddenWordLabel.setForeground(Color.WHITE);
            hiddenWordLabel.setHorizontalAlignment(SwingConstants.CENTER);
            hiddenWordLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            hiddenWordLabel.setBounds(
                    0,
                    categoryLabel.getY() + categoryLabel.getPreferredSize().height + 50,
                    CommonConstants.FRAME_SIZE.width,
                    hiddenWordLabel.getPreferredSize().height
            );

            GridLayout gridLayout = new GridLayout(4, 7);
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBounds(
                    -5,
                    hiddenWordLabel.getY() + hiddenWordLabel.getPreferredSize().height,
                    CommonConstants.BUTTON_PANEL_SIZE.width,
                    CommonConstants.BUTTON_PANEL_SIZE.height
            );
            buttonPanel.setLayout(gridLayout);

            for(char c = 'A'; c <= 'Z'; c++){
                JButton button = new JButton(Character.toString(c));
                button.setBackground(CommonConstants.BUTTON_BG_COLOR);
                button.setFont(new Font("Arial", Font.BOLD, 22));
                button.setForeground(CommonConstants.BUTTON_TEXT_COLOR);
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
                button.addActionListener(this);

                int currentIndex = c - 'A';

                letterButtons[currentIndex] = button;
                buttonPanel.add(letterButtons[currentIndex]);
            }

            JButton resetButton = new JButton("Reset");
            resetButton.setFont(new Font("Arial", Font.BOLD, 22));
            resetButton.setForeground(CommonConstants.TEXT_COLOR);
            resetButton.setBackground(CommonConstants.SECONDARY_COLOR);
            resetButton.addActionListener(this);
            buttonPanel.add(resetButton);

            JButton quitButton = new JButton("Quit");
            quitButton.setFont(new Font("Arial", Font.BOLD, 22));
            quitButton.setForeground(CommonConstants.TEXT_COLOR);
            quitButton.setBackground(CommonConstants.SECONDARY_COLOR);
            quitButton.addActionListener(this);
            buttonPanel.add(quitButton);

            getContentPane().add(categoryLabel);
            getContentPane().add(hangmanImage);
            getContentPane().add(hiddenWordLabel);
            getContentPane().add(buttonPanel);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if(command.equals("Reset") || command.equals("Restart")){
                resetGame();

                if(command.equals("Restart")){
                    resultDialog.setVisible(false);
                }
            }else if(command.equals("Quit")){
                dispose();
            }else{
                JButton button = (JButton) e.getSource();
                button.setEnabled(false);

                if(wordChallenge[1].contains(command)){
                    button.setBackground(Color.GREEN);
                    char[] hiddenWord = hiddenWordLabel.getText().toCharArray();

                    for(int i = 0; i < wordChallenge[1].length(); i++){
                        if(wordChallenge[1].charAt(i) == command.charAt(0)){
                            hiddenWord[i] = command.charAt(0);
                        }
                    }

                    hiddenWordLabel.setText(String.valueOf(hiddenWord));

                    if(!hiddenWordLabel.getText().contains("*")){
                        resultLabel.setText("You got it right!");
                        resultDialog.setVisible(true);
                    }

                }else{
                    button.setBackground(Color.RED);

                    ++incorrectGuesses;

                    CustomTools.updateImage(hangmanImage, "resources/" + (incorrectGuesses + 1) + ".png");

                    if(incorrectGuesses >= 6){
                        resultLabel.setText("Too Bad, Try Again?");
                        resultDialog.setVisible(true);
                    }
                }
                wordLabel.setText("Word: " + wordChallenge[1]);
            }

        }

        private void createResultDialog(){
            resultDialog = new JDialog();
            resultDialog.setTitle("Result");
            resultDialog.setSize(CommonConstants.RESULT_DIALOG_SIZE);
            resultDialog.getContentPane().setBackground(CommonConstants.BACKGROUND_COLOR);
            resultDialog.setResizable(false);
            resultDialog.setLocationRelativeTo(this);
            resultDialog.setModal(true);
            resultDialog.setLayout(new GridLayout(3, 1));
            resultDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    resetGame();
                }
            });

            resultLabel = new JLabel();
            resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
            resultLabel.setForeground(Color.WHITE);
            resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

            wordLabel = new JLabel();
            wordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            wordLabel.setForeground(Color.WHITE);
            wordLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JButton restartButton = new JButton("Restart");
            restartButton.setFont(new Font("Arial", Font.PLAIN, 20));
            restartButton.setForeground(Color.WHITE);
            restartButton.setBackground(CommonConstants.SECONDARY_COLOR);
            restartButton.addActionListener(this);

            resultDialog.add(resultLabel);
            resultDialog.add(wordLabel);
            resultDialog.add(restartButton);
        }

        private void resetGame(){
            wordChallenge = wordDB.loadChallenge();
            incorrectGuesses = 0;

            CustomTools.updateImage(hangmanImage, CommonConstants.IMAGE_PATH);

            categoryLabel.setText(wordChallenge[0]);

            String hiddenWord = CustomTools.hideWords(wordChallenge[1]);
            hiddenWordLabel.setText(hiddenWord);

            for(int i = 0; i < letterButtons.length; i++){
                letterButtons[i].setEnabled(true);
                letterButtons[i].setBackground(CommonConstants.PRIMARY_COLOR);
            }
        }
    }