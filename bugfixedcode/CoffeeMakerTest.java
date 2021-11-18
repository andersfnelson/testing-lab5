import static org.junit.Assert.assertEquals;

import java.beans.Transient;

import org.junit.Before;
import org.junit.Test;

import jdk.jfr.Timestamp;

public class CoffeeMakerTest {
	
	/**
	 * The object under test.
	 */
	private CoffeeMaker coffeeMaker;
	private Inventory inventory;
	private RecipeBook rb;
	// Sample recipes to use in testing.
	private Recipe recipe1;
	private Recipe recipe2;
	private Recipe recipe3;
	private Recipe recipe4;

	/**
	 * Initializes some recipes to test with and the {@link CoffeeMaker} 
	 * object we wish to test.
	 * 
	 * @throws RecipeException  if there was an error parsing the ingredient 
	 * 		amount when setting up the recipe.
	 */
	@Before
	public void setUp() throws RecipeException {
		coffeeMaker = new CoffeeMaker();
		inventory = new Inventory();
		rb = new RecipeBook();
		//Set up for r1
		recipe1 = new Recipe();
		recipe1.setName("Coffee");
		recipe1.setAmtChocolate("0");
		recipe1.setAmtCoffee("3");
		recipe1.setAmtMilk("1");
		recipe1.setAmtSugar("1");
		recipe1.setPrice("50");
		
		//Set up for r2
		recipe2 = new Recipe();
		recipe2.setName("Mocha");
		recipe2.setAmtChocolate("20");
		recipe2.setAmtCoffee("3");
		recipe2.setAmtMilk("1");
		recipe2.setAmtSugar("1");
		recipe2.setPrice("75");
		
		//Set up for r3
		recipe3 = new Recipe();
		recipe3.setName("Latte");
		recipe3.setAmtChocolate("0");
		recipe3.setAmtCoffee("3");
		recipe3.setAmtMilk("3");
		recipe3.setAmtSugar("1");
		recipe3.setPrice("100");
		
		//Set up for r4
		recipe4 = new Recipe();
		recipe4.setName("Hot Chocolate");
		recipe4.setAmtChocolate("4");
		recipe4.setAmtCoffee("0");
		recipe4.setAmtMilk("1");
		recipe4.setAmtSugar("1");
		recipe4.setPrice("65");

		
	}
	
	
	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with well-formed quantities
	 * Then we do not get an exception trying to read the inventory quantities.
	 * 
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test
	public void testAddInventory() throws InventoryException {
		coffeeMaker.addInventory("4","7","0","9");
	}
	
	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with malformed quantities (i.e., a negative 
	 * quantity and a non-numeric string)
	 * Then we get an inventory exception
	 * 
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddInventoryException() throws InventoryException {
		coffeeMaker.addInventory("4", "-1", "asdf", "3");
	}


	/**
	 * Given a coffee maker with one valid recipe
	 * When we make coffee, selecting the valid recipe and paying more than 
	 * 		the coffee costs
	 * Then we get the correct change back.
	 */
	@Test
	public void testMakeCoffee() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(25, coffeeMaker.makeCoffee(0, 75));
	}

	/**
	  * The coffee maker should only allow three recipies
	*/
	@Test
	public void testAdd4Recipes() {
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.addRecipe(recipe2);
		coffeeMaker.addRecipe(recipe3);
		assertEquals(false, coffeeMaker.addRecipe(recipe4));
	}

// These are the tests I added
	//Check that the edit recipe returns the name of the recipe (which cannot be changed)
	@Test
	public void testEditRecipe(){
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.addRecipe(recipe2);
		coffeeMaker.addRecipe(recipe3);
		coffeeMaker.editRecipe(1, recipe4);
		assertEquals("Mocha", recipe4.getName());
	}
	// Test that making recipe1 subtracts three units of coffee from the inventory
	@Test
	public void testMakeCoffee2(){
		coffeeMaker.addRecipe(recipe1);
		inventory.useIngredients(recipe1);
		assertEquals(12, inventory.getCoffee());

	}
	// Check that you get an inventory exception when entering negative number for sugar
	@Test(expected = InventoryException.class)
	public void testAddInventoryExceptionSugar() throws InventoryException {
		coffeeMaker.addInventory("4", "1", "-2", "3");
	}
	
	// Test that change of 5 (full amount) comes back when you try to purchase a recipe that is out of range
	@Test
	public void testChange(){
		assertEquals(5, coffeeMaker.makeCoffee(-1, 5));
	}
	//Check that deleting a recipe sets that spot in the array to null
	@Test
	public void testDelete(){
		rb.addRecipe(recipe1);
		rb.deleteRecipe(0);
		Recipe [] r = rb.getRecipes();
		assertEquals(null, r[0]);
	}

}
