package cryptotool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

// Crypto libraries
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.*;
import java.util.*;


public class CryptoTool extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Main project window setup
        BorderPane bord = new BorderPane();
        VBox v = new VBox(10);

        Label lp1 = new Label("Project"); // Title label
        lp1.setStyle("-fx-font-size: 20px;");

         // Three main cipher buttons
        Button bt1 = new Button("        AES         ");
        Button bt2 = new Button("Beaufort Cipher");
        Button bt3 = new Button("Vigenere Cipher   ");

        // Add buttons vertically; center alignment
        v.getChildren().addAll(bt1, bt2, bt3);
        v.setAlignment(Pos.CENTER);

         // Place title at top, cipher choices at center
        bord.setTop(lp1);
        BorderPane.setAlignment(lp1, Pos.CENTER);
        bord.setCenter(v);

        // Actions: Each button opens another window for a cipher
        bt1.setOnAction(e -> {
            primaryStage.close(); // Close main window
            showAESGUI(primaryStage); // Open AES window
        });

        bt2.setOnAction(e -> {
            primaryStage.close();
            showSubGUI(primaryStage); // Open Beaufort window
        });

        bt3.setOnAction(e -> {
            primaryStage.close();
            showVegeGUI(primaryStage); // Open Vigenere window
        });

          // Set scene and display main menu
        Scene s = new Scene(bord, 250, 250);
        primaryStage.setTitle("crypto project");
        primaryStage.setScene(s);
        primaryStage.show();

    }//start
    //------------------------------------------------------

     // AES Cipher GUI & handling
    public void showAESGUI(Stage primaryStage) {
        BorderPane bord = new BorderPane();

        VBox v = new VBox(15);

        Label lpA = new Label("AES");
        lpA.setStyle("-fx-font-size: 20px;");

        // Encryption/Decryption options
        Button encrypt = new Button("Encryption🔒");
        Button decrypt = new Button("Decryption🔓");

        TextField tx = new TextField();
        tx.setPromptText("Enter the Key"); // User types AES key here
        tx.setPrefColumnCount(10);
        tx.setPrefWidth(250);

        TextArea tx2 = new TextArea(); // Result box
        tx2.setPromptText("The result");
        tx2.setEditable(false);

        TextField tx3 = new TextField(); // Uploaded file content (non-editable)
        tx3.setAlignment(Pos.CENTER);
        tx3.setEditable(false);
        tx3.setStyle("-fx-font-size: 10px;");

        Button upload = new Button("📂");

        // Upload button: let user choose a text file and read its contents
        upload.setOnAction(e -> {
            FileChooser fi = new FileChooser();
            fi.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File selectedFi = fi.showOpenDialog(primaryStage);

            if (selectedFi != null) {
                String fcontent = readFile(selectedFi);
                tx3.setText(fcontent); // Display file content in tx3
            }
        });

        // AES Encrypt button: Encrypts the file content using the provided key
        encrypt.setOnAction(e -> {
            try {
                String text = tx3.getText();
                String key = tx.getText();
                if (text == null || text.isEmpty()) {
                    tx2.setText("Please upload a file first.");
                    return;
                }
                SecretKey secretKey = getKeyAES(key); // Validate key length
                String result = encryptAES(text, key);
                tx2.setText(result);
            } catch (IllegalArgumentException ex) {
                tx2.setText("Invalid key length. Key must be 16, 24, or 32 bytes.");
            } catch (Exception ex) {
                tx2.setText("Encryption error: " + ex.getMessage());
            }
        }
        );

        // AES Decrypt button: Decrypts the Base64 ciphertext using given key
        decrypt.setOnAction(e -> {
            try {
                String text = tx3.getText();
                String key = tx.getText();
                if (text == null || text.isEmpty()) {
                    tx2.setText("Please upload a file first.");
                    return;
                }
                String result = decrypt(text, key);
                tx2.setText(result);

            } catch (IllegalArgumentException ex) {
                tx2.setText("Invalid key length. Key must be 16, 24, or 32 bytes.");
            } catch (Exception ex) {
                tx2.setText("Decryption error: " + ex.getMessage());
            }
        }
        );

        // Arrange input fields and buttons
        HBox h2 = new HBox(10);
        h2.getChildren().addAll(tx, upload);
        h2.setStyle("-fx-alignment: center;");

        HBox h3 = new HBox(10);
        h3.getChildren().addAll(encrypt, decrypt);
        h3.setStyle("-fx-alignment: center;");
        v.getChildren().addAll(h2, tx3, h3, tx2);

        // Layout: title at center, rest at bottom
        bord.setCenter(lpA);
        BorderPane.setAlignment(lpA, Pos.CENTER);
        bord.setBottom(v);
        
        Scene s = new Scene(bord, 350, 350);
        primaryStage.setTitle("AES");
        primaryStage.setScene(s);
        primaryStage.show();

    }//showAESGUI

    //------------------------------------------------------
     // Beaufort Cipher GUI and logic
    public void showSubGUI(Stage primaryStage) {
        BorderPane bord = new BorderPane();

        VBox v = new VBox(10);

        Label lpA = new Label("Beaufort Cipher");
        lpA.setStyle("-fx-font-size: 20px;");

        Button encrypt = new Button("Encryption🔒");
        Button decrypt = new Button("Decryption🔓");

        TextField tx = new TextField();
        tx.setPromptText("Enter the Key");
        tx.setPrefColumnCount(10);
        tx.setPrefWidth(250);

        TextArea tx2 = new TextArea();
        tx2.setPromptText("The Result");
        tx2.setEditable(false);

        TextField tx3 = new TextField();
        tx3.setAlignment(Pos.CENTER);
        tx3.setEditable(false);
        tx3.setStyle("-fx-font-size: 10px;");

        Button upload = new Button("📂");

        // Uploads the text to encrypt/decrypt
        upload.setOnAction(e -> {
            FileChooser fi = new FileChooser();
            fi.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File selectedFi = fi.showOpenDialog(primaryStage);

            if (selectedFi != null) {
                String fcontent = readFile(selectedFi);
                tx3.setText(fcontent);
            }

        });

        // Encrypt using Beaufort algorithm (custom classical cipher)
        encrypt.setOnAction(e -> {
            String text = tx3.getText().toLowerCase();
            if (text == null || text.isEmpty()) {
                tx2.setText("Please upload a file first.");
                return;
            }
            String key = tx.getText().toLowerCase();
            String result = encryptBeaufort(text, key);
            tx2.setText(result);
        });

        // Decrypt using Beaufort algorithm (same as encryption)
        decrypt.setOnAction(e -> {
            String text = tx3.getText().toLowerCase();
            if (text == null || text.isEmpty()) {
                tx2.setText("Please upload a file first.");
                return;
            }
            String key = tx.getText().toLowerCase();
            String result = decryptBeaufort(text, key);
            tx2.setText(result);
        });

        // Interface arrangement
        HBox h2 = new HBox(10);
        h2.getChildren().addAll(tx, upload);
        h2.setStyle("-fx-alignment: center;");

        HBox h3 = new HBox(10);
        h3.getChildren().addAll(encrypt, decrypt);
        h3.setStyle("-fx-alignment: center;");

        v.getChildren().addAll(h2, tx3, h3, tx2);

        bord.setCenter(lpA);
        BorderPane.setAlignment(lpA, Pos.CENTER);
        bord.setBottom(v);

        Scene s = new Scene(bord, 350, 350);
        primaryStage.setTitle("Subsitution Cipher");
        primaryStage.setScene(s);
        primaryStage.show();

    }//showSubGUI

    //------------------------------------------------------
    // Vigenere Cipher GUI and logic
    public void showVegeGUI(Stage primaryStage) {
        BorderPane bord = new BorderPane();

        VBox v = new VBox(10);

        Label lpA = new Label("Vigenere Cipher ");
        lpA.setStyle("-fx-font-size: 20px;");

        Button encrypt = new Button("Encryption🔒");
        Button decrypt = new Button("Decryption🔓");

        TextField tx3 = new TextField();
        tx3.setAlignment(Pos.CENTER);
        tx3.setEditable(false);
        tx3.setStyle("-fx-font-size: 10px;");

        TextField tx = new TextField();
        tx.setPromptText("Enter the Key");
        tx.setPrefColumnCount(10);
        tx.setPrefWidth(250);

        TextArea tx2 = new TextArea();
        tx2.setPromptText("The Result");
        tx2.setEditable(false);

        Button upload = new Button("📂");

        // Upload content for Vigenere cipher
        upload.setOnAction(e -> {
            FileChooser fi = new FileChooser();
            fi.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File selectedFi = fi.showOpenDialog(primaryStage);

            if (selectedFi != null) {
                String fcontent = readFile(selectedFi);
                tx3.setText(fcontent);
            }

        });
        //done
        // Encrypt using Vigenere
        encrypt.setOnAction(e -> {
            String text = tx3.getText().toLowerCase();
            if (text == null || text.isEmpty()) {
                tx2.setText("Please upload a file first.");
                return;
            }
            String key = tx.getText().toLowerCase();
            String result = encryptVigenere(text, key);
            tx2.setText(result);
        });
        //done
        // Decrypt using Vigenere
        decrypt.setOnAction(e -> {
            String text = tx3.getText().toLowerCase();
            if (text == null || text.isEmpty()) {
                tx2.setText("Please upload a file first.");
                return;
            }
            String key = tx.getText().toLowerCase();
            String result = decryptVigenere(text, key);
            tx2.setText(result);
        });

        HBox h2 = new HBox(10);
        h2.getChildren().addAll(tx, upload);
        h2.setStyle("-fx-alignment: center;");

        HBox h3 = new HBox(10);
        h3.getChildren().addAll(encrypt, decrypt);
        h3.setStyle("-fx-alignment: center;");

        v.getChildren().addAll(h2, tx3, h3, tx2);

        bord.setTop(lpA);
        BorderPane.setAlignment(lpA, Pos.CENTER);
        bord.setBottom(v);

        Scene s = new Scene(bord, 350, 350);
        primaryStage.setTitle("Vigenere Cipher ");
        primaryStage.setScene(s);
        primaryStage.show();

    }//showVegeGUI

    //------------------------------------------------------
    // Helper method: Reads file content into a string
    String readFile(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader read = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = read.readLine()) != null) {
                content.append(line).append("\n");
            }

        } catch (IOException e) {
            System.out.println("Something went wrong!");
        }
        return content.toString();
    }//readFile

    //------------------------------------------------------
    // AES key preparation: Converts a string into a SecretKey, checks valid key length
    public SecretKey getKeyAES(String inputkey) {
        byte[] keyByte = inputkey.getBytes(StandardCharsets.UTF_8);

        if (keyByte.length == 16 || keyByte.length == 24 || keyByte.length == 32) {
            return new SecretKeySpec(keyByte, "AES");
        } else {
            throw new IllegalArgumentException("The key should be 16 byte or 24 byte or 32 byte");
        }
    }//getKeyAES

    //------------------------------------------------------
    // Vigenere Encryption: Classical polyalphabetic cipher using key
    public String encryptVigenere(String plainText, String key) {
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        String cipherText = "";
        int i, j, k, indexKey = 0;

        for (i = 0; i < plainText.length(); i++) {
            if (plainText.charAt(i) == ' ') {
                cipherText += ' ';
                continue;
            }
            for (j = 0; j < alpha.length(); j++) {
                if (plainText.charAt(i) == alpha.charAt(j)) {
                    break;
                }
            }
            for (k = 0; k < alpha.length(); k++) {
                if (key.charAt(indexKey) == alpha.charAt(k)) {
                    break;
                }
            }
            cipherText += alpha.charAt((j + k) % 26);
            if (indexKey == key.length() - 1) {
                indexKey = 0;
            } else {
                indexKey++;
            }
        }
        return cipherText;
    }//encryptVigenere

    //------------------------------------------------------
    // Vigenere Decryption: Reverses above logic
    public String decryptVigenere(String cipherMessage, String key) {
        String plainText = "";
        int keyIndex = 0;

        for (int i = 0; i < cipherMessage.length(); i++) {
            char cipherChar = cipherMessage.charAt(i);

            if (Character.isLetter(cipherChar)) {
                char keyChar = key.charAt(keyIndex % key.length());
                int cipherValue = cipherChar - 'A'; // Should be 'a' if using lowercase only!
                int keyValue = keyChar - 'A'; // Should also match lowercase!
                int plainValue = (cipherValue - keyValue + 26) % 26;
                plainText += (char) (plainValue + 'A');
                keyIndex++;
            } else {
                plainText += cipherChar;
            }
        }

        return plainText;
    }//decryptVigenere

    //------------------------------------------------------
    // Beaufort Cipher Encryption: Kind of like Vigenere but with different calculation
    public String encryptBeaufort(String plainText, String key) {
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        String cipherText = "";
        int i, j, k, indexKey = 0;

        for (i = 0; i < plainText.length(); i++) {
            if (plainText.charAt(i) == ' ') {
                cipherText += ' ';
                continue;
            }
            for (j = 0; j < alpha.length(); j++) {
                if (plainText.charAt(i) == alpha.charAt(j)) {
                    break;
                }
            }
            for (k = 0; k < alpha.length(); k++) {
                if (key.charAt(indexKey) == alpha.charAt(k)) {
                    break;
                }
            }
            cipherText += alpha.charAt((k - j + 26) % 26);

            if (indexKey == key.length() - 1) {
                indexKey = 0;
            } else {
                indexKey++;
            }
        }

        return cipherText;
    }//encryptBeaufort

    //------------------------------------------------------
    // Beaufort Cipher Decryption: Identical logic to encryption
    public static String decryptBeaufort(String cipherMessage, String key) {
        String plainText = "";
        int keyIndex = 0;

        for (int i = 0; i < cipherMessage.length(); i++) {
            char cipherChar = cipherMessage.charAt(i);

            if (Character.isLetter(cipherChar)) {
                char keyChar = key.charAt(keyIndex % key.length());
                int cipherValue = cipherChar - 'a';
                int keyValue = keyChar - 'a';
                int plainValue = (keyValue - cipherValue + 26) % 26;
                plainText += (char) (plainValue + 'a');
                keyIndex++;
            } else {
                plainText += cipherChar;
            }
        }

        return plainText;
    }//decryptBeaufort

    //------------------------------------------------------
    // AES Encryption (CBC mode): Encodes with a random IV; returns Base64 encoded result
    public String encryptAES(String input, String key) throws Exception {

        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length != 16 && keyBytes.length != 24 && keyBytes.length != 32) {
            throw new IllegalArgumentException("Key length must be 16, 24, or 32 bytes.");
        }

        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv); // New IV for each encryption
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] ciphertext = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));

        // Concatenate IV and ciphertext, encode as Base64
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(iv);
        outputStream.write(ciphertext);

        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }//encryptAES

    //------------------------------------------------------
    // AES Decryption (CBC mode): Expects Base64 containing IV+ciphertext
    public String decrypt(String encryptedBase64, String key) throws Exception {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedBase64);
        // نحول للبايت

        Cipher cipher;
        byte[] iv;
        byte[] actualCiphertext;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        iv = Arrays.copyOfRange(encryptedBytes, 0, 16); // First 16 bytes are IV
        actualCiphertext = Arrays.copyOfRange(encryptedBytes, 16, encryptedBytes.length); // Rest is ciphertext

        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decryptedBytes = cipher.doFinal(actualCiphertext);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }//decrypt

    public static void main(String[] args) {
        launch(args);
    }

}
