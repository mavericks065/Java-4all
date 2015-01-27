package fr.ng.algoTests;

import java.util.ArrayList;
import java.util.List;

public class SlidingAverage
{

   public static void main(final String[] args) {
		final List<Integer> integersToTransform = new ArrayList<Integer>();
		int integer = 10;
		for (int i = 0; i < 500000; i++) {
			integersToTransform.add(integer);
			integer += 10;
		}

		final int slidingInt = 3;

		final Long start = System.currentTimeMillis();
		final List<Integer> firstWayToTransform = SlidingAverage.imbricatedWay(
				integersToTransform, slidingInt);
		final Long end = System.currentTimeMillis();

		System.out.println("duration : ");
		System.out.println(end - start);

		System.out.println(firstWayToTransform.subList(0, 100).toString());

		final Long start1 = System.currentTimeMillis();
		final List<Integer> secondWayToTransform = SlidingAverage.substratedWayWay(
				integersToTransform, slidingInt);
		final Long end1 = System.currentTimeMillis();

		System.out.println("duration 1 : ");
		System.out.println(end1 - start1);

		System.out.println(secondWayToTransform.subList(0, 100).toString());

	}

	public static List<Integer> imbricatedWay(final List<Integer> listToTransform,
			final int slidingInt) {
		List<Integer> slidingAverageList = new ArrayList<Integer>();

		for (int i = 0; i < listToTransform.size() + 1 - slidingInt; i++) {
			int k = 0;
			for (int j = i; j < i + slidingInt; j++) {
				k += listToTransform.get(j);
			}

			slidingAverageList.add(Integer.valueOf(k));
		}
		return slidingAverageList;
	}

	public static List<Integer> substratedWayWay(final List<Integer> listToTransform,
			final int slidingInt) {
		List<Integer> slidingAverageList = new ArrayList<Integer>();
		int k = 0;
		for (int i = 0; i < listToTransform.size() + 1 - slidingInt; i++) {
			k += listToTransform.get(i);

			if (i > slidingInt - 1) {
				slidingAverageList.add(Integer.valueOf(k));
				k += listToTransform.get(i + 1 - slidingInt);
			}

		}
		return slidingAverageList;
	}
}