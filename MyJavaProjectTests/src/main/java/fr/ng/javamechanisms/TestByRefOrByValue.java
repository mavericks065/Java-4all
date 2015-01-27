package fr.ng.javamechanisms;

import fr.ng.javamechanisms.Apple;

public class TestByRefOrByValue
{


	public static void main(final String[] args) {

		// Test by copy
		int value = 5;

		System.out.println(value);
		modify(value);
		System.out.println(value);

		// Test by reference : we can change the value of the attributes
		Apple apple = new Apple();

		System.out.println(apple.color);
		changeApple(apple);
		System.out.println(apple.color);
		
		// but we can't change the value of the object
		System.out.println(apple);
		nullifyApple(apple);
		System.out.println(apple);
	}

	private static void nullifyApple(Apple apple) {
		apple = null;
	}

	private static void changeApple(final Apple apple) {
		apple.color = "green";
	}

	private static void modify(int copy) {
		copy = copy - 112;
	}
}