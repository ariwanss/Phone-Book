package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.*;

public class Main {

    private static boolean switchToLinear = false;
    private static long timer = 0;
    private static long start = 0;
    private static long stop = 0;

    public static void main(String[] args) {
        File phoneBook = new File("D:\\directory.txt");
        List<Contact> contactList = new ArrayList<>();
        try (Scanner scanner = new Scanner(phoneBook)) {
            while (scanner.hasNext()) {
                String entry = scanner.nextLine();
                String number = entry.substring(0, entry.indexOf(' '));
                String name = entry.substring(entry.indexOf(' ') + 1);
                contactList.add(new Contact(number, name));
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }

        File toFind = new File("D:\\find.txt");
        List<String> contactsToFind = new ArrayList<>();
        try (Scanner scanner = new Scanner(toFind)) {
            while (scanner.hasNext()) {
                String entry = scanner.nextLine().trim();
                contactsToFind.add(entry);
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }

        unsortedLinearSearch(contactList, contactsToFind);
        bubbleSortAndJumpSearch(contactList, contactsToFind);
        quickSortAndBinarySearch(contactList, contactsToFind);
        hashTableSearch(contactList, contactsToFind);
    }

    public static void unsortedLinearSearch(List<Contact> contactList, List<String> contactsToFind) {
        System.out.println("Start searching (linear search)...");
        int entriesCount = 0;
        int foundCount = 0;
        long searchStart = System.currentTimeMillis();
        for (String name : contactsToFind) {
            entriesCount += 1;
            if (linearSearch(contactList, name)) {
                foundCount += 1;
            }
        }
        long searchStop = System.currentTimeMillis();
        System.out.print("Found " + foundCount + " / " + entriesCount + " entries. ");
        printTotalTime(searchStart, searchStop);
        System.out.println();
    }

    public static void bubbleSortAndJumpSearch(List<Contact> contactList, List<String> contactsToFind) {
        System.out.println("Start searching (bubble sort + jump search)...");
        int entriesCount = 0;
        int foundCount = 0;
        long sortStart = System.currentTimeMillis();
        List<Contact> toSearchFrom = bubbleSortWithTimer(contactList);
        long sortStop = System.currentTimeMillis();
        long searchStart = System.currentTimeMillis();
        if (switchToLinear) {
            for (String name : contactsToFind) {
                entriesCount += 1;
                if (linearSearch(toSearchFrom, name)) {
                    foundCount += 1;
                }
            }
        } else {
            for (String name : contactsToFind) {
                entriesCount += 1;
                if (jumpSearch(toSearchFrom, name)) {
                    foundCount += 1;
                }
            }
        }
        long searchStop = System.currentTimeMillis();
        System.out.print("Found " + foundCount + " / " + entriesCount + " entries. ");
        printTotalTime(sortStart, searchStop);
        printSortTime(sortStart, sortStop);
        printSearchTime(searchStart, searchStop);
        System.out.println();
    }

    public static void quickSortAndBinarySearch(List<Contact> contactList, List<String> contactsToFind) {
        System.out.println("Start searching (quick sort + binary search)...");
        int entriesCount = 0;
        int foundCount = 0;
        long sortStart = System.currentTimeMillis();
        List<Contact> toSearchFrom = quickSortWithTimer(contactList);
        long sortStop = System.currentTimeMillis();
        long searchStart = System.currentTimeMillis();
        if (switchToLinear) {
            for (String name : contactsToFind) {
                entriesCount += 1;
                if (linearSearch(toSearchFrom, name)) {
                    foundCount += 1;
                }
            }
        } else {
            for (String name : contactsToFind) {
                entriesCount += 1;
                if (binarySearch(toSearchFrom, name)) {
                    foundCount += 1;
                }
            }
        }
        long searchStop = System.currentTimeMillis();
        System.out.print("Found " + foundCount + " / " + entriesCount + " entries. ");
        printTotalTime(sortStart, searchStop);
        printSortTime(sortStart, sortStop);
        printSearchTime(searchStart, searchStop);
        System.out.println();
    }

    public static void hashTableSearch(List<Contact> contactList, List<String> contactsToFind) {
        System.out.println("Start searching (hash table)...");
        int entriesCount = 0;
        int foundCount = 0;
        long createStart = System.currentTimeMillis();
        Map<String, Contact> contactMap = listToHashMap(contactList);
        long createStop = System.currentTimeMillis();
        long searchStart = System.currentTimeMillis();
        for (String name : contactsToFind) {
            entriesCount += 1;
            if (contactMap.get(name) != null) {
                foundCount += 1;
            }
        }
        long searchStop = System.currentTimeMillis();
        System.out.print("Found " + foundCount + " / " + entriesCount + " entries. ");
        printTotalTime(createStart, searchStop);
        printTime(createStart, createStop, "Creating");
        printSearchTime(searchStart, searchStop);
        System.out.println();
    }

    public static Map<String, Contact> listToHashMap(List<Contact> contactList) {
        Map<String, Contact> toReturn = new HashMap<>();
        for (Contact contact : contactList) {
            toReturn.put(contact.getName(), contact);
        }
        return toReturn;
    }

    public static List<Contact> bubbleSortWithTimer(List<Contact> contactList) {
        List<Contact> copy = new ArrayList<>(contactList);
        timer = 0;
        start = System.currentTimeMillis();
        for (int i = 0; i < copy.size(); i++) {
            for (int j = 0; j < copy.size() - 1; j++) {
                if (timer > 30000) {
                    switchToLinear = true;
                    return copy;
                }
                if (copy.get(j).isGreaterThan(copy.get(j + 1))) {
                    Contact tmp = copy.get(j);
                    copy.set(j, copy.get(j + 1));
                    copy.set(j + 1, tmp);
                }
                stop = System.currentTimeMillis();
                timer = stop - start;
            }
        }
        return copy;
    }

    public static List<Contact> quickSortWithTimer(List<Contact> contactList) {
        List<Contact> copy = new ArrayList<>(contactList);
        timer = 0;
        start = System.currentTimeMillis();
        quickSortRecursionWithTimer(copy, 0, copy.size() - 1);
        return copy;
    }

    public static void quickSortRecursionWithTimer(List<Contact> contactList, int first, int last) {
        if (timer < 30000) {
            switchToLinear = true;
            return;
        }
        if (first < last) {
            int pivot_index = partition(contactList, first, last);
            quickSortRecursion(contactList, first, pivot_index - 1);
            quickSortRecursion(contactList, pivot_index + 1, last);
        }
        stop = System.currentTimeMillis();
        timer = stop - start;
    }

    public static List<Contact> bubbleSort(List<Contact> contactList) {
        List<Contact> copy = new ArrayList<>(contactList);
        for (int i = 0; i < copy.size(); i++) {
            for (int j = 0; j < copy.size() - 1; j++) {
                if (copy.get(j).isGreaterThan(copy.get(j + 1))) {
                    Contact tmp = copy.get(j);
                    copy.set(j, copy.get(j + 1));
                    copy.set(j + 1, tmp);
                }
            }
        }
        return copy;
    }

    public static int partition(List<Contact> contactList, int first, int last) {
        Contact pivotValue = contactList.get(last);
        int pivot_index = last;
        int i = first;
        while (i < pivot_index) {
            if (contactList.get(i).isGreaterThan(pivotValue)) {
                contactList.add(last, contactList.remove(i));
                pivot_index -= 1;
            } else {
                i += 1;
            }
        }
        return pivot_index;
    }

    public static void quickSortRecursion(List<Contact> contactList, int first, int last) {
        if (first < last) {
            int pivot_index = partition(contactList, first, last);
            quickSortRecursion(contactList, first, pivot_index - 1);
            quickSortRecursion(contactList, pivot_index + 1, last);
        }
    }

    public static List<Contact> quickSort(List<Contact> contactList) {
        List<Contact> copy = new ArrayList<>(contactList);
        quickSortRecursion(copy, 0, copy.size() - 1);
        return copy;
    }

    public static boolean jumpSearch(List<Contact> contactList, String contactToFind) {
        int n = (int) Math.sqrt(contactList.size());
        for (int i = 0; i < contactList.size(); i += n) {
            i = Math.min(i, contactList.size() - 1);
            int match = contactList.get(i).compareTo(contactToFind);
            switch (match) {
                case 0:
                    return true;
                case -1:
                    continue;
                case 1:
                    for (int j = i - 1; j >= i - n; j--) {
                        if (contactList.get(j).getName().equals(contactToFind)) {
                            return true;
                        }
                    }
                    break;
            }
        }
        return false;
    }

    public static boolean linearSearch(List<Contact> contactList, String contactToFind) {
        for (Contact contact : contactList) {
            if (contactToFind.equals(contact.getName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean binarySearch(List<Contact> contactList, String contactToFind) {
        int left = 0;
        int right = contactList.size() - 1;
        while (left <= right) {
            int middle = (left + right) / 2;
            int compare = contactList.get(middle).compareTo(contactToFind);
            switch (compare) {
                case 0:
                    return true;
                case 1:
                    right = middle - 1;
                    break;
                case -1:
                    left = middle + 1;
                    break;
            }
        }
        return false;
    }

    public static LocalTime getLocalTimeFromMilisecond(long start, long stop) {
        return LocalTime.ofNanoOfDay((stop - start) * 1000000);
    }

    public static void printTotalTime(long start, long stop) {
        LocalTime time = getLocalTimeFromMilisecond(start, stop);
        System.out.println("Time taken: " +
                time.getMinute() + " min. " +
                time.getSecond() + " sec. " +
                time.getNano() / 1000000 + " ms.");
    }

    public static void printSortTime(long start, long stop) {
        LocalTime time = getLocalTimeFromMilisecond(start, stop);
        System.out.println("Sorting time: " +
                time.getMinute() + " min. " +
                time.getSecond() + " sec. " +
                time.getNano() / 1000000 + " ms.");
    }

    public static void printSearchTime(long start, long stop) {
        LocalTime time = getLocalTimeFromMilisecond(start, stop);
        System.out.println("Searching time: " +
                time.getMinute() + " min. " +
                time.getSecond() + " sec. " +
                time.getNano() / 1000000 + " ms.");
    }

    public static void printTime(long start, long stop, String whatProcess) {
        LocalTime time = getLocalTimeFromMilisecond(start, stop);
        System.out.println(whatProcess + " time: " +
                time.getMinute() + " min. " +
                time.getSecond() + " sec. " +
                time.getNano() / 1000000 + " ms.");
    }
}
