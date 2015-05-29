package edu.psu.cmpsc421.lab1;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Everything we need to know about a particular recipe
 * 
 * @author jjb24
 *
 */
public class Recipe {
	
	/**
	 * A review of a recipe
	 * 
	 * @author jjb24
	 *
	 */
	private static class Review {
		String author;
		Double ratingValue;
		
		public Review(String author, Double ratingValue) {
			this.author = author; 
			this.ratingValue = ratingValue;
		}
		
		@Override
		public String toString() {
			return "author: " + author + "(" + ratingValue + ")";
		}
	}

	
	TargetURL source; // Where we found the recipe
	String name;      // The name of the recipe
	String author;    // The author of the recipe
	String description; // A description of the recipe
	
	// The categories for the recipe 
	List<String> recipeCategories = new LinkedList<String>();
	
	// The list of recipe ingredients
	List<String> ingredients = new LinkedList<String>();
	
	// The list of cooking methods (fry, bake, etc)
	List<String> cookingMethods = new LinkedList<String>();
	
	// The list of cuisines (e.g. French, Indian, etc.)
	List<String> recipeCuisines = new LinkedList<String>();
	
	// The list of instructions for how to make the recipe
	List<String> recipeInstructions = new LinkedList<String>();
	 
	String totalTime; // The time required to make the recipe (ISO 8601 duration)
	String prepTime; // The prep time required (ISO 8601 duration)
	String cookTime; // The cooking time required (ISO 8601 duration)
	
	Integer aggregateRatingCount; // The total number of ratings/reviews
	Double aggregateRatingValue; // The average rating
	
