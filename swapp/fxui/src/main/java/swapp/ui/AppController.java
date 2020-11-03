package swapp.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.RadioButton;
import swapp.core.SwappItem;
import swapp.core.SwappItemList;
import swapp.json.SwappPersistence;

public class AppController {

  @FXML
  private ListView<SwappItem> listView;

  @FXML
  private ChoiceBox<String> filterChoiceBox;
  
  @FXML
  private TextField textField;

  @FXML
  private Button addButton;

  @FXML
  private Button removeButton;

  @FXML
  private MenuItem openButton;

  @FXML
  private MenuItem saveButton;

  @FXML
  private TextField nameField;
  
  @FXML
  private TextArea descriptionFieldArea;

  @FXML
  private TextField contactInfoField;

  @FXML
  private RadioButton nyRadio;

  @FXML
  private RadioButton litt_bruktRadio;

  @FXML
  private RadioButton godt_bruktRadio;
  
  private SwappPersistence swappPersistence = new SwappPersistence();
  private SwappItemList swappList;
  private ToggleGroup toggleGroup;
  private File file = Paths.get(System.getProperty("user.home"), "items.json").toFile();

  private final static String SwappItemListWithTwoItems = 
    "[{\"itemName\":\"name1\",\"itemStatus\":\"Ny\""
    + ",\"itemDescription\":\"description1\",\"itemContactInfo\":\"contactInfo1\"},"
    + "{\"itemName\":\"name2\",\"itemStatus\":\"Ny\""
    + ",\"itemDescription\":\"description2\",\"itemContactInfo\":\"contactInfo2\"}]";

  /** Initializes appcontroller. */
  public AppController() {
    listView = new ListView<SwappItem>();
    filterChoiceBox = new ChoiceBox<>();
    swappList = new SwappItemList();

    loadItems();
  }

  public void setFile(File file) {
    this.file = file;
    loadItems();
  }

  public void filterSwappItemByStatusChoiceBox(){
    updateSwappItems();
  }

  void loadItems() {
    Reader reader = null;
    try {
      try {
        reader = new FileReader(file, StandardCharsets.UTF_8);
      } catch (IOException ioex1) {
        System.err.println("Fant ingen fil lokalt. Laster inn eksempelfil..");
        URL url = getClass().getResource("items.json");
        if (url != null) {
          reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
        } else {
          System.err.println("Fant ingen eksempelfil. Parser string direkte..");
          reader = new StringReader(SwappItemListWithTwoItems);
        }
      }
      /*
       * For å printe ut fil til konsoll: BufferedReader reader2 = new
       * BufferedReader(reader); String linje; while ((linje = reader2.readLine()) !=
       * null) { System.out.println(linje); }
       */
      SwappItemList list = swappPersistence.readSwappList(reader);
      swappList.setSwappItemlist(list);
    } catch (IOException ioex2) {
      System.err.println("Legger til gjenstander direkte..");
      swappList.addItem(new SwappItem("name1", "Ny", "description1", "contactInfo1"));
      swappList.addItem(new SwappItem("name2", "Ny", "description2", "contactInfo2"));
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException ioex3) {
        System.err.println("Problem med reader..");
      }
    }
  }

  public void initializeChoiceBox(){
    filterChoiceBox.getItems().add("All");
    filterChoiceBox.getItems().add("New");
    filterChoiceBox.getItems().add("Used");
    filterChoiceBox.setValue("All");
    filterChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> updateSwappItems());
  }

  public void inizializeToggleGroup() {
    toggleGroup = new ToggleGroup();
    nyRadio.setToggleGroup(toggleGroup);
    litt_bruktRadio.setToggleGroup(toggleGroup);
    godt_bruktRadio.setToggleGroup(toggleGroup);
  }

  /** Initialize with lambda expression for listeners of SwappItemList. */
  @FXML
  void initialize() {
    //statusChoiceBox.getItems().addAll("Ny", "Litt brukt", "Godt brukt");
    inizializeToggleGroup();
    initializeChoiceBox();
    updateSwappItems();
    swappList.addSwappItemListListener(swappList -> {
      updateSwappItems();
      autoSave();
    });
  }

  @FXML
  void addSwappItemButtonClicked() {
    if (!nameField.getText().isBlank()) {
      SwappItem item = new SwappItem(nameField.getText(), /*statusChoiceBox.getSelectionModel().getSelectedItem().toString()*/ ((RadioButton)toggleGroup.getSelectedToggle()).getText(), descriptionFieldArea.getText(), contactInfoField.getText());
      swappList.addItem(item);
      nameField.setText("");
      descriptionFieldArea.setText("");
    }
    
    //statusChoiceBox.getSelectionModel().clearSelection();
  }

  @FXML
  void removeSwappItemButtonClicked() {
    SwappItem item = (SwappItem) listView.getSelectionModel().getSelectedItem();
    if (!(item == null)) {
      swappList.removeItem(item);
    }
  }

  public void updateSwappItems() {
    listView.getItems().setAll(swappList.getItemsByStatus(filterChoiceBox.getSelectionModel().getSelectedItem()));
  }

  public SwappItemList getItems() {
    return swappList;
  }

  private void autoSave() {
    Writer writer = null;
    try {
      writer = new FileWriter(file, StandardCharsets.UTF_8);
      swappPersistence.writeSwappList(swappList, writer);
    } catch (IOException ioex) {
      System.err.println("Feil med fillagring.");
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException e) {
        System.err.println("Feil med fillagring..");
      }
    }
  }

  /*
   * Metoder for dokumentmetafor:
   * 
   * @FXML void handleSaveAction() {saveItems();}
   * 
   * @FXML void handleOpenAction() {loadItems();}
   */
}
