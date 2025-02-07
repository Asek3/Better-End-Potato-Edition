package mod.beethoven92.betterendforge.common.interfaces;

import mod.beethoven92.betterendforge.common.recipes.AnvilSmithingRecipe;

import java.util.List;

public interface ExtendedRepairContainer
{
	void be_updateCurrentRecipe(AnvilSmithingRecipe recipe);
	AnvilSmithingRecipe be_getCurrentRecipe();
	List<AnvilSmithingRecipe> be_getRecipes();
	
	default void be_nextRecipe() 
	{
		List<AnvilSmithingRecipe> recipes = this.be_getRecipes();
		AnvilSmithingRecipe current = this.be_getCurrentRecipe();
		int i = recipes.indexOf(current) + 1;
		if (i >= recipes.size()) 
		{
			i = 0;
		}
		this.be_updateCurrentRecipe(recipes.get(i));
	}
	
	default void be_previousRecipe() 
	{
		List<AnvilSmithingRecipe> recipes = this.be_getRecipes();
		AnvilSmithingRecipe current = this.be_getCurrentRecipe();
		int i = recipes.indexOf(current) - 1;
		if (i <= 0) 
		{
			i = recipes.size() - 1;
		}
		this.be_updateCurrentRecipe(recipes.get(i));
	}
}
