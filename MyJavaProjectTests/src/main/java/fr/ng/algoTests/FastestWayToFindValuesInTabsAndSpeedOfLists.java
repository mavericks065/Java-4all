package fr.ng.algoTests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FastestWayToFindValuesInTabsAndSpeedOfLists {

	public static void main(final String[] args) {

		List<Integer> toTab = new ArrayList<Integer>(500000);
		for (int i = 0; i < 500000; i++) {
			toTab.add(Integer.valueOf(i));
		}

		// Test of speed between research into an array using Loops, lists, sets
		Object[] tab = toTab.toArray();
		Integer integer1 = Integer.valueOf(100000);
		Integer integer2 = Integer.valueOf(240000);
		Integer integer3 = Integer.valueOf(400000);

		final Long start = System.currentTimeMillis();

		boolean set1 = FastestWayToFindValuesInTabsAndSpeedOfLists.useSet(tab, integer1);
		boolean set2 = FastestWayToFindValuesInTabsAndSpeedOfLists.useSet(tab, integer2);
		boolean set3 = FastestWayToFindValuesInTabsAndSpeedOfLists.useSet(tab, integer3);

		final Long end = System.currentTimeMillis();

		System.out.println("duration useSet : ");
		System.out.println(end - start);
		System.out.println("set1 : " + set1);
		System.out.println("set2 : " + set2);
		System.out.println("set3 : " + set3);

		final Long startList = System.currentTimeMillis();

		boolean list1 = FastestWayToFindValuesInTabsAndSpeedOfLists.useList(tab, integer1);
		boolean list2 = FastestWayToFindValuesInTabsAndSpeedOfLists.useList(tab, integer2);
		boolean list3 = FastestWayToFindValuesInTabsAndSpeedOfLists.useList(tab, integer3);

		final Long endList = System.currentTimeMillis();

		System.out.println("duration useList : ");
		System.out.println(endList - startList);
		System.out.println("list1 : " + list1);
		System.out.println("list2 : " + list2);
		System.out.println("list3 : " + list3);

		final Long startLoop = System.currentTimeMillis();

		boolean loop1 = FastestWayToFindValuesInTabsAndSpeedOfLists.useLoop(tab, integer1);
		boolean loop2 = FastestWayToFindValuesInTabsAndSpeedOfLists.useLoop(tab, integer2);
		boolean loop3 = FastestWayToFindValuesInTabsAndSpeedOfLists.useLoop(tab, integer3);

		final Long endLoop = System.currentTimeMillis();

		System.out.println("duration useLoop : ");
		System.out.println(endLoop - startLoop);
		System.out.println("Loop1 : " + loop1);
		System.out.println("Loop2 : " + loop2);
		System.out.println("Loop3 : " + loop3);

		System.out.println("----------------- ArrayList vs LinkedList -------------- ");

		List<Integer> arrayList = new ArrayList<Integer>();
		List<Integer> linkedList = new LinkedList<Integer>();

		// ArrayList add
		long startTime = System.nanoTime();

		for (int i = 0; i < 500000; i++) {
			arrayList.add(i);
		}

		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		System.out.println("ArrayList add:  " + duration);

		// LinkedList add
		startTime = System.nanoTime();

		for (int i = 0; i < 500000; i++) {
			linkedList.add(i);
		}
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("LinkedList add: " + duration);

		// ArrayList get
		startTime = System.nanoTime();

		for (int i = 0; i < 50000; i++) {
			arrayList.get(i);
		}
		arrayList.get(100000);
		arrayList.get(220000);
		arrayList.get(400000);

		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("ArrayList get duration :  " + duration);

		// LinkedList get
		startTime = System.nanoTime();

		for (int i = 0; i < 50000; i++) {
			linkedList.get(i);
		}
		linkedList.get(100000);
		linkedList.get(220000);
		linkedList.get(400000);

		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("LinkedList get duration : " + duration);

		// ArrayList remove
		startTime = System.nanoTime();

		for (int i = 9999; i >= 0; i--) {
			arrayList.remove(i);
		}
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("ArrayList remove first 9999:  " + duration);

		// LinkedList remove
		startTime = System.nanoTime();

		for (int i = 9999; i >= 0; i--) {
			linkedList.remove(i);
		}
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("LinkedList remove first 9999 : " + duration);

	}

	public static boolean useList(final Object[] arr, final Integer targetValue) {
		return Arrays.asList(arr).contains(targetValue);
	}

	public static boolean useSet(final Object[] arr, final Integer targetValue) {
		Set<Object> set = new HashSet<Object>(Arrays.asList(arr));
		return set.contains(targetValue);
	}

	/**
	 * fastest way.
	 * 
	 * @param arr
	 * @param targetValue
	 * @return
	 */
	public static boolean useLoop(final Object[] arr, final Integer targetValue) {
		for (Object integer : arr) {
			if ((integer instanceof Integer) && integer.equals(targetValue)) {
				return true;
			}
		}
		return false;
	}

}