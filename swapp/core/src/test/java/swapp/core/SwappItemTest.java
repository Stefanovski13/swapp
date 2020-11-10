package swapp.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class SwappItemTest {

  private SwappItem item;

  @BeforeEach
  public void beforeEach() {
    item = new SwappItem("name", "New", "description", "contactInfo");
  }

  @Test
  public void testBlankName() {
    assertThrows(IllegalArgumentException.class, () -> {
        SwappItem item2 = new SwappItem(" ");
    });
  }

  @Test
  public void testGetName() {
    assertEquals(item.getName(), "name");
  }

  @Test
  public void testGetStatus() {
    assertEquals(item.getStatus(), "New");
  }

  @Test
  public void testGetDescription() {
    assertEquals(item.getDescription(), "description");
  }

  @Test
  public void testGetContactInfo() {
    assertEquals(item.getContactInfo(), "contactInfo");
  }

  
  @Test
  public void testToString(){
    SwappItem item = new SwappItem("name");
    assertEquals(item.getName() + "    " + 
    item.getStatus() + "  " + item.getDescription() + 
    "  " + item.getContactInfo(), item.toString());
  }

  @Test
  public void testNameEquals(){
    SwappItem item2 = new SwappItem("name");
    SwappItem item3 = new SwappItem("differentName");
    assertTrue(item.nameEquals(item2));
    assertFalse(item.nameEquals(item3));
  }

  @Test
  public void testAllAttributesEquals(){
    SwappItem item2 = new SwappItem("name", "New", "description", "contactInfo");
    SwappItem item3 = new SwappItem("name", "New", "description2", "contactInfo");
    assertTrue(item.allAttributesEquals(item2));
    assertFalse(item.allAttributesEquals(item3));
  }
    
}
