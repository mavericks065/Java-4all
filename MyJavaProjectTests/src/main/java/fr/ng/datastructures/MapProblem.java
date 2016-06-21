package fr.ng.datastructures;

import java.util.*;

public class MapProblem {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);
        // my code
        Map<String, Integer> contacts = new HashMap<>();
        int n = in.nextInt();
        in.nextLine();
        for (int i = 0; i < n; i++) {
            String name = in.nextLine();
            int phone = in.nextInt();
            // my code
            contacts.put(name, phone);
            in.nextLine();
        }
        while (in.hasNext()) {
            String s = in.nextLine();
            // my code
            if (contacts.get(s) != null) {
                System.out.println(s + "=" + contacts.get(s));
            } else {
                System.out.println("Not found");
            }
        }
    }
}