	// A list of all of the reviews of the recipe
	List<Review> reviews = new LinkedList<Review>();
	
	
	/**
	 * Create a recipe object by parsing the jsoup Element
	 * 
	 * @param source The URL from which this recipe was retrieved
	 * @param recipe The jsoup Element containing the recipe
	 */
	public Recipe(TargetURL source, Element recipe) {
		this.source = source;
		
		// To Do:
		// Parse the recipe and populate the remaining member variables
		this.name = recipe.getElementsByAttributeValue("itemprop", "name").text();	
		this.author = recipe.getElementsByAttributeValue("itemprop", "author").text();
		this.description = recipe.getElementsByAttributeValue("itemprop", "description").text();
		
		if(this.source.toString().contains("food.com"))
		{
			this.totalTime = recipe.getElementsByAttributeValue("itemprop", "totalTime").attr("content").toString();
			this.prepTime = recipe.getElementsByAttributeValue("itemprop", "prepTime").attr("content").toString();
			this.cookTime = recipe.getElementsByAttributeValue("itemprop", "cookTime").attr("content").toString();
		}
		else
		{
			this.totalTime = recipe.getElementsByAttributeValue("itemprop", "totalTime").attr("datetime").toString();
			this.prepTime = recipe.getElementsByAttributeValue("itemprop", "prepTime").attr("datetime").toString();
			this.cookTime = recipe.getElementsByAttributeValue("itemprop", "cookTime").attr("datetime").toString();
		}
		
		
		Elements categories = recipe.getElementsByAttributeValue("itemprop", "recipeCategory");
		if(!categories.isEmpty()){
			for(Element e: categories){this.recipeCategories.add(e.text());}	
		}

		Elements ingredients = recipe.getElementsByAttributeValue("itemprop", "ingredients");
		if(!ingredients.isEmpty()){
			for(Element e: ingredients){this.ingredients.add(e.text());}
		}

		Elements cookingMethods = recipe.getElementsByAttributeValue("itemprop", "cookingMethod");
		if(!cookingMethods.isEmpty()){
			for(Element e: cookingMethods){this.cookingMethods.add(e.attr("content"));}
		}

		Elements recipeCuisine = recipe.getElementsByAttributeValue("itemprop", "recipeCuisine");
		if(!recipeCuisine.isEmpty()){
			for(Element e: recipeCuisine){this.recipeCuisines.add(e.attr("content"));}
		}

		Elements recipeInstruction = recipe.getElementsByAttributeValue("itemprop", "recipeInstructions");
		if(!recipeInstruction.isEmpty()){
			for(Element e: recipeInstruction){this.recipeInstructions.add(e.text());}
		}

		Elements ratingCount = recipe.getElementsByAttributeValue("itemtype", "http://schema.org/AggregateRating");
		if(!ratingCount.isEmpty()){
			for(Element e:ratingCount){			
				Elements count = e.getElementsByAttributeValue("itemprop", "reviewCount");
				try{
					this.aggregateRatingCount = Integer.parseInt(count.text());
				}catch(NumberFormatException ex){
					this.aggregateRatingCount = -1;
				}
			}
		}
		
		if(this.source.toString().contains("food.com"))
		{
			Elements ratingValue = recipe.getElementsByAttributeValue("itemtype", "http://schema.org/AggregateRating");
			if(!ratingValue.isEmpty()){
				for(Element e : ratingValue){			
					Elements value = e.getElementsByAttributeValue("itemprop", "ratingValue");
					try{
						this.aggregateRatingValue = Double.parseDouble(value.text());
					}catch(NumberFormatException ex){
						this.aggregateRatingValue = -1.0;
					}
				}
			}
		}else{
			Elements ratingValue = recipe.getElementsByAttributeValue("itemtype", "http://schema.org/AggregateRating");
			if(!ratingValue.isEmpty()){
				for(Element e : ratingValue){			
					Elements value = e.getElementsByAttributeValue("itemprop", "ratingValue");
					try{
						this.aggregateRatingValue = Double.parseDouble(value.attr("content"));
					}catch(NumberFormatException ex){
						this.aggregateRatingValue = -1.0;
					}
				}
			}
		}
		
		if(this.source.toString().contains("food.com"))
		{
			Elements review = recipe.getElementsByAttributeValue("itemtype", "http://schema.org/Review");
			
			for(Element e: review){
							
				Elements author = e.getElementsByAttributeValue("itemprop", "author");
				Elements ratingVal = e.getElementsByAttributeValue("itemprop", "ratingValue");
				
				String a = author.text();
				
				if(!ratingVal.isEmpty()){
					try{
						Double rate = Double.parseDouble(ratingVal.text());
						Review newReview = new Review(a, rate);
						reviews.add(newReview);	
					}catch(NumberFormatException ex){
						Review newReview = new Review(a, -1.0);
						reviews.add(newReview);	
					}
				}
				else{
					Review newReview = new Review(a, -1.0);
					reviews.add(newReview);	
				}
				
			}
		}else{
			
			Elements review = recipe.getElementsByAttributeValue("itemtype", "http://schema.org/Review");
			
			for(Element e: review){
							
				Elements author = e.getElementsByAttributeValue("itemprop", "author");
				Elements ratingVal = e.getElementsByAttributeValue("itemprop", "ratingValue");
				
				String a = author.text();
				
				if(!ratingVal.isEmpty()){
					try{
						Double rate = Double.parseDouble(ratingVal.attr("content"));
						Review newReview = new Review(a, rate);
						reviews.add(newReview);	
					}catch(NumberFormatException ex){
						Review newReview = new Review(a, -1.0);
						reviews.add(newReview);	
					}
				}
				else{
					Review newReview = new Review(a, -1.0);
					reviews.add(newReview);	
				}			
			}
		}
	}

	/**
	 * Create a nice, readable display of data in this object 
	 *
	 * @return A string with data from recipe
	 */
	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer();
		
		retVal.append(source.toString() + "\n");
		
		retVal.append("  name: " + name + "\n");

		retVal.append("    author:" + author + "\n");
		
		retVal.append("    description:" + description + "\n");

		retVal.append("    category:\n");
		for (String rc: recipeCategories)
			retVal.append("      " + rc + "\n");

		retVal.append("    ingredients:\n");
		for (String ing: ingredients)
			retVal.append("      " + ing + "\n");

		retVal.append("    cooking method:\n");
		for (String cm: cookingMethods)
			retVal.append("      " + cm + "\n");

		retVal.append("    recipe cuisine:\n");
		for (String rc: recipeCuisines)
			retVal.append("      " + rc + "\n");

		retVal.append("    recipe instructions:\n");
		for (String ri: recipeInstructions)
			retVal.append("      " + ri + "\n");
		
		retVal.append("    rating count:" + aggregateRatingCount + "\n");
		retVal.append("    overall rating:" + aggregateRatingValue + "\n");
		retVal.append("    total time:" + totalTime + "\n");
		retVal.append("    prep time:" + prepTime + "\n");
		retVal.append("    cook time:" + cookTime + "\n");

		
		retVal.append("    reviews:\n");
		for (Review r: reviews) 
			retVal.append("      " + r.toString());
		
		return retVal.toString();
	}
}
